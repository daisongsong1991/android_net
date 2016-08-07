package com.netease.idate.net.api;

/**
 * Created by daisongsong on 16-7-25.
 */
public class NetHandler {

    public void onResponse(HttpResponse response) {
        Logger.d("NetHandler", String.valueOf(response));
    }

    public void onFailure(HttpResponse response) {
        Logger.d("NetHandler", String.valueOf(response));
    }
}
