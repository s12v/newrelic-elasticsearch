package me.snov.newrelic.elasticsearch.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

abstract class AbstractParser<T> {

    protected static final String HTTP = "http";

    private final Class<T> typeParameterClass;
    private final URL url;
    private final Gson gson;
    private HttpURLConnection connection;

    public AbstractParser(Class<T> typeParameterClass, URL url) {
        this.typeParameterClass = typeParameterClass;
        this.url = url;
        this.gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();
    }

    private InputStream getInputStream(URL url) throws IOException {
        connection = (HttpURLConnection) url.openConnection();
        connection.addRequestProperty("Accept", "application/json");
        return connection.getInputStream();
    }

    public final T request() throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = getInputStream(url);
            return parse(inputStream);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public final T parse(InputStream stream) throws IOException {
        return gson.fromJson(new InputStreamReader(stream), typeParameterClass);
    }
}
