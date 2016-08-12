package com.example.daisongsong.myapplication;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * Created by NetEase on 2016/4/29 0029.
 */
public class PhoneUtils {
    private static int sStatusBarHeight = Integer.MIN_VALUE;
    private static int sBottomMenuHeight = 0;   //针对魅族

    private PhoneUtils() {

    }

    public static int getStatusBarHeight(Context context) {
        if (sStatusBarHeight != Integer.MIN_VALUE) {
            return sStatusBarHeight;
        }

        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sStatusBarHeight = context.getResources().getDimensionPixelSize(x);
            return sStatusBarHeight;
        } catch (Exception e) {
            Log.d("PhoneUtils", "getStatusBarHeight Exception", e);
            return 0;
        }
    }

    public static int getBottomMenuHeight() {
        return sBottomMenuHeight;
    }

    public static void setBottomMenuHeight(int bottomMenuHeight) {
        sBottomMenuHeight = bottomMenuHeight;
    }
}
