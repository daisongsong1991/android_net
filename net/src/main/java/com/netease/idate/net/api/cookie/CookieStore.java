package com.netease.idate.net.api.cookie;

import java.net.URI;
import java.util.List;

/**
 * Created by daisongsong on 16/8/6.
 */

public interface CookieStore {

    void add(URI uri,HttpCookie cookie);

    List<HttpCookie> get(URI uri);
}
