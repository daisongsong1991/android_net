package com.example.daisongsong.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.netease.idate.net.api.Headers;
import com.netease.idate.net.api.HttpRequest;
import com.netease.idate.net.api.HttpResponse;
import com.netease.idate.net.api.NetClient;
import com.netease.idate.net.api.NetHandler;
import com.netease.idate.net.api.RequestParams;
import com.netease.idate.net.api.cookie.impl.SharePreferenceCookieStore;
import com.netease.idate.net.okhttp.OkHttpNetClient;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

public class MainActivity extends Activity {
    private static final String BASE_URL = "http://192.168.31.120:8080";
    private TextView mTextViewContent;
    private NetClient mNetClient = new OkHttpNetClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNetClient.setCookieStore(new SharePreferenceCookieStore(getApplicationContext()));
        mTextViewContent = (TextView) findViewById(R.id.mTextViewContent);

        findViewById(R.id.mButtonTestSaveCookie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNetClient.enqueue(
                        new HttpRequest.Builder()
                                .url(BASE_URL + "/set_cookie")
                                .addHeader("HEADER1", "header")
                                .addHeader("HEADER1", "header1")
                                .addHeader("CLIENT_TIME", String.valueOf(System.currentTimeMillis()))
                                .addParam("params1", "English")
                                .addParam("params2", "中文")
                                .build(),
                        new NetHandler() {
                            @Override
                            public void onResponse(HttpResponse response) {
                                try {
                                    setContentText(new String(response.getData(), "UTF-8"));
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(HttpResponse response) {
                                setContentText(response.getException().getMessage());
                            }
                        });
            }
        });

        findViewById(R.id.mButtonTestUseCookie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNetClient.enqueue(new HttpRequest.Builder()
                                .headers(new Headers.Builder()
                                        .addHeader("HEADER1", "header")
                                        .addHeader("HEADER1", "header1")
                                        .addHeader("CLIENT_TIME", String.valueOf(System.currentTimeMillis()))
                                        .build())
                                .url(BASE_URL + "/use_cookie")
                                .params(new RequestParams.Builder()
                                        .addParam("params1", "网络")
                                        .addParam("params2", "安卓")
                                        .build())
                                .method(HttpRequest.POST)
                                .build(),
                        new NetHandler() {
                            @Override
                            public void onResponse(HttpResponse response) {
                                try {
                                    setContentText(new String(response.getData(), "UTF-8"));
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(HttpResponse response) {
                                setContentText(response.getException().getMessage());
                            }
                        });
            }
        });
        findViewById(R.id.mButtonTestMultiPart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ByteArrayInputStream bais = new ByteArrayInputStream("start,数据流类型,stream,end".getBytes());
                mNetClient.enqueue(new HttpRequest.Builder()
                                .headers(new Headers.Builder()
                                        .addHeader("HEADER1", "header")
                                        .addHeader("HEADER1", "header1")
                                        .addHeader("CLIENT_TIME", String.valueOf(System.currentTimeMillis()))
                                        .build())
                                .url(BASE_URL + "/multipart")
                                .params(new RequestParams.Builder()
                                        .addParam("params1", "网络")
                                        .addParam("params2", "安卓")
                                        .addParam("stream", bais)
                                        .build())
                                .method(HttpRequest.POST)
                                .build(),
                        new NetHandler() {
                            @Override
                            public void onResponse(HttpResponse response) {
                                try {
                                    setContentText(new String(response.getData(), "UTF-8"));
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(HttpResponse response) {
                                setContentText(response.getException().getMessage());
                            }
                        });
            }
        });

    }


    private void setContentText(final String msg) {
        mTextViewContent.post(new Runnable() {
            @Override
            public void run() {
                mTextViewContent.setText(msg);
            }
        });
    }

}
