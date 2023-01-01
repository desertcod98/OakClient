package me.leeeaf.oakclient.utils.net;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpManger {
    private static final HttpClient httpClient = HttpClient.newBuilder().build();
    private static final Gson gson = new Gson();

    public static JsonObject GETJsonNoHeaders(String url) {
        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(request == null){
            return null;
        }
        HttpResponse<String> response = request(request);
        return gson.fromJson(response.body(), JsonObject.class);
    }

    private static HttpResponse<String> request(HttpRequest request){
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
