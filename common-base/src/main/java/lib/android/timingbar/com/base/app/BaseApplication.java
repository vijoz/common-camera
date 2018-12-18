package lib.android.timingbar.com.base.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import lib.android.timingbar.com.base.imageloader.ImageLoader;
import lib.android.timingbar.com.base.integration.AppManager;
import lib.android.timingbar.com.base.mvp.IRepositoryManager;
import lib.android.timingbar.com.base.mvp.RepositoryManagerImpl;

/**
 * BaseApplication
 * -----------------------------------------------------------------------------------------------------------------------------------
 * 基类application
 *
 * @author rqmei on 2018/1/29
 */

public class BaseApplication extends Application implements IApp {
    private AppDelegateImpl mAppDelegate;
    AppManager appManager;
    IRepositoryManager repositoryManager;
    ImageLoader imageLoader;


    @Override
    public void onCreate() {
        super.onCreate ();
        appManager = new AppManager (this);
        this.mAppDelegate = new AppDelegateImpl (this);
        this.imageLoader = new ImageLoader ();
        this.repositoryManager = new RepositoryManagerImpl ();
        this.mAppDelegate.onCreate ();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext (base);
        MultiDex.install (base);
    }

    /**
     * 在模拟环境中程序终止时会被调用
     */
    @Override
    public void onTerminate() {
        super.onTerminate ();
        if (mAppDelegate != null)
            this.mAppDelegate.onTerminate ();
    }

    /**
     * 将IAppComponent返回出去,供其它地方使用, IAppComponent接口中声明的方法返回的实例,在getAppComponent()拿到对象后都可以直接使用
     *
     * @return IAppComponent实例对象
     */
    @Override
    public AppManager appManager() {
        if (appManager == null) {
            appManager = new AppManager (this);
        }
        return appManager;
    }

    @Override
    public IRepositoryManager repositoryManager() {
        return repositoryManager == null ? new RepositoryManagerImpl () : repositoryManager;
    }

    @Override
    public ImageLoader imageLoader() {
        return imageLoader == null ? new ImageLoader () : imageLoader;
    }
}
