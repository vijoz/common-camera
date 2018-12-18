package lib.android.timingbar.com.base.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import lib.android.timingbar.com.base.mvp.IPresenter;

/**
 * IFragment
 * -----------------------------------------------------------------------------------------------------------------------------------
 * Fragment 通用的一些方法
 *
 * @author rqmei on 2018/1/30
 */

public interface IFragment<P extends IPresenter> {
    /**
     * 是否使用eventBus传递事件处理
     *
     * @return true 表示需要使用eventBus传递事件，这个时候需要注册订阅
     */
    boolean isRegisterEventBus();

    /**
     * 返回布局 resId
     *
     * @return layoutId
     */
    int getLayoutRes();

    /**
     * 在fragment首次可见时回调，可用于加载数据，防止每次进入都重复加载数据
     */
    void onFragmentFirstVisible();

    /**
     * 去除setUserVisibleHint()多余的回调场景，保证只有当fragment可见状态发生变化时才回调
     * 回调时机在view创建完后，所以支持ui操作，解决在setUserVisibleHint()里进行ui操作有可能报null异常的问题
     * <p>
     * 可在该回调方法里进行一些ui显示
     */
    void onFragmentResume();

    /**
     * fragment 对用户不可见
     */
    void onFragmentPause();

    /**
     * @return
     */
    P obtainPresenter();

    /**
     * 设置协调Model和View模块工作，处理交互的Presenter
     *
     * @param presenter
     */
    void setPresenter(P presenter);
}
