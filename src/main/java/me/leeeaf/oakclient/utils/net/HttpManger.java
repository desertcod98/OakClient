package me.leeeaf.oakclient.utils.net;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class HttpManger {
    private static final HttpClient httpClient = HttpClient.newBuilder().build();
    private static final Gson gson = new Gson();

    public static JsonObject GETJson(String url) {
        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .timeout(Duration.ofSeconds(5))
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
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
