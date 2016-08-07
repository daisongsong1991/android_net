package com.netease.idate.net.api;

import java.util.ArrayList;
import java.util.Arrays;
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

    public int size() {
        int size = mNameValues == null ? 0 : mNameValues.length;
        size /= 2;
        return size;
    }

    public String getName(int index) {
        index = index * 2;
        if (mNameValues == null || index >= mNameValues.length) {
            return null;
        }
        return mNameValues[index];
    }

    public String getValue(int index) {
        index = index * 2 + 1;
        if (mNameValues == null || index >= mNameValues.length) {
            return null;
        }
        return mNameValues[index];
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


    @Override
    public String toString() {
        return "Headers{" +
                "mNameValues=" + Arrays.toString(mNameValues) +
                '}';
    }
}
