package lib.android.timingbar.com.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lib.android.timingbar.com.base.app.BaseApplication;
import lib.android.timingbar.com.base.mvp.EventMessage;
import lib.android.timingbar.com.base.mvp.IPresenter;
import lib.android.timingbar.com.base.util.EventBusUtils;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * BaseFragment
 * -----------------------------------------------------------------------------------------------------------------------------------
 *
 * @author rqmei on 2018/11/6
 */

public abstract class BaseFragment<P extends IPresenter> extends Fragment implements IFragment<P> {
    protected P mPresenter;
    protected BaseApplication application;
    Unbinder unbinder;
    /**
     * onCreateView方法中方法的view,
     */
    protected View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate (getLayoutRes (), container, false);
        }
        if (isRegisterEventBus ()) {
            EventBusUtils.register (this);
        }
        unbinder = ButterKnife.bind (this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated (savedInstanceState);
        if (application == null) {
            application = (BaseApplication) getActivity ().getApplication ();
        }
        if (mPresenter == null) {
            mPresenter = obtainPresenter ();
        }
    }

    /**
     * 通知fragment，该视图层已保存
     * 每次新建Fragment都会发生.
     * 它并不是实例状态恢复的方法, 只是一个View状态恢复的回调.
     * 在onActivityCreated()和onStart()之间调用
     *
     * @param savedInstanceState
     */
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored (savedInstanceState);
        if (application == null) {
            application = (BaseApplication) getActivity ().getApplication ();
        }
        if (mPresenter == null) {
            mPresenter = obtainPresenter ();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView ();
        if (isRegisterEventBus ()) {
            EventBusUtils.unregister (this);
        }
        if (unbinder != null) {
            unbinder.unbind ();
        }
    }

    /**
     * 是否使用eventBus,默认为使用(true)，
     */
    @Override
    public boolean isRegisterEventBus() {
        return true;
    }

    /**
     * 接收到分发的事件
     *
     * @param event 事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveEvent(EventMessage event) {
    }

    /**
     * 接受到分发的粘性事件
     *
     * @param event 粘性事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceiveStickyEvent(EventMessage event) {
    }

    @Override
    public void setPresenter(P presenter) {
        this.mPresenter = presenter;
    }
}
