package me.snov.newrelic.elasticsearch.parsers;

import me.snov.newrelic.elasticsearch.responses.ClusterStats;

import java.net.MalformedURLException;
import java.net.URL;

public class ClusterStatsParser extends AbstractParser<ClusterStats> {

    private static final String URL_CLUSTER_STATS = "/_cluster/stats";

    public ClusterStatsParser() {
        super(ClusterStats.class, null, null, null);
    }

    public ClusterStatsParser(String protocol, String host, int port, String basePath, String username, String password) throws MalformedURLException {
        super(ClusterStats.class, new URL(protocol, host, port, basePath + URL_CLUSTER_STATS), username, password);
    }
}
