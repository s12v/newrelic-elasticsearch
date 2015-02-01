package me.snov.newrelic.elasticsearch.dto;

import java.util.ArrayList;
import java.util.Map;

public class NodesStats {
    private class NodeStats {
        private class Indices {
            private class Docs {
                public Long count;
                public Long deleted;
            }
            private class Store {
                public Long size_in_bytes;
                public Long throttle_time_in_millis;
            }
            private class Indexing {
                public Long index_total;
                public Long index_time_in_millis;
                public Long index_current;
                public Long delete_total;
                public Long delete_time_in_millis;
                public Long delete_current;
                public Long noop_update_total;
                public Long is_throttled;
                public Long throttle_time_in_millis;
            }
            private class Get {
                public Long total;
                public Long time_in_millis;
                public Long exists_total;
                public Long exists_time_in_millis;
                public Long missing_total;
                public Long missing_time_in_millis;
                public Long current;
            }
            private class Search {
                public Long open_contexts;
                public Long query_total;
                public Long query_time_in_millis;
                public Long query_current;
                public Long fetch_total;
                public Long fetch_time_in_millis;
                public Long fetch_current;
            }
            private class Merges {
                public Long current;
                public Long current_docs;
                public Long current_size_in_bytes;
                public Long total;
                public Long total_time_in_millis;
                public Long total_docs;
                public Long total_size_in_bytes;
            }
            private class Refresh {
                public Long total;
                public Long total_time_in_millis;
            }
            private class Flush {
                public Long total;
                public Long total_time_in_millis;
            }
            private class Warmer {
                public Long current;
                public Long total;
                public Long total_time_in_millis;
            }
            private class FilterCache {
                public Long memory_size_in_bytes;
                public Long evictions;
            }
            private class IdCache {
                public Long memory_size_in_bytes;
            }
            private class Fielddata {
                public Long memory_size_in_bytes;
                public Long evictions;
            }
            private class Percolate {
                public Long total;
                public Long time_in_millis;
                public Long current;
                public Long memory_size_in_bytes;
                public String memory_size;
                public Long queries;
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
            private class Translog {
                public Long operations;
                public Long size_in_bytes;
            }
            private class Suggest {
                public Long total;
                public Long time_in_millis;
                public Long current;
            }
            private class QueryCache {
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
        private class Os {
            private class Cpu {
                public Long sys;
                public Long user;
                public Long idle;
                public Long usage;
                public Long stolen;
            }
            private class Mem {
                public Long free_in_bytes;
                public Long used_in_bytes;
                public Long free_percent;
                public Long used_percent;
                public Long actual_free_in_bytes;
                public Long actual_used_in_bytes;
            }
            private class Swap {
                public Long used_in_bytes;
                public Long free_in_bytes;
            }

            public Long uptime_in_millis;
            public ArrayList<Double> load_average;
            public Cpu cpu;
            public Mem mem;
            public Swap swap;
        }
        private class Process {
            private class Cpu {
                public Long percent;
                public Long sys_in_millis;
                public Long user_in_millis;
                public Long total_in_millis;
            }
            private class Mem {
                public Long resident_in_bytes;
                public Long share_in_bytes;
                public Long total_virtual_in_bytes;
            }

            public Long open_file_descriptors;
            public Cpu cpu;
            public Mem mem;

        }
        private class Jvm {
            private class Mem {
                public Long heap_used_in_bytes;
                public Long heap_used_percent;
                public Long heap_committed_in_bytes;
                public Long non_heap_used_in_bytes;
                public Long non_heap_committed_in_bytes;
            }
            private class Pools {
                private class PoolStats {
                    public Long used_in_bytes;
                    public Long max_in_bytes;
                    public Long peak_used_in_bytes;
                    public Long peak_max_in_bytes;
                }

                public PoolStats young;
                public PoolStats survivor;
                public PoolStats old;
            }
            private class Threads {
                public Long count;
                public Long peak_count;
            }
            private class Gc {
                private class GcCollector {
                    public Long collection_count;
                    public Long collection_time_in_millis;
                }

                public GcCollector young;
                // todo survivor?
                public GcCollector old;
            }
            private class BufferPools {
                private class BufferPoolStats {
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
        private class ThreadPool {
            private class ThreadPoolStats {
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
        private class Network {
            private class Tcp {
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
        private class Fs {
            private class FsTotal {
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
            private class FsData {
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
        private class Transport {
           public Long server_open;
           public Long rx_count;
           public Long rx_size_in_bytes;
           public Long tx_count;
           public Long tx_size_in_bytes;
        }
        private class Http {
            public Long current_open;
            public Long total_opened;
        }
        private class Breakers {
            private class BreakerStats {
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
        public Process processs;
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

