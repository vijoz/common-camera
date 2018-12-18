package com.timingbar.android.library.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.timingbar.android.library.R;
import lib.android.timingbar.com.base.activity.BaseActivity;
import lib.android.timingbar.com.camera.CameraPreview;
import lib.android.timingbar.com.camera.PictureCallback;

/**
 * CameraActivity
 * -----------------------------------------------------------------------------------------------------------------------------------
 *
 * @author rqmei on 2018/4/25
 */

public class CameraActivity extends BaseActivity {
    @BindView(R.id.camera)
    CameraPreview camera;

    @Override
    public int getLayoutResId() {
        return R.layout.camera;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        camera.setPictureCallback (new PictureCallback () {
            @Override
            public void onPictureTakenResult(Bitmap bitmap) {
                Log.i ("CameraActivity", " setPictureCallback拍照成功了" + bitmap);
            }
        });
    }
}
