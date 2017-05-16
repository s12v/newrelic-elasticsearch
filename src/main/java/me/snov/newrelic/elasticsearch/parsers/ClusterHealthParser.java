package me.snov.newrelic.elasticsearch.parsers;

import me.snov.newrelic.elasticsearch.responses.ClusterHealth;

import java.net.MalformedURLException;
import java.net.URL;

public class ClusterHealthParser extends AbstractParser<ClusterHealth> {

    private static final String URL_CLUSTER_HEALTH = "/_cluster/health";

    public ClusterHealthParser() {
        super(ClusterHealth.class, null, null, null);
    }

    public ClusterHealthParser(String protocol, String host, int port, String basePath, String username, String password) throws MalformedURLException {
        super(ClusterHealth.class, new URL(protocol, host, port, basePath + URL_CLUSTER_HEALTH), username, password);
    }
}
