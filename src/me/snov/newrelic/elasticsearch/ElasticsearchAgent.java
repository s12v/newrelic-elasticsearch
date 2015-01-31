package me.snov.newrelic.elasticsearch;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.newrelic.metrics.publish.Agent;
import com.newrelic.metrics.publish.configuration.ConfigurationException;
import com.newrelic.metrics.publish.processors.EpochCounter;
import com.newrelic.metrics.publish.processors.Processor;

/**
 * An agent for Elasticsearch
 * @author jstenhouse
 */
public class ElasticsearchAgent extends Agent {

    private static final String GUID = "me.snov.newrelic.elasticsearch";
    private static final String VERSION = "0.0.1";

    private static final String HTTP = "http";
    private static final String WIKIPEDIA_URL = "/w/api.php?action=query&format=json&meta=siteinfo&siprop=statistics";

    private String name;
    private URL url;
    private Processor articleCreationRate;

    /**
     * Constructor for Elastisearch Agent.
     * @param name
     * @param host
     * @throws ConfigurationException if URL for Wikipedia's metric service could not be built correctly from provided host
     */
    public ElasticsearchAgent(String name, String host) throws ConfigurationException {
        super(GUID, VERSION);
        try {
            this.name = name;
            this.url = new URL(HTTP, host, WIKIPEDIA_URL);
            this.articleCreationRate = new EpochCounter();
        } catch (MalformedURLException e) {
            throw new ConfigurationException("Wikipedia metric URL could not be parsed", e);
        }
    }

    @Override
    public String getComponentHumanLabel() {
        return name;
    }

    @Override
    public void pollCycle() {
        Integer numArticles = getNumArticles();
        if (numArticles != null) {
             reportMetric("Articles/Created", "articles/sec", articleCreationRate.process(numArticles));
             reportMetric("Articles/Count", "articles", numArticles);
        } else {
            //TODO: log numArticles when null
        }
    }

    /**
     * Get Number of Articles from Wikipedia
     * @return Integer the number of articles
     */
    private Integer getNumArticles() {
        JSONObject jsonObj = getJSONResponse();
        if (jsonObj != null) {
            JSONObject query = (JSONObject) jsonObj.get("query");
            JSONObject statistics = (JSONObject) query.get("statistics");
            Long numArticles = (Long) statistics.get("articles");
            return numArticles.intValue();
        } else {
            return null;
        }
    }

    /**
     * Get JSON Response from Wikipedia
     * @return JSONObject the JSONObject response
     */
    private JSONObject getJSONResponse() {
        Object response = null;
        InputStream inputStream = null;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("Accept", "application/json");
            inputStream = connection.getInputStream();
            response = JSONValue.parse(new InputStreamReader(inputStream));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {}
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return (JSONObject) response;
    }

}
