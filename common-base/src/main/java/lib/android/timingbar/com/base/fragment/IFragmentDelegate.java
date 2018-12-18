package lib.android.timingbar.com.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * IFragmentDelegate
 * -----------------------------------------------------------------------------------------------------------------------------------
 * Fragment各生命周期方法相关的接口
 *
 * @author rqmei on 2018/1/30
 */

public interface IFragmentDelegate extends Parcelable {
    String FRAGMENT_DELEGATE = "fragment_delegate";

    /**
     * 调用该方法，将fragment与activity相关联
     *
     * @param context
     */
    void onAttach(Context context);

    /**
     * 调用该方法，将fragment初始化；
     *
     * @param savedInstanceState
     */
    void onCreate(Bundle savedInstanceState);

    /**
     * 调用该方法，创建视图，返回结果View；
     *
     * @param view
     * @param savedInstanceState
     */
    void onCreateView(View view, Bundle savedInstanceState);

    /**
     * onViewCreated是在onCreateView后被触发的事件
     *
     * @param view
     * @param savedInstanceState
     */
    void onViewCreated(View view, @Nullable Bundle savedInstanceState);

    /**
     * 调用该方法，通知fragment，activity中的oncreate()方法已完成；
     *
     * @param savedInstanceState
     */
    void onActivityCreate(Bundle savedInstanceState);

    /**
     * 调用该方法，通知fragment，该视图层已保存；
     *
     * @param savedInstanceState
     */
    void onViewStateRestored(Bundle savedInstanceState);

    /**
     * 调用该方法，和activity的started方法相关联，将fragment展示给用户；
     */
    void onStart();

    /**
     * 调用该方法，和activity的resumed方法相关联，使fragment与用户交互；
     */
    void onResume();

    /**
     * 调用该方法，fragment不与用户进行交互，此时activity的paused方法被调用，或者fragment正在被activity修改；
     */
    void onPause();

    /**
     * 调用该方法，fragment不再展示，此时activity的stopped方法被调用，或者fragment正在被activity修改；
     */
    void onStop();

    void onSaveInstanceState(Bundle outState);

    /**
     * 调用该方法，fragment清除掉资源与该activity的关联；
     */
    void onDestroyView();

    /**
     * 调用该方法，清除fragment的状态；
     */
    void onDestroy();

    /**
     * 调用该方法，清除fragment与该activity的关联；
     */
    void onDetach();

    /**
     * Return true if the fragment is currently added to its activity.
     */
    boolean isAdded();
}
