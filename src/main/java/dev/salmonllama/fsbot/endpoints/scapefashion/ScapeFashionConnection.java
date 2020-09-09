/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.endpoints.scapefashion;

import com.google.gson.Gson;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class ScapeFashionConnection {
    private final String RS3_REQUEST_URL = "https://api.rune.scape.fashion";
    private final String RS3_LINK_URL = "https://rune.scape.fashion";

    private final String OSRS_REQUEST_URL = "https://api.scape.fashion";
    private final String OSRS_LINK_URL = "https://scape.fashion";

    public ScapeFashionConnection() {}

    // Uses the color endpoint to search for items
    // Returns an object with a list of the top results, and a link redirect to see full list
    public ScapeFashionResult osrsColor(String color) throws Exception {
        String uri = OSRS_REQUEST_URL + "/colors/" + encode(color);
        String link = OSRS_LINK_URL + "/colors/" + encode(color);

        var response = makeRequest(uri);
        response.setLink(link);
        return response;
    }

    private ScapeFashionResult osrsColor(String color, String slot) throws Exception {
        String uri = OSRS_REQUEST_URL + "/colors/" + encode(color) + "?slot=" + encode(slot);
        String link = OSRS_LINK_URL + "/colors/" + encode(color) + "?slot=" + encode(slot);

        var response = makeRequest(uri);
        response.setLink(link);
        return response;
    }

    private ScapeFashionResult osrsItem(String item) throws Exception {
        String uri = OSRS_REQUEST_URL + "/items/" + encode(item);
        String link = OSRS_LINK_URL + "/items/" + encode(item);

        var response = makeRequest(uri);
        response.setLink(link);
        return response;
    }

    private ScapeFashionResult osrsItem(String item, String slot) throws Exception {
        String uri = OSRS_REQUEST_URL + "/items/" + encode(item) + "?slot=" + encode(slot);
        String link = OSRS_LINK_URL + "/items/" + encode(item) + "?slot=" + encode(slot);

        var response = makeRequest(uri);
        response.setLink(link);
        return response;
    }

    private void rs3Color(String color) {

    }

    private void rs3Color(String color, String slot) {

    }

    private void rs3Item(String item) {

    }

    private void rs3Item(String item, String slot) {

    }

    private ScapeFashionResult makeRequest(String url) throws Exception {
        var client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

        String USER_AGENT = "Fashionscape-Bot github.com/salmonllama/fashionscape-bot";

        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .header("Content-Type", "application/json")
                .header("User-Agent", USER_AGENT)
                .GET()
                .build();

        Gson gson = new Gson();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        var json = response.body();
        return gson.fromJson(json, ScapeFashionResult.class);
    }

    private String encode(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    }
}
