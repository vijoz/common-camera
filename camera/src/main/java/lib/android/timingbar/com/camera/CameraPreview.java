package lib.android.timingbar.com.camera;

import android.Manifest;
import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import camera.android.timingbar.com.cameralibrary.R;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.List;

/**
 * CameraPreview
 * -----------------------------------------------------------------------------------------------------------------------------------
 * 拍照预览页面
 *
 * @author rqmei on 2018/4/25
 */

public class CameraPreview extends FrameLayout implements SurfaceHolder.Callback {
    CameraPictureAnalysis cameraPictureAnalysis;
    private CameraManager mCameraManager;
    private SurfaceView mSurfaceView;
    private View headView, buttomView;
    private HeadViewHolder headViewHolder;
    Context context;

    public CameraPreview(@NonNull Context context) {
        this (context, null);
    }

    public CameraPreview(@NonNull Context context, @Nullable AttributeSet attrs) {
        this (context, attrs, 0);
    }

    public CameraPreview(@NonNull final Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super (context, attrs, defStyleAttr);
        this.context = context;
        if (mCameraManager == null) {
            mCameraManager = new CameraManager (context);
        }
        //  动态获取相机权限并打开camera
        AndPermission.with (context)
                .permission (Manifest.permission.CAMERA)
                .callback (new PermissionListener () {
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                        initCamera (Camera.CameraInfo.CAMERA_FACING_FRONT);
                        cameraPictureAnalysis = new CameraPictureAnalysis (getContext (), mCameraManager);
                    }

                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                        AndPermission.defaultSettingDialog (context).show ();
                    }
                })
                .start ();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i ("CameraPreview", "cameraPreview surfaceCreated");
        startCameraPreview (mSurfaceView.getHolder ());
        mCameraManager.getmOrEventListener ().enable ();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i ("cameraManager", "cameraPreview surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCameraManager.getmOrEventListener ().disable ();
    }

    /**
     * Camera start preview.
     */
    public boolean initCamera(int cameraId) {
        try {
            mCameraManager.openDriver (cameraId);
        } catch (Exception e) {
            Log.i ("CameraPreview", "CameraPreview 打开相机失败。。。" + e.getMessage ());
            return false;
        }
        if (mSurfaceView == null) {
            Log.i ("CameraPreview", "CameraPreview 开起预览。。。");
            mSurfaceView = new SurfaceView (getContext ());
            Point point = mCameraManager.getCameraResolution ();
            Point screenResolution = mCameraManager.getScreenResolution ();
            LayoutParams layoutParams = new LayoutParams (point.y, point.x);
            float screenProp = (float) (screenResolution.y * 1.0 / screenResolution.x);
            float previewProp = point.y == 0 ? 0.0F : ((float) point.x / (float) point.y);
            if (screenProp > 1.8 && previewProp != 0) { //屏幕的宽高比大于1.8的时候处理
                int measuredHeight = screenResolution.y;
                int measuredWidth = screenResolution.x;
                float clacWidth = measuredHeight / previewProp;   //计算出要显示的预览界面的宽度。
                layoutParams = new LayoutParams ((int) clacWidth, measuredHeight);
                if (clacWidth > 800 && Math.abs (clacWidth - measuredWidth) > clacWidth * 0.1F) {  //计算的宽度大于 800 并且和显示正常的布局的误差超过10%
                    layoutParams.width = (int) clacWidth;
                }
                Log.i ("CameraPreview=", "width=" + clacWidth + ",layoutParams.height=" + measuredHeight);
            }
            Log.i ("CameraPreview", "start point=" + point.x + "," + point.y);
            addView (mSurfaceView, layoutParams);
            SurfaceHolder holder = mSurfaceView.getHolder ();
            holder.addCallback (this);
            holder.setType (SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        addHeadView ();
        addButtomView ();
        return true;
    }


    /**
     * 自定义相机顶部view
     */
    private void addHeadView() {
        if (headView == null) {
            headView = LayoutInflater.from (getContext ()).inflate (R.layout.camera_header_bar, this, false);
            addView (headView);
            headViewHolder = new HeadViewHolder (headView);
            //设置闪光灯
            headViewHolder.ivFlashMode.setOnClickListener (new OnClickListener () {
                @Override
                public void onClick(View v) {
                    CameraManager.FlashMode flashMode = mCameraManager.getFlashMode ();
                    if (flashMode == CameraManager.FlashMode.AUTO) {
                        mCameraManager.setFlashMode (CameraManager.FlashMode.TORCH);
                        headViewHolder.ivFlashMode.setImageResource (R.drawable.camera_flash_torch);
                    } else if (flashMode == CameraManager.FlashMode.OFF) {
                        mCameraManager.setFlashMode (CameraManager.FlashMode.AUTO);
                        headViewHolder.ivFlashMode.setImageResource (R.drawable.camera_flash_auto);
                    } else if (flashMode == CameraManager.FlashMode.ON) {
                        mCameraManager.setFlashMode (CameraManager.FlashMode.OFF);
                        headViewHolder.ivFlashMode.setImageResource (R.drawable.camera_flash_off);
                    } else if (flashMode == CameraManager.FlashMode.TORCH) {
                        mCameraManager.setFlashMode (CameraManager.FlashMode.ON);
                        headViewHolder.ivFlashMode.setImageResource (R.drawable.camera_flash_on);
                    }
                }
            });
            //切换摄像头
            headViewHolder.ivSwitchCamera.setOnClickListener (new OnClickListener () {
                @Override
                public void onClick(View v) {
                    stop ();
                    Log.i ("CameraPreview", "cameraPreView 开始切换camera摄像头isFrontCamera=" + mCameraManager.isFrontCamera () + "," + mCameraManager.isOpen ());
                    if (mCameraManager.isFrontCamera ()) {
                        if (initCamera (Camera.CameraInfo.CAMERA_FACING_BACK)) {
                            startCameraPreview (mSurfaceView.getHolder ());
                        }
                    } else {
                        if (initCamera (Camera.CameraInfo.CAMERA_FACING_FRONT)) {
                            startCameraPreview (mSurfaceView.getHolder ());
                        }
                    }
                }
            });
        }
    }

    /**
     * 自定义相机底部view
     */
    private void addButtomView() {
        if (buttomView == null) {
            buttomView = LayoutInflater.from (getContext ()).inflate (R.layout.camera_bottom_bar, this, false);
            addView (buttomView);
            ImageView ivShutterCamera = (ImageView) buttomView.findViewById (R.id.iv_shutter_camera);
            ivShutterCamera.setOnClickListener (new OnClickListener () {
                @Override
                public void onClick(View v) {

                    mCameraManager.doTakePicture (cameraPictureAnalysis);
                }
            });
        }
    }

    class HeadViewHolder {
        ImageView ivFlashMode;
        ImageView ivSwitchCamera;

        HeadViewHolder(View view) {
            ivFlashMode = (ImageView) view.findViewById (R.id.iv_flash_mode);
            ivSwitchCamera = (ImageView) view.findViewById (R.id.iv_switch_camera);
        }
    }

    /**
     * Camera stop preview.
     */
    public void stop() {
        removeCallbacks (mAutoFocusTask);
        mCameraManager.stopPreview ();
        mCameraManager.closeDriver ();
    }

    /**
     * camera开起预览
     *
     * @param holder
     */
    private void startCameraPreview(SurfaceHolder holder) {
        try {
            mCameraManager.startOrientationChangeListener (context);
            mCameraManager.startPreview (holder, null);
            mCameraManager.autoFocus (mFocusCallback);
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    /**
     * 拍照成功后是否保存照片到手机中
     *
     * @param saveBtimap true:是
     */
    public void setSaveBtimap(boolean saveBtimap) {
        mCameraManager.setSaveBtimap (saveBtimap);
    }

    private Camera.AutoFocusCallback mFocusCallback = new Camera.AutoFocusCallback () {
        public void onAutoFocus(boolean success, Camera camera) {
            if (success) {
                postDelayed (mAutoFocusTask, 1000);
            }
        }
    };
    /**
     * 自动对焦
     */
    private Runnable mAutoFocusTask = new Runnable () {
        public void run() {
            mCameraManager.autoFocus (mFocusCallback);
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        stop ();
        super.onDetachedFromWindow ();
    }

    public void setPictureCallback(PictureCallback callback) {
        if (cameraPictureAnalysis != null) {
            cameraPictureAnalysis.setPictureCallback (callback);
        }
    }
}
