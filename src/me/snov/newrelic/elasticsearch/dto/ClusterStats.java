package me.snov.newrelic.elasticsearch.dto;

import java.util.ArrayList;
import java.util.HashSet;

public class ClusterStats {
    private class Indices {
        private class Shards {
            private class Index {
                private class IdontKnowWhatIsIt {
                    public Long min;
                    public Long max;
                    public Long avg;
                }

                public IdontKnowWhatIsIt shards;
                public IdontKnowWhatIsIt primaries;
                public IdontKnowWhatIsIt replication;
            }

            public Long total;
            public Long primaries;
            public Long replication;
        }
        private class Docs {
            public Long count;
            public Long deleted;
        }
        private class Store {
            public Long size_in_bytes;
            public Long throttle_time_in_millis;
        }
        private class Fielddata {
            public Long memory_size_in_bytes;
            public Long evictions;
        }
        private class FilterCache {
            public Long memory_size_in_bytes;
            public Long evictions;
        }
        private class IdCache {
            public Long memory_size_in_bytes;
        }
        private class Completion {
            public Long size_in_bytes;
        }
        private class Segments {
            public Long count;
            public Long memory_in_bytes;
            public Long index_writer_memory_in_bytes;
            public Long index_writer_max_memory_in_bytes;
            public Long version_map_memory_in_bytes;
            public Long fixed_bit_set_memory_in_bytes;
        }
        private class Percolate {
            public Long total;
            public Long time_in_millis;
            public Long current;
            public Long memory_size_in_bytes;
            public String memory_size;
            public Long queries;
        }

        public Long count;

        public Shards shards;
        public Docs docs;
        public Store store;
        public Fielddata fielddata;
        public FilterCache filter_cache;
        public IdCache id_cache;
        public Completion completion;
        public Segments segments;
        public Percolate percolate;
    }

    private class Nodes {
        private class Count {
            public Long total;
            public Long master_only;
            public Long data_only;
            public Long master_data;
            public Long client;
        }

        public Count count;
        public ArrayList<String> versions;
    }

    public String status;
    public String cluster_name;
    public Indices indices;
    public Nodes nodes;

    /**
     * @return true if nodes have different versions
     */
    public boolean isVersionMismatch() {
        return nodes.versions.size() != 0 && new HashSet<>(nodes.versions).size() > 1;
    }
}
