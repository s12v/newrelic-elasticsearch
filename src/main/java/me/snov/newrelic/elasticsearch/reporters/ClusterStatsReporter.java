package me.snov.newrelic.elasticsearch.reporters;

import me.snov.newrelic.elasticsearch.interfaces.AgentInterface;
import me.snov.newrelic.elasticsearch.responses.ClusterStats;
import me.snov.newrelic.elasticsearch.services.ClusterStatsService;

public class ClusterStatsReporter {

    private final AgentInterface agent;
    private final ClusterStatsService clusterStatsService;

    public ClusterStatsReporter(AgentInterface agent) {
        this.agent = agent;
        this.clusterStatsService = new ClusterStatsService();
    }

    public void reportClusterStats(ClusterStats clusterStats) {
        // Cluster status (we represent unhealthy states as an int to allow alerting on values > 0)
        agent.reportMetric("V1/ClusterStats/Status/IsYellow", "bool", asInt(clusterStatsService.isYellow(clusterStats)));
        agent.reportMetric("V1/ClusterStats/Status/IsRed", "bool", asInt(clusterStatsService.isRed(clusterStats)));

        // Component/V1/ClusterStats/Indices/Docs/*
        agent.reportMetric("V1/ClusterStats/Indices/Docs/Count", "documents", clusterStats.indices.docs.count);
        agent.reportMetric("V1/ClusterStats/Indices/Docs/Deleted", "documents", clusterStats.indices.docs.deleted);

        // Nodes (table)
        // Component/V1/ClusterStats/Nodes/Count/*
        agent.reportMetric("V1/ClusterStats/Nodes/Count/Total", "nodes", clusterStats.nodes.count.total);
        agent.reportMetric("V1/ClusterStats/Nodes/Count/Master and data", "nodes", clusterStats.nodes.count.master_data);
        agent.reportMetric("V1/ClusterStats/Nodes/Count/Master only", "nodes", clusterStats.nodes.count.master_only);
        agent.reportMetric("V1/ClusterStats/Nodes/Count/Data only", "nodes", clusterStats.nodes.count.data_only);
        agent.reportMetric("V1/ClusterStats/Nodes/Count/Client", "nodes", clusterStats.nodes.count.client);

        // Indices and Shards (table)
        // Component/V1/ClusterStats/Indices/Group1/*
        agent.reportMetric("V1/ClusterStats/Indices/Group1/Indices", "indices", clusterStats.indices.count);
        agent.reportMetric("V1/ClusterStats/Indices/Group1/Shards", "shards", clusterStats.indices.shards.total);
        agent.reportMetric("V1/ClusterStats/Indices/Group1/Primaries", "shards", clusterStats.indices.shards.total);
        agent.reportMetric("V1/ClusterStats/Indices/Group1/Replication", "shards", clusterStats.indices.shards.replication);

        // Component/V1/ClusterStats/Indices/Segments/Count
        agent.reportMetric("V1/ClusterStats/Indices/Segments/Count", "segments", clusterStats.indices.segments.count);

        // Component/V1/ClusterStats/Indices/Store/Size
        agent.reportMetric("V1/ClusterStats/Indices/Store/Size", "bytes", clusterStats.indices.store.size_in_bytes);


        /******************* Summary *******************/

        // Component/V1/ClusterStats/NumberOfVersionsInCluster
        agent.reportMetric("V1/ClusterStats/NumberOfVersionsInCluster", "versions",
                clusterStatsService.getNumberOfVersionsInCluster(clusterStats));
    }

    private int asInt(boolean value) {
        return value ? 1 : 0;
    }
}
