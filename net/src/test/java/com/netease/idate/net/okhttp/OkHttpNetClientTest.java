package com.netease.idate.net.okhttp;

import com.netease.idate.net.api.NetClient;
import com.netease.idate.net.api.NetHandler;

import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

/**
 * Created by daisongsong on 16-7-26.
 */
public class OkHttpNetClientTest {
    private NetClient mNetClient;

    @Before
    public void init() {
        mNetClient = new OkHttpNetClient();
    }

    @Test
    public void testGet() throws Exception {
        mNetClient.get("http://www.163.com", null, new NetHandler() {
            @Override
            public void onResponse(int httpCode, byte[] body) {
                try {
                    String b = new String(body, "UTF-8");
                    System.out.println("code=" + httpCode + ",body=" + b);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int httpCode, String message) {
                System.out.println("code=" + httpCode + ",message=" + message);
            }
        });

        Thread.sleep(2_000L);
    }
}