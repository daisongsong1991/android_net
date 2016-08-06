package com.netease.idate.net.api;

import com.netease.idate.net.api.cookie.CookieStore;

/**
 * Created by daisongsong on 16-7-25.
 */
public interface NetClient {

//    Object get(String url, Map<String, Object> params, NetHandler handler);
//
//    Object post(String url, Map<String, Object> params, NetHandler handler);

    void cancel(Object tag);

    void setCookieStore(CookieStore cookieStore);

    Object enqueue(HttpRequest request, NetHandler handler);

    HttpResponse execute(HttpRequest request);
}
