package me.snov.newrelic.elasticsearch;

import com.newrelic.metrics.publish.Agent;
import com.newrelic.metrics.publish.AgentFactory;
import com.newrelic.metrics.publish.configuration.ConfigurationException;
import me.snov.newrelic.elasticsearch.parsers.ClusterStatsParser;
import me.snov.newrelic.elasticsearch.parsers.NodesStatsParser;
import me.snov.newrelic.elasticsearch.reporters.ClusterStatsReporter;
import me.snov.newrelic.elasticsearch.reporters.NodesStatsReporter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

public class ElasticsearchAgentFactory extends AgentFactory {

    @Override
    public Agent createConfiguredAgent(Map<String, Object> properties) throws ConfigurationException {
        String host = (String) properties.get("host");
        String protocol = (String) properties.get("protocol");
        String basePath = (String) properties.get("basePath");
        String username = (String) properties.get("username");
        String password = (String) properties.get("password");
        Long port = (Long) properties.get("port");
        String name = (String) properties.get("name");

        if (host == null || port == null) {
            throw new ConfigurationException("'host' and 'port' must be specified. Do you have a 'config/plugin.json' file?");
        }

        if (protocol == null) {
            protocol = "http";
        }

        if (basePath == null) {
            basePath = "";
        }

        try {
            ClusterStatsParser clusterStatsParser = new ClusterStatsParser(protocol, host, port.intValue(), basePath, username, password);
            String clusterName = name != null && name.length() > 0  ? name  : clusterStatsParser.request().cluster_name;
            ElasticsearchAgent agent = new ElasticsearchAgent(clusterName);

            ClusterStatsReporter clusterStatsReporter = new ClusterStatsReporter(agent);
            NodesStatsParser nodeStatsParser = new NodesStatsParser(protocol, host, port.intValue(), basePath, username, password);
            NodesStatsReporter nodeStatsReporter = new NodesStatsReporter(agent);
            agent.configure(clusterStatsParser, clusterStatsReporter, nodeStatsParser, nodeStatsReporter);

            return agent;
        } catch (MalformedURLException e) {
            throw new ConfigurationException(String.format("URL could not be parsed: %s", e.getMessage()));
        } catch (IOException e) {
            throw new ConfigurationException(
                    String.format("Can't connect to elasticsearch at %s:%d: %s", host, port, e.getMessage()), e);
        }
    }
}
