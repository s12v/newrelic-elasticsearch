package me.snov.newrelic.elasticsearch;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.newrelic.metrics.publish.Agent;
import com.newrelic.metrics.publish.configuration.ConfigurationException;
import com.newrelic.metrics.publish.processors.EpochCounter;
import com.newrelic.metrics.publish.processors.Processor;

import me.snov.newrelic.elasticsearch.dto.ClusterStats;
import me.snov.newrelic.elasticsearch.dto.NodesStats;
import me.snov.newrelic.elasticsearch.service.ClusterStatsParser;
import me.snov.newrelic.elasticsearch.service.NodesStatsParser;

/**
 * An agent for Elasticsearch cluster
 */
public class ElasticsearchAgent extends Agent {

    private static final String GUID = "me.snov.newrelic.elasticsearch";
    private static final String VERSION = "0.0.1";

    private final String clusterName;
    private final Processor articleCreationRate;
    private final ClusterStatsParser clusterStatsParser;
    private final NodesStatsParser nodesStatsParser;

    /**
     * Constructor for Elastisearch Agent
     */
    public ElasticsearchAgent(String host, Integer port) throws ConfigurationException {
        super(GUID, VERSION);
        try {
            this.clusterStatsParser = new ClusterStatsParser(host, port);
            this.nodesStatsParser = new NodesStatsParser(host, port);
            this.articleCreationRate = new EpochCounter();
            this.clusterName = getClusterName();
        } catch (MalformedURLException e) {
            throw new ConfigurationException("URL could not be parsed", e);
        }
    }

    private String getClusterName()
    {
        return clusterStatsParser.getClusterStats().cluster_name;
    }

    @Override
    public String getComponentHumanLabel() {
        return clusterName;
    }

    @Override
    public void pollCycle() {
        System.out.println("pollCycle");
        ClusterStats clusterStats = clusterStatsParser.getClusterStats();
        NodesStats nodesStats = nodesStatsParser.getNodesStats();
//        Integer numArticles = getNumArticles();
//        if (numArticles != null) {
//             reportMetric("Articles/Created", "articles/sec", articleCreationRate.process(numArticles));
//             reportMetric("Articles/Count", "articles", numArticles);
//        } else {
            //TODO: log numArticles when null
//        }
    }
}
