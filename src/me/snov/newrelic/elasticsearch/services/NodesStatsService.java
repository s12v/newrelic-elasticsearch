package me.snov.newrelic.elasticsearch.services;

import me.snov.newrelic.elasticsearch.responses.NodesStats;

public class NodesStatsService {

    public static class QueriesStat {
        public Long search = 0l;
        public Long fetch = 0l;
        public Long get = 0l;
        public Long index = 0l;
        public Long delete = 0l;
    }

    /**
     * @return Numbers of queries across the cluster
     */
    public QueriesStat getTotalNumberOfQueries(NodesStats nodesStats) {
        QueriesStat result = new QueriesStat();
        if (nodesStats != null && nodesStats.nodes != null) {
            for (NodesStats.NodeStats nodeStats : nodesStats.nodes.values()) {
                if (nodeStats.indices.search.query_total != null) {
                    result.search += nodeStats.indices.search.query_total.longValue();
                }
                if (nodeStats.indices.search.fetch_total != null) {
                    result.fetch += nodeStats.indices.search.fetch_total.longValue();
                }
                if (nodeStats.indices.get.total != null) {
                    result.get += nodeStats.indices.get.total.longValue();
                }
                if (nodeStats.indices.indexing.index_total != null) {
                    result.index += nodeStats.indices.indexing.index_total.longValue();
                }
                if (nodeStats.indices.indexing.delete_total != null) {
                    result.delete += nodeStats.indices.indexing.delete_total.longValue();
                }
            }
        }

        return result;
    }
}
