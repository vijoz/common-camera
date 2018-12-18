package com.timingbar.android.app;

import lib.android.timingbar.com.base.app.BaseApplication;
import lib.android.timingbar.com.http.EasyHttp;

/**
 * App
 * -----------------------------------------------------------------------------------------------------------------------------------
 *
 * @author rqmei on 2018/9/7
 */

public class ModuleApp extends BaseApplication {
    private static ModuleApp app;

    public static ModuleApp getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate ();
        app = this;
        EasyHttp.init (this);
        EasyHttp.getInstance ().debug ("lib-camera", true).setBaseUrl ("http://www.jsyxx.cn/edu/mobile/");
        Thread.setDefaultUncaughtExceptionHandler (AppException.getAppExceptionHandler (this));
    }

}
