package com.netease.idate.net.okhttp;

import com.netease.idate.net.api.HttpRequest;
import com.netease.idate.net.api.HttpResponse;
import com.netease.idate.net.api.NetClient;
import com.netease.idate.net.api.NetHandler;
import com.netease.idate.net.api.cookie.CookieStore;
import com.netease.idate.net.okhttp.cookie.CookieJarAdapter;

import java.io.IOException;

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

    @Override
    public Object enqueue(final HttpRequest request, final NetHandler handler) {
        Request okRequest = mRequestFactory.createRequest(request);
        Call call = mOkHttpClient.newCall(okRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                HttpResponse httpResponse = new HttpResponse.Builder()
                        .code(HttpResponse.CODE_NOT_FOUND)
                        .exception(e)
                        .request(request)
                        .build();
                handler.onFailure(httpResponse);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                HttpResponse.Builder builder = new HttpResponse.Builder();
                builder.code(response.code())
                        .request(request)
                        .data(response.body().bytes());
                HttpResponse httpResponse = builder.build();
                handler.onResponse(httpResponse);
            }
        });
        return call;
    }

    @Override
    public HttpResponse execute(HttpRequest httpRequest) {
        HttpResponse httpResponse;
        Request okRequest = mRequestFactory.createRequest(httpRequest);
        try {
            Response response = mOkHttpClient.newCall(okRequest).execute();
            HttpResponse.Builder builder = new HttpResponse.Builder();
            builder.code(response.code())
                    .data(response.body().bytes())
                    .request(httpRequest);
            httpResponse = builder.build();
        } catch (IOException e) {
            e.printStackTrace();
            httpResponse = new HttpResponse.Builder()
                    .code(HttpResponse.CODE_NOT_FOUND)
                    .exception(e)
                    .request(httpRequest)
                    .build();
        }
        return httpResponse;
    }

}
