package me.snov.newrelic.elasticsearch.dto;

public class ClusterStats {
    public class Indices {
        public class Docs {
            public Long count;
            public Long deleted;
        }
        public class Store {
            public Long sizeInBytes;
            public Long throttleTimeInMillis;
        }
        public class Fielddata {
            public Long memorySizeInBytes;
            public Long evictions;
        }
        public class FilterCache {
            public Long memorySizeInBytes;
            public Long evictions;
        }
        public class IdCache {
            public Long memorySizeInBytes;
        }
        public class Completion {
            public Long sizeInBytes;
        }
        public class Segments {
            public Long count;
            public Long memoryInBytes;
            public Long indexwriterMemoryInBytes;
            public Long indexwriterMaxMemoryInBytes;
            public Long versionMapMemoryInBytes;
            public Long fixedBitSetMemoryInBytes;
        }
        public class Percolate {
            public Long total;
            public Long timeInMillis;
            public Long current;
            public Long memorySizeInBytes;
            public String memorySize;
            public Long queries;
        }

        public Long count;
        public Docs docs;
        public Store store;
        public Fielddata fielddata;
        public FilterCache filterCache;
        public IdCache isCache;
        public Completion completion;
        public Segments segments;
        public Percolate percolate;
    }
    public class Nodes {
        public class Count {
            public Long total;
            public Long masterOnly;
            public Long dataOnly;
            public Long masterData;
            public Long client;
        }
    }

    public String status;
    public String clusterName;
    public Indices indices;
    public Nodes nodes;
}

