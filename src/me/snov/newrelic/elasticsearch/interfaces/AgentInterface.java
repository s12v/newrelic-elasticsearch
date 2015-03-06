package me.snov.newrelic.elasticsearch.interfaces;

public interface AgentInterface {
    public void reportMetric(String metricName, String units, Number value);
}
