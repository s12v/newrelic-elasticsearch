package me.snov.newrelic.elasticsearch;

import com.newrelic.metrics.publish.Agent;
import com.newrelic.metrics.publish.util.Logger;
import me.snov.newrelic.elasticsearch.interfaces.AgentInterface;
import me.snov.newrelic.elasticsearch.parsers.*;
import me.snov.newrelic.elasticsearch.reporters.*;

import java.io.IOException;

/**
 * Agent for Elasticsearch cluster
 */
public class ElasticsearchAgent extends Agent implements AgentInterface {

	private static final String GUID = "me.snov.newrelic-elasticsearch";
	private static final String VERSION = "2.3.0";

	private final String clusterName;
	private final Logger logger;

	private ClusterStatsParser clusterStatsParser;
	private ClusterStatsReporter clusterStatsReporter;
	private ClusterHealthParser clusterHealthParser;
	private ClusterHealthReporter clusterHealthReporter;
	private NodesStatsParser nodesStatsParser;
	private NodesStatsReporter nodesStatsReporter;

	public ElasticsearchAgent(String clusterName) {
		super(GUID, VERSION);
		this.clusterName = clusterName;
		this.logger = Logger.getLogger(ElasticsearchAgent.class);
	}

	@Override
	public String getAgentName() {
		return clusterName;
	}

	@Override
	public void pollCycle() {
		try {
			clusterStatsReporter.reportClusterStats(clusterStatsParser.request());
			clusterHealthReporter.reportClusterHealth(clusterHealthParser.request());
			nodesStatsReporter.reportNodesStats(nodesStatsParser.request());
		} catch (IOException e) {
			logger.error("Unable to perform poll cycle", e);
		}
	}

	public void configure(ClusterStatsParser clusterStatsParser, ClusterStatsReporter clusterStatsReporter,
			NodesStatsParser nodesStatsParser, NodesStatsReporter nodesStatsReporter,
			ClusterHealthParser clusterHealthParser, ClusterHealthReporter clusterHealthReporter) {
		this.clusterStatsParser = clusterStatsParser;
		this.clusterStatsReporter = clusterStatsReporter;
		this.clusterHealthParser = clusterHealthParser;
		this.clusterHealthReporter = clusterHealthReporter;
		this.nodesStatsParser = nodesStatsParser;
		this.nodesStatsReporter = nodesStatsReporter;
	}
}
