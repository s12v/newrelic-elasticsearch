package me.snov.newrelic.elasticsearch.services;

import me.snov.newrelic.elasticsearch.parsers.NodesStatsParser;
import me.snov.newrelic.elasticsearch.reporters.NodesStatsReporter;
import me.snov.newrelic.elasticsearch.responses.NodesStats;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.*;

public class NodesStatsReporterTest {

    private static final String nodesStatsUrl = "http://localhost:9200/_nodes/stats"; 
    
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

    private NodesStats parseJson(InputStream stream) throws IOException {
        return parser.parse(stream);
    }
    
    private NodesStats parseJsonFromFile(String path) throws IOException {
        return parseJson(getClass().getResourceAsStream(path));
    }
    
    private NodesStats parseJsonFromUrl(String url) throws IOException {
        return parseJson(new URL(url).openConnection().getInputStream());
    }

    @Test
    public void testReportNodesStatsV090() throws Exception {
        NodesStats nodesStats = parseJsonFromFile("/resources/nodes_stats_0.90.12.json");
        reporter.reportNodesStats(nodesStats);
        assertTrue("Number of reported metrics > 0", agent.getReportedMetricsCount() > 0);
    }

    @Test
    public void testReportNodesStatsV134() throws Exception {
        NodesStats nodesStats = parseJsonFromFile("/resources/nodes_stats_1.3.4.json");
        reporter.reportNodesStats(nodesStats);
        assertTrue("Number of reported metrics > 0", agent.getReportedMetricsCount() > 0);
    }

    @Test
    public void testReportNodesStatsV142() throws Exception {
        NodesStats nodesStats = parseJsonFromFile("/resources/nodes_stats_1.4.2.json");
        reporter.reportNodesStats(nodesStats);
        assertTrue("Number of reported metrics > 0", agent.getReportedMetricsCount() > 0);
    }

    @Test
    public void testReportNodesStatsV142LimitedOsStats() throws Exception {
        NodesStats nodesStats = parseJsonFromFile("/resources/nodes_stats_1.4.2_incomplete_os_stats.json");
        reporter.reportNodesStats(nodesStats);
        assertTrue("Number of reported metrics > 0", agent.getReportedMetricsCount() > 0);
    }

    @Test
    public void testReportNodesStatsV151() throws Exception {
        NodesStats nodesStats = parseJsonFromFile("/resources/nodes_stats_1.5.1.json");
        reporter.reportNodesStats(nodesStats);
        assertTrue("Number of reported metrics > 0", agent.getReportedMetricsCount() > 0);
    }

    @Test
    public void testReportNodesStatsV210() throws Exception {
        NodesStats nodesStats = parseJsonFromUrl(nodesStatsUrl);
        reporter.reportNodesStats(nodesStats);
        assertTrue("Number of reported metrics > 0", agent.getReportedMetricsCount() > 0);
    }
}
