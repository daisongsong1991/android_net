package com.netease.idate.net.api;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by daisongsong on 16/8/7.
 */

public abstract class JsonNetHandler<T> extends NetHandler {
    private Type mType;

    public JsonNetHandler() {
        Type superclass = getClass().getGenericSuperclass();
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
    public final void onResponse(HttpResponse response) {
        super.onResponse(response);

        byte[] data = response.getData();
        try {
            Object o = JSON.parseObject(new String(data, "UTF-8"), mType);
            onResponse(response.getCode(), (T) o);
        } catch (Exception e) {
            e.printStackTrace();
            onResponse(new HttpResponse.Builder()
                    .code(HttpResponse.CODE_JSON_ERROR)
                    .request(response.getHttpRequest())
                    .exception(e)
                    .build());
        }
    }

    protected abstract void onResponse(int httpCode, T data);

}
