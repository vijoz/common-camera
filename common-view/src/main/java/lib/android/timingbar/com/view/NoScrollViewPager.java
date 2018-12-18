package lib.android.timingbar.com.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * NoScrollViewPager
 * -----------------------------------------------------------------------------------------------------------------------------------
 * 不能滚动的viewpager
 *
 * @author rqmei on 2018/11/5
 */

public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(@NonNull Context context) {
        super (context);
    }

    public NoScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super (context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return false;//不拦截事件，让嵌套的子viewpager有机会响应触摸事件
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        // 重写ViewPager滑动事件，改什么都不做
        return true;
    }

    @Override
    public void setCurrentItem(int item) {
        //表示切换的时候，不需要切换时间。
        super.setCurrentItem (item, false);
    }
}
