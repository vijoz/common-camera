package lib.android.timingbar.com.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import lib.android.timingbar.com.base.util.BaseLog;

/**
 * FragmentLifecycle
 * -----------------------------------------------------------------------------------------------------------------------------------
 * fragment 的生命周期管理
 *
 * @author rqmei on 2018/9/13
 */

public class FragmentLifecycle extends FragmentManager.FragmentLifecycleCallbacks {
    @Override
    public void onFragmentAttached(FragmentManager fm, Fragment f, Context context) {
        super.onFragmentAttached (fm, f, context);
        BaseLog.w (f.toString () + " - onFragmentAttached");
        if (f instanceof IFragment && f.getArguments () != null) {
            IFragmentDelegate fragmentDelegate = fetchFragmentDelegate (f);
            if (fragmentDelegate == null || !fragmentDelegate.isAdded ()) {
                fragmentDelegate = new FragmentDelegateImpl (fm, f);
                f.getArguments ().putParcelable (IFragmentDelegate.FRAGMENT_DELEGATE, fragmentDelegate);
            }
            fragmentDelegate.onAttach (context);
        }
    }

    @Override
    public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
        super.onFragmentCreated (fm, f, savedInstanceState);
        BaseLog.w (f.toString () + " - onFragmentCreated");
        IFragmentDelegate fragmentDelegate = fetchFragmentDelegate (f);
        if (fragmentDelegate != null) {
            fragmentDelegate.onCreate (savedInstanceState);
        }
    }

    @Override
    public void onFragmentViewCreated(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
        super.onFragmentViewCreated (fm, f, v, savedInstanceState);
        BaseLog.w (f.toString () + " - onFragmentViewCreated");
        IFragmentDelegate fragmentDelegate = fetchFragmentDelegate (f);
        if (fragmentDelegate != null) {
            fragmentDelegate.onCreateView (v, savedInstanceState);
        }
    }

    @Override
    public void onFragmentActivityCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
        super.onFragmentActivityCreated (fm, f, savedInstanceState);
        BaseLog.w (f.toString () + " - onFragmentActivityCreated");
        IFragmentDelegate fragmentDelegate = fetchFragmentDelegate (f);
        if (fragmentDelegate != null) {
            fragmentDelegate.onActivityCreate (savedInstanceState);
        }
    }

    @Override
    public void onFragmentStarted(FragmentManager fm, Fragment f) {
        super.onFragmentStarted (fm, f);
        BaseLog.w (f.toString () + " - onFragmentStarted");
        IFragmentDelegate fragmentDelegate = fetchFragmentDelegate (f);
        if (fragmentDelegate != null) {
            fragmentDelegate.onStart ();
        }
    }

    @Override
    public void onFragmentResumed(FragmentManager fm, Fragment f) {
        super.onFragmentResumed (fm, f);
        BaseLog.w (f.toString () + " - onFragmentResumed");
        IFragmentDelegate fragmentDelegate = fetchFragmentDelegate (f);
        if (fragmentDelegate != null) {
            fragmentDelegate.onResume ();
        }
    }

    @Override
    public void onFragmentPaused(FragmentManager fm, Fragment f) {
        super.onFragmentPaused (fm, f);
        BaseLog.w (f.toString () + " - onFragmentPaused");
        IFragmentDelegate fragmentDelegate = fetchFragmentDelegate (f);
        if (fragmentDelegate != null) {
            fragmentDelegate.onPause ();
        }
    }

    @Override
    public void onFragmentStopped(FragmentManager fm, Fragment f) {
        super.onFragmentStopped (fm, f);
        BaseLog.w (f.toString () + " - onFragmentStopped");
        IFragmentDelegate fragmentDelegate = fetchFragmentDelegate (f);
        if (fragmentDelegate != null) {
            fragmentDelegate.onStop ();
        }
    }

    @Override
    public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
        super.onFragmentViewDestroyed (fm, f);
        BaseLog.w (f.toString () + " - onFragmentViewDestroyed");
        IFragmentDelegate fragmentDelegate = fetchFragmentDelegate (f);
        if (fragmentDelegate != null) {
            fragmentDelegate.onDestroyView ();
        }
    }

    @Override
    public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
        super.onFragmentDestroyed (fm, f);
        BaseLog.w (f.toString () + " - onFragmentDestroyed");
        IFragmentDelegate fragmentDelegate = fetchFragmentDelegate (f);
        if (fragmentDelegate != null) {
            fragmentDelegate.onDestroy ();
        }
    }

    @Override
    public void onFragmentDetached(FragmentManager fm, Fragment f) {
        super.onFragmentDetached (fm, f);
        BaseLog.w (f.toString () + " - onFragmentDetached");
        IFragmentDelegate fragmentDelegate = fetchFragmentDelegate (f);
        if (fragmentDelegate != null) {
            fragmentDelegate.onDetach ();
            f.getArguments ().clear ();
        }
    }

    /**
     * 将fragment 转换为自定义的 IFragmentDelegate
     *
     * @param fragment
     * @return
     */
    private IFragmentDelegate fetchFragmentDelegate(Fragment fragment) {
        if (fragment instanceof IFragment) {
            return fragment.getArguments () == null ? null : (IFragmentDelegate) fragment.getArguments ().getParcelable (IFragmentDelegate.FRAGMENT_DELEGATE);
        }
        return null;
    }

}
