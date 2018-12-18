package lib.android.timingbar.com.base.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import lib.android.timingbar.com.base.fragment.BaseLazyFragment;
import lib.android.timingbar.com.base.mvp.IPresenter;

/**
 * IActivity
 * -----------------------------------------------------------------------------------------------------------------------------------
 * activity 通用的一些方法
 *
 * @author rqmei on 2018/1/30
 */

public interface IActivity<P extends IPresenter> {
    /**
     * 是否使用eventBus传递事件处理
     *
     * @return true 表示需要使用eventBus传递事件，这个时候需要注册订阅
     */
    boolean isRegisterEventBus();

    /**
     * 如果getLayoutResId返回0,框架则不会调用{@link android.app.Activity#setContentView(int)}
     *
     * @return view 的layout id
     */
    @LayoutRes
    int getLayoutResId();

    /**
     * 初始化view相关
     *
     * @param savedInstanceState
     * @return
     */
    void initView(Bundle savedInstanceState);

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    void initData(Bundle savedInstanceState);

    /**
     * 监听事件
     */
    void initListener();

    /**
     * @return
     */
    P obtainPresenter();

    /**
     * @param presenter
     */
    void setPresenter(P presenter);

    /**
     * 这个Activity是否会使用Fragment,框架会根据这个属性判断是否注册{@link android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks}
     * 如果返回false,那意味着这个Activity不需要绑定Fragment,那你再在这个Activity中绑定继承于 {@link BaseLazyFragment} 的Fragment将不起任何作用
     *
     * @return
     */
    boolean useFragment();
}
