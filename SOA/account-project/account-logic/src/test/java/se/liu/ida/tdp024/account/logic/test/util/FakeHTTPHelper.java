package se.liu.ida.tdp024.account.logic.test.util;

import java.util.HashMap;
import java.util.Map;

import se.liu.ida.tdp024.account.logic.impl.util.http.HTTPHelper;

public class FakeHTTPHelper implements HTTPHelper {

    public Map<String, String> responses = new HashMap<>();

    @Override
    public String get(String endpoint, String... parameters) {
        String url = endpoint + "?" + parameters[0] + "=" + parameters[1];
        if (responses.containsKey(url)) {
            return responses.get(url);
        }
        return "";
    }

    @Override
    public String postJSON(String endpoint, String[] queryParameters, String[] dataParameters) {
        return "";
    }
}
