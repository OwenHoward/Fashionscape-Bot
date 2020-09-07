/*
 * Copyright (c) 2020. Aleksei Gryczewski
 * All rights reserved.
 */

package dev.salmonllama.fsbot.endpoints.scapefashion;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class ScapeFashionConnection {
    private final String RS3_REQUEST_URL = "https://api.rune.scape.fashion";
    private final String RS3_LINK_URL = "https://rune.scape.fashion";

    private final String OSRS_REQUEST_URL = "https://api.scape.fashion";
    private final String OSRS_LINK_URL = "https://scape.fashion";

    private final String USER_AGENT = "Fashionscape-Bot github.com/salmonllama/fashionscape-bot";

    private final OkHttpClient client;
    private final Request.Builder requestBuilder;

    public ScapeFashionConnection() {

        client = new OkHttpClient().newBuilder().build();
        requestBuilder = new Request.Builder();
    }

    // Uses the color endpoint to search for items
    // Returns an object with a list of the top results, and a link redirect to see full list
    public void osrsColor(String color) throws IOException {
        String url = OSRS_REQUEST_URL + "/colors/" + encode(color);
        System.out.println(url);

        makeRequestNEW(url);
    }

    private void osrsColor(String color, String slot) {

    }

    private void osrsItem(String item) {

    }

    private void osrsItem(String item, String slot) {

    }

    private void rs3Color(String color) {

    }

    private void rs3Color(String color, String slot) {

    }

    private void rs3Item(String item) {

    }

    private void rs3Item(String item, String slot) {

    }

    private JSONArray makeRequest(String url) throws IOException {
        // Returns the items JSONObject
        Request request = requestBuilder.get().url(url).addHeader("User-Agent", USER_AGENT).build();

        Response response = client.newCall(request).execute();
        // returns a JSONArray of JSONObjects
        System.out.println(response.body().string());
        return new JSONObject(response.body().string()).getJSONArray("items");
    }

    private void makeRequestNEW(String url) {
        var client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .header("Content-Type", "application/json")
                .header("User-Agent", USER_AGENT)
                .GET()
                .build();

        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        response.thenAcceptAsync(res -> System.out.println(res.body()));
    }

    private String encode(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    }

    private ScapeFashionResult extract(JSONObject json) {

        return null;
    }
}
