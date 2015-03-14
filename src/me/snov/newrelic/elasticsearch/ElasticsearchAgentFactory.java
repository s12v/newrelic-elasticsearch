package me.snov.newrelic.elasticsearch;

import java.util.Map;

import com.newrelic.metrics.publish.Agent;
import com.newrelic.metrics.publish.AgentFactory;
import com.newrelic.metrics.publish.configuration.ConfigurationException;

public class ElasticsearchAgentFactory extends AgentFactory {

    private String getClusterName(Map<String, Object> properties) {
        return properties.containsKey("name")
            ? (String) properties.get("name")
            : null;
    }

    @Override
    public Agent createConfiguredAgent(Map<String, Object> properties) throws ConfigurationException {
        String host = (String) properties.get("host");
        Long port = (Long) properties.get("port");
        String name = getClusterName(properties);

        if (host == null || port == null) {
            throw new ConfigurationException("'name' and 'host' cannot be null. Do you have a 'config/plugin.json' file?");
        }

        return new ElasticsearchAgent(host, port.intValue(), name);
    }
}
