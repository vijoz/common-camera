package lib.android.timingbar.com.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.internal.BaselineLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lib.android.timingbar.com.base.mvp.EventMessage;
import lib.android.timingbar.com.base.mvp.IPresenter;
import lib.android.timingbar.com.base.util.BaseLog;
import lib.android.timingbar.com.base.util.EventBusUtils;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * BaseFragment
 * -----------------------------------------------------------------------------------------------------------------------------------
 * 因为java只能单继承,所以如果有需要继承特定Fragment的三方库,那你就需要自己自定义Fragment
 * 继承于这个特定的Fragment,然后按照将BaseFragment的格式,复制过去,记住一定要实现{@link IFragment}
 * Fragment基类，封装了懒加载的实现
 * 2、Viewpager + Fragment情况下，fragment的生命周期因Viewpager的缓存机制而失去了具体意义
 * 该抽象类自定义一个新的回调方法，当fragment可见状态改变时会触发的回调方法
 *
 * @author rqmei on 2018/1/30
 */

public abstract class BaseLazyFragment<P extends IPresenter> extends BaseFragment {
    /**
     * fragment是否是首次可见
     */
    private boolean isFirstVisible;
    /**
     * view 是否创建成功
     */
    private boolean isViewCreated = false;
    /**
     * fragment当前显示、隐藏的状态
     */
    private boolean currentVisibleState = false;

    public BaseLazyFragment() {
        //必须确保在Fragment实例化时setArguments()
        setArguments (new Bundle ());
    }

    /**
     * onViewCreated是在onCreateView后被触发的事件
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //如果setUserVisibleHint()在rootView创建前调用时，那么
        //就等到rootView创建完后才回调onFragmentVisibleChange(true)
        //保证onFragmentVisibleChange()的回调发生在rootView创建完成之后，以便支持ui操作
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated (savedInstanceState);
        BaseLog.e (getClass ().getSimpleName () + "  onActivityCreated   hidden " + isHidden () + "getUserVisibleHint=" + getUserVisibleHint ());
        isViewCreated = true;
    }


    /**
     * 视图是否已经对用户可见，系统的方法,在fragment的所有生命周期之前执行
     * setUserVisibleHint()在Fragment创建时会先被调用一次，传入isVisibleToUser = false
     * 如果当前Fragment可见，那么setUserVisibleHint()会再次被调用一次，传入isVisibleToUser = true
     * 如果Fragment从可见->不可见，那么setUserVisibleHint()也会被调用，传入isVisibleToUser = false
     * 总结：setUserVisibleHint()除了Fragment的可见状态发生变化时会被回调外，在new Fragment()时也会被回调
     *
     * @param isVisibleToUser true:ragment被用户可见;false 不可见
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint (isVisibleToUser);
        BaseLog.e (getClass ().getSimpleName () + "  setUserVisibleHint   isVisibleToUser " + isVisibleToUser + "currentVisibleState=" + currentVisibleState);
        if (rootView == null) {
            return;
        }
        // 对于默认 tab 和 间隔 checked tab 需要等到 isViewCreated = true 后才可以通过此通知用户可见
        // 这种情况下第一次可见不是在这里通知 因为 isViewCreated = false 成立,等从别的界面回到这里后会使用 onFragmentResume 通知可见
        // 对于非默认 tab mIsFirstVisible = true 会一直保持到选择则这个 tab 的时候，因为在 onActivityCreated 会返回 false
        if (isViewCreated) {
            if (isVisibleToUser && !currentVisibleState) {
                dispatchUserVisibleHint (true);
            } else if (!isVisibleToUser && currentVisibleState) {
                dispatchUserVisibleHint (false);
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged (hidden);
        BaseLog.e (getClass ().getSimpleName () + "  onHiddenChanged dispatchChildVisibleState  hidden " + hidden);
        if (hidden) {
            dispatchUserVisibleHint (false);
        } else {
            dispatchUserVisibleHint (true);
        }
    }

    @Override
    public void onResume() {
        super.onResume ();
        BaseLog.e (getClass ().getSimpleName () + "  isFirstVisible " + isFirstVisible + ",isHidden=" + isHidden () + ",currentVisibleState=" + currentVisibleState + ",getUserVisibleHint=" + getUserVisibleHint ());
        if (!isFirstVisible) {
            if (!isHidden () && !currentVisibleState && getUserVisibleHint ()) {
                dispatchUserVisibleHint (true);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause ();
        // 当前 Fragment 包含子 Fragment 的时候 dispatchUserVisibleHint 内部本身就会通知子 Fragment 不可见
        // 子 fragment 走到这里的时候自身又会调用一遍 ？
        if (currentVisibleState && getUserVisibleHint ()) {
            dispatchUserVisibleHint (false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView ();
        isViewCreated = false;
        isFirstVisible = true;
    }

    private boolean isFragmentVisible(Fragment fragment) {
        return !fragment.isHidden () && fragment.getUserVisibleHint ();
    }

    /**
     * 用于分发可见时间的时候父获取 fragment 是否隐藏
     *
     * @return true fragment 不可见， false 父 fragment 可见
     */
    private boolean isParentInvisible() {
        BaseLazyFragment fragment = (BaseLazyFragment) getParentFragment ();
        return fragment != null && !fragment.isSupportVisible ();

    }

    private boolean isSupportVisible() {
        return currentVisibleState;
    }


    /**
     * 统一处理 显示隐藏
     *
     * @param visible
     */
    private void dispatchUserVisibleHint(boolean visible) {
        //当前 Fragment 是 child 时候 作为缓存 Fragment 的子 fragment getUserVisibleHint = true
        //但当父 fragment 不可见所以 currentVisibleState = false 直接 return 掉
        //        LogUtils.e(getClass().getSimpleName() + "  dispatchUserVisibleHint isParentInvisible() " + isParentInvisible());
        // 这里限制则可以限制多层嵌套的时候子 Fragment 的分发
        if (visible && isParentInvisible ())
            return;
        //
        //        //此处是对子 Fragment 不可见的限制，因为 子 Fragment 先于父 Fragment回调本方法 currentVisibleState 置位 false
        //        // 当父 dispatchChildVisibleState 的时候第二次回调本方法 visible = false 所以此处 visible 将直接返回
        if (currentVisibleState == visible) {
            return;
        }

        currentVisibleState = visible;

        if (visible) {
            if (isFirstVisible) {
                isFirstVisible = false;
                onFragmentFirstVisible ();
            }
            onFragmentResume ();
            dispatchChildVisibleState (true);
        } else {
            dispatchChildVisibleState (false);
            onFragmentPause ();
        }
    }

    /**
     * 当前 Fragment 是 child 时候 作为缓存 Fragment 的子 fragment 的唯一或者嵌套 VP 的第一 fragment 时 getUserVisibleHint = true
     * 但是由于父 Fragment 还进入可见状态所以自身也是不可见的， 这个方法可以存在是因为庆幸的是 父 fragment 的生命周期回调总是先于子 Fragment
     * 所以在父 fragment 设置完成当前不可见状态后，需要通知子 Fragment 我不可见，你也不可见，
     * <p>
     * 因为 dispatchUserVisibleHint 中判断了 isParentInvisible 所以当 子 fragment 走到了 onActivityCreated 的时候直接 return 掉了
     * <p>
     * 当真正的外部 Fragment 可见的时候，走 setVisibleHint (VP 中)或者 onActivityCreated (hide show) 的时候
     * 从对应的生命周期入口调用 dispatchChildVisibleState 通知子 Fragment 可见状态
     *
     * @param visible
     */
    private void dispatchChildVisibleState(boolean visible) {
        FragmentManager childFragmentManager = getChildFragmentManager ();
        List<Fragment> fragments = childFragmentManager.getFragments ();
        if (!fragments.isEmpty ()) {
            for (Fragment child : fragments) {
                if (child instanceof BaseLazyFragment && !child.isHidden () && child.getUserVisibleHint ()) {
                    ((BaseLazyFragment) child).dispatchUserVisibleHint (visible);
                }
            }
        }
    }

    @Override
    public void onFragmentFirstVisible() {
        BaseLog.e (getClass ().getSimpleName () + "  对用户第一次可见");

    }

    @Override
    public void onFragmentResume() {
        BaseLog.e (getClass ().getSimpleName () + "  对用户可见");
    }

    @Override
    public void onFragmentPause() {
        BaseLog.e (getClass ().getSimpleName () + "  对用户不可见");
    }

}

