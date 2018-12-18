package com.timingbar.android.library.ui.adapter;

import android.support.v4.app.FragmentManager;
import com.timingbar.android.library.ui.activity.fragment.EnhanceItemFragment;
import lib.android.timingbar.com.base.adapter.BaseFragmentPagerAdapter;
import lib.android.timingbar.com.base.fragment.BaseLazyFragment;

import java.util.List;

/**
 * EnhanceTabPagerAdapter
 * -----------------------------------------------------------------------------------------------------------------------------------
 *
 * @author rqmei on2018/11/1
 */

public class EnhanceTabPagerAdapter extends BaseFragmentPagerAdapter<BaseLazyFragment> {

    public EnhanceTabPagerAdapter(FragmentManager fm) {
        super (fm);
    }

    public EnhanceTabPagerAdapter(FragmentManager fm, String[] titls, List<BaseLazyFragment> fragments) {
        super (fm, titls, fragments);
    }

    @Override
    protected void init(FragmentManager fm, List<BaseLazyFragment> list) {
        for (int i = 0; i < 5; i++) {
            list.add (EnhanceItemFragment.newInstance (i + 1));
        }
    }
}