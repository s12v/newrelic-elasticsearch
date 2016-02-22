package me.snov.newrelic.elasticsearch.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class ClusterStatsServiceGetVersionMismatchTest {

    private final ArrayList<String> nodes;
    private final int expected;
    private final ClusterStatsService clusterStatsService;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            { new ArrayList<String>(), 0},
            { new ArrayList<String>(Arrays.asList("1")), 1},
            { new ArrayList<String>(Arrays.asList("1.3.1", "1.4.2")), 2},
            { new ArrayList<String>(Arrays.asList("1", "2", "3")), 3},
        });
    }

    public ClusterStatsServiceGetVersionMismatchTest(ArrayList<String> nodes, int expected) {
        this.nodes = nodes;
        this.expected = expected;
        this.clusterStatsService = new ClusterStatsService();
    }

    @Test
    public void test() throws Exception {
        assertEquals(expected, clusterStatsService.getNumberOfVersions(nodes));
    }
}