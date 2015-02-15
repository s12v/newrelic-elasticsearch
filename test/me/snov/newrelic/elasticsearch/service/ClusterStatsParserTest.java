package me.snov.newrelic.elasticsearch.service;

import me.snov.newrelic.elasticsearch.response.ClusterStats;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ClusterStatsParserTest {

    private ClusterStatsParser clusterStatsParser;

    /**
     * Sets up the test fixture.
     * (Called before every test case method.)
     */
    @Before
    public void setUp() {
        clusterStatsParser = new ClusterStatsParser();
    }

    private ClusterStats parseJson(String path) throws IOException {
        return clusterStatsParser.parse(getClass().getResourceAsStream(path));
    }

    @Test
    public void testV142() throws Exception {
        ClusterStats clusterStats = parseJson("/resources/cluster_stats_1.4.2.json");
        assertEquals("elasticsearch_snov", clusterStats.cluster_name);
        assertEquals(1601396l, clusterStats.indices.segments.memory_in_bytes.longValue());
        assertEquals(1l, clusterStats.nodes.count.total.longValue());
    }
}
