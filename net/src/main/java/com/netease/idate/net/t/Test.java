package com.netease.idate.net.t;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by daisongsong on 16-7-21.
 */
public class Test {
    private static final String TAG = "Test";

    public static int add(int a, int b) {
        return a + b;
    }


    public void testNet(final NetCallback callback) {
        final Handler handler = new Handler();
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://www.163.com")
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                Log.d(TAG, "onFailure() called with: " + "call = [" + call + "], e = [" + e + "]");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailure(e.toString());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String s = response.body().toString();
                Log.d(TAG, "onResponse() called with: " + "call = [" + call + "], response = [" + response + "]");
                Log.d(TAG, "response string is [" + s + "]");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(s);
                    }
                });
            }
        });

    }

    public interface NetCallback {
        void onSuccess(String s);

        void onFailure(String e);
    }
}
