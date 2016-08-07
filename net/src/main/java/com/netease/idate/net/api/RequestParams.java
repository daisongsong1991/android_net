package com.netease.idate.net.api;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by daisongsong on 16/8/7.
 */

public class RequestParams {
    private String[] mNames;
    private Object[] mValues;

    private boolean mHasMultiPartData = false;

    private RequestParams() {
    }

    public int size() {
        return mNames == null ? 0 : mNames.length;
    }

    public Object getValue(int index) {
        if (mValues == null || index >= mValues.length) {
            return null;
        }
        return mValues[index];
    }

    public String getName(int index) {
        if (mNames == null || index >= mNames.length) {
            return null;
        }
        return mNames[index];
    }

    public boolean hasMultiPartData() {
        return mHasMultiPartData;
    }

    public static class Builder {
        private List<String> mNames = new ArrayList<>();
        private List<Object> mValues = new ArrayList<>();
        private boolean mHasMultiPartData = false;

        public Builder addParam(String name, Object value) {
            mNames.add(name);
            mValues.add(value);
            checkMultiPartData(value);
            return this;
        }

        public RequestParams build() {
            RequestParams params = new RequestParams();
            params.mNames = mNames.toArray(new String[mNames.size()]);
            params.mValues = mValues.toArray(new Object[mValues.size()]);
            params.mHasMultiPartData = mHasMultiPartData;
            return params;
        }

        private void checkMultiPartData(Object value) {
            if (mHasMultiPartData) {
                return;
            }

            if (value != null && (value instanceof File || value instanceof InputStream)) {
                mHasMultiPartData = true;
            }
        }
    }

    @Override
    public String toString() {
        return "RequestParams{" +
                "mNames=" + Arrays.toString(mNames) +
                ", mValues=" + Arrays.toString(mValues) +
                ", mHasMultiPartData=" + mHasMultiPartData +
                '}';
    }
}
