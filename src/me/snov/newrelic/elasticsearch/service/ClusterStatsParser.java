package me.snov.newrelic.elasticsearch.service;

import me.snov.newrelic.elasticsearch.dto.ClusterStats;

import java.net.MalformedURLException;
import java.net.URL;

public class ClusterStatsParser extends AbstractParser {

    private static final String URL_CLUSTER_STATS = "_cluster/stats";
    private final URL clusterStatsUrl;

    public ClusterStatsParser(String host, Integer port) throws MalformedURLException {
        this.clusterStatsUrl = new URL(HTTP, host, port, URL_CLUSTER_STATS);
    }

    public ClusterStats getClusterStats()
    {
        return getGsonResponse(clusterStatsUrl, ClusterStats.class);
    }
}
