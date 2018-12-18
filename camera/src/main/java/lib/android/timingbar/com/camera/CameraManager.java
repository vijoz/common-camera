package lib.android.timingbar.com.camera;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;


/**
 * CameraManager
 * -----------------------------------------------------------------------------------------------------------------------------------
 * 相机管理类
 *
 * @author rqmei on 2018/4/4
 */
public final class CameraManager {
    private final CameraConfiguration mConfiguration;
    private Camera mCamera;
    int mCameraId = 0;
    //是否是前置摄像头
    private boolean isFrontCamera = false;
    //是否拍照后要保存照片
    private boolean isSaveBtimap = false;
    Context context;

    public CameraManager(Context context) {
        this.mConfiguration = new CameraConfiguration (context);
        this.context = context;
    }

    /**
     * 打开相机，获取camera（扫描二维码）
     **/
    public synchronized void openDriver(int cameraId) throws Exception {
        Log.i ("CameraManager", "CameraManager 。。。" + mCamera);
        if (mCamera != null)
            return;
        this.mCameraId = cameraId;
        mCamera = Camera.open (cameraId);
        if (cameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            isFrontCamera = true;
        } else {
            isFrontCamera = false;
        }
        if (mCamera == null) {
            Log.i ("CameraManager", "CameraManager 相机对象为空。。。");
            throw new IOException ("The camera is occupied.");
        }
        mConfiguration.initFromCameraParameters (mCamera);
        Camera.Parameters parameters = mCamera.getParameters ();
        String parametersFlattened = parameters == null ? null : parameters.flatten ();
        try {
            mConfiguration.setDesiredCameraParameters (mCamera);
        } catch (RuntimeException re) {
            Log.i ("CameraManager", "CameraManager RuntimeException11。。。");
            if (parametersFlattened != null) {
                parameters = mCamera.getParameters ();
                parameters.unflatten (parametersFlattened);
                try {
                    mCamera.setParameters (parameters);
                    mConfiguration.setDesiredCameraParameters (mCamera);
                } catch (RuntimeException e) {
                    Log.i ("CameraManager", "CameraManager RuntimeException。。。");
                    e.printStackTrace ();
                }
            }
        }
    }

    /**
     * 关闭相机，释放相关资源
     */
    public synchronized void closeDriver() {
        if (mCamera != null) {
            mCamera.setPreviewCallback (null);
            mCamera.release ();
            mCamera = null;
        }
    }

    /**
     * Camera is opened.
     *
     * @return true, other wise false.
     */
    public boolean isOpen() {
        return mCamera != null;
    }


    public boolean isFrontCamera() {
        return isFrontCamera;
    }

    public boolean isSaveBtimap() {
        return isSaveBtimap;
    }

    public void setSaveBtimap(boolean saveBtimap) {
        isSaveBtimap = saveBtimap;
    }

    /**
     * Get camera configuration.
     *
     * @return {@link CameraConfiguration}.
     */
    public CameraConfiguration getConfiguration() {
        return mConfiguration;
    }

    public int getStatusBarHeight(Context context) {
        try {
            Class<?> c = Class.forName ("com.android.internal.R$dimen");
            Object obj = c.newInstance ();
            Field field = c.getField ("status_bar_height");
            int x = Integer.parseInt (field.get (obj).toString ());
            return context.getResources ().getDimensionPixelSize (x);
        } catch (Exception e) {
            e.printStackTrace ();
        }
        return 0;
    }

    /**
     * Camera resolution.
     *
     * @return {@link Point}.
     */
    public Point getCameraResolution() {
        return mConfiguration.getCameraResolution ();
    }

    public Point getScreenResolution() {
        return mConfiguration.getScreenResolution ();
    }

    /**
     * Camera start preview.
     *
     * @param holder          {@link SurfaceHolder}.
     * @param previewCallback {@link Camera.PreviewCallback}.
     * @throws IOException if the method fails (for example, if the surface is unavailable or unsuitable).
     */
    public void startPreview(SurfaceHolder holder, Camera.PreviewCallback previewCallback) throws IOException {
        if (mCamera != null) {
            mConfiguration.setDesiredCameraParameters (mCamera);
            mCamera.setPreviewDisplay (holder);
            mCamera.setPreviewCallback (previewCallback);
            mCamera.startPreview ();
            isPreviewing = true;
        }
    }

    /**
     * Camera stop preview.
     */
    public void stopPreview() {
        if (mCamera != null) {
            try {
                mCamera.stopPreview ();
                isPreviewing = false;
            } catch (Exception ignored) {
                // nothing.
            }
            try {
                mCamera.setPreviewDisplay (null);
            } catch (IOException ignored) {
                // nothing.
            }
        }
    }

    /**
     * Focus on, make a scan action.
     *
     * @param callback {@link Camera.AutoFocusCallback}.
     */
    public void autoFocus(Camera.AutoFocusCallback callback) {
        if (mCamera != null)
            try {
                mCamera.autoFocus (callback);
            } catch (Exception e) {
                e.printStackTrace ();
            }
    }

    //----------------------------闪关灯控制----------------------------------------------------

    /**
     * 闪光灯类型枚举 默认为关闭
     */
    public enum FlashMode {
        /**
         * ON:拍照时打开闪光灯
         */
        ON,
        /**
         * OFF：不打开闪光灯
         */
        OFF,
        /**
         * AUTO：系统决定是否打开闪光灯
         */
        AUTO,
        /**
         * TORCH：一直打开闪光灯
         */
        TORCH
    }

    /**
     * 当前闪光灯类型，默认为关闭
     */
    private FlashMode mFlashMode = FlashMode.ON;

    /**
     * 设置闪光灯
     *
     * @param flashMode
     */
    public void setFlashMode(FlashMode flashMode) {
        if (mCamera == null)
            return;
        mFlashMode = flashMode;
        Camera.Parameters parameters = mCamera.getParameters ();
        List<String> FlashModes = parameters.getSupportedFlashModes ();
        if (FlashModes != null) {
            Log.i ("CameraManager", "setFlashMode================================FlashModes==" + FlashModes);
            switch (flashMode) {
                case ON://拍照时闪光灯；
                    if (FlashModes.contains (Camera.Parameters.FLASH_MODE_ON)) {
                        parameters.setFlashMode (Camera.Parameters.FLASH_MODE_ON);
                    }
                    break;
                case AUTO:// 自动模式，当光线较暗时自动打开闪光灯
                    if (FlashModes.contains (Camera.Parameters.FLASH_MODE_AUTO)) {
                        parameters.setFlashMode (Camera.Parameters.FLASH_MODE_AUTO);
                    }
                    break;
                case TORCH://打开
                    if (FlashModes.contains (Camera.Parameters.FLASH_MODE_TORCH)) {
                        parameters.setFlashMode (Camera.Parameters.FLASH_MODE_TORCH);
                    }
                    break;
                default://关闭闪光灯
                    if (FlashModes.contains (Camera.Parameters.FLASH_MODE_OFF)) {
                        parameters.setFlashMode (Camera.Parameters.FLASH_MODE_OFF);
                    }
                    break;
            }
            mCamera.setParameters (parameters);
        } else {
            // Toasts.show("无闪关灯！");
            Log.i ("CameraManager", "无闪关灯=========================");
        }
    }

    /**
     * 获取当前闪光灯类型
     *
     * @return
     */
    public FlashMode getFlashMode() {
        return mFlashMode;
    }

    //---------------------------------------拍照---------------------------------------------------------
    boolean isPreviewing = false;

    /**
     * 拍照
     */
    public void doTakePicture(CameraPictureAnalysis cameraPictureAnalysis) {
        if (isPreviewing && (mCamera != null)) {
            mCamera.takePicture (null, null, cameraPictureAnalysis);
            isPreviewing = false;
        }
    }

    /**
     * 当前屏幕旋转角度
     */
    private int rotation = 0;
    OrientationEventListener mOrEventListener;

    /**
     * 屏幕方向监听，防止照片颠倒
     *
     * @param context
     */
    public void startOrientationChangeListener(Context context) {
        mOrEventListener = new OrientationEventListener (context) {
            @Override
            public void onOrientationChanged(int orientation) {
                Camera.CameraInfo info = new Camera.CameraInfo ();
                Camera.getCameraInfo (mCameraId, info);
                int degrees = 0;
                switch (rotation) {
                    case Surface.ROTATION_0:
                        degrees = 0;
                        break;
                    case Surface.ROTATION_90:
                        degrees = 90;
                        break;
                    case Surface.ROTATION_180:
                        degrees = 180;
                        break;
                    case Surface.ROTATION_270:
                        degrees = 270;
                        break;
                }

                int result;
                if (isFrontCamera) {
                    result = (info.orientation + degrees) % 360;
                    result = (360 - result) % 360;  // compensate the mirror
                } else {  // back-facing
                    result = (info.orientation - degrees + 360) % 360;
                }
                if (mCamera != null) {
                    mCamera.setDisplayOrientation (result);
                }
            }
        };
    }

    public OrientationEventListener getmOrEventListener() {
        return mOrEventListener;
    }
}
