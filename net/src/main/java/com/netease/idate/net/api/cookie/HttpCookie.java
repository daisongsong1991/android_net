package com.netease.idate.net.api.cookie;

import java.io.Serializable;

/**
 * Created by daisongsong on 16/8/6.
 */

public class HttpCookie implements Serializable {
    private String name;
    private String value;
    private long expiresAt;
    private String domain;
    private String path;
    private boolean secure;
    private boolean httpOnly;

    private boolean persistent; // True if 'expires' or 'max-age' is present.
    private boolean hostOnly;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(long expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }

    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    public boolean isHostOnly() {
        return hostOnly;
    }

    public void setHostOnly(boolean hostOnly) {
        this.hostOnly = hostOnly;
    }

    public boolean isValid() {
        long t = System.currentTimeMillis();
        return t < expiresAt && (this.value != null && this.value.length() > 0);
    }

    public boolean matchPath(String path) {
        if (this.path == null || this.path.length() == 0) {
            return true;
        }

        if (path == null) {
            return false;
        }

        return path.startsWith(this.path);
    }
}
