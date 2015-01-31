package me.snov.newrelic.elasticsearch.service;

import me.snov.newrelic.elasticsearch.dto.NodesStats;

import java.net.MalformedURLException;
import java.net.URL;

public class NodesStatsParser extends AbstractParser {

    private static final String URL_CLUSTER_STATS = "_nodes/stats";
    private final URL nodesStatsUrl;

    public NodesStatsParser(String host, Integer port) throws MalformedURLException {
        this.nodesStatsUrl = new URL(HTTP, host, port, URL_CLUSTER_STATS);
    }

    public NodesStats getNodesStats()
    {
        return getGsonResponse(nodesStatsUrl, NodesStats.class);
    }
}
