package com.timingbar.android.app;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import lib.android.timingbar.com.base.activity.ActivityLifecycle;
import lib.android.timingbar.com.base.app.AppDelegateImpl;
import lib.android.timingbar.com.base.integration.IConfigModule;
import retrofit2.HttpException;

import java.util.List;

/**
 * GlobalConfiguration
 * -----------------------------------------------------------------------------------------------------------------------------------
 * 配置框架的自定义属性
 * GlobalConfigModule使用建造者模式将App的全局配置信息封装进Module(使用Dagger注入到需要配置信息的地方),可以配置CacheFile,Interceptor等,
 * 甚至于Retrofit,Okhttp,RxCache都可以自定义配置,因为使用的是建造者模式所以如你有其他配置信息需要使用Dagger注入,直接就可以添加进Builder并且不会影响到其他地方
 *
 * @author rqmei on 2018/1/29
 */

public class GlobalConfiguration implements IConfigModule {
    ActivityLifecycle activityLifecycle;


    /**
     * 向Application的生命周期中注入一些自定义逻辑
     *
     * @param context
     * @param lifecycles
     */
    @Override
    public void injectAppLifecycle(Context context, List<AppDelegateImpl.Lifecycle> lifecycles) {
        // AppDelegate.Lifecycle 的所有方法都会在基类Application对应的生命周期中被调用,所以在对应的方法中可以扩展一些自己需要的逻辑
        lifecycles.add (new AppDelegateImpl.Lifecycle () {
            @Override
            public void onCreate(Application application) {
                
            }

            @Override
            public void onTerminate(Application application) {

            }
        });

    }

    /**
     * 向Activity的生命周期中注入一些自定义逻辑
     *
     * @param context
     * @param lifecycles
     */
    @Override
    public void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycles) {
       /* lifecycles.add (new Application.ActivityLifecycleCallbacks () {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });*/
    }

    /**
     * 向Fragment的生命周期中注入一些自定义逻辑
     *
     * @param context
     * @param lifecycles
     */
    @Override
    public void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {
        //        lifecycles.add (new FragmentManager.FragmentLifecycleCallbacks () {
        //            @Override
        //            public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
        //                super.onFragmentCreated (fm, f, savedInstanceState);
        //            }
        //
        //            @Override
        //            public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
        //                super.onFragmentDestroyed (fm, f);
        //            }
        //        });
    }

    private String convertStatusCode(HttpException httpException) {
        String msg;
        if (httpException.code () == 500) {
            msg = "服务器发生错误";
        } else if (httpException.code () == 404) {
            msg = "请求地址不存在";
        } else if (httpException.code () == 400) {
            msg = "移动端和服务端接口参数不对";
        } else if (httpException.code () == 403) {
            msg = "请求被服务器拒绝";
        } else if (httpException.code () == 307) {
            msg = "请求被重定向到其他页面";
        } else {
            msg = httpException.message ();
        }
        return msg;
    }
}
