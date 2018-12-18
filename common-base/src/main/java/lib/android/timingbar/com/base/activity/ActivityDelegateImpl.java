package lib.android.timingbar.com.base.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Parcel;
import lib.android.timingbar.com.base.mvp.IPresenter;

/**
 * ActivityDelegateImpl
 * -----------------------------------------------------------------------------------------------------------------------------------
 * activity的接口代表具体实现
 *
 * @author rqmei on 2018/1/30
 */

@SuppressLint("ParcelCreator")
public class ActivityDelegateImpl implements IActivityDelegate {
    private Activity mActivity;
    private IActivity iActivity;
   // private Unbinder mUnbinder;
    private IPresenter iPresenter;


    public ActivityDelegateImpl(Activity activity) {
        this.mActivity = activity;
        this.iActivity = (IActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
