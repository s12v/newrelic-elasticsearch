package me.snov.newrelic.elasticsearch.reporters;

import me.snov.newrelic.elasticsearch.interfaces.AgentInterface;

public class MockAgent implements AgentInterface {

    private int reportedMetricsCount = 0;

    @Override
    public void reportMetric(String metricName, String units, Number value) {
        reportedMetricsCount++;
    }

    public int getReportedMetricsCount() {
        return reportedMetricsCount;
    }
}
