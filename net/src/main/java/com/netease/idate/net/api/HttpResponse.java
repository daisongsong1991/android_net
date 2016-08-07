package com.netease.idate.net.api;

import java.util.Arrays;

/**
 * Created by daisongsong on 16/8/6.
 */

public class HttpResponse {
    public static final int CODE_NOT_FOUND = 404;
    public static final int CODE_JSON_ERROR = 601;
    private int mCode;
    private byte[] mData;

    private Exception mException;

    private HttpRequest mHttpRequest;

    private HttpResponse() {
    }

    public int getCode() {
        return mCode;
    }

    public byte[] getData() {
        return mData;
    }

    public Exception getException() {
        return mException;
    }

    public HttpRequest getHttpRequest() {
        return mHttpRequest;
    }

    public static final class Builder {
        private int mCode;
        private byte[] mData;
        private Exception mException;
        private HttpRequest mHttpRequest;

        public Builder() {

        }

        public Builder code(int code) {
            this.mCode = code;
            return this;
        }

        public Builder data(byte[] data) {
            this.mData = data;
            return this;
        }

        public Builder exception(Exception e) {
            this.mException = e;
            return this;
        }

        public Builder request(HttpRequest request){
            this.mHttpRequest = request;
            return this;
        }

        public HttpResponse build() {
            HttpResponse response = new HttpResponse();
            response.mCode = mCode;
            response.mData = mData;
            response.mException = mException;
            response.mHttpRequest = mHttpRequest;
            return response;
        }
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "mCode=" + mCode +
                ", mData=" + Arrays.toString(mData) +
                ", mException=" + mException +
                ", mHttpRequest=" + mHttpRequest +
                '}';
    }
}
