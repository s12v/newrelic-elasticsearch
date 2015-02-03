package me.snov.newrelic.elasticsearch.service;

import com.newrelic.metrics.publish.processors.EpochCounter;
import com.newrelic.metrics.publish.processors.Processor;

import java.util.HashMap;

public class EpochCounterFactory {

    private HashMap<String, Processor> processors;

    public EpochCounterFactory()
    {
        processors = new HashMap<String, Processor>();
    }

    private String getProcessorKey(String nodeName, String id)
    {
        return id + "-" + nodeName;
    }

    public Processor getProcessorForNode(String id, String nodeName)
    {
        String key = getProcessorKey(id, nodeName);
        if (!processors.containsKey(key)) {
            processors.put(key, new EpochCounter());
        }

        return processors.get(key);
    }

    public Processor getProcessor(String key)
    {
        if (!processors.containsKey(key)) {
            processors.put(key, new EpochCounter());
        }

        return processors.get(key);
    }
}
