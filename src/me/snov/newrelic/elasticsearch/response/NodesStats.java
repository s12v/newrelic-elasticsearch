package me.snov.newrelic.elasticsearch.response;

import java.util.ArrayList;
import java.util.Map;

public class NodesStats {
    public static class NodeStats {
        public static class Indices {
            public static class Docs {
                public Long count;
                public Long deleted;
            }
            public static class Store {
                public Long size_in_bytes;
                public Long throttle_time_in_millis;
            }
            public static class Indexing {
                public Long index_total;
                public Long index_time_in_millis;
                public Long index_current;
                public Long delete_total;
                public Long delete_time_in_millis;
                public Long delete_current;
                public Long noop_update_total;
                public Boolean is_throttled;
                public Long throttle_time_in_millis;
            }
            public static class Get {
                public Long total;
                public Long time_in_millis;
                public Long exists_total;
                public Long exists_time_in_millis;
                public Long missing_total;
                public Long missing_time_in_millis;
                public Long current;
            }
            public static class Search {
                public Long open_contexts;
                public Long query_total;
                public Long query_time_in_millis;
                public Long query_current;
                public Long fetch_total;
                public Long fetch_time_in_millis;
                public Long fetch_current;
            }
            public static class Merges {
                public Long current;
                public Long current_docs;
                public Long current_size_in_bytes;
                public Long total;
                public Long total_time_in_millis;
                public Long total_docs;
                public Long total_size_in_bytes;
            }
            public static class Refresh {
                public Long total;
                public Long total_time_in_millis;
            }
            public static class Flush {
                public Long total;
                public Long total_time_in_millis;
            }
            public static class Warmer {
                public Long current;
                public Long total;
                public Long total_time_in_millis;
            }
            public static class FilterCache {
                public Long memory_size_in_bytes;
                public Long evictions;
            }
            public static class IdCache {
                public Long memory_size_in_bytes;
            }
            public static class Fielddata {
                public Long memory_size_in_bytes;
                public Long evictions;
            }
            public static class Percolate {
                public Long total;
                public Long time_in_millis;
                public Long current;
                public Long memory_size_in_bytes;
                public String memory_size;
                public Long queries;
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
            public static class Translog {
                public Long operations;
                public Long size_in_bytes;
            }
            public static class Suggest {
                public Long total;
                public Long time_in_millis;
                public Long current;
            }
            public static class QueryCache {
                public Long memory_size_in_bytes;
                public Long evictions;
                public Long hit_count;
                public Long miss_count;
                public Long getHitRatio()
                {
                    return (hit_count > 0 && miss_count > 0)
                        ? hit_count / (hit_count + miss_count)
                        : null;
                }
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
                public Long sys;
                public Long user;
                public Long idle;
                public Long usage;
                public Long stolen;
            }
            public static class Mem {
                public Long free_in_bytes;
                public Long used_in_bytes;
                public Long free_percent;
                public Long used_percent;
                public Long actual_free_in_bytes;
                public Long actual_used_in_bytes;
            }
            public static class Swap {
                public Long used_in_bytes;
                public Long free_in_bytes;
            }

            public Long uptime_in_millis;
            public ArrayList<Double> load_average;
            public Cpu cpu;
            public Mem mem;
            public Swap swap;
        }
        public static class Process {
            public static class Cpu {
                public Long percent;
                public Long sys_in_millis;
                public Long user_in_millis;
                public Long total_in_millis;
            }
            public static class Mem {
                public Long resident_in_bytes;
                public Long share_in_bytes;
                public Long total_virtual_in_bytes;
            }

            public Long open_file_descriptors;
            public Cpu cpu;
            public Mem mem;

        }
        public static class Jvm {
            public static class Mem {
                public Long heap_used_in_bytes;
                public Long heap_used_percent;
                public Long heap_committed_in_bytes;
                public Long non_heap_used_in_bytes;
                public Long non_heap_committed_in_bytes;
            }
            public static class Pools {
                public static class PoolStats {
                    public Long used_in_bytes;
                    public Long max_in_bytes;
                    public Long peak_used_in_bytes;
                    public Long peak_max_in_bytes;
                }

                public PoolStats young;
                public PoolStats survivor;
                public PoolStats old;
            }
            public static class Threads {
                public Long count;
                public Long peak_count;
            }
            public static class Gc {
                public static class GcCollectors {
                    public static class GcCollector {
                        public Long collection_count;
                        public Long collection_time_in_millis;
                    }

                    public GcCollector young;
                    public GcCollector old;
                }
                public GcCollectors collectors;
            }
            public static class BufferPools {
                public static class BufferPoolStats {
                    public Long count;
                    public Long used_in_bytes;
                    public Long total_capacity_in_bytes;
                }

                public BufferPoolStats direct;
                public BufferPoolStats mapped;
            }

            public Long uptime_in_millis;
            public Mem mem;
            public Pools pools;
            public Threads threads;
            public Gc gc;
            public BufferPools buffer_pools;
        }
        public static class ThreadPool {
            public static class ThreadPoolStats {
                public Long threads;
                public Long queue;
                public Long active;
                public Long rejected;
                public Long largest;
                public Long completed;
            }

            public ThreadPoolStats generic;
            public ThreadPoolStats index;
            public ThreadPoolStats bench;
            public ThreadPoolStats get;
            public ThreadPoolStats snapshot;
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
                public Long active_opens;
                public Long passive_opens;
                public Long curr_estab;
                public Long in_segs;
                public Long out_segs;
                public Long retrans_segs;
                public Long estab_resets;
                public Long attempt_fails;
                public Long in_errs;
                public Long out_rsts;
            }

            public Tcp tcp;
        }
        public static class Fs {
            public static class FsTotal {
                public Long total_in_bytes;
                public Long free_in_bytes;
                public Long available_in_bytes;
                public Long disk_reads;
                public Long disk_writes;
                public Long disk_io_op;
                public Long disk_read_size_in_bytes;
                public Long disk_write_size_in_bytes;
                public Long disk_io_size_in_bytes;
            }
            public static class FsData {
                public String path;
                public String mount;
                public String dev;
                public Long total_in_bytes;
                public Long free_in_bytes;
                public Long available_in_bytes;
                public Long disk_reads;
                public Long disk_writes;
                public Long disk_io_op;
                public Long disk_read_size_in_bytes;
                public Long disk_write_size_in_bytes;
                public Long disk_io_size_in_bytes;
            }

            public FsTotal total;
            public ArrayList<FsData> data;
        }
        public static class Transport {
           public Long server_open;
           public Long rx_count;
           public Long rx_size_in_bytes;
           public Long tx_count;
           public Long tx_size_in_bytes;
        }
        public static class Http {
            public Long current_open;
            public Long total_opened;
        }
        public static class Breakers {
            public static class BreakerStats {
                public Long limit_size_in_bytes;
                public String limit_size;
                public Long estimated_size_in_bytes;
                public String estimated_size;
                public Double overhead;
                public Long tripped;
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

