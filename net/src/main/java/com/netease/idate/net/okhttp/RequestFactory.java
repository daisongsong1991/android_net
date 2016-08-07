package com.netease.idate.net.okhttp;

import com.netease.idate.net.api.Headers;
import com.netease.idate.net.api.HttpRequest;
import com.netease.idate.net.api.RequestParams;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

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
        Headers headers = httpRequest.getHeaders();

        RequestParams params = httpRequest.getParams();
        String fullUrl = createGetUrl(url, params);

        okhttp3.Headers okHeaders = makeHeaders(headers);

        return new Request.Builder()
                .url(fullUrl)
                .method("GET", null)
                .headers(okHeaders)
                .build();
    }

    private String createGetUrl(String url, RequestParams params) {
        String fullUrl = url;
        if (params != null) {
            String requestBody = null;
            StringBuilder sb = new StringBuilder("?");

            int size = params.size();
            for (int i = 0; i < size; ++i) {
                String value = null;
                try {
                    value = URLEncoder.encode(String.valueOf(params.getValue(i)), CHARSET);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                sb.append(String.format("%s=%s&", params.getName(i), value));
            }
            requestBody = sb.substring(0, sb.length() - 1);
            fullUrl = requestBody == null || requestBody.length() == 0 ? url : url + requestBody;
        }
        return fullUrl;
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

    private RequestBody createPostRequestBody(RequestParams params) {
        int size = params == null ? 0 : params.size();
        boolean hasMultiPart = params != null && params.hasMultiPartData();
        if (hasMultiPart) {
            MultipartBody.Builder builder = new MultipartBody.Builder();
            for (int i = 0; i < size; i++) {
                String name = params.getName(i);
                Object value = params.getValue(i);
                if (value instanceof File) {
                    builder.addFormDataPart(name,
                            ((File) value).getAbsolutePath(),
                            RequestBody.create(MediaType.parse("multipart/form-data"), (File) value));
                } else if (value instanceof InputStream) {
                    final InputStream inputStream = (InputStream) value;
                    builder.addFormDataPart(name, inputStream.toString(), new RequestBody() {
                        @Override
                        public MediaType contentType() {
                            return MediaType.parse("multipart/form-data");
                        }

                        @Override
                        public long contentLength() throws IOException {
                            return inputStream.available();
                        }

                        @Override
                        public void writeTo(BufferedSink sink) throws IOException {
                            Source source = null;
                            try {
                                source = Okio.source(inputStream);
                                sink.writeAll(source);
                            } finally {
                                Util.closeQuietly(source);
                            }
                        }
                    });
                } else {
                    builder.addFormDataPart(name, String.valueOf(value));
                }
            }
            return builder.build();
        } else {
            FormBody.Builder formBuilder = new FormBody.Builder();
            for (int i = 0; i < size; i++) {
                String name = params.getName(i);
                Object value = params.getValue(i);
                formBuilder.add(name, String.valueOf(value));
            }
            return formBuilder.build();
        }
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
