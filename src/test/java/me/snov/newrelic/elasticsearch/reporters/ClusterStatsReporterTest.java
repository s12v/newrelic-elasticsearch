package me.snov.newrelic.elasticsearch.reporters;

import me.snov.newrelic.elasticsearch.interfaces.AgentInterface;
import me.snov.newrelic.elasticsearch.parsers.ClusterStatsParser;
import me.snov.newrelic.elasticsearch.responses.ClusterStats;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ClusterStatsReporterTest {

    private ClusterStatsReporter clusterStatsReporter;
    private ClusterStats clusterStats;
    @Mock private AgentInterface agent;

    @Before
    public void setUp() throws Exception {
        clusterStatsReporter = new ClusterStatsReporter(agent);
        clusterStats = new ClusterStatsParser().parse(getClass().getResourceAsStream("/resources/cluster_stats_2.1.1.json"));
    }

    @Test
    public void shouldReportGreenStatusCorrectly() throws Exception {
        clusterStats.status = "green";

        clusterStatsReporter.reportClusterStats(clusterStats);

        verify(agent).reportMetric("V1/ClusterStats/Status/IsYellow", "bool", 0);
        verify(agent).reportMetric("V1/ClusterStats/Status/IsRed", "bool", 0);
    }

    @Test
    public void shouldReportRedStatusCorrectly() throws Exception {
        clusterStats.status = "red";

        clusterStatsReporter.reportClusterStats(clusterStats);

        verify(agent).reportMetric("V1/ClusterStats/Status/IsYellow", "bool", 0);
        verify(agent).reportMetric("V1/ClusterStats/Status/IsRed", "bool", 1);
    }

    @Test
    public void shouldReportYellowStatusCorrectly() throws Exception {
        clusterStats.status = "yellow";

        clusterStatsReporter.reportClusterStats(clusterStats);

        verify(agent).reportMetric("V1/ClusterStats/Status/IsYellow", "bool", 1);
        verify(agent).reportMetric("V1/ClusterStats/Status/IsRed", "bool", 0);
    }
}