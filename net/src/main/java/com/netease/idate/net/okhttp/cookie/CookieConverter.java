package com.netease.idate.net.okhttp.cookie;

import com.netease.idate.net.api.cookie.HttpCookie;

import okhttp3.Cookie;

/**
 * Created by daisongsong on 16/8/6.
 */

public class CookieConverter {

    public HttpCookie convertToHttpCookie(Cookie cookie) {
        HttpCookie httpCookie = new HttpCookie();
        httpCookie.setName(cookie.name());
        httpCookie.setValue(cookie.value());
        httpCookie.setDomain(cookie.domain());
        httpCookie.setPath(cookie.path());
        httpCookie.setExpiresAt(cookie.expiresAt());
        httpCookie.setSecure(cookie.secure());
        httpCookie.setHostOnly(cookie.hostOnly());
        httpCookie.setPersistent(cookie.persistent());
        return httpCookie;
    }

    public Cookie convertToCookie(HttpCookie httpCookie) {
        Cookie.Builder builder = new Cookie.Builder()
                .domain(httpCookie.getDomain())
                .path(httpCookie.getPath())
                .name(httpCookie.getName())
                .value(httpCookie.getValue())
                .expiresAt(httpCookie.getExpiresAt());
        if(httpCookie.isHostOnly()){
            builder.hostOnlyDomain(httpCookie.getDomain());
        }
        if(httpCookie.isHttpOnly()){
            builder.httpOnly();
        }
        if(httpCookie.isSecure()){
            builder.secure();
        }
        return builder.build();
    }
}
