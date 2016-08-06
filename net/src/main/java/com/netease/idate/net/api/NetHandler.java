package com.netease.idate.net.api;

/**
 * Created by daisongsong on 16-7-25.
 */
public interface NetHandler {

    void onResponse(HttpResponse response);

    void onFailure(HttpResponse response);
}
