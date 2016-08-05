package com.netease.idate.net.okhttp;

import com.netease.idate.net.api.NetClient;
import com.netease.idate.net.api.NetHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by daisongsong on 16-7-25.
 */
public class OkHttpNetClient extends NetClient {
    private OkHttpClient mOkHttpClient;

    private RequestFactory mRequestFactory = RequestFactory.getInstance();

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
                })
                .build();
    }

    @Override
    public Object get(String url, Map<String, Object> params, NetHandler handler) {
        Request request = mRequestFactory.createGetRequest(url, params);
        return enqueueRequest(request, handler);
    }

    @Override
    public Object post(String url, Map<String, Object> params, NetHandler handler) {
        if (params == null || params.isEmpty()) {
            throw new RuntimeException("method POST must have Params");
        }

        Request request = mRequestFactory.createPostRequest(url, params);

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
