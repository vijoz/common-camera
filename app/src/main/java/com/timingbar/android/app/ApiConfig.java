package com.timingbar.android.app;

/**
 * ApiConfig
 * -----------------------------------------------------------------------------------------------------------------------------------
 * 网络相关常量配置
 *
 * @author rqmei on 2018/2/5
 */

public class ApiConfig {
    public static boolean DEBUG = false;
    /**
     * 项目连接服务端域名
     */
    public static String HOSTSERVER = DEBUG ? "http://edu.timingbar.com/edu/mobile/" : "http://www.jsyxx.cn/edu/mobile/";
    /**
     * 请求成功标识
     */
    public static boolean RequestSuccess = true;
    /**
     * 获取服务器的版本号
     */
    public static final String GET_VERSION_CODE = "version/";
    /**
     * 获取视频章
     */
    public static final String GET_LESSON_PHASE = "find/train/lesson/phase/";
}
