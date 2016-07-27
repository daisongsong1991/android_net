package com.example.daisongsong.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.netease.idate.net.api.NetClient;
import com.netease.idate.net.api.NetHandler;
import com.netease.idate.net.okhttp.OkHttpNetClient;
import com.netease.idate.net.t.Test;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

public class MainActivity extends Activity {
    private TextView mTextViewTest;

    private TextView mTextViewContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewTest = (TextView) findViewById(R.id.mTextViewTest);
        mTextViewContent = (TextView) findViewById(R.id.mTextViewContent);

        mTextViewTest.setText(String.format(Locale.CHINA, "10+20=%d", Test.add(10, 20)));

        findViewById(R.id.mButtonNet).setOnClickListener(new View.OnClickListener() {
            private NetClient mNetClient = new OkHttpNetClient();

            @Override
            public void onClick(View v) {
                mNetClient.get("http://10.250.22.52:8080", null, new NetHandler() {
                    @Override
                    public void onResponse(int httpCode, final byte[] body) {
                        mTextViewContent.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    mTextViewContent.setText(new String(body, "UTF-8"));
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(int httpCode, final String message) {
                        mTextViewContent.post(new Runnable() {
                            @Override
                            public void run() {
                                mTextViewContent.setText(message);
                            }
                        });
                    }
                });
            }
        });
    }
}
