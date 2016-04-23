package me.snov.newrelic.elasticsearch.services;

import me.snov.newrelic.elasticsearch.responses.ClusterStats;

import java.util.HashSet;
import java.util.List;

public class ClusterStatsService {

    /**
     * @return Number of different versions
     */
    public int getNumberOfVersions(List<String> versions) {
        return versions.size() == 0
            ? 0
            : new HashSet<String>(versions).size();
    }

    /**
     * @return Number of different versions in cluster
     */
    public int getNumberOfVersionsInCluster(ClusterStats clusterStats) {
        return clusterStats == null
            ? 0
            : getNumberOfVersions(clusterStats.nodes.versions);
    }

    public boolean isYellow(ClusterStats clusterStats) {
        return "yellow".equals(clusterStats.status);
    }

    public boolean isRed(ClusterStats clusterStats) {
        return "red".equals(clusterStats.status);
    }
}
