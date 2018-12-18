package lib.android.timingbar.com.base.activity;

import android.os.Bundle;
import android.os.Parcelable;

/**
 * ActivityDelegate
 * -----------------------------------------------------------------------------------------------------------------------------------
 * activity各生命周期方法相关的接口
 *
 * @author rqmei on 2018/1/29
 */

public interface IActivityDelegate extends Parcelable {
    String LAYOUT_LINEARLAYOUT = "LinearLayout";
    String LAYOUT_FRAMELAYOUT = "FrameLayout";
    String LAYOUT_RELATIVELAYOUT = "RelativeLayout";
    String ACTIVITY_DELEGATE = "activity_delegate";

    void onCreate(Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onSaveInstanceState(Bundle outState);

    void onDestroy();
}
