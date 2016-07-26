package com.netease.idate.net.okhttp;

import com.netease.idate.net.api.NetClient;
import com.netease.idate.net.api.NetHandler;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by daisongsong on 16-7-25.
 */
public class OkHttpNetClient extends NetClient {
    private OkHttpClient mOkHttpClient;

    public OkHttpNetClient() {
        mOkHttpClient = new OkHttpClient();
    }

    @Override
    public void get(String url, Map<String, Object> params, NetHandler handler) {
        String requestBody = null;
        if (params != null) {
            StringBuilder sb = new StringBuilder("?");
            for (Map.Entry<String, Object> e : params.entrySet()) {
                sb.append(String.format("%s=%s&", e.getKey(), String.valueOf(e.getValue())));
            }
            requestBody = sb.substring(0, sb.length() - 1);
        }

        Request request = new Request.Builder()
                .url(url + requestBody)
                .method("GET", null)
                .build();
        enqueueRequest(request, handler);
    }

    @Override
    public void post(String url, Map<String, Object> params, NetHandler handler) {
        if (params == null || params.isEmpty()) {
            throw new RuntimeException("method POST must have Params");
        }

        RequestBody body = null;
        for (Map.Entry<String, Object> e : params.entrySet()) {
            
        }


        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .build();
        enqueueRequest(request, handler);
    }


    private void enqueueRequest(Request request, final NetHandler handler) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.onFailure(e.hashCode(), e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                handler.onResponse(response.code(), response.body().bytes());
            }
        });
    }
}
