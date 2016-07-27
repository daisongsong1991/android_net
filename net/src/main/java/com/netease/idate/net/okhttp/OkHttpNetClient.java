package com.netease.idate.net.okhttp;

import com.netease.idate.net.api.NetClient;
import com.netease.idate.net.api.NetHandler;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by daisongsong on 16-7-25.
 */
public class OkHttpNetClient extends NetClient {
    private OkHttpClient mOkHttpClient;

    public OkHttpNetClient() {
        mOkHttpClient = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    private Map<String, Map<String, Cookie>> mCookieMap = new HashMap<String, Map<String, Cookie>>();

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        System.out.println("CookieJar url=" + url + ",cookies=" + cookies);
                        if (cookies != null) {
                            Map<String, Cookie> map = mCookieMap.get(url.host());
                            if (map == null) {
                                map = new HashMap<String, Cookie>();
                                mCookieMap.put(url.host(), map);
                            }
                            for (Cookie cookie : cookies) {
                                map.put(cookie.name(), cookie);
                            }
                        }

                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = new ArrayList<Cookie>();
                        Map<String, Cookie> map = mCookieMap.get(url.host());
                        if (map != null) {
                            for (Map.Entry<String, Cookie> entry : map.entrySet()) {
                                cookies.add(entry.getValue());
                            }
                        }
                        return cookies;
                    }
                }).build();
    }

    @Override
    public Object get(String url, Map<String, Object> params, NetHandler handler) {
        String requestBody = null;
        if (params != null) {
            StringBuilder sb = new StringBuilder("?");
            for (Map.Entry<String, Object> e : params.entrySet()) {
                sb.append(String.format("%s=%s&", e.getKey(), String.valueOf(e.getValue())));
            }
            requestBody = sb.substring(0, sb.length() - 1);
        }

        String fullUrl = requestBody == null || requestBody.length() == 0 ? url : url + requestBody;

        Request request = new Request.Builder()
                .url(fullUrl)
                .method("GET", null)
                .build();

        return enqueueRequest(request, handler);
    }

    @Override
    public Object post(String url, Map<String, Object> params, NetHandler handler) {
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
        return enqueueRequest(request, handler);
    }


    private Call enqueueRequest(Request request, final NetHandler handler) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.onFailure(e.hashCode(), e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                handler.onResponse(response.code(), response.body().bytes());
            }
        });
        return call;
    }

    @Override
    public void cancel(Object tag) {
        if (tag != null && tag instanceof Call) {
            Call call = (Call) tag;
            if (!call.isCanceled()) {
                call.cancel();
            }
        }
    }
}
