package lib.android.timingbar.com.base.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * BaseFragmentPagerAdapter
 * -----------------------------------------------------------------------------------------------------------------------------------
 *
 * @author rqmei on 2018/11/2
 */

public class BaseFragmentPagerAdapter<T extends Fragment> extends FragmentPagerAdapter {
    private List<T> mFragments = new ArrayList<> (); // Fragment集合

    private T mCurrentFragment; // 当前显示的Fragment

    private String[] titles;

    /**
     * 在Activity中使用ViewPager适配器
     */
    public BaseFragmentPagerAdapter(FragmentActivity activity) {
        this (activity.getSupportFragmentManager ());
    }

    /**
     * 在Fragment中使用ViewPager适配器
     */
    public BaseFragmentPagerAdapter(Fragment fragment) {
        this (fragment.getChildFragmentManager ());
    }

    public BaseFragmentPagerAdapter(FragmentManager fm) {
        super (fm);
        init (fm, mFragments);
    }

    public BaseFragmentPagerAdapter(FragmentManager fm, List<T> mFragments) {
        super (fm);
        this.mFragments = mFragments;
    }

    public BaseFragmentPagerAdapter(FragmentManager fm, String[] titles, List<T> mFragments) {
        super (fm);
        this.titles = titles;
        this.mFragments = mFragments;
    }

    //初始化Fragment
    protected void init(FragmentManager fm, List<T> list) {
    }

    ;

    @Override
    public T getItem(int position) {
        return mFragments.get (position);
    }

    @Override
    public int getCount() {
        return mFragments.size ();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment () != object) {
            // 记录当前的Fragment对象
            mCurrentFragment = (T) object;
        }
        super.setPrimaryItem (container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles == null ? super.getPageTitle (position) : titles[position];
    }

    /**
     * 获取Fragment集合
     */
    public List<T> getAllFragment() {
        return mFragments;
    }

    /**
     * 获取当前的Fragment
     */
    public T getCurrentFragment() {
        return mCurrentFragment;
    }
}
