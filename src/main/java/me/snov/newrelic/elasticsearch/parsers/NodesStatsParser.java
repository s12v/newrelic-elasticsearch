package me.snov.newrelic.elasticsearch.parsers;

import me.snov.newrelic.elasticsearch.responses.NodesStats;

import java.net.MalformedURLException;
import java.net.URL;

public class NodesStatsParser extends AbstractParser<NodesStats> {

    private static final String URL_CLUSTER_STATS = "/_nodes/stats";

    public NodesStatsParser() {
        super(NodesStats.class, null, null, null);
    }

    public NodesStatsParser(String host, int port, String username, String password) throws MalformedURLException {
        super(NodesStats.class, new URL(HTTP, host, port, URL_CLUSTER_STATS), username, password);
    }
}
