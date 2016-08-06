package com.netease.idate.net.okhttp;

import com.netease.idate.net.api.HttpRequest;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by daisongsong on 16-8-5.
 */
public class RequestFactory {


    private RequestFactory() {
    }

    public static final RequestFactory getInstance() {
        return RequestFactoryInstance.INSTANCE;
    }

    public Request createRequest(HttpRequest httpRequest) {
        Request request = null;
        switch (httpRequest.getMethod()) {
            case HttpRequest.GET:
                request = createGetRequest(httpRequest.getUrl(), httpRequest.getParams());
                break;
            case HttpRequest.POST:
                request = createPostRequest(httpRequest.getUrl(), httpRequest.getParams());
                break;
            default:
                break;
        }
        return request;
    }

    Request createGetRequest(String url, Map<String, Object> params) {
        String requestBody = null;
        if (params != null) {
            StringBuilder sb = new StringBuilder("?");
            for (Map.Entry<String, Object> e : params.entrySet()) {
                sb.append(String.format("%s=%s&", e.getKey(), String.valueOf(e.getValue())));
            }
            requestBody = sb.substring(0, sb.length() - 1);
        }

        String fullUrl = requestBody == null || requestBody.length() == 0 ? url : url + requestBody;

        Request request = new Request.Builder()
                .url(fullUrl)
                .method("GET", null)
                .build();
        return request;
    }

    Request createPostRequest(String url, Map<String, Object> params) {
        RequestBody body = createRequestBody(params);

        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .build();
        return request;
    }

    private RequestBody createRequestBody(Map<String, Object> params) {
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

    private static final class RequestFactoryInstance {
        private static final RequestFactory INSTANCE = new RequestFactory();
    }
}
