package com.netease.idate.net.okhttp.cookie;

import com.netease.idate.net.api.cookie.CookieStore;
import com.netease.idate.net.api.cookie.HttpCookie;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by daisongsong on 16/8/6.
 */

public class CookieJarAdapter implements CookieJar {
    private CookieStore mCookieStore;
    private CookieConverter mCookieConverter;

    public CookieJarAdapter(CookieStore cookieStore) {
        mCookieStore = cookieStore;
        mCookieConverter = new CookieConverter();
        if (mCookieStore == null) {
            throw new RuntimeException("CookieStore can not be NULL!");
        }
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null) {
            for (int i = cookies.size() - 1; i >= 0; --i) {
                Cookie cookie = cookies.get(i);
                HttpCookie httpCookie = mCookieConverter.convertToHttpCookie(cookie);
                mCookieStore.add(url.uri(), httpCookie);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = new ArrayList<>();

        List<HttpCookie> httpCookies = mCookieStore.get(url.uri());
        if (httpCookies != null) {
            for (int i = httpCookies.size() - 1; i >= 0; --i) {
                HttpCookie httpCookie = httpCookies.get(i);
                Cookie cookie = mCookieConverter.convertToCookie(httpCookie);
                cookies.add(cookie);
            }
        }

        return cookies;
    }
}
