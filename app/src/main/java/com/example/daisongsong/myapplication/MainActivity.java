package com.example.daisongsong.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.netease.idate.net.t.Test;

import java.util.Locale;

public class MainActivity extends Activity {
    private TextView mTextViewTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewTest = (TextView) findViewById(R.id.mTextViewTest);

        mTextViewTest.setText(String.format(Locale.CHINA, "10+20=%d", Test.add(10, 20)));
    }
}
