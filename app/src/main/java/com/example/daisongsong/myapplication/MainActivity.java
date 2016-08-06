package com.example.daisongsong.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.netease.idate.net.api.NetClient;
import com.netease.idate.net.api.NetHandler;
import com.netease.idate.net.api.cookie.impl.SharePreferenceCookieStore;
import com.netease.idate.net.okhttp.OkHttpNetClient;

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
                mNetClient.get(BASE_URL + "/set_cookie", null, new NetHandler() {
                    @Override
                    public void onResponse(int httpCode, byte[] body) {
                        setContentText(new String(body));
                    }

                    @Override
                    public void onFailure(int httpCode, String message) {
                        setContentText(message);
                    }
                });
            }
        });

        findViewById(R.id.mButtonTestUseCookie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNetClient.get(BASE_URL + "/use_cookie", null, new NetHandler() {
                    @Override
                    public void onResponse(int httpCode, byte[] body) {
                        setContentText(new String(body));
                    }

                    @Override
                    public void onFailure(int httpCode, String message) {
                        setContentText(message);
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
