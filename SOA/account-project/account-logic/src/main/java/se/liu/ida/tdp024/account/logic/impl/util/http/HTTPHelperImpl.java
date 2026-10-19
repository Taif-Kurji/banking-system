package se.liu.ida.tdp024.account.logic.impl.util.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Component;

@Component
public class HTTPHelperImpl implements HTTPHelper {

    private static final int TIMEOUT_MS = 5000;

    @Override
    public String get(String endpoint, String... parameters) {
        String urlToRead = createURL(endpoint, parameters);

        try {
            HttpURLConnection conn = (HttpURLConnection) URI.create(urlToRead).toURL().openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(TIMEOUT_MS);
            conn.setReadTimeout(TIMEOUT_MS);

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "";
            }
            return readAll(conn.getInputStream());

        } catch (IOException | IllegalArgumentException e) {
            return "";
        }
    }

    @Override
    public String postJSON(String endpoint, String[] queryParameters, String[] dataParameters) {
        String urlToRead = createURL(endpoint, queryParameters);
        String dataPayload = createJSONPayload(dataParameters);

        try {
            HttpURLConnection conn = (HttpURLConnection) URI.create(urlToRead).toURL().openConnection();
            conn.addRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(TIMEOUT_MS);
            conn.setReadTimeout(TIMEOUT_MS);
            conn.setRequestMethod("POST");

            try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8)) {
                writer.write(dataPayload);
            }

            int code = conn.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK || code == HttpURLConnection.HTTP_CREATED) {
                return readAll(conn.getInputStream());
            }
            InputStream error = conn.getErrorStream();
            return error != null ? readAll(error) : "";

        } catch (IOException | IllegalArgumentException e) {
            return null;
        }
    }

    private String readAll(InputStream stream) throws IOException {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        }
        return builder.toString();
    }

    private String createURL(String endpoint, String... parameters) {
        StringBuilder urlBuilder = new StringBuilder(endpoint);
        for (int i = 0; i < parameters.length - 1; i += 2) {
            urlBuilder.append(i == 0 ? "?" : "&")
                      .append(URLEncoder.encode(parameters[i], StandardCharsets.UTF_8))
                      .append("=")
                      .append(URLEncoder.encode(parameters[i + 1], StandardCharsets.UTF_8));
        }
        return urlBuilder.toString();
    }

    private String createJSONPayload(String[] dataParameters) {
        StringBuilder dataBuilder = new StringBuilder("{");
        for (int i = 0; i < dataParameters.length - 1; i += 2) {
            try {
                long value = Long.parseLong(dataParameters[i + 1]);
                dataBuilder.append("\"").append(dataParameters[i]).append("\":").append(value).append(",");
            } catch (NumberFormatException e) {
                dataBuilder.append("\"").append(dataParameters[i]).append("\":\"").append(dataParameters[i + 1]).append("\",");
            }
        }
        if (dataBuilder.length() > 1) {
            dataBuilder.deleteCharAt(dataBuilder.length() - 1);
        }
        dataBuilder.append("}");
        return dataBuilder.toString().replaceAll("\\n", "\\\\n");
    }
}
