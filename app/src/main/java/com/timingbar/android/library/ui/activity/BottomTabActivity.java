package com.timingbar.android.library.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import butterknife.BindView;
import com.timingbar.android.library.R;
import com.timingbar.android.library.ui.activity.fragment.EnhanceItemFragment;
import com.timingbar.android.library.ui.activity.fragment.EnhanceTabTestFragment;
import com.timingbar.android.library.ui.adapter.EnhanceTabPagerAdapter;
import lib.android.timingbar.com.base.activity.BaseActivity;
import lib.android.timingbar.com.base.fragment.BaseLazyFragment;
import lib.android.timingbar.com.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * BottomTabActivity
 * -----------------------------------------------------------------------------------------------------------------------------------
 *
 * @author rqmei on 2018/11/5
 */

public class BottomTabActivity extends BaseActivity {
    @BindView(R.id.vp_bottom)
    NoScrollViewPager vpBottom;
    @BindView(R.id.tab_bottom)
    TabLayout tabBottom;
    EnhanceTabPagerAdapter adapter;
    List<BaseLazyFragment> fragments = new ArrayList<> ();
    String[] titls = {"首页", "理论", "习题", "个人"};

    @Override
    public int getLayoutResId() {
        return R.layout.bottom_tab;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        fragments.add (new EnhanceTabTestFragment ());
        fragments.add (EnhanceItemFragment.newInstance (2));
        fragments.add (EnhanceItemFragment.newInstance (3));
        fragments.add (EnhanceItemFragment.newInstance (4));
        adapter = new EnhanceTabPagerAdapter (getSupportFragmentManager (),titls,fragments);
        vpBottom.setAdapter (adapter);
        tabBottom.setupWithViewPager (vpBottom);
        for (int i = 0; i < tabBottom.getTabCount (); i++) {
            TabLayout.Tab tab = tabBottom.getTabAt (i);
            Drawable d = null;
            switch (i) {
                case 0:
                    d = getResources ().getDrawable (R.drawable.selector_home);
                    break;
                case 1:
                    d = getResources ().getDrawable (R.drawable.selector_home);
                    break;
                case 2:
                    d = getResources ().getDrawable (R.drawable.selector_home);
                    break;
                case 3:
                    d = getResources ().getDrawable (R.drawable.selector_home);
                    break;
            }
            tab.setIcon (d);
        }
        //        viewpager.setCurrentItem(2);  
    }

}
