package lib.android.timingbar.com.camera;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import camera.android.timingbar.com.cameralibrary.R;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.zbar.Image;

import java.util.List;


/**
 * CameraPreview
 * -----------------------------------------------------------------------------------------------------------------------------------
 * 扫描预览界面
 *
 * @author rqmei on 2018/4/4
 */
public class ScanCameraPreview extends FrameLayout implements
        SurfaceHolder.Callback {
    private CameraManager mCameraManager;
    private CameraScanAnalysis mPreviewCallback;
    private SurfaceView mSurfaceView;
    private ImageView scanLine;
    private ValueAnimator mScanAnimator;
    private RelativeLayout scanContainer, scanCropView;
    Context context;

    public ScanCameraPreview(Context context) {
        this (context, null);
    }

    public ScanCameraPreview(Context context, AttributeSet attrs) {
        this (context, attrs, 0);
    }

    public ScanCameraPreview(final Context context, AttributeSet attrs, int defStyleAttr) {
        super (context, attrs, defStyleAttr);
        this.context = context;
        mPreviewCallback = new CameraScanAnalysis (context, new CameraScanAnalysis.PreviewInterface () {
            @Override
            public void onPreviewFrame(Image barcode) {
                Rect mCropRect = initCrop ();
                if (mCropRect != null) {
                    barcode.setCrop (mCropRect.left, mCropRect.top, mCropRect.width (), mCropRect.height ());
                }
            }
        });
        mCameraManager = mPreviewCallback.getCameraManager ();
        AndPermission.with (context)
                .permission (Manifest.permission.CAMERA)
                .callback (new PermissionListener () {
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                        if (!start ()) {
                            Log.i ("ScanCameraPreview", "相机呗占用");
                            new AlertDialog.Builder (context)
                                    .setTitle (R.string.camera_failure)
                                    .setMessage (R.string.camera_hint)
                                    .setCancelable (false)
                                    .setPositiveButton (R.string.ok, new DialogInterface.OnClickListener () {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (mPreviewCallback.mCallback != null) {
                                                mPreviewCallback.mCallback.openCameraFail ();
                                            }
                                        }
                                    })
                                    .show ();
                        }
                    }

                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                        AndPermission.defaultSettingDialog (context).show ();
                    }
                })
                .start ();
    }

    /**
     * Set Scan results callback.
     *
     * @param callback {@link ScanCallback}.
     */
    public void setScanCallback(ScanCallback callback) {
        mPreviewCallback.setScanCallback (callback);
    }

    /**
     * Camera start preview.
     */
    public boolean start() {
        try {
            mCameraManager.openDriver (Camera.CameraInfo.CAMERA_FACING_BACK);
        } catch (Exception e) {
            Log.i ("ScanCameraPreview", "CameraPreview 打开相机失败。。。");
            return false;
        }
        mPreviewCallback.onStart ();
        if (mSurfaceView == null) {
            Log.i ("ScanCameraPreview", "CameraPreview 开起预览。。。");
            mSurfaceView = new SurfaceView (getContext ());
            addView (mSurfaceView, new LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            SurfaceHolder holder = mSurfaceView.getHolder ();
            holder.addCallback (this);
            holder.setType (SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        startCameraPreview (mSurfaceView.getHolder ());
        initScanAnimator ();
        return true;
    }

    /**
     * Camera stop preview.
     */
    public void stop() {
        if (mScanAnimator != null) {
            mScanAnimator.cancel ();
        }
        removeCallbacks (mAutoFocusTask);
        mPreviewCallback.onStop ();
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
            mCameraManager.startPreview (holder, mPreviewCallback);
            mCameraManager.autoFocus (mFocusCallback);
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (holder.getSurface () == null) {
            return;
        }
        mCameraManager.stopPreview ();
        startCameraPreview (holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
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

    private void initScanAnimator() {
        int dimen = getResources ().getDimensionPixelSize (R.dimen.x200);
        if (scanLine == null) {
            View view = LayoutInflater.from (getContext ()).inflate (R.layout.scan_container, this, false);
            LayoutParams layoutParams = new LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            scanLine = view.findViewById (R.id.scan_line);
            scanContainer = view.findViewById (R.id.scan_container);
            scanCropView = view.findViewById (R.id.scan_crop_view);
            this.addView (view, layoutParams);
        }
        if (mScanAnimator == null) {
            Log.i ("ScanCameraPreview", "ScanActivity onWindowFocusChanged111--------");
            int height = dimen - 60;
            mScanAnimator = ObjectAnimator.ofFloat (scanLine, "translationY", 0F, height).setDuration (3000);
            mScanAnimator.setInterpolator (new LinearInterpolator ());
            mScanAnimator.setRepeatCount (ValueAnimator.INFINITE);
            mScanAnimator.setRepeatMode (ValueAnimator.REVERSE);
        }
        if (mScanAnimator != null) {
            mScanAnimator.start ();
        }
    }

    /**
     * 初始化截取的矩形区域
     */
    public Rect initCrop() {
        Rect rect = null;
        int cameraWidth = mCameraManager.getCameraResolution ().y;
        int cameraHeight = mCameraManager.getCameraResolution ().x;

        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        scanCropView.getLocationInWindow (location);

        int cropLeft = location[0];
        int cropTop = location[1] - mCameraManager.getStatusBarHeight (context);

        int cropWidth = scanCropView.getWidth ();
        int cropHeight = scanCropView.getHeight ();

        /** 获取布局容器的宽高 */
        int containerWidth = scanContainer.getWidth ();
        int containerHeight = scanContainer.getHeight ();
        if (containerWidth != 0 && containerHeight != 0) {
            /** 计算最终截取的矩形的左上角顶点x坐标 */
            int x = cropLeft * cameraWidth / containerWidth;
            /** 计算最终截取的矩形的左上角顶点y坐标 */
            int y = cropTop * cameraHeight / containerHeight;

            /** 计算最终截取的矩形的宽度 */
            int width = cropWidth * cameraWidth / containerWidth;
            /** 计算最终截取的矩形的高度 */
            int height = cropHeight * cameraHeight / containerHeight;
            /** 生成最终的截取的矩形 */
            rect = new Rect (x, y, width + x, height + y);
        }
        return rect;
    }
}
