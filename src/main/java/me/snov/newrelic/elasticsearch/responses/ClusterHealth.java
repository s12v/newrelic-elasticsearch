package me.snov.newrelic.elasticsearch.responses;

import java.util.ArrayList;

public class ClusterHealth {
    public String status; //
    public Number number_of_nodes; //
    public Boolean timed_out;
    public Number number_of_data_nodes; //
    public Number active_primary_shards; //
    public Number active_shards; //
    public Number relocating_shards;//
    public Number initializing_shards; //
    public Number unassigned_shards; //
    public Number delayed_unassigned_shards; //
    public Number number_of_pending_tasks; //
    public Number number_of_in_flight_fetch;
    public Number task_max_waiting_in_queue_millis; //
    public Number active_shards_percent_as_number; //
}
