package com.timingbar.android.library.ui.activity.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import butterknife.BindView;
import com.timingbar.android.library.R;
import com.timingbar.android.library.ui.adapter.EnhanceTabPagerAdapter;
import lib.android.timingbar.com.base.activity.BaseActivity;
import lib.android.timingbar.com.base.fragment.BaseLazyFragment;
import lib.android.timingbar.com.base.mvp.IPresenter;
import lib.android.timingbar.com.view.EnhanceTabLayout;

/**
 * EnhanceTabTestActivity
 * -----------------------------------------------------------------------------------------------------------------------------------
 *
 * @author rqmei on 2018/11/1
 */

public class EnhanceTabTestFragment extends BaseLazyFragment {
    @BindView(R.id.enhance_tab_layout)
    EnhanceTabLayout enhanceTabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    String[] sTitle = {"首页", "新闻", "公告", "热点", "咨询"};
    EnhanceTabPagerAdapter adapter;

    @Override
    public int getLayoutRes() {
        return R.layout.enhance_tab_test;
    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

    @Override
    public void onFragmentFirstVisible() {

    }

    @Override
    public void onFragmentResume() {
        Log.i ("enhance", "count==" + enhanceTabLayout.getChildCount ());
        if (adapter == null) {
            for (int i = 0; i < sTitle.length; i++) {
                enhanceTabLayout.addTab (sTitle[i]);
            }
            adapter = new EnhanceTabPagerAdapter (getChildFragmentManager ());
            viewPager.setAdapter (adapter);
            viewPager.addOnPageChangeListener (new TabLayout.TabLayoutOnPageChangeListener (enhanceTabLayout.getTabLayout ()));
            viewPager.setCurrentItem (0);
            enhanceTabLayout.setupWithViewPager (viewPager);
        }
    }

    @Override
    public void onFragmentPause() {
        super.onFragmentPause ();
    }
}
