package com.netease.idate.net.api.cookie.impl;

import android.content.Context;
import android.content.SharedPreferences;

import com.netease.idate.net.api.cookie.CookieStore;
import com.netease.idate.net.api.cookie.HttpCookie;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by daisongsong on 16/8/6.
 */

public class SharePreferenceCookieStore implements CookieStore {
    private Map<String, Map<String, HttpCookie>> mMap = new HashMap<>();
    private SharedPreferences mSharedPreferences;

    public SharePreferenceCookieStore(Context context) {
        mSharedPreferences = context.getSharedPreferences("cookies.sp", Context.MODE_PRIVATE);
        initMap();
    }

    private void initMap() {
        Map<String, ?> all = mSharedPreferences.getAll();
        if (all != null) {
            for (Map.Entry<String, ?> entry : all.entrySet()) {
                String[] keys = decodeKey(entry.getKey());
                String domain = keys[0];
                String name = keys[1];
                HttpCookie cookie = CookieUtil.stringToHttpCookie((String) entry.getValue());

                Map<String, HttpCookie> cookieMap = mMap.get(domain);
                if (cookieMap == null) {
                    cookieMap = new HashMap<>();
                    mMap.put(domain, cookieMap);
                }
                cookieMap.put(name, cookie);
            }
        }
    }

    @Override
    public void add(URI uri, HttpCookie cookie) {
        Map<String, HttpCookie> cookieMap = mMap.get(uri.getHost());
        if (cookieMap == null) {
            cookieMap = new HashMap<>();
            mMap.put(uri.getHost(), cookieMap);
        }

        cookieMap.put(cookie.getName(), cookie);
        mSharedPreferences.edit().putString(generateSPKey(cookie), CookieUtil.httpCookieToString(cookie)).apply();
    }

    @Override
    public List<HttpCookie> get(URI uri) {
        List<HttpCookie> cookieList = new LinkedList<>();
        String domain = uri.getHost();
        String path = uri.getPath();
        Map<String, HttpCookie> cookieMap = mMap.get(domain);
        if (cookieMap != null && cookieMap.size() > 0) {
            for (Map.Entry<String, HttpCookie> entry : cookieMap.entrySet()) {
                HttpCookie cookie = entry.getValue();
                if (cookie.isValid()) {
                    if (cookie.matchPath(path)) {
                        cookieList.add(cookie);
                    }
                } else {
                    removeCookie(cookie);
                }
            }
        }
        return cookieList;
    }


    private void removeCookie(HttpCookie cookie) {
        Map<String, HttpCookie> cookieMap = mMap.get(cookie.getDomain());
        if (cookieMap != null) {
            cookieMap.remove(cookie.getName());
        }

        mSharedPreferences.edit().remove(generateSPKey(cookie)).apply();
    }


    private String generateSPKey(HttpCookie cookie) {
        return String.format(Locale.CHINA, "%s_%s", cookie.getDomain(), cookie.getName());
    }

    private String[] decodeKey(String key) {
        String[] results = new String[2];
        if (key != null) {
            String[] strings = key.split("_");
            if (strings != null && strings.length >= 2) {
                results[0] = strings[0];
                results[1] = strings[1];
            } else if (strings != null && strings.length == 1) {
                results[0] = strings[0];
            }
        }
        return results;
    }

}
