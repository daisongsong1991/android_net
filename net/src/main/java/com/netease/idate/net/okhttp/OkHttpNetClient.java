package com.netease.idate.net.okhttp;

import com.netease.idate.net.api.NetClient;
import com.netease.idate.net.api.NetHandler;
import com.netease.idate.net.api.cookie.CookieStore;
import com.netease.idate.net.okhttp.cookie.CookieJarAdapter;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by daisongsong on 16-7-25.
 */
public class OkHttpNetClient implements NetClient {
    private OkHttpClient mOkHttpClient;

    private RequestFactory mRequestFactory = RequestFactory.getInstance();

    public OkHttpNetClient() {
        mOkHttpClient = new OkHttpClient.Builder().build();
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

    @Override
    public void setCookieStore(CookieStore cookieStore) {
        CookieJarAdapter adapter = new CookieJarAdapter(cookieStore);
        OkHttpClient.Builder builder = mOkHttpClient.newBuilder().cookieJar(adapter);
        mOkHttpClient = builder.build();
    }
}
