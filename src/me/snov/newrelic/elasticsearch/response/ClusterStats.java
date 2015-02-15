package me.snov.newrelic.elasticsearch.response;

import java.util.ArrayList;

public class ClusterStats {
    public static class Indices {
        public static class Shards {
            public static class Index {
                public static class IndexStats {
                    public Number min;
                    public Number max;
                    public Number avg;
                }

                public IndexStats shards;
                public IndexStats primaries;
                public IndexStats replication;
            }

            public Number total;
            public Number primaries;
            public Number replication;
        }
        public static class Docs {
            public Number count;
            public Number deleted;
        }
        public static class Store {
            public Number size_in_bytes;
            public Number throttle_time_in_millis;
        }
        public static class Fielddata {
            public Number memory_size_in_bytes;
            public Number evictions;
        }
        public static class FilterCache {
            public Number memory_size_in_bytes;
            public Number evictions;
        }
        public static class IdCache {
            public Number memory_size_in_bytes;
        }
        public static class Completion {
            public Number size_in_bytes;
        }
        public static class Segments {
            public Number count;
            public Number memory_in_bytes;
            public Number index_writer_memory_in_bytes;
            public Number index_writer_max_memory_in_bytes;
            public Number version_map_memory_in_bytes;
            public Number fixed_bit_set_memory_in_bytes;
        }
        public static class Percolate {
            public Number total;
            public Number time_in_millis;
            public Number current;
            public Number memory_size_in_bytes;
            public String memory_size;
            public Number queries;
        }

        public Number count;

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
            public Number total;
            public Number master_only;
            public Number data_only;
            public Number master_data;
            public Number client;
        }

        public Count count;
        public ArrayList<String> versions;
    }

    public String status;
    public String cluster_name;
    public Indices indices;
    public Nodes nodes;
}
