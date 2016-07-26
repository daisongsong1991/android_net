package com.netease.idate.net.okhttp;

import com.netease.idate.net.api.NetClient;
import com.netease.idate.net.api.NetHandler;
import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

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
        Map<String, Object> params = new HashMap<>();
        params.put("name", "netease");
        params.put("id", 123);
        mNetClient.get("http://www.163.com", params, new NetHandler() {
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

    @Test
    public void testCancelGet() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "netease");
        params.put("id", 123);
        Object tag = mNetClient.get("http://www.163.com", params, new NetHandler() {
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
        mNetClient.cancel(tag);
        Thread.sleep(2_000L);
    }

    @Test
    public void testPost() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "netease");
        params.put("id", 123);
        mNetClient.post("http://www.163.com", params, new NetHandler() {
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