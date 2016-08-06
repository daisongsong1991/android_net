package com.netease.idate.net.api;

/**
 * Created by daisongsong on 16/8/6.
 */

public class HttpResponse {
    public static final int CODE_NOT_FOUND = 404;
    private int mCode;
    private byte[] mData;

    private Exception mException;

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

    public static final class Builder {
        private int mCode;
        private byte[] mData;
        private Exception mException;

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

        public HttpResponse build() {
            HttpResponse response = new HttpResponse();
            response.mCode = mCode;
            response.mData = mData;
            response.mException = mException;
            return response;
        }

    }
}
