package com.netease.idate.net.okhttp;

import com.netease.idate.net.api.Headers;
import com.netease.idate.net.api.HttpRequest;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by daisongsong on 16-8-5.
 */
class RequestFactory {
    private static String CHARSET = "UTF-8";

    private RequestFactory() {
    }

    static RequestFactory getInstance() {
        return RequestFactoryInstance.INSTANCE;
    }

    Request createRequest(HttpRequest httpRequest) {
        Request request = null;
        switch (httpRequest.getMethod()) {
            case HttpRequest.GET:
                request = createGetRequest(httpRequest);
                break;
            case HttpRequest.POST:
                request = createPostRequest(httpRequest);
                break;
            default:
                break;
        }
        return request;
    }

    private Request createGetRequest(HttpRequest httpRequest) {
        String url = httpRequest.getUrl();
        Map<String, Object> params = httpRequest.getParams();
        Headers headers = httpRequest.getHeaders();

        String requestBody = null;
        if (params != null) {
            StringBuilder sb = new StringBuilder("?");
            for (Map.Entry<String, Object> e : params.entrySet()) {
                try {
                    String value = URLEncoder.encode(String.valueOf(e.getValue()), CHARSET);
                    sb.append(String.format("%s=%s&", e.getKey(), value));
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
            }
            requestBody = sb.substring(0, sb.length() - 1);
        }
        String fullUrl = requestBody == null || requestBody.length() == 0 ? url : url + requestBody;

        okhttp3.Headers okHeaders = makeHeaders(headers);

        return new Request.Builder()
                .url(fullUrl)
                .method("GET", null)
                .headers(okHeaders)
                .build();
    }

    private Request createPostRequest(HttpRequest httpRequest) {
        RequestBody body = createPostRequestBody(httpRequest.getParams());

        okhttp3.Headers okHeaders = makeHeaders(httpRequest.getHeaders());

        return new Request.Builder()
                .url(httpRequest.getUrl())
                .method("POST", body)
                .headers(okHeaders)
                .build();
    }

    private RequestBody createPostRequestBody(Map<String, Object> params) {
        boolean hasMultiData = false;
        RequestBody body = null;
        MultipartBody.Builder builder = new MultipartBody.Builder();
        FormBody.Builder formBuilder = null;

        for (Map.Entry<String, Object> e : params.entrySet()) {
            Object value = e.getValue();
            String key = e.getKey();
            if (value instanceof File) {
                // TODO: 16-7-27
                hasMultiData = true;
            } else if (value instanceof InputStream) {
                // TODO: 16-7-27
                hasMultiData = true;
            } else {
                if (formBuilder == null) {
                    formBuilder = new FormBody.Builder();
                }
                formBuilder.add(key, String.valueOf(value));
            }
        }

        if (formBuilder != null) {
            if (hasMultiData) {
                MultipartBody.Part part = MultipartBody.Part.create(formBuilder.build());
                builder.addPart(part);
                body = builder.build();
            } else {
                body = formBuilder.build();
            }
        }
        return body;
    }

    private okhttp3.Headers makeHeaders(Headers headers) {
        okhttp3.Headers.Builder builder = new okhttp3.Headers.Builder();

        String[] nameValueArray = headers.getNameValueArray();
        if (nameValueArray == null || nameValueArray.length == 0) {
            return builder.build();
        }

        int size = nameValueArray.length;
        size = size % 2 == 0 ? size : size - 1;

        for (int i = 0; i < size; i += 2) {
            String name = nameValueArray[i];
            String value = nameValueArray[i + 1];
            builder.add(name, value);
        }

        return builder.build();
    }


    private static final class RequestFactoryInstance {
        private static final RequestFactory INSTANCE = new RequestFactory();
    }
}
