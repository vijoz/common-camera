package com.timingbar.android.library.ui.activity.fragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import butterknife.BindView;
import com.timingbar.android.library.R;
import com.timingbar.android.library.presenter.CommonPresenter;
import lib.android.timingbar.com.base.fragment.BaseLazyFragment;
import lib.android.timingbar.com.base.mvp.IPresenter;

/**
 * EnhanceItemFragment
 * -----------------------------------------------------------------------------------------------------------------------------------
 *
 * @author rqmei on 2018/11/1
 */

public class EnhanceItemFragment extends BaseLazyFragment<CommonPresenter> {

    @BindView(R.id.text)
    TextView text;

    public static EnhanceItemFragment newInstance(int position) {
        EnhanceItemFragment enhanceItemFragment = new EnhanceItemFragment ();
        Bundle bundle = new Bundle ();
        bundle.putInt ("position", position);
        enhanceItemFragment.setArguments (bundle);
        return enhanceItemFragment;
    }

    @Override
    public void onFragmentFirstVisible() {
        //去服务器下载数据
    }

    @Override
    public int getLayoutRes() {
        return R.layout.common_text;
    }

    @Override
    public void onFragmentResume() {
        Log.i ("EnhanceItemFragment", "onFragmentResume========");
        text.setText ("当前是第" + getArguments ().getInt ("position", 0) + "个页面");
    }

    @Override
    public IPresenter obtainPresenter() {
        return new CommonPresenter (application);
    }

}
