package com.fearth.gdk.utils;

import android.util.Log;
import org.jetbrains.annotations.NotNull;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

public class FearthHttpHelper {

    private static final OkHttpClient client = new OkHttpClient();
    private static final String TAG = "FearthHttpHelper";

    public static void get(String url, Map<String, String> headers, HttpCallback callback) {
        Log.d(TAG, "<get> url = " + url + ", headers = " + headers);
        Request request = createRequestBuilder(url, headers).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                handleFailure(e, callback);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                handleResponse(call, response, callback);
            }
        });
    }

    public static void post(String url, Map<String, String> headers, String body, HttpCallback callback) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        if (!headers.containsKey("Content-Type")) {
            headers.put("Content-Type", "application/json");
        }
        Log.d(TAG, "<post> url = " + url + ", headers = " + headers + ", body: " + body);
        RequestBody requestBody = createRequestBody(body);
        Request request = createRequestBuilder(url, headers).post(requestBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                handleFailure(e, callback);
            }
    
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                handleResponse(call, response, callback);
            }
        });
    }

    public static void put(String url, Map<String, String> headers, String body, HttpCallback callback) {
        Log.d(TAG, "<put> url = " + url + ", headers = " + headers + ", body: " + body);
        if (headers == null) {
            headers = new HashMap<>();
        }
        if (!headers.containsKey("Content-Type")) {
            headers.put("Content-Type", "application/json");
        }        RequestBody requestBody = createRequestBody(body);
        Request request = createRequestBuilder(url, headers).put(requestBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                handleFailure(e, callback);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                handleResponse(call, response, callback);
            }
        });
    }

    public static void delete(String url, Map<String, String> headers, HttpCallback callback) {
        Log.d(TAG, "<delete> url = " + url + ", headers = " + headers);
        Request request = createRequestBuilder(url, headers).delete().build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                handleFailure(e, callback);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                handleResponse(call, response, callback);
            }
        });
    }

    private static Request.Builder createRequestBuilder(String url, Map<String, String> headers) {
        Request.Builder requestBuilder = new Request.Builder().url(url);
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        return requestBuilder;
    }

    private static RequestBody createRequestBody(String body) {
        if (body != null && !body.isEmpty()) {
            return RequestBody.create(body, okhttp3.MediaType.parse("application/json; charset=utf-8"));
        }
        return RequestBody.create("", okhttp3.MediaType.parse("application/json; charset=utf-8"));
    }

    private static void handleResponse(Call call, Response response, HttpCallback callback) {
        try {
            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                callback.onResponse(response.code(), responseBody != null ? responseBody.string() : "");
            } else {
                Log.e(TAG, "Request failed with status code: " + response.code() + ", message: " + response.message());
                callback.onResponse(response.code(), "Error: " + response.message());
            }
        } catch (IOException e) {
            Log.e(TAG, "Error processing response: " + e.getMessage());
            callback.onResponse(500, "Error: " + e.getMessage());
        }
    }

    private static void handleFailure(IOException e, HttpCallback callback) {
        Log.e(TAG, "Request failed: " + e.getMessage());
        callback.onResponse(500, "Error: " + e.getMessage());
    }

    public interface HttpCallback {
        void onResponse(int status, String response);
    }
}
