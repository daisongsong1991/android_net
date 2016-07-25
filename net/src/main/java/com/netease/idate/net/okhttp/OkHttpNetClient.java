package com.netease.idate.net.okhttp;

import com.netease.idate.net.api.NetClient;
import com.netease.idate.net.api.NetHandler;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

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
        RequestBody body = null;
        if (params != null) {
            FormBody.Builder builder = new FormBody.Builder();
            for (Map.Entry<String, Object> e : params.entrySet()) {
                builder.add(e.getKey(), String.valueOf(e.getValue()));
            }
            body = builder.build();
        }

        Request request = new Request.Builder()
                .url(url)
                .method("GET", body)
                .build();
        mOkHttpClient.newCall(request).cancel();
    }
}
