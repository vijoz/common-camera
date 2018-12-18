package lib.android.timingbar.com.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * DisplayUtils
 * -----------------------------------------------------------------------------------------------------------------------------------
 * 关于UI展示的工具类
 *
 * @author rqmei on 2018/4/3
 */

public class DisplayUtils {
    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidthPixels(Activity context) {
        DisplayMetrics metric = new DisplayMetrics ();
        context.getWindowManager ().getDefaultDisplay ().getMetrics (metric);
        return metric.widthPixels;
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidthPixels(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService (Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay ().getWidth ();
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeightPixels(Activity context) {
        DisplayMetrics metric = new DisplayMetrics ();
        context.getWindowManager ().getDefaultDisplay ().getMetrics (metric);
        return metric.heightPixels;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeightPixels(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService (Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay ().getHeight ();
    }

    /**
     * 切换屏幕的方向.
     */
    public static void toggleScreenOrientation(Activity activity) {
        activity.setRequestedOrientation (isPortrait (activity)
                ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 获得当前屏幕的方向.
     *
     * @return 是否竖屏.
     */
    public static boolean isPortrait(Context context) {
        int orientation = context.getResources ().getConfiguration ().orientation;
        return orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * 获取状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources ().getIdentifier ("status_bar_height", "dimen", "android");
        return context.getResources ().getDimensionPixelSize (resourceId);
    }
}
