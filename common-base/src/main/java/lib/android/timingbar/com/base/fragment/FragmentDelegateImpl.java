package lib.android.timingbar.com.base.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import lib.android.timingbar.com.base.mvp.IPresenter;

/**
 * FragmentDelegateImpl
 * -----------------------------------------------------------------------------------------------------------------------------------
 * Fragment的接口代表具体实现
 *
 * @author rqmei on 2018/1/30
 */

@SuppressLint("ParcelCreator")
public class FragmentDelegateImpl implements IFragmentDelegate {
    private FragmentManager mFragmentManager;
    private Fragment mFragment;
    private IFragment iFragment;
  //  private Unbinder mUnbinder;
    private IPresenter iPresenter;

    public FragmentDelegateImpl(FragmentManager fragmentManager, Fragment fragment) {
        this.mFragmentManager = fragmentManager;
        this.mFragment = fragment;
        this.iFragment = (IFragment) fragment;
    }

    @Override
    public void onAttach(Context context) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

    }

    @Override
    public void onCreateView(View view, Bundle savedInstanceState) {

    }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    
  }

  @Override
    public void onActivityCreate(Bundle savedInstanceState) {

    }

  @Override
  public void onViewStateRestored(Bundle savedInstanceState) {

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
    public void onDestroyView() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onDetach() {

    }

    @Override
    public boolean isAdded() {
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
