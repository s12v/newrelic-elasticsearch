package me.snov.newrelic.elasticsearch;

import com.newrelic.metrics.publish.Agent;
import com.newrelic.metrics.publish.configuration.ConfigurationException;
import com.newrelic.metrics.publish.util.Logger;

import me.snov.newrelic.elasticsearch.response.ClusterStats;
import me.snov.newrelic.elasticsearch.response.NodesStats;
import me.snov.newrelic.elasticsearch.service.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

/**
 * Agent for Elasticsearch cluster
 */
public class ElasticsearchAgent extends Agent {

    private static final String GUID = "me.snov.newrelic-elasticsearch";
    private static final String VERSION = "1.0.0";

    private final String clusterName;
    private final ClusterStatsParser clusterStatsParser;
    private final ClusterStatsService clusterStatsService;
    private final NodesStatsService nodesStatsService;
    private final NodesStatsParser nodesStatsParser;
    private final EpochCounterFactory processorFactory;
    private final Logger logger;

    /**
     * Constructor for Elastisearch Agent
     */
    public ElasticsearchAgent(String host, Integer port) throws ConfigurationException {
        super(GUID, VERSION);
        try {
            logger = Logger.getLogger(ElasticsearchAgent.class);
            clusterStatsParser = new ClusterStatsParser(host, port);
            nodesStatsParser = new NodesStatsParser(host, port);
            processorFactory = new EpochCounterFactory();
            clusterStatsService = new ClusterStatsService();
            nodesStatsService = new NodesStatsService();
            clusterName = getClusterName();
        } catch (MalformedURLException e) {
            throw new ConfigurationException("URL could not be parsed", e);
        } catch (IOException e) {
            throw new ConfigurationException(
                String.format("Can't connect to elasticsearch at %s:%d", host, port),
                e
            );
        }
    }

    private String getClusterName() throws IOException {
        return clusterStatsParser.request().cluster_name;
    }

    @Override
    public String getAgentName() {
        return clusterName;
    }

    @Override
    public void pollCycle() {
        try {
            reportClusterStats(clusterStatsParser.request());
            reportNodesStats(nodesStatsParser.request());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void reportClusterStats(ClusterStats clusterStats) {

        /******************* Cluster *******************/

        // Component/V1/ClusterStats/Indices/Docs/*
        reportMetric("V1/ClusterStats/Indices/Docs/Count", "documents", clusterStats.indices.docs.count);
        reportMetric("V1/ClusterStats/Indices/Docs/Deleted", "documents", clusterStats.indices.docs.deleted);

        // Nodes (table)
        // Component/V1/ClusterStats/Nodes/Count/*
        reportMetric("V1/ClusterStats/Nodes/Count/Total", "nodes", clusterStats.nodes.count.total);
        reportMetric("V1/ClusterStats/Nodes/Count/Master and data", "nodes", clusterStats.nodes.count.master_data);
        reportMetric("V1/ClusterStats/Nodes/Count/Master only", "nodes", clusterStats.nodes.count.master_only);
        reportMetric("V1/ClusterStats/Nodes/Count/Data only", "nodes", clusterStats.nodes.count.data_only);
        reportMetric("V1/ClusterStats/Nodes/Count/Client", "nodes", clusterStats.nodes.count.client);

        // Indices and Shards (table)
        // Component/V1/ClusterStats/Indices/Group1/*
        reportMetric("V1/ClusterStats/Indices/Group1/Indices", "indices", clusterStats.indices.count);
        reportMetric("V1/ClusterStats/Indices/Group1/Shards", "shards", clusterStats.indices.shards.total);
        reportMetric("V1/ClusterStats/Indices/Group1/Primaries", "shards", clusterStats.indices.shards.total);
        reportMetric("V1/ClusterStats/Indices/Group1/Replication", "shards", clusterStats.indices.shards.replication);

        // Component/V1/ClusterStats/Indices/Segments/Count
        reportMetric("V1/ClusterStats/Indices/Segments/Count", "segments", clusterStats.indices.segments.count);

        // Component/V1/ClusterStats/Indices/Store/Size
        reportMetric("V1/ClusterStats/Indices/Store/Size", "bytes", clusterStats.indices.store.size_in_bytes);


        /******************* Summary *******************/

        // Component/V1/ClusterStats/NumberOfVersionsInCluster
        reportMetric("V1/ClusterStats/NumberOfVersionsInCluster", "versions",
            clusterStatsService.getNumberOfVersionsInCluster(clusterStats));
    }

    private void reportNodeStats(NodesStats.NodeStats nodeStats) {
        String nodeName = nodeStats.name;

        /******************* Nodes *******************/

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

        /******************* Indexing *******************/

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

        // Refresh
        // Component/V1/NodeStats/Indices/Refresh/Total/*
        reportNodeProcessedMetric("V1/NodeStats/Indices/Refresh/Total", "operations/second", nodeName,
            nodeStats.indices.refresh.total);

        // Refresh time
        // Component/V1/NodeStats/Indices/Refresh/TotalTimeInMillis/*
        reportNodeProcessedMetric("V1/NodeStats/Indices/Refresh/TotalTimeInMillis", "milliseconds", nodeName,
            nodeStats.indices.refresh.total_time_in_millis);

        // Flush
        // Component/V1/NodeStats/Indices/Flush/Total/*
        reportNodeProcessedMetric("V1/NodeStats/Indices/Flush/Total", "operations/second", nodeName,
            nodeStats.indices.flush.total);

        // Flush time
        // Component/V1/NodeStats/Indices/Flush/TotalTimeInMillis/*
        reportNodeProcessedMetric("V1/NodeStats/Indices/Flush/TotalTimeInMillis", "milliseconds", nodeName,
            nodeStats.indices.flush.total_time_in_millis);

        // Warmer
        // Component/V1/NodeStats/Indices/Warmer/Total/*
        reportNodeProcessedMetric("V1/NodeStats/Indices/Warmer/Total", "operations/second", nodeName,
            nodeStats.indices.warmer.total);

        // Warmer time
        // Component/V1/NodeStats/Indices/Warmer/TotalTimeInMillis/*
        reportNodeProcessedMetric("V1/NodeStats/Indices/Warmer/TotalTimeInMillis", "milliseconds", nodeName,
            nodeStats.indices.warmer.total_time_in_millis);


        /******************* Search *******************/

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

        // Suggest
        // Component/V1/NodeStats/Indices/Suggest/Total/*
        reportNodeProcessedMetric("V1/NodeStats/Indices/Suggest/Total", "requests/second", nodeName,
            nodeStats.indices.suggest.total);

        // Suggest time
        // Component/V1/NodeStats/Indices/Suggest/TimeInMillis/*
        reportNodeProcessedMetric("V1/NodeStats/Indices/Suggest/TimeInMillis", "milliseconds", nodeName,
            nodeStats.indices.suggest.time_in_millis);


        /******************* Merges *******************/

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

        // Merged segments
        // Component/V1/NodeStats/Indices/Segments/Count/*
        reportNodeProcessedMetric("V1/NodeStats/Indices/Segments/Count", "segments/second", nodeName,
            nodeStats.indices.segments.count);


        /******************* Cache *******************/

        // Filter cache
        // Component/V1/NodeStats/Indices/FilterCache/Size/*
        reportNodeMetric("V1/NodeStats/Indices/FilterCache/Size", "bytes", nodeName,
            nodeStats.indices.filter_cache.memory_size_in_bytes);

        // Filter evictions
        // Component/V1/NodeStats/Indices/FilterCache/Evictions/*
        reportNodeProcessedMetric("V1/NodeStats/Indices/FilterCache/Evictions", "evictions/second", nodeName,
            nodeStats.indices.filter_cache.evictions);

        // Field data
        // Component/V1/NodeStats/Indices/Fielddata/Size/*
        reportNodeMetric("V1/NodeStats/Indices/Fielddata/Size", "bytes", nodeName,
            nodeStats.indices.fielddata.memory_size_in_bytes);

        // Field evictions
        // Component/V1/NodeStats/Indices/Fielddata/Evictions/*
        reportNodeProcessedMetric("V1/NodeStats/Indices/Fielddata/Evictions", "evictions/second", nodeName,
            nodeStats.indices.fielddata.evictions);

        // Id cache
        // Component/V1/NodeStats/Indices/IdCache/Size/*
        reportNodeMetric("V1/NodeStats/Indices/IdCache/Size", "bytes", nodeName,
            nodeStats.indices.id_cache.memory_size_in_bytes);

        // Completion
        // Component/V1/NodeStats/Indices/Completion/Size/*
        reportNodeMetric("V1/NodeStats/Indices/Completion/Size", "bytes", nodeName,
            nodeStats.indices.completion.size_in_bytes);


        /******************* System *******************/

        // CPU used, %
        // Component/V1/NodeStats/Os/Cpu/Usage/*
        reportNodeMetric("V1/NodeStats/Os/Cpu/Usage", "percent", nodeName,
            nodeStats.os.cpu.usage);

        // Memory used, %
        // Component/V1/NodeStats/Os/Mem/UsedPercent/*
        reportNodeMetric("V1/NodeStats/Os/Mem/UsedPercent", "percent", nodeName,
            nodeStats.os.mem.used_percent);

        // Memory used
        // Component/V1/NodeStats/Os/Mem/UsedInBytes/*
        reportNodeMetric("V1/NodeStats/Os/Mem/UsedInBytes", "bytes", nodeName,
            nodeStats.os.mem.used_in_bytes);

        // Load average
        // Component/V1/NodeStats/Os/LoadAverage/*
        if (nodeStats.os.load_average != null && nodeStats.os.load_average.size() > 0) {
            reportNodeMetric("V1/NodeStats/Os/LoadAverage", "units", nodeName,
                nodeStats.os.load_average.get(0));
        }

        // Swap usage
        // Component/V1/NodeStats/Os/Swap/Percent/*
        Long swap_used = 0l;
        if (nodeStats.os.swap.used_in_bytes != null && nodeStats.os.swap.free_in_bytes != null) {
            Long swap_total = nodeStats.os.swap.used_in_bytes + nodeStats.os.swap.free_in_bytes;
            swap_used = swap_total > 0
                ? nodeStats.os.swap.used_in_bytes / swap_total
                : 0;
        }
        reportNodeMetric("V1/NodeStats/Os/Swap/Percent", "percent", nodeName, swap_used);


        /******************* JVM *******************/

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

        /******************* I/O *******************/

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


        /******************* Network *******************/

        // Transport connections
        // Component/V1/NodeStats/Transport/ServerOpen/*
        reportNodeProcessedMetric("V1/NodeStats/Transport/ServerOpen", "connections", nodeName,
            nodeStats.transport.server_open);

        // Client connections
        // Component/V1/NodeStats/Http/TotalOpened/*
        reportNodeProcessedMetric("V1/NodeStats/Http/TotalOpened", "connections", nodeName,
            nodeStats.http.total_opened);

        // Transmit
        // Component/V1/NodeStats/Transport/TxSizeInBytes/*
        reportNodeProcessedMetric("V1/NodeStats/Transport/TxSizeInBytes", "bytes/second", nodeName,
            nodeStats.transport.tx_size_in_bytes);

        // Receive
        // Component/V1/NodeStats/Transport/RxSizeInBytes/*
        reportNodeProcessedMetric("V1/NodeStats/Transport/RxSizeInBytes", "bytes/second", nodeName,
                nodeStats.transport.rx_size_in_bytes);


        /******************* Thread pool *******************/

        // Search
        // Component/V1/NodeStats/ThreadPool/Search/Completed/*
        reportNodeProcessedMetric("V1/NodeStats/ThreadPool/Search/Completed", "threads", nodeName,
            nodeStats.thread_pool.search.completed);

        // Search queue
        // Component/V1/NodeStats/ThreadPool/Search/Queue/*
        reportNodeMetric("V1/NodeStats/ThreadPool/Search/Queue", "threads", nodeName,
            nodeStats.thread_pool.search.queue);

        // Index
        // Component/V1/NodeStats/ThreadPool/Index/Completed/*
        reportNodeProcessedMetric("V1/NodeStats/ThreadPool/Index/Completed", "threads", nodeName,
            nodeStats.thread_pool.index.completed);

        // Index queue
        // Component/V1/NodeStats/ThreadPool/Index/Queue/*
        reportNodeMetric("V1/NodeStats/ThreadPool/Index/Queue", "threads", nodeName,
            nodeStats.thread_pool.index.queue);

        // Bulk
        // Component/V1/NodeStats/ThreadPool/Bulk/Completed/*
        reportNodeProcessedMetric("V1/NodeStats/ThreadPool/Bulk/Completed", "threads", nodeName,
            nodeStats.thread_pool.bulk.completed);

        // Bulk queue
        // Component/V1/NodeStats/ThreadPool/Bulk/Queue/*
        reportNodeMetric("V1/NodeStats/ThreadPool/Bulk/Queue", "threads", nodeName,
            nodeStats.thread_pool.bulk.queue);

        // Get
        // Component/V1/NodeStats/ThreadPool/Get/Completed/*
        reportNodeProcessedMetric("V1/NodeStats/ThreadPool/Get/Completed", "threads", nodeName,
            nodeStats.thread_pool.get.completed);

        // Get queue
        // Component/V1/NodeStats/ThreadPool/Get/Queue/*
        reportNodeMetric("V1/NodeStats/ThreadPool/Get/Queue", "threads", nodeName,
            nodeStats.thread_pool.get.queue);

        // Merge
        // Component/V1/NodeStats/ThreadPool/Merge/Completed/*
        reportNodeProcessedMetric("V1/NodeStats/ThreadPool/Merge/Completed", "threads", nodeName,
            nodeStats.thread_pool.merge.completed);

        // Merge queue
        // Component/V1/NodeStats/ThreadPool/Merge/Queue/*
        reportNodeMetric("V1/NodeStats/ThreadPool/Merge/Queue", "threads", nodeName,
            nodeStats.thread_pool.merge.queue);

        // Suggest
        // Component/V1/NodeStats/ThreadPool/Suggest/Completed/*
        reportNodeProcessedMetric("V1/NodeStats/ThreadPool/Suggest/Completed", "threads", nodeName,
            nodeStats.thread_pool.suggest.completed);

        // Suggest queue
        // Component/V1/NodeStats/ThreadPool/Suggest/Queue/*
        reportNodeMetric("V1/NodeStats/ThreadPool/Suggest/Queue", "threads", nodeName,
            nodeStats.thread_pool.suggest.queue);

        // Warmer
        // Component/V1/NodeStats/ThreadPool/Warmer/Completed/*
        reportNodeProcessedMetric("V1/NodeStats/ThreadPool/Warmer/Completed", "threads", nodeName,
            nodeStats.thread_pool.warmer.completed);

        // Warmer queue
        // Component/V1/NodeStats/ThreadPool/Warmer/Queue/*
        reportNodeMetric("V1/NodeStats/ThreadPool/Warmer/Queue", "threads", nodeName,
            nodeStats.thread_pool.warmer.queue);

        // Flush
        // Component/V1/NodeStats/ThreadPool/Flush/Completed/*
        reportNodeProcessedMetric("V1/NodeStats/ThreadPool/Flush/Completed", "threads", nodeName,
            nodeStats.thread_pool.flush.completed);

        // Flush queue
        // Component/V1/NodeStats/ThreadPool/Flush/Queue/*
        reportNodeMetric("V1/NodeStats/ThreadPool/Flush/Queue", "threads", nodeName,
            nodeStats.thread_pool.flush.queue);

        // Refresh
        // Component/V1/NodeStats/ThreadPool/Refresh/Completed/*
        reportNodeProcessedMetric("V1/NodeStats/ThreadPool/Refresh/Completed", "threads", nodeName,
            nodeStats.thread_pool.refresh.completed);

        // Refresh queue
        // Component/V1/NodeStats/ThreadPool/Refresh/Queue/*
        reportNodeMetric("V1/NodeStats/ThreadPool/Refresh/Queue", "threads", nodeName,
            nodeStats.thread_pool.refresh.queue);
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
    }

    private void reportNodesStats(NodesStats nodesStats) {
        if (nodesStats.nodes != null) {
            reportCalculatedClusterStats(nodesStats);
            for (Map.Entry<String, NodesStats.NodeStats> entry : nodesStats.nodes.entrySet()) {
                reportNodeStats(entry.getValue());
            }
        }
    }

    private String nodeMetricName(String metricName, String nodeName) {
        return metricName + "/" + nodeName;
    }

    private void reportNodeMetric(String metricName, String units, String nodeName, Number value)
    {
        reportMetric(nodeMetricName(metricName, nodeName), units, value);
    }

    private void reportNodeProcessedMetric(String metricName, String units, String nodeName, Number value)
    {
        Number processedValue = processorFactory.getProcessorForNode(metricName, nodeName).process(value);
        reportNodeMetric(metricName, units, nodeName, processedValue);
    }

    private void reportProcessedMetric(String metricName, String units, Number value)
    {
        Number processedValue = processorFactory.getProcessor(metricName).process(value);
        reportMetric(metricName, units, processedValue);
    }
}
