package me.snov.newrelic.elasticsearch.reporters;

import me.snov.newrelic.elasticsearch.interfaces.AgentInterface;
import me.snov.newrelic.elasticsearch.responses.ClusterHealth;
import me.snov.newrelic.elasticsearch.services.ClusterHealthService;

public class ClusterHealthReporter {

    private final AgentInterface agent;
    private final ClusterHealthService ClusterHealthService;

    public ClusterHealthReporter(AgentInterface agent) {
        this.agent = agent;
        this.ClusterHealthService = new ClusterHealthService();
    }

    public void reportClusterHealth(ClusterHealth ClusterHealth) {
        // Cluster status (we represent unhealthy states as an int to allow alerting on values > 0)
        agent.reportMetric("V1/ClusterHealth/Status/IsYellow", "bool", asInt(ClusterHealthService.isYellow(ClusterHealth)));
        agent.reportMetric("V1/ClusterHealth/Status/IsRed", "bool", asInt(ClusterHealthService.isRed(ClusterHealth)));

        // Component/V1/ClusterHealth/Indices/Docs/*
        agent.reportMetric("V1/ClusterHealth/Shards/Count/Initializing", "shards", ClusterHealth.initializing_shards);
        agent.reportMetric("V1/ClusterHealth/Shards/Count/Primary Active", "shards", ClusterHealth.active_primary_shards);
        agent.reportMetric("V1/ClusterHealth/Shards/Count/Active", "shards", ClusterHealth.active_shards);
        agent.reportMetric("V1/ClusterHealth/Shards/Count/Relocating", "shards", ClusterHealth.relocating_shards);
        agent.reportMetric("V1/ClusterHealth/Shards/Count/Unassigned", "shards", ClusterHealth.unassigned_shards);
        agent.reportMetric("V1/ClusterHealth/Shards/Count/Delayed Unassigned", "shards", ClusterHealth.delayed_unassigned_shards);
        agent.reportMetric("V1/ClusterHealth/Shards/Count/Active Percent", "shards", ClusterHealth.active_shards_percent_as_number);

        agent.reportMetric("V1/ClusterHealth/Node/Count/Total", "nodes", ClusterHealth.number_of_nodes);
        agent.reportMetric("V1/ClusterHealth/Nodes/Count/Data Nodes", "nodes", ClusterHealth.number_of_data_nodes);
        agent.reportMetric("V1/ClusterHealth/Tasks/Count/Pending", "tasks", ClusterHealth.number_of_pending_tasks);
        agent.reportMetric("V1/ClusterHealth/Tasks/Max Wait In Queue Millis", "tasks", ClusterHealth.task_max_waiting_in_queue_millis);
        
        agent.reportMetric("V1/ClusterHealth/Timed Out", "other", asInt(ClusterHealth.timed_out));
        agent.reportMetric("V1/ClusterHealth/Number Inflight Fetch", "other", ClusterHealth.number_of_in_flight_fetch);

    }

    private int asInt(boolean value) {
        return value ? 1 : 0;
    }
}
