package me.snov.newrelic.elasticsearch.parsers;

import me.snov.newrelic.elasticsearch.responses.NodesStats;

import java.net.MalformedURLException;
import java.net.URL;

public class NodesStatsParser extends AbstractParser<NodesStats> {

    private static final String URL_CLUSTER_STATS = "/_nodes/stats";

    public NodesStatsParser() {
        super(NodesStats.class, null, null, null);
    }

    public NodesStatsParser(String protocol, String host, int port, String basePath, String username, String password) throws MalformedURLException {
        super(NodesStats.class, new URL(protocol, host, port, basePath + URL_CLUSTER_STATS), username, password);
    }
}
