package com.netease.idate.net.api.cookie.impl;

import com.netease.idate.net.api.cookie.HttpCookie;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.Locale;

/**
 * Created by daisongsong on 16/8/6.
 */

public class CookieUtil {

    public static String httpCookieToString(HttpCookie cookie) {
        String result = null;
        ByteArrayOutputStream baos = null;
        ObjectOutputStream stream = null;
        try {
            baos = new ByteArrayOutputStream();
            stream = new ObjectOutputStream(baos);
            stream.writeObject(cookie);
            result = byteArrayToHexString(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static HttpCookie stringToHttpCookie(String cookieString) {
        HttpCookie cookie = null;
        byte[] cookieData = hexStringToByteArray(cookieString);
        ByteArrayInputStream bais = null;
        ObjectInputStream stream = null;
        try {
            bais = new ByteArrayInputStream(cookieData);
            stream = new ObjectInputStream(bais);
            cookie = (HttpCookie) stream.readObject();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return cookie;
    }


    private static String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte element : bytes) {
            int v = element & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.US);
    }

    private static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }
}
