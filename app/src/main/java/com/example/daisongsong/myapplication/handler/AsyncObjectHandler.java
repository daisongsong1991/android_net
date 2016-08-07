package com.example.daisongsong.myapplication.handler;

import android.os.Handler;
import android.os.Looper;

import com.netease.idate.net.api.HttpResponse;

/**
 * Created by daisongsong on 16/8/7.
 */

public abstract class AsyncObjectHandler<T> extends ObjectHandler<T> {
    private Handler mHandler;

    public AsyncObjectHandler() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    protected final void onObjectResponse(final int httpCode, final T data) {
        System.out.println("AsyncObjectHandler.onObjectResponse " + data);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onSuccess(httpCode, data);
            }
        });
    }

    @Override
    public final void onFailure(final HttpResponse response) {
        super.onFailure(response);

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onError(response);
            }
        });
    }

    protected abstract void onSuccess(int httpCode, T data);

    protected abstract void onError(HttpResponse response);
}
