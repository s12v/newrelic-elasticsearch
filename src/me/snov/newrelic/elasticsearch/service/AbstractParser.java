package me.snov.newrelic.elasticsearch.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

abstract class AbstractParser {

    protected static final String HTTP = "http";

    private final Gson gson;

    public AbstractParser() {
        this.gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();
    }

    protected final <T> T getGsonResponse(URL url, Class<T> classOfT) {
        InputStream inputStream = null;
        HttpURLConnection connection = null;
        T response = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("Accept", "application/json");
            inputStream = connection.getInputStream();
            response = gson.fromJson(new InputStreamReader(inputStream), classOfT);
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
        return response;
    }
}
