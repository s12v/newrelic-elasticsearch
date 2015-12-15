package me.snov.newrelic.elasticsearch.responses;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NodesStats {
    public static class NodeStats {
        public static class Indices {
            public static class Docs {
                public Number count;
                public Number deleted;
            }
            public static class Store {
                public Number size_in_bytes;
                public Number throttle_time_in_millis;
            }
            public static class Indexing {
                public Number index_total;
                public Number index_time_in_millis;
                public Number index_current;
                public Number delete_total;
                public Number delete_time_in_millis;
                public Number delete_current;
                public Number noop_update_total;
                public Boolean is_throttled;
                public Number throttle_time_in_millis;
            }
            public static class Get {
                public Number total;
                public Number time_in_millis;
                public Number exists_total;
                public Number exists_time_in_millis;
                public Number missing_total;
                public Number missing_time_in_millis;
                public Number current;
            }
            public static class Search {
                public Number open_contexts;
                public Number query_total;
                public Number query_time_in_millis;
                public Number query_current;
                public Number fetch_total;
                public Number fetch_time_in_millis;
                public Number fetch_current;
            }
            public static class Merges {
                public Number current;
                public Number current_docs;
                public Number current_size_in_bytes;
                public Number total;
                public Number total_time_in_millis;
                public Number total_docs;
                public Number total_size_in_bytes;
            }
            public static class Refresh {
                public Number total;
                public Number total_time_in_millis;
            }
            public static class Flush {
                public Number total;
                public Number total_time_in_millis;
            }
            public static class Warmer {
                public Number current;
                public Number total;
                public Number total_time_in_millis;
            }
            public static class FilterCache {
                public Number memory_size_in_bytes;
                public Number evictions;
            }
            public static class IdCache {
                public Number memory_size_in_bytes;
            }
            public static class Fielddata {
                public Number memory_size_in_bytes;
                public Number evictions;
            }
            public static class Percolate {
                public Number total;
                public Number time_in_millis;
                public Number current;
                public Number memory_size_in_bytes;
                public String memory_size;
                public Number queries;
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
            public static class Translog {
                public Number operations;
                public Number size_in_bytes;
            }
            public static class Suggest {
                public Number total;
                public Number time_in_millis;
                public Number current;
            }
            public static class QueryCache {
                public Number memory_size_in_bytes;
                public Number evictions;
                public Number hit_count;
                public Number miss_count;
            }

            public Docs docs;
            public Store store;
            public Indexing indexing;
            public Get get;
            public Search search;
            public Merges merges;
            public Refresh refresh;
            public Flush flush;
            public Warmer warmer;
            public FilterCache filter_cache;
            public IdCache id_cache;
            public Fielddata fielddata;
            public Percolate percolate;
            public Completion completion;
            public Segments segments;
            public Translog translog;
            public Suggest suggest;
            public QueryCache query_cache;
        }
        public static class Os {
            public static class Cpu {
                public Number sys;
                public Number user;
                public Number idle;
                public Number usage;
                public Number stolen;
            }
            public static class Mem {
                public Number free_in_bytes;
                public Number used_in_bytes;
                public Number free_percent;
                public Number used_percent;
                public Number actual_free_in_bytes;
                public Number actual_used_in_bytes;
            }
            public static class Swap {
                public Number used_in_bytes;
                public Number free_in_bytes;
            }

            public Number uptime_in_millis;
            private Object load_average;
            public Cpu cpu;
            public Mem mem;
            public Swap swap;

            public Number getLoadAverage() {
                if (load_average instanceof List && ((List) load_average).size() > 0) {
                    return ((List<Number>) load_average).get(0);
                } else if (load_average instanceof Number) {
                    return (Number) load_average;
                } else {
                    return null;
                }
            }
        }
        public static class Process {
            public static class Cpu {
                public Number percent;
                public Number sys_in_millis;
                public Number user_in_millis;
                public Number total_in_millis;
            }
            public static class Mem {
                public Number resident_in_bytes;
                public Number share_in_bytes;
                public Number total_virtual_in_bytes;
            }

            public Number open_file_descriptors;
            public Cpu cpu;
            public Mem mem;

        }
        public static class Jvm {
            public static class Mem {
                public Number heap_used_in_bytes;
                public Number heap_used_percent;
                public Number heap_committed_in_bytes;
                public Number non_heap_used_in_bytes;
                public Number non_heap_committed_in_bytes;
            }
            public static class Pools {
                public static class PoolStats {
                    public Number used_in_bytes;
                    public Number max_in_bytes;
                    public Number peak_used_in_bytes;
                    public Number peak_max_in_bytes;
                }

                public PoolStats young;
                public PoolStats survivor;
                public PoolStats old;
            }
            public static class Threads {
                public Number count;
                public Number peak_count;
            }
            public static class Gc {
                public static class GcCollectors {
                    public static class GcCollector {
                        public Number collection_count;
                        public Number collection_time_in_millis;
                    }

                    public GcCollector young;
                    public GcCollector old;
                }
                public GcCollectors collectors;
            }
            public static class BufferPools {
                public static class BufferPoolStats {
                    public Number count;
                    public Number used_in_bytes;
                    public Number total_capacity_in_bytes;
                }

                public BufferPoolStats direct;
                public BufferPoolStats mapped;
            }

            public Number uptime_in_millis;
            public Mem mem;
            public Pools pools;
            public Threads threads;
            public Gc gc;
            public BufferPools buffer_pools;
        }
        public static class ThreadPool {
            public static class ThreadPoolStats {
                public Number threads;
                public Number queue;
                public Number active;
                public Number rejected;
                public Number largest;
                public Number completed;
            }

            public ThreadPoolStats generic;
            public ThreadPoolStats index;
            public ThreadPoolStats bench;
            public ThreadPoolStats get;
            public ThreadPoolStats snapshot;
            public ThreadPoolStats force_merge;
            public ThreadPoolStats merge;
            public ThreadPoolStats suggest;
            public ThreadPoolStats bulk;
            public ThreadPoolStats optimize;
            public ThreadPoolStats warmer;
            public ThreadPoolStats flush;
            public ThreadPoolStats search;
            public ThreadPoolStats listener;
            public ThreadPoolStats percolate;
            public ThreadPoolStats management;
            public ThreadPoolStats refresh;
        }
        public static class Network {
            public static class Tcp {
                public Number active_opens;
                public Number passive_opens;
                public Number curr_estab;
                public Number in_segs;
                public Number out_segs;
                public Number retrans_segs;
                public Number estab_resets;
                public Number attempt_fails;
                public Number in_errs;
                public Number out_rsts;
            }

            public Tcp tcp;
        }
        public static class Fs {
            public static class FsTotal {
                public Number total_in_bytes;
                public Number free_in_bytes;
                public Number available_in_bytes;
                public Number disk_reads;
                public Number disk_writes;
                public Number disk_io_op;
                public Number disk_read_size_in_bytes;
                public Number disk_write_size_in_bytes;
                public Number disk_io_size_in_bytes;
            }
            public static class FsData {
                public String path;
                public String mount;
                public String dev;
                public Number total_in_bytes;
                public Number free_in_bytes;
                public Number available_in_bytes;
                public Number disk_reads;
                public Number disk_writes;
                public Number disk_io_op;
                public Number disk_read_size_in_bytes;
                public Number disk_write_size_in_bytes;
                public Number disk_io_size_in_bytes;
            }

            public FsTotal total;
            public ArrayList<FsData> data;
        }
        public static class Transport {
           public Number server_open;
           public Number rx_count;
           public Number rx_size_in_bytes;
           public Number tx_count;
           public Number tx_size_in_bytes;
        }
        public static class Http {
            public Number current_open;
            public Number total_opened;
        }
        public static class Breakers {
            public static class BreakerStats {
                public Number limit_size_in_bytes;
                public String limit_size;
                public Number estimated_size_in_bytes;
                public String estimated_size;
                public Double overhead;
                public Number tripped;
            }

            public BreakerStats fielddata;
            public BreakerStats request;
            public BreakerStats parent;
        }

        public String name;
        public Indices indices;
        public Os os;
        public Process process;
        public Jvm jvm;
        public ThreadPool thread_pool;
        public Network network;
        public Fs fs;
        public Transport transport;
        public Http http;
        public Breakers breakers;
    }

    public Map<String, NodeStats> nodes;
}

