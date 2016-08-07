package com.netease.idate.net.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daisongsong on 16/8/6.
 */

public class Headers {
    private String[] mNameValues = null;

    private Headers() {

    }

    public String[] getNameValueArray() {
        return mNameValues;
    }

    public static class Builder {
        private List<String> mNameValues = new ArrayList<>();

        public Builder addHeader(String name, String value) {
            mNameValues.add(name);
            mNameValues.add(value);
            return this;
        }

        public Headers build() {
            Headers headers = new Headers();
            headers.mNameValues = mNameValues.toArray(new String[mNameValues.size()]);
            return headers;
        }
    }

}
