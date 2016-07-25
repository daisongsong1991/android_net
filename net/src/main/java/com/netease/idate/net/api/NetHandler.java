package com.netease.idate.net.api;

/**
 * Created by daisongsong on 16-7-25.
 */
public interface NetHandler {

    void onResponse(int httpCode, byte[] body);

    void onFailure(int httpCode, String message);
}
