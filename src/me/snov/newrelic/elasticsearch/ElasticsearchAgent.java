package me.snov.newrelic.elasticsearch;

import com.newrelic.metrics.publish.Agent;
import com.newrelic.metrics.publish.configuration.ConfigurationException;
import com.newrelic.metrics.publish.util.Logger;

import me.snov.newrelic.elasticsearch.interfaces.AgentInterface;
import me.snov.newrelic.elasticsearch.parsers.ClusterStatsParser;
import me.snov.newrelic.elasticsearch.parsers.NodesStatsParser;
import me.snov.newrelic.elasticsearch.reporters.NodesStatsReporter;
import me.snov.newrelic.elasticsearch.responses.ClusterStats;
import me.snov.newrelic.elasticsearch.services.*;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Agent for Elasticsearch cluster
 */
public class ElasticsearchAgent extends Agent implements AgentInterface {

    private static final String GUID = "me.snov.newrelic-elasticsearch";
    private static final String VERSION = "2.0.0";

    private final String clusterName;
    private final ClusterStatsParser clusterStatsParser;
    private final ClusterStatsService clusterStatsService;
    private final NodesStatsParser nodesStatsParser;
    private final Logger logger;

    private NodesStatsReporter nodesStatsReporter;

    /**
     * Constructor for Elastisearch Agent
     */
    public ElasticsearchAgent(String host, Integer port, String name, String username, String password) throws ConfigurationException {
        super(GUID, VERSION);
        try {
            logger = Logger.getLogger(ElasticsearchAgent.class);
            clusterStatsParser = new ClusterStatsParser(host, port, username, password);
            nodesStatsParser = new NodesStatsParser(host, port, username, password);
            clusterStatsService = new ClusterStatsService();
            clusterName = (name != null && name.length() > 0)
                ? name
                : getClusterName();
        } catch (MalformedURLException e) {
            throw new ConfigurationException("URL could not be parsed", e);
        } catch (IOException e) {
            throw new ConfigurationException(
                String.format("Can't connect to elasticsearch at %s:%d. Error message was: %s", host, port, e.getMessage()),
                e
            );
        }
    }

    private String getClusterName() throws IOException {
        return clusterStatsParser.request().cluster_name;
    }

    private void initReporters()
    {
        if (nodesStatsReporter == null) {
            nodesStatsReporter = new NodesStatsReporter(this);
        }
    }

    @Override
    public String getAgentName() {
        return clusterName;
    }

    @Override
    public void pollCycle() {
        try {
            initReporters();
            reportClusterStats(clusterStatsParser.request());
            nodesStatsReporter.reportNodesStats(nodesStatsParser.request());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void reportClusterStats(ClusterStats clusterStats) {

        /******************* Cluster *******************/

        // Component/V1/ClusterStats/Indices/Docs/*
        reportMetric("V1/ClusterStats/Indices/Docs/Count", "documents", clusterStats.indices.docs.count);
        reportMetric("V1/ClusterStats/Indices/Docs/Deleted", "documents", clusterStats.indices.docs.deleted);

        // Nodes (table)
        // Component/V1/ClusterStats/Nodes/Count/*
        reportMetric("V1/ClusterStats/Nodes/Count/Total", "nodes", clusterStats.nodes.count.total);
        reportMetric("V1/ClusterStats/Nodes/Count/Master and data", "nodes", clusterStats.nodes.count.master_data);
        reportMetric("V1/ClusterStats/Nodes/Count/Master only", "nodes", clusterStats.nodes.count.master_only);
        reportMetric("V1/ClusterStats/Nodes/Count/Data only", "nodes", clusterStats.nodes.count.data_only);
        reportMetric("V1/ClusterStats/Nodes/Count/Client", "nodes", clusterStats.nodes.count.client);

        // Indices and Shards (table)
        // Component/V1/ClusterStats/Indices/Group1/*
        reportMetric("V1/ClusterStats/Indices/Group1/Indices", "indices", clusterStats.indices.count);
        reportMetric("V1/ClusterStats/Indices/Group1/Shards", "shards", clusterStats.indices.shards.total);
        reportMetric("V1/ClusterStats/Indices/Group1/Primaries", "shards", clusterStats.indices.shards.total);
        reportMetric("V1/ClusterStats/Indices/Group1/Replication", "shards", clusterStats.indices.shards.replication);

        // Component/V1/ClusterStats/Indices/Segments/Count
        reportMetric("V1/ClusterStats/Indices/Segments/Count", "segments", clusterStats.indices.segments.count);

        // Component/V1/ClusterStats/Indices/Store/Size
        reportMetric("V1/ClusterStats/Indices/Store/Size", "bytes", clusterStats.indices.store.size_in_bytes);


        /******************* Summary *******************/

        // Component/V1/ClusterStats/NumberOfVersionsInCluster
        reportMetric("V1/ClusterStats/NumberOfVersionsInCluster", "versions",
            clusterStatsService.getNumberOfVersionsInCluster(clusterStats));
    }
}
