package me.snov.newrelic.elasticsearch.reporters;

import me.snov.newrelic.elasticsearch.interfaces.AgentInterface;
import me.snov.newrelic.elasticsearch.responses.NodesStats;
import me.snov.newrelic.elasticsearch.services.EpochCounterFactory;
import me.snov.newrelic.elasticsearch.services.NodesStatsService;

public class NodesStatsReporter {

    private final AgentInterface agent;
    private final EpochCounterFactory processorFactory;
    private final NodesStatsService nodesStatsService;

    public NodesStatsReporter(AgentInterface agent) {
        this.agent = agent;
        this.nodesStatsService = new NodesStatsService();
        this.processorFactory = new EpochCounterFactory();
    }

    public void reportNodesStats(NodesStats nodesStats) {
        if (nodesStats.nodes != null) {
            reportCalculatedClusterStats(nodesStats);
            for (NodesStats.NodeStats nodeStats : nodesStats.nodes.values()) {
                reportNodeStats(nodeStats);
            }
        }
    }

    private void reportCalculatedClusterStats(NodesStats nodesStats) {
        /******************* Queries stats *******************/

        NodesStatsService.QueriesStat queriesStat = nodesStatsService.getTotalNumberOfQueries(nodesStats);

        // Component/V1/QueriesPerSecond/*
        reportProcessedMetric("V1/QueriesPerSecond/Search", "requests/second", queriesStat.search);
        reportProcessedMetric("V1/QueriesPerSecond/Fetch", "requests/second", queriesStat.fetch);
        reportProcessedMetric("V1/QueriesPerSecond/Get", "requests/second", queriesStat.get);
        reportProcessedMetric("V1/QueriesPerSecond/Index", "requests/second", queriesStat.index);
        reportProcessedMetric("V1/QueriesPerSecond/Delete", "requests/second", queriesStat.delete);

        /******************* Max heap used, % *******************/
        int maxHeapPercent = 0;
        for (NodesStats.NodeStats nodeStats : nodesStats.nodes.values()) {
            if (nodeStats.jvm != null && nodeStats.jvm.mem.heap_used_percent.intValue() > maxHeapPercent) {
                maxHeapPercent = nodeStats.jvm.mem.heap_used_percent.intValue();
            }
        }

        // Max heap used, %
        // Component/V1/Summary/Jvm/Mem/MaxHeapUsedPercent
        agent.reportMetric("V1/Summary/Jvm/Mem/MaxHeapUsedPercent", "percent", maxHeapPercent);
    }

    private String nodeMetricName(String metricName, String nodeName) {
        return metricName + "/" + nodeName;
    }

    private void reportNodeMetric(String metricName, String units, String nodeName, Number value)
    {
        agent.reportMetric(nodeMetricName(metricName, nodeName), units, value);
    }

    private void reportNodeProcessedMetric(String metricName, String units, String nodeName, Number value)
    {
        Number processedValue = processorFactory.getProcessorForNode(metricName, nodeName).process(value);
        reportNodeMetric(metricName, units, nodeName, processedValue);
    }

    private void reportProcessedMetric(String metricName, String units, Number value)
    {
        Number processedValue = processorFactory.getProcessor(metricName).process(value);
        agent.reportMetric(metricName, units, processedValue);
    }

    private void reportNodeStats(NodesStats.NodeStats nodeStats) {
        String nodeName = nodeStats.name;

        reportGeneral(nodeStats, nodeName);
        reportIndexing(nodeStats, nodeName);
        reportSearch(nodeStats, nodeName);
        reportMerges(nodeStats, nodeName);
        reportCache(nodeStats, nodeName);
        reportSystem(nodeStats, nodeName);
        reportJvm(nodeStats, nodeName);
        reportIo(nodeStats, nodeName);
        reportNetwork(nodeStats, nodeName);
        reportThreadPool(nodeStats, nodeName);
    }

    /**
     * Thread pool
     */
    private void reportThreadPool(NodesStats.NodeStats nodeStats, String nodeName) {
        if (nodeStats.thread_pool != null) {
            // Search
            // Component/V1/NodeStats/ThreadPool/Search/Completed/*
            reportNodeProcessedMetric("V1/NodeStats/ThreadPool/Search/Completed", "threads/second", nodeName,
                    nodeStats.thread_pool.search.completed);

            // Search: queue
            // Component/V1/NodeStats/ThreadPool/Search/Queue/*
            reportNodeMetric("V1/NodeStats/ThreadPool/Search/Queue", "threads", nodeName,
                    nodeStats.thread_pool.search.queue);

            // Search: rejected
            // Component/V1/NodeStats/ThreadPool/Search/Rejected/*
            reportNodeProcessedMetric("V1/NodeStats/ThreadPool/Search/Rejected", "threads/second", nodeName,
                    nodeStats.thread_pool.search.rejected);

            // Get
            // Component/V1/NodeStats/ThreadPool/Get/Completed/*
            reportNodeProcessedMetric("V1/NodeStats/ThreadPool/Get/Completed", "threads/second", nodeName,
                    nodeStats.thread_pool.get.completed);

            // Get: queue
            // Component/V1/NodeStats/ThreadPool/Get/Queue/*
            reportNodeMetric("V1/NodeStats/ThreadPool/Get/Queue", "threads", nodeName,
                    nodeStats.thread_pool.get.queue);

            // Get: rejected
            // Component/V1/NodeStats/ThreadPool/Get/Rejected/*
            reportNodeProcessedMetric("V1/NodeStats/ThreadPool/Get/Rejected", "threads/second", nodeName,
                    nodeStats.thread_pool.get.rejected);

            // Suggest
            // Component/V1/NodeStats/ThreadPool/Suggest/Completed/*
            reportNodeProcessedMetric("V1/NodeStats/ThreadPool/Suggest/Completed", "threads/second", nodeName,
                    nodeStats.thread_pool.suggest.completed);

            // Suggest: queue
            // Component/V1/NodeStats/ThreadPool/Suggest/Queue/*
            reportNodeMetric("V1/NodeStats/ThreadPool/Suggest/Queue", "threads", nodeName,
                    nodeStats.thread_pool.suggest.queue);

            // Suggest: rejected
            // Component/V1/NodeStats/ThreadPool/Suggest/Rejected/*
            reportNodeProcessedMetric("V1/NodeStats/ThreadPool/Suggest/Rejected", "threads/second", nodeName,
                    nodeStats.thread_pool.suggest.rejected);

            // Index
            // Component/V1/NodeStats/ThreadPool/Index/Completed/*
            reportNodeProcessedMetric("V1/NodeStats/ThreadPool/Index/Completed", "threads/second", nodeName,
                    nodeStats.thread_pool.index.completed);

            // Index queue
            // Component/V1/NodeStats/ThreadPool/Index/Queue/*
            reportNodeMetric("V1/NodeStats/ThreadPool/Index/Queue", "threads", nodeName,
                    nodeStats.thread_pool.index.queue);

            // Index rejected
            // Component/V1/NodeStats/ThreadPool/Index/Rejected/*
            reportNodeProcessedMetric("V1/NodeStats/ThreadPool/Index/Rejected", "threads/second", nodeName,
                    nodeStats.thread_pool.index.rejected);

            if (nodeStats.thread_pool.force_merge != null) {
                // Merge
                // Component/V1/NodeStats/ThreadPool/Merge/Completed/*
                reportNodeProcessedMetric("V1/NodeStats/ThreadPool/Merge/Completed", "threads/second", nodeName,
                        nodeStats.thread_pool.force_merge.completed);

                // Merge: queue
                // Component/V1/NodeStats/ThreadPool/Merge/Queue/*
                reportNodeMetric("V1/NodeStats/ThreadPool/Merge/Queue", "threads", nodeName,
                        nodeStats.thread_pool.force_merge.queue);

                // Merge: rejected
                // Component/V1/NodeStats/ThreadPool/Merge/Rejected/*
                reportNodeProcessedMetric("V1/NodeStats/ThreadPool/Merge/Rejected", "threads/second", nodeName,
                        nodeStats.thread_pool.force_merge.rejected);
            } else if (nodeStats.thread_pool.merge != null) {
                // Merge
                // Component/V1/NodeStats/ThreadPool/Merge/Completed/*
                reportNodeProcessedMetric("V1/NodeStats/ThreadPool/Merge/Completed", "threads/second", nodeName,
                        nodeStats.thread_pool.merge.completed);

                // Merge: queue
                // Component/V1/NodeStats/ThreadPool/Merge/Queue/*
                reportNodeMetric("V1/NodeStats/ThreadPool/Merge/Queue", "threads", nodeName,
                        nodeStats.thread_pool.merge.queue);

                // Merge: rejected
                // Component/V1/NodeStats/ThreadPool/Merge/Rejected/*
                reportNodeProcessedMetric("V1/NodeStats/ThreadPool/Merge/Rejected", "threads/second", nodeName,
                        nodeStats.thread_pool.merge.rejected);
            }

            // Bulk
            // Component/V1/NodeStats/ThreadPool/Bulk/Completed/*
            reportNodeProcessedMetric("V1/NodeStats/ThreadPool/Bulk/Completed", "threads/second", nodeName,
                    nodeStats.thread_pool.bulk.completed);

            // Bulk: queue
            // Component/V1/NodeStats/ThreadPool/Bulk/Queue/*
            reportNodeMetric("V1/NodeStats/ThreadPool/Bulk/Queue", "threads", nodeName,
                    nodeStats.thread_pool.bulk.queue);

            // Bulk: rejected
            // Component/V1/NodeStats/ThreadPool/Bulk/Rejected/*
            reportNodeProcessedMetric("V1/NodeStats/ThreadPool/Bulk/Rejected", "threads/second", nodeName,
                    nodeStats.thread_pool.bulk.rejected);

            // Warmer
            // Component/V1/NodeStats/ThreadPool/Warmer/Completed/*
            reportNodeProcessedMetric("V1/NodeStats/ThreadPool/Warmer/Completed", "threads/second", nodeName,
                    nodeStats.thread_pool.warmer.completed);

            // Warmer: queue
            // Component/V1/NodeStats/ThreadPool/Warmer/Queue/*
            reportNodeMetric("V1/NodeStats/ThreadPool/Warmer/Queue", "threads", nodeName,
                    nodeStats.thread_pool.warmer.queue);

            // Warmer: rejected
            // Component/V1/NodeStats/ThreadPool/Warmer/Rejected/*
            reportNodeProcessedMetric("V1/NodeStats/ThreadPool/Warmer/Rejected", "threads/second", nodeName,
                    nodeStats.thread_pool.warmer.rejected);

            // Flush
            // Component/V1/NodeStats/ThreadPool/Flush/Completed/*
            reportNodeProcessedMetric("V1/NodeStats/ThreadPool/Flush/Completed", "threads/second", nodeName,
                    nodeStats.thread_pool.flush.completed);

            // Flush: queue
            // Component/V1/NodeStats/ThreadPool/Flush/Queue/*
            reportNodeMetric("V1/NodeStats/ThreadPool/Flush/Queue", "threads", nodeName,
                    nodeStats.thread_pool.flush.queue);

            // Flush: rejected
            // Component/V1/NodeStats/ThreadPool/Flush/Rejected/*
            reportNodeProcessedMetric("V1/NodeStats/ThreadPool/Flush/Rejected", "threads/second", nodeName,
                    nodeStats.thread_pool.flush.rejected);

            // Refresh
            // Component/V1/NodeStats/ThreadPool/Refresh/Completed/*
            reportNodeProcessedMetric("V1/NodeStats/ThreadPool/Refresh/Completed", "threads/second", nodeName,
                    nodeStats.thread_pool.refresh.completed);

            // Refresh: queue
            // Component/V1/NodeStats/ThreadPool/Refresh/Queue/*
            reportNodeMetric("V1/NodeStats/ThreadPool/Refresh/Queue", "threads", nodeName,
                    nodeStats.thread_pool.refresh.queue);

            // Refresh: rejected
            // Component/V1/NodeStats/ThreadPool/Refresh/Rejected/*
            reportNodeProcessedMetric("V1/NodeStats/ThreadPool/Refresh/Rejected", "threads/second", nodeName,
                    nodeStats.thread_pool.refresh.rejected);
        }
    }

    /**
     * Network
     */
    private void reportNetwork(NodesStats.NodeStats nodeStats, String nodeName) {
        if (nodeStats.transport != null) {
            // Opened transport connections
            // Component/V1/NodeStats/Transport/ServerOpen/*
            reportNodeMetric("V1/NodeStats/Transport/ServerOpen", "connections", nodeName,
                    nodeStats.transport.server_open);

            // Transmit
            // Component/V1/NodeStats/Transport/TxSizeInBytes/*
            reportNodeProcessedMetric("V1/NodeStats/Transport/TxSizeInBytes", "bytes/second", nodeName,
                    nodeStats.transport.tx_size_in_bytes);

            // Receive
            // Component/V1/NodeStats/Transport/RxSizeInBytes/*
            reportNodeProcessedMetric("V1/NodeStats/Transport/RxSizeInBytes", "bytes/second", nodeName,
                    nodeStats.transport.rx_size_in_bytes);
        }

        if (nodeStats.http != null) {
            // Client connections
            // Component/V1/NodeStats/Http/CurrentOpen/*
            reportNodeMetric("V1/NodeStats/Http/CurrentOpen", "connections", nodeName,
                    nodeStats.http.current_open);
        }
    }

    /**
     * I/O
     */
    private void reportIo(NodesStats.NodeStats nodeStats, String nodeName) {
        if (nodeStats.fs != null) {
            // Disk reads
            // Component/V1/NodeStats/Fs/Total/DiskReadSizeInBytes/*
            reportNodeProcessedMetric("V1/NodeStats/Fs/Total/DiskReadSizeInBytes", "bytes/second", nodeName,
                    nodeStats.fs.total.disk_read_size_in_bytes);

            // Disk writes
            // Component/V1/NodeStats/Fs/Total/DiskWriteSizeInBytes/*
            reportNodeProcessedMetric("V1/NodeStats/Fs/Total/DiskWriteSizeInBytes", "bytes/second", nodeName,
                    nodeStats.fs.total.disk_write_size_in_bytes);

            // Open file descriptors
            // Component/V1/NodeStats/Process/OpenFileDescriptors/*
            reportNodeMetric("V1/NodeStats/Process/OpenFileDescriptors", "descriptors", nodeName,
                    nodeStats.process.open_file_descriptors);

            // Store throttle time
            // Component/V1/NodeStats/Indices/Store/ThrottleTimeInMillis/*
            reportNodeProcessedMetric("V1/NodeStats/Indices/Store/ThrottleTimeInMillis", "milliseconds", nodeName,
                    nodeStats.indices.store.throttle_time_in_millis);
        }
    }

    /**
     * JVM
     */
    private void reportJvm(NodesStats.NodeStats nodeStats, String nodeName) {
        if (nodeStats.jvm != null) {
            // Heap used, %
            // Component/V1/NodeStats/Jvm/Mem/HeapUsedPercent/*
            reportNodeMetric("V1/NodeStats/Jvm/Mem/HeapUsedPercent", "percent", nodeName,
                    nodeStats.jvm.mem.heap_used_percent);

            // Heap used, bytes
            // Component/V1/NodeStats/Jvm/Mem/HeapUsedInBytes/*
            reportNodeMetric("V1/NodeStats/Jvm/Mem/HeapUsedInBytes", "bytes", nodeName,
                    nodeStats.jvm.mem.heap_used_in_bytes);

            // Non-Heap used, bytes
            // Component/V1/NodeStats/Jvm/Mem/NonHeapUsedInBytes/*
            reportNodeMetric("V1/NodeStats/Jvm/Mem/NonHeapUsedInBytes", "bytes", nodeName,
                    nodeStats.jvm.mem.non_heap_used_in_bytes);

            // GC collections (old)
            // Component/V1/NodeStats/Jvm/Gc/Old/CollectionCount/*
            reportNodeProcessedMetric("V1/NodeStats/Jvm/Gc/Old/CollectionCount", "collections/second", nodeName,
                    nodeStats.jvm.gc.collectors.old.collection_count);

            // GC collection time (old)
            // Component/V1/NodeStats/Jvm/Gc/Old/CollectionTime/*
            reportNodeProcessedMetric("V1/NodeStats/Jvm/Gc/Old/CollectionTime", "milliseconds", nodeName,
                    nodeStats.jvm.gc.collectors.old.collection_time_in_millis);

            // GC collections (young)
            // Component/V1/NodeStats/Jvm/Gc/Young/CollectionCount/*
            reportNodeProcessedMetric("V1/NodeStats/Jvm/Gc/Young/CollectionCount", "collections/second", nodeName,
                    nodeStats.jvm.gc.collectors.young.collection_count);

            // GC collection time (young)
            // Component/V1/NodeStats/Jvm/Gc/Young/CollectionTime/*
            reportNodeProcessedMetric("V1/NodeStats/Jvm/Gc/Young/CollectionTime", "milliseconds", nodeName,
                    nodeStats.jvm.gc.collectors.young.collection_time_in_millis);

            // JVM uptime
            // Component/V1/NodeStats/Jvm/UptimeInMillis/*
            reportNodeMetric("V1/NodeStats/Jvm/UptimeInMillis", "milliseconds", nodeName, nodeStats.jvm.uptime_in_millis);
        }
    }

    /**
     * System
     */
    private void reportSystem(NodesStats.NodeStats nodeStats, String nodeName) {
        if (nodeStats.os == null) {
            return;
        }

        if (nodeStats.process != null && nodeStats.process.cpu != null && nodeStats.process.cpu.percent != null) {
            // CPU used, %
            // Component/V1/NodeStats/Os/Cpu/Usage/*
            reportNodeMetric("V1/NodeStats/Os/Cpu/Usage", "percent", nodeName, nodeStats.process.cpu.percent);
        }

        if (nodeStats.os.mem != null) {
            // Memory used, %
            // Component/V1/NodeStats/Os/Mem/UsedPercent/*
            reportNodeMetric("V1/NodeStats/Os/Mem/UsedPercent", "percent", nodeName,
                    nodeStats.os.mem.used_percent);

            // Memory used
            // Component/V1/NodeStats/Os/Mem/UsedInBytes/*
            reportNodeMetric("V1/NodeStats/Os/Mem/UsedInBytes", "bytes", nodeName,
                    nodeStats.os.mem.used_in_bytes);
        }

        // Load average
        // Component/V1/NodeStats/Os/LoadAverage/*
        if (nodeStats.os.getLoadAverage() != null) {
            reportNodeMetric("V1/NodeStats/Os/LoadAverage", "units", nodeName, nodeStats.os.getLoadAverage());
        }

        // Uptime
        // Component/V1/NodeStats/Os/UptimeInMillis/*
        if (nodeStats.os.uptime_in_millis != null) {
            reportNodeMetric("V1/NodeStats/Os/UptimeInMillis", "milliseconds", nodeName,
                    nodeStats.os.uptime_in_millis);
        }

        if (nodeStats.os.swap != null) {
            // Swap usage
            // Component/V1/NodeStats/Os/Swap/Percent/*
            Long swapUsed = 0l;
            if (nodeStats.os.swap.used_in_bytes != null && nodeStats.os.swap.free_in_bytes != null) {
                Long swapTotal = nodeStats.os.swap.used_in_bytes.longValue() + nodeStats.os.swap.free_in_bytes.longValue();
                swapUsed = swapTotal > 0
                        ? nodeStats.os.swap.used_in_bytes.longValue() / swapTotal
                        : 0;
            }
            reportNodeMetric("V1/NodeStats/Os/Swap/Percent", "percent", nodeName, swapUsed);
        }
    }

    /**
     * Cache
     */
    private void reportCache(NodesStats.NodeStats nodeStats, String nodeName) {
        if (nodeStats.indices.filter_cache != null) {
            // Filter cache
            // Component/V1/NodeStats/Indices/FilterCache/Size/*
            reportNodeMetric("V1/NodeStats/Indices/FilterCache/Size", "bytes", nodeName,
                    nodeStats.indices.filter_cache.memory_size_in_bytes);

            // Filter evictions
            // Component/V1/NodeStats/Indices/FilterCache/Evictions/*
            reportNodeProcessedMetric("V1/NodeStats/Indices/FilterCache/Evictions", "evictions/second", nodeName,
                    nodeStats.indices.filter_cache.evictions);
        }

        if (nodeStats.indices.fielddata != null) {
            // Field data
            // Component/V1/NodeStats/Indices/Fielddata/Size/*
            reportNodeMetric("V1/NodeStats/Indices/Fielddata/Size", "bytes", nodeName,
                    nodeStats.indices.fielddata.memory_size_in_bytes);

            // Field evictions
            // Component/V1/NodeStats/Indices/Fielddata/Evictions/*
            reportNodeProcessedMetric("V1/NodeStats/Indices/Fielddata/Evictions", "evictions/second", nodeName,
                    nodeStats.indices.fielddata.evictions);
        }

        if (nodeStats.indices.id_cache != null) {
            // Id cache
            // Component/V1/NodeStats/Indices/IdCache/Size/*
            reportNodeMetric("V1/NodeStats/Indices/IdCache/Size", "bytes", nodeName,
                    nodeStats.indices.id_cache.memory_size_in_bytes);
        }

        if (nodeStats.indices.completion != null) {
            // Completion
            // Component/V1/NodeStats/Indices/Completion/Size/*
            reportNodeMetric("V1/NodeStats/Indices/Completion/Size", "bytes", nodeName,
                    nodeStats.indices.completion.size_in_bytes);
        }
    }

    /**
     * Merges
     */
    private void reportMerges(NodesStats.NodeStats nodeStats, String nodeName) {
        if (nodeStats.indices.merges != null) {
            // Merges
            // Component/V1/NodeStats/Indices/Merges/Total/*
            reportNodeProcessedMetric("V1/NodeStats/Indices/Merges/Total", "merges/second", nodeName,
                    nodeStats.indices.merges.total);

            // Merge size
            // Component/V1/NodeStats/Indices/Merges/TotalSizeInBytes/*
            reportNodeProcessedMetric("V1/NodeStats/Indices/Merges/TotalSizeInBytes", "bytes/second", nodeName,
                    nodeStats.indices.merges.total_size_in_bytes);

            // Merge time
            // Component/V1/NodeStats/Indices/Merges/TotalTimeInMillis/*
            reportNodeProcessedMetric("V1/NodeStats/Indices/Merges/TotalTimeInMillis", "milliseconds", nodeName,
                    nodeStats.indices.merges.total_time_in_millis);

            // Merged docs
            // Component/V1/NodeStats/Indices/Merges/TotalDocs/*
            reportNodeProcessedMetric("V1/NodeStats/Indices/Merges/TotalDocs", "docs/second", nodeName,
                    nodeStats.indices.merges.total_docs);
        }

        if (nodeStats.indices.segments != null) {
            // Component/V1/NodeStats/Indices/Segments/Count/*
            reportNodeMetric("V1/NodeStats/Indices/Segments/Count", "segments", nodeName,
                    nodeStats.indices.segments.count);
        }
    }

    /**
     * Search
     */
    private void reportSearch(NodesStats.NodeStats nodeStats, String nodeName) {
        // Query
        // Component/V1/NodeStats/Indices/Search/QueryTotal/*
        reportNodeProcessedMetric("V1/NodeStats/Indices/Search/QueryTotal", "requests/second", nodeName,
                nodeStats.indices.search.query_total);

        // Query time
        // Component/V1/NodeStats/Indices/Search/QueryTimeInMillis/*
        reportNodeProcessedMetric("V1/NodeStats/Indices/Search/QueryTimeInMillis", "milliseconds", nodeName,
                nodeStats.indices.search.query_time_in_millis);

        // Fetch
        // Component/V1/NodeStats/Indices/Search/FetchTotal/*
        reportNodeProcessedMetric("V1/NodeStats/Indices/Search/FetchTotal", "requests/second", nodeName,
                nodeStats.indices.search.fetch_total);

        // Fetch time
        // Component/V1/NodeStats/Indices/Search/FetchTimeInMillis/*
        reportNodeProcessedMetric("V1/NodeStats/Indices/Search/FetchTimeInMillis", "milliseconds", nodeName,
                nodeStats.indices.search.fetch_time_in_millis);

        // Get
        // Component/V1/NodeStats/Indices/Get/Total/*
        reportNodeProcessedMetric("V1/NodeStats/Indices/Get/Total", "requests/second", nodeName,
                nodeStats.indices.get.total);

        // Get time
        // Component/V1/NodeStats/Indices/Get/TimeInMillis/*
        reportNodeProcessedMetric("V1/NodeStats/Indices/Get/TimeInMillis", "milliseconds", nodeName,
                nodeStats.indices.get.time_in_millis);

        if (nodeStats.indices.suggest != null) {
            // Suggest
            // Component/V1/NodeStats/Indices/Suggest/Total/*
            reportNodeProcessedMetric("V1/NodeStats/Indices/Suggest/Total", "requests/second", nodeName,
                    nodeStats.indices.suggest.total);

            // Suggest time
            // Component/V1/NodeStats/Indices/Suggest/TimeInMillis/*
            reportNodeProcessedMetric("V1/NodeStats/Indices/Suggest/TimeInMillis", "milliseconds", nodeName,
                    nodeStats.indices.suggest.time_in_millis);
        }
    }

    /**
     * Indexing
     */
    private void reportIndexing(NodesStats.NodeStats nodeStats, String nodeName) {
        // Index
        // Component/V1/NodeStats/Indices/Indexing/Index/*
        reportNodeProcessedMetric("V1/NodeStats/Indices/Indexing/Index", "operations/second", nodeName,
                nodeStats.indices.indexing.index_total);

        // Index time
        // Component/V1/NodeStats/Indices/Indexing/IndexTimeInMillis/*
        reportNodeProcessedMetric("V1/NodeStats/Indices/Indexing/IndexTimeInMillis", "milliseconds", nodeName,
                nodeStats.indices.indexing.index_time_in_millis);

        // Delete
        // Component/V1/NodeStats/Indices/Indexing/DeleteTotal/*
        reportNodeProcessedMetric("V1/NodeStats/Indices/Indexing/DeleteTotal", "operations/second", nodeName,
                nodeStats.indices.indexing.delete_total);

        // Delete time
        // Component/V1/NodeStats/Indices/Indexing/DeleteTimeInMillis/*
        reportNodeProcessedMetric("V1/NodeStats/Indices/Indexing/DeleteTimeInMillis", "milliseconds", nodeName,
                nodeStats.indices.indexing.delete_time_in_millis);

        if (nodeStats.indices.refresh != null) {
            // Refresh
            // Component/V1/NodeStats/Indices/Refresh/Total/*
            reportNodeProcessedMetric("V1/NodeStats/Indices/Refresh/Total", "operations/second", nodeName,
                    nodeStats.indices.refresh.total);

            // Refresh time
            // Component/V1/NodeStats/Indices/Refresh/TotalTimeInMillis/*
            reportNodeProcessedMetric("V1/NodeStats/Indices/Refresh/TotalTimeInMillis", "milliseconds", nodeName,
                    nodeStats.indices.refresh.total_time_in_millis);
        }

        if (nodeStats.indices.flush != null) {
            // Flush
            // Component/V1/NodeStats/Indices/Flush/Total/*
            reportNodeProcessedMetric("V1/NodeStats/Indices/Flush/Total", "operations/second", nodeName,
                    nodeStats.indices.flush.total);

            // Flush time
            // Component/V1/NodeStats/Indices/Flush/TotalTimeInMillis/*
            reportNodeProcessedMetric("V1/NodeStats/Indices/Flush/TotalTimeInMillis", "milliseconds", nodeName,
                    nodeStats.indices.flush.total_time_in_millis);
        }

        if (nodeStats.indices.warmer != null) {
            // Warmer
            // Component/V1/NodeStats/Indices/Warmer/Total/*
            reportNodeProcessedMetric("V1/NodeStats/Indices/Warmer/Total", "operations/second", nodeName,
                    nodeStats.indices.warmer.total);

            // Warmer time
            // Component/V1/NodeStats/Indices/Warmer/TotalTimeInMillis/*
            reportNodeProcessedMetric("V1/NodeStats/Indices/Warmer/TotalTimeInMillis", "milliseconds", nodeName,
                    nodeStats.indices.warmer.total_time_in_millis);
        }
    }

    /**
     * Nodes
     */
    private void reportGeneral(NodesStats.NodeStats nodeStats, String nodeName) {
        // Documents
        // Component/V1/NodeStats/Nodes/Indices/Docs/Count/*
        reportNodeMetric("V1/NodeStats/Nodes/Indices/Docs/Count", "documents", nodeName,
                nodeStats.indices.docs.count);

        // Store size
        // Component/V1/NodeStats/Indices/Store/Size/*
        reportNodeMetric("V1/NodeStats/Indices/Store/Size", "bytes", nodeName,
                nodeStats.indices.store.size_in_bytes);

        // Deleted documents
        // Component/V1/NodeStats/Nodes/Indices/Docs/Deleted/*
        reportNodeMetric("V1/NodeStats/Nodes/Indices/Docs/Deleted", "documents", nodeName,
                nodeStats.indices.docs.deleted);
    }
}
