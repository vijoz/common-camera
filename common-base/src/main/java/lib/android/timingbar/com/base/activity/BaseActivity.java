package lib.android.timingbar.com.base.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lib.android.timingbar.com.base.app.BaseApplication;
import lib.android.timingbar.com.base.mvp.EventMessage;
import lib.android.timingbar.com.base.mvp.IPresenter;
import lib.android.timingbar.com.base.util.EventBusUtils;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * BaseActivity
 * -----------------------------------------------------------------------------------------------------------------------------------
 *
 * @author rqmei on 2018/2/5
 */

public abstract class BaseActivity<P extends IPresenter> extends AppCompatActivity implements IActivity<P> {
    protected P mPresenter;
    protected BaseApplication application;
    protected Unbinder unbinder;

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView (name, context, attrs);
    }

    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (getLayoutResId ());
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder ().permitAll ().build ();
            StrictMode.setThreadPolicy (policy);
        }
        if (isRegisterEventBus ()) {
            EventBusUtils.register (this);
        }
        unbinder = ButterKnife.bind (this);
        initView (savedInstanceState);
        initData (savedInstanceState);
        initListener ();
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState (savedInstanceState);
        if (application == null) {
            application = (BaseApplication) this.getApplication ();
        }
        if (mPresenter == null) {
            mPresenter = obtainPresenter ();
        }

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (application == null) {
            application = (BaseApplication) this.getApplication ();
        }
        if (mPresenter == null) {
            mPresenter = obtainPresenter ();
        }
    }

    @Override
    public void initListener() {

    }


    @Override
    public P obtainPresenter() {
        return null;
    }

    @Override
    public void setPresenter(P presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public boolean useFragment() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy ();
        if (isRegisterEventBus ()) {
            EventBusUtils.unregister (this);
        }
        if (unbinder != null)
            unbinder.unbind ();
    }

    /**
     * 是否注册事件分发
     *
     * @return true 注册；false 不注册，默认不注册
     */
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
}
