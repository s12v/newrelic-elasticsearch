package me.snov.newrelic.elasticsearch.interfaces;

public interface AgentInterface {
    void reportMetric(String metricName, String units, Number value);
}
