package com.netease.idate.net.api;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by daisongsong on 16/8/6.
 */

public class HttpRequest {
    public static final int GET = 0;
    public static final int POST = 1;
    private String mUrl;
    private int mMethod;
    private RequestParams mParams;
    private Headers mHeaders;

    private HttpRequest() {

    }

    public int getMethod() {
        return mMethod;
    }

    public String getUrl() {
        return mUrl;
    }

    public Headers getHeaders() {
        return mHeaders;
    }

    public RequestParams getParams() {
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
        private RequestParams.Builder mParamsBuilder;
        private Headers.Builder mHeadersBuilder;

        public Builder() {
            mMethod = GET;
        }

        public Builder url(String url) {
            this.mUrl = url;
            return this;
        }

        public Builder addParam(String name, Object value) {
            if (mParamsBuilder == null) {
                mParamsBuilder = new RequestParams.Builder();
            }
            mParamsBuilder.addParam(name, value);
            return this;
        }

        public Builder params(RequestParams params) {
            int size = params == null ? 0 : params.size();
            for (int i = 0; i < size; i++) {
                addParam(params.getName(i), params.getValue(i));
            }
            return this;
        }

        public Builder method(@Method int method) {
            mMethod = method;
            return this;
        }

        public Builder headers(Headers headers) {
            int size = headers == null ? 0 : headers.size();
            for (int i = 0; i < size; i++) {
                addHeader(headers.getName(i), headers.getValue(i));
            }
            return this;
        }

        public Builder addHeader(String name, String value) {
            if (mHeadersBuilder == null) {
                mHeadersBuilder = new Headers.Builder();
            }
            this.mHeadersBuilder.addHeader(name, value);
            return this;
        }

        public HttpRequest build() {
            HttpRequest httpRequest = new HttpRequest();
            httpRequest.mMethod = mMethod;
            httpRequest.mUrl = mUrl;
            httpRequest.mParams = mParamsBuilder == null ? null : mParamsBuilder.build();
            httpRequest.mHeaders = mHeadersBuilder == null ? null : mHeadersBuilder.build();
            return httpRequest;
        }
    }
}
