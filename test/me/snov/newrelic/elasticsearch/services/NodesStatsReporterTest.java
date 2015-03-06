package me.snov.newrelic.elasticsearch.services;

import me.snov.newrelic.elasticsearch.parsers.NodesStatsParser;
import me.snov.newrelic.elasticsearch.reporters.NodesStatsReporter;
import me.snov.newrelic.elasticsearch.responses.NodesStats;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class NodesStatsReporterTest {

    private MockAgent agent;
    private NodesStatsParser parser;
    private NodesStatsReporter reporter;

    /**
     * Sets up the test fixture.
     * (Called before every test case method.)
     */
    @Before
    public void setUp() {
        agent = new MockAgent();
        parser = new NodesStatsParser();
        reporter = new NodesStatsReporter(agent);
    }

    private NodesStats parseJson(String path) throws IOException {
        InputStream stream = getClass().getResourceAsStream(path);
        assertNotNull(String.format("Resource %s exists", path), stream);
        return parser.parse(stream);
    }

    @Test
    public void testReportNodesStatsV134() throws Exception {
        NodesStats nodesStats = parseJson("/resources/nodes_stats_1.3.4.json");
        reporter.reportNodesStats(nodesStats);
        assertTrue("Number of reported metrics > 0", agent.getReportedMetricsCount() > 0);
    }

    @Test
    public void testReportNodesStatsV142() throws Exception {
        NodesStats nodesStats = parseJson("/resources/nodes_stats_1.4.2.json");
        reporter.reportNodesStats(nodesStats);
        assertTrue("Number of reported metrics > 0", agent.getReportedMetricsCount() > 0);
    }

    @Test
    public void testReportNodesStatsV142LimitedOsStats() throws Exception {
        NodesStats nodesStats = parseJson("/resources/nodes_stats_1.4.2_incomplete_os_stats.json");
        reporter.reportNodesStats(nodesStats);
        assertTrue("Number of reported metrics > 0", agent.getReportedMetricsCount() > 0);
    }
}
