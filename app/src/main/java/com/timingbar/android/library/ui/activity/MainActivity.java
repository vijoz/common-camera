package com.timingbar.android.library.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.timingbar.android.library.R;
import com.timingbar.android.library.control.CommonControl;
import com.timingbar.android.library.module.entity.Lesson;
import com.timingbar.android.library.presenter.CommonPresenter;
import lib.android.timingbar.com.base.activity.BaseActivity;
import lib.android.timingbar.com.base.imageloader.glide.GlideImageConfig;
import lib.android.timingbar.com.base.mvp.EventMessage;
import lib.android.timingbar.com.http.util.HttpLog;

import java.util.List;

public class MainActivity extends BaseActivity<CommonPresenter> implements View.OnClickListener, CommonControl.View {
    @BindView(R.id.btn_scan)
    Button btnScan;
    @BindView(R.id.btn_camera)
    Button btnCamera;
    @BindView(R.id.btn_hand_sign)
    Button btnHandSign;
    @BindView(R.id.btn_player)
    Button btnPlayer;
    @BindView(R.id.iv_glid)
    ImageView ivGlid;
    ProgressDialog progressDialog;
    @BindView(R.id.btn_enhance_tab)
    Button btnEnhanceTab;

    @Override
    public void onClick(View v) {
        switch (v.getId ()) {
            case R.id.btn_scan://二维码
                startActivity (new Intent (this, ScanActivity.class));
                break;
            case R.id.btn_camera://相机
                startActivity (new Intent (this, CameraActivity.class));
                break;
            case R.id.btn_hand_sign://手写签名
                HttpLog.i ("mainActivity==" + application.appManager ().getActivityList ().size ());
                startActivity (new Intent (this, HandSignActivity.class));
                break;
            case R.id.btn_player://播放器
                startActivity (new Intent (this, PlayerActivity.class));
                break;
            case R.id.btn_enhance_tab:
                startActivity (new Intent (this, BottomTabActivity.class));
                break;
        }
    }

    @Override
    public void showLoading() {
        if (progressDialog != null && !progressDialog.isShowing ()) {
            progressDialog.show ();
        }

    }

    @Override
    public void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing ()) {
            progressDialog.dismiss ();
        }
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        progressDialog = new ProgressDialog (MainActivity.this);
        progressDialog.setMessage ("正在获取章节列表");
        btnScan.setOnClickListener (this);
        btnCamera.setOnClickListener (this);
        btnHandSign.setOnClickListener (this);
        btnPlayer.setOnClickListener (this);
        btnEnhanceTab.setOnClickListener (this);
        mPresenter.getVersionCode ();
        mPresenter.getLsssonPhase (this);
        application.imageLoader ().loadImage (getApplicationContext (), GlideImageConfig.builder ().imageView (ivGlid).placeholder (R.mipmap.ic_launcher).transformationType (4).url ("http://ww1.sinaimg.cn/mw600/6345d84ejw1dvxp9dioykg.gif").build ());
        application.imageLoader ().loadImage (this, GlideImageConfig.builder ().url ("http://pic14.nipic.com/20110607/6776092_111031284000_2.jpg").loadAnimal (R.anim.slide_in_left).cacheStrategy (1).transformationType (2).bgView (btnPlayer).placeholder (R.mipmap.ic_launcher).build ());
    }

    @Override
    public CommonPresenter obtainPresenter() {
        return new CommonPresenter (application);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy ();
        mPresenter.onDestroy ();
    }

    @Override
    public void onReceiveEvent(EventMessage event) {
        List<Lesson> lessons = (List<Lesson>) event.getData ();
        HttpLog.i ("mainActivity onReceiveEvent===" + lessons.size ());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind (this);
    }
}
