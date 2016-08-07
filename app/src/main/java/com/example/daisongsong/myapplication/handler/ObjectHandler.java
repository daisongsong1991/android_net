package com.example.daisongsong.myapplication.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.idate.net.api.HttpResponse;
import com.netease.idate.net.api.NetHandler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by daisongsong on 16/8/7.
 */

public abstract class ObjectHandler<T> extends NetHandler {
    private static final String CHARSET = "UTF-8";

    private Type mType;

    public ObjectHandler() {
        java.lang.reflect.Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) superclass).getActualTypeArguments();
            if (actualTypeArguments != null && actualTypeArguments.length > 0) {
                mType = actualTypeArguments[0];
            }
        }

        if (mType == null) {
            throw new RuntimeException("can not parse data type of T");
        }
    }

    @Override
    public void onResponse(HttpResponse response) {
        super.onResponse(response);

        try {
            String jsonString = new String(response.getData(), CHARSET);
            JSONObject object = JSON.parseObject(jsonString);

            Object o = object.getObject("data", (Class<Object>) mType);
            T data = (T) o;
            onObjectResponse(response.getCode(), data);
        } catch (Exception e) {
            onFailure(new HttpResponse.Builder()
                    .code(HttpResponse.CODE_JSON_ERROR)
                    .request(response.getHttpRequest())
                    .exception(e)
                    .build());
        }
    }

    protected abstract void onObjectResponse(int httpCode, T data);

    public static class D<T> {
        public int resultCode;
        public T data;

        @Override
        public String toString() {
            return "D{" +
                    "resultCode=" + resultCode +
                    ", data=" + data +
                    '}';
        }
    }
}
