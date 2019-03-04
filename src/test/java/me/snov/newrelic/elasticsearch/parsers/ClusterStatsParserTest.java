package me.snov.newrelic.elasticsearch.parsers;

import me.snov.newrelic.elasticsearch.responses.ClusterStats;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import static org.junit.Assert.assertEquals;

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
    public void testV661() throws Exception {
        ClusterStats clusterStats = parseJson("/cluster_stats_6.6.1.json");
        assertEquals("docker-cluster", clusterStats.cluster_name);
        assertEquals("green", clusterStats.status);
        assertEquals(1, clusterStats.nodes.count.total.longValue());
        assertEquals(1, clusterStats.nodes.versions.size());
    }

    @Test
    public void testV511() throws Exception {
        ClusterStats clusterStats = parseJson("/cluster_stats_5.1.1.json");
        assertEquals("elasticsearch", clusterStats.cluster_name);
        assertEquals("yellow", clusterStats.status);
        assertEquals(1, clusterStats.nodes.count.total.longValue());
        assertEquals(1, clusterStats.nodes.versions.size());
    }

    @Test
    public void testV211() throws Exception {
        ClusterStats clusterStats = parseJson("/cluster_stats_2.1.1.json");
        assertEquals("elasticsearch", clusterStats.cluster_name);
        assertEquals("green", clusterStats.status);
        assertEquals(2L, clusterStats.nodes.count.total.longValue());
        assertEquals(1, clusterStats.nodes.versions.size());
    }

    @Test
    public void testV210() throws Exception {
        ClusterStats clusterStats = parseJson("/cluster_stats_2.1.0.json");
        assertEquals("elasticsearch", clusterStats.cluster_name);
        assertEquals(1L, clusterStats.nodes.count.total.longValue());
        assertEquals(1, clusterStats.nodes.versions.size());
    }

    @Test
    public void testV151() throws Exception {
        ClusterStats clusterStats = parseJson("/cluster_stats_1.5.1.json");
        assertEquals("elasticsearch", clusterStats.cluster_name);
        assertEquals(1L, clusterStats.nodes.count.total.longValue());
        assertEquals(1, clusterStats.nodes.versions.size());
    }

    @Test
    public void testV142() throws Exception {
        ClusterStats clusterStats = parseJson("/cluster_stats_1.4.2.json");
        assertEquals("elasticsearch_snov", clusterStats.cluster_name);
        assertEquals(1601396L, clusterStats.indices.segments.memory_in_bytes.longValue());
        assertEquals(1L, clusterStats.nodes.count.total.longValue());
    }

    @Test
    public void testV134() throws Exception {
        ClusterStats clusterStats = parseJson("/cluster_stats_1.3.4.json");
        assertEquals("esearch-testcluster", clusterStats.cluster_name);
        assertEquals(3L, clusterStats.nodes.count.total.longValue());
        assertEquals(1, clusterStats.nodes.versions.size());
    }

    @Test
    public void testV090() throws Exception {
        ClusterStats clusterStats = parseJson("/cluster_stats_0.90.12.json");
        assertEquals("elasticsearch", clusterStats.cluster_name);
        assertEquals(1L, clusterStats.nodes.count.total.longValue());
        assertEquals(1, clusterStats.nodes.versions.size());
    }
}
