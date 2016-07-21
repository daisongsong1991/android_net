package com.example.daisongsong.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.netease.idate.net.t.Test;

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
            @Override
            public void onClick(View v) {
                Test t = new Test();
                t.testNet(new Test.NetCallback() {
                    @Override
                    public void onSuccess(String s) {
                        mTextViewContent.setText(s);
                    }

                    @Override
                    public void onFailure(String e) {
                        mTextViewContent.setText(e);
                    }
                });
            }
        });
    }
}
