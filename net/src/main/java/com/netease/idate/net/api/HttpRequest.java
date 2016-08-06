package com.netease.idate.net.api;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by daisongsong on 16/8/6.
 */

public class HttpRequest {
    public static final int GET = 0;
    public static final int POST = 1;
    private String mUrl;
    private int mMethod;
    private Map<String, Object> mParams;


    private HttpRequest() {

    }

    public int getMethod() {
        return mMethod;
    }

    public String getUrl() {
        return mUrl;
    }

    public Map<String, Object> getParams() {
        return mParams;
    }

    @IntDef({
            GET,
            POST
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Method {
    }

    public static final class Builder {
        private String mUrl;
        private int mMethod;
        private Map<String, Object> mParams;

        public Builder() {
            mMethod = GET;
        }

        public Builder url(String url) {
            this.mUrl = url;
            return this;
        }

        public Builder addParams(String key, Object value) {
            if (mParams == null) {
                mParams = new HashMap<>();
            }
            mParams.put(key, value);
            return this;
        }

        public Builder method(@Method int method) {
            mMethod = method;
            return this;
        }

        public HttpRequest build() {
            HttpRequest httpRequest = new HttpRequest();
            httpRequest.mMethod = mMethod;
            httpRequest.mUrl = mUrl;
            httpRequest.mParams = mParams;
            return httpRequest;
        }
    }
}
