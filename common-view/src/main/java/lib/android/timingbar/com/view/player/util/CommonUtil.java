package lib.android.timingbar.com.view.player.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.view.WindowManager;

/**
 * CommonUtil
 * -----------------------------------------------------------------------------------------------------------------------------------
 *
 * @author rqmei on 2018/9/6
 */

public class CommonUtil {
    /**
     * Get activity from context object
     *
     * @param context something
     * @return object of Activity or null if it is not Activity
     */
    public static Activity scanForActivity(Context context) {
        if (context == null)
            return null;
        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return scanForActivity (((ContextWrapper) context).getBaseContext ());
        }
        return null;
    }

    /**
     * 根据context 获取AppCompatActivity
     *
     * @param context
     * @return
     */
    private static AppCompatActivity getAppCompActivity(Context context) {
        if (context == null)
            return null;
        if (context instanceof AppCompatActivity) {
            return (AppCompatActivity) context;
        } else if (context instanceof ContextThemeWrapper) {
            return getAppCompActivity (((ContextThemeWrapper) context).getBaseContext ());
        }
        return null;
    }

    @SuppressLint("RestrictedApi")
    public static void showActionBar(Context context) {
        AppCompatActivity appCompatActivity = getAppCompActivity (context);
        if (appCompatActivity != null) {
            ActionBar ab = appCompatActivity.getSupportActionBar ();
            if (ab != null) {
                ab.setShowHideAnimationEnabled (false);
                ab.show ();
            }
        }
        scanForActivity (context)
                .getWindow ()
                .clearFlags (WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @SuppressLint("RestrictedApi")
    public static void hideActionBar(Context context) {
        AppCompatActivity appCompatActivity = getAppCompActivity (context);
        if (appCompatActivity != null) {
            ActionBar ab = appCompatActivity.getSupportActionBar ();
            if (ab != null) {
                ab.setShowHideAnimationEnabled (false);
                ab.hide ();
            }
        }
        scanForActivity (context)
                .getWindow ()
                .setFlags (WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 判断网络是否连接
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>}</p>
     *
     * @param context 上下文
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService (Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo ();
        return info != null && info.isConnected ();
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals (input))
            return true;

        for (int i = 0; i < input.length (); i++) {
            char c = input.charAt (i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }
}
