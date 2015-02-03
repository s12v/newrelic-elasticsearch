package me.snov.newrelic.elasticsearch.response;

import java.util.ArrayList;

public class ClusterStats {
    public static class Indices {
        public static class Shards {
            public static class Index {
                public static class IndexStats {
                    public Long min;
                    public Long max;
                    public Long avg;
                }

                public IndexStats shards;
                public IndexStats primaries;
                public IndexStats replication;
            }

            public Long total;
            public Long primaries;
            public Long replication;
        }
        public static class Docs {
            public Long count;
            public Long deleted;
        }
        public static class Store {
            public Long size_in_bytes;
            public Long throttle_time_in_millis;
        }
        public static class Fielddata {
            public Long memory_size_in_bytes;
            public Long evictions;
        }
        public static class FilterCache {
            public Long memory_size_in_bytes;
            public Long evictions;
        }
        public static class IdCache {
            public Long memory_size_in_bytes;
        }
        public static class Completion {
            public Long size_in_bytes;
        }
        public static class Segments {
            public Long count;
            public Long memory_in_bytes;
            public Long index_writer_memory_in_bytes;
            public Long index_writer_max_memory_in_bytes;
            public Long version_map_memory_in_bytes;
            public Long fixed_bit_set_memory_in_bytes;
        }
        public static class Percolate {
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

    public static class Nodes {
        public static class Count {
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
}
