package me.snov.newrelic.elasticsearch.parsers;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

abstract class AbstractParser<T> {

    private final Class<T> typeParameterClass;
    private final URL url;
    private final Gson gson;
    private final String username;
    private final String password;
    private HttpURLConnection connection;

    public AbstractParser(Class<T> typeParameterClass, URL url, String username, String password) {
        this.typeParameterClass = typeParameterClass;
        this.url = url;
        this.username = username;
        this.password = password;
        this.gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();
    }

    private InputStream getInputStream(URL url) throws IOException {
        connection = (HttpURLConnection) url.openConnection();
        connection.addRequestProperty("Accept", "application/json");

        // If we have basic authentication credentials defined, generate the Authorization header.
        if (username != null && password != null
                && !username.isEmpty() && !password.isEmpty()) {
            String authString = username + ":" + password;
            byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
            String authStringEnc = new String(authEncBytes);

            connection.setRequestProperty("Authorization", "Basic " + authStringEnc);
        }
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
