package me.snov.newrelic.elasticsearch.service;

import me.snov.newrelic.elasticsearch.response.ClusterStats;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.*;

public class ClusterStatsServiceTest {

    private ClusterStatsParser clusterStatsParser;
    private ClusterStatsService clusterStatsService;

    /**
     * Sets up the test fixture.
     * (Called before every test case method.)
     */
    @Before
    public void setUp() {
        clusterStatsParser = new ClusterStatsParser();
        clusterStatsService = new ClusterStatsService();
    }

    private ClusterStats parseJson(String path) throws IOException {

        URL url = getClass().getResource("/resources/cluster_stats_version_mismatch.json");

        return clusterStatsParser.parse(getClass().getResourceAsStream(path));
    }

    @Test
    public void testGetNumberOfVersionsInCluster() throws Exception {
        ClusterStats clusterStats = parseJson("/resources/cluster_stats_version_mismatch.json");
        assertEquals(2, clusterStatsService.getNumberOfVersionsInCluster(clusterStats));
    }
}
