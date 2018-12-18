package lib.android.timingbar.com.camera;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.util.*;


/**
 * CameraConfiguration
 * -----------------------------------------------------------------------------------------------------------------------------------
 * camera配置信息设置
 *
 * @author rqmei on 2018/4/4
 */
public final class CameraConfiguration {
    private static final String TAG = "CameraConfiguration";
    private static final int MIN_PREVIEW_PIXELS = 480 * 320;
    private static final double MAX_ASPECT_DISTORTION = 0.15;
    private final Context context;
    //手机屏幕尺寸
    private Point screenResolution;
    //相机预览最佳尺寸
    private Point cameraResolution;

    CameraConfiguration(Context context) {
        this.context = context;
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public void initFromCameraParameters(Camera camera) {
        Camera.Parameters parameters = camera.getParameters ();
        WindowManager manager = (WindowManager) context.getSystemService (Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay ();

        screenResolution = getDisplaySize (display);

        Point screenResolutionForCamera = new Point ();
        screenResolutionForCamera.x = screenResolution.x;
        screenResolutionForCamera.y = screenResolution.y;

        // Convert to vertical screen.
        if (screenResolution.x < screenResolution.y) {
            screenResolutionForCamera.x = screenResolution.y;
            screenResolutionForCamera.y = screenResolution.x;
        }
        Log.w (TAG, "屏幕尺寸 screenResolution=" + screenResolution.x + "," + screenResolution.y);
        cameraResolution = findBestPreviewSizeValue (parameters, screenResolutionForCamera);
    }

    /**
     * 获取手机屏幕的宽高
     *
     * @param display
     * @return
     */
    private Point getDisplaySize(final Display display) {
        final Point point = new Point ();
        if (Build.VERSION.SDK_INT >= 15)
            display.getSize (point);
        else {
            point.set (display.getWidth (), display.getHeight ());
        }
        return point;
    }

    /**
     * 设置相机预览的宽高
     *
     * @param camera
     */
    public void setDesiredCameraParameters(Camera camera) {
        Camera.Parameters parameters = camera.getParameters ();
        if (parameters == null) {
            Log.w (TAG, "Device error: no camera parameters are available. Proceeding without configuration.");
            return;
        }
        parameters.setPreviewSize (cameraResolution.x, cameraResolution.y);
        Camera.Size pictureSize = getBestPictureSize (parameters.getSupportedPictureSizes (), cameraResolution.x, cameraResolution.y);
        if (pictureSize != null) {
            parameters.setPictureSize (pictureSize.width, pictureSize.height);
        }
        parameters.setPictureFormat (PixelFormat.JPEG);// 设置拍照后存储的图片格式
        camera.setParameters (parameters);
        Camera.Parameters afterParameters = camera.getParameters ();
        Camera.Size afterSize = afterParameters.getPreviewSize ();
        if (afterSize != null && (cameraResolution.x != afterSize.width || cameraResolution.y != afterSize.height)) {
            cameraResolution.x = afterSize.width;
            cameraResolution.y = afterSize.height;
        }
        camera.setDisplayOrientation (90);
    }
    /**
     * 获取最佳相机照片Size参数
     *
     * @return
     */
    public Camera.Size getBestPictureSize(List<Camera.Size> sizes, int w, int h) {
        Camera.Size optimalSize = null;
        float targetRadio = h / (float) w;
        float optimalDif = Float.MAX_VALUE; //最匹配的比例
        int optimalMaxDif = Integer.MAX_VALUE;//最优的最大值差距
        for (Camera.Size size : sizes) {
            float newOptimal = size.width / (float) size.height;
            float newDiff = Math.abs (newOptimal - targetRadio);
            if (newDiff < optimalDif) { //更好的尺寸
                optimalDif = newDiff;
                optimalSize = size;
                optimalMaxDif = Math.abs (h - size.width);
            } else if (newDiff == optimalDif) {//更好的尺寸
                int newOptimalMaxDif = Math.abs (h - size.width);
                if (newOptimalMaxDif < optimalMaxDif) {
                    optimalDif = newDiff;
                    optimalSize = size;
                    optimalMaxDif = newOptimalMaxDif;
                }
            }
        }
        return optimalSize;
    }

    /**
     * Camera resolution.
     *
     * @return {@link Point}.
     */
    public Point getCameraResolution() {
        return cameraResolution;
    }

    /**
     * Screen resolution.
     *
     * @return {@link Point}.
     */
    public Point getScreenResolution() {
        return screenResolution;
    }

    /**
     * Calculate the preview interface size.
     *
     * @param parameters       camera params.
     * @param screenResolution screen resolution.
     * @return {@link Point}.
     */
    private Point findBestPreviewSizeValue(Camera.Parameters parameters, Point screenResolution) {
        List<Camera.Size> rawSupportedSizes = parameters.getSupportedPreviewSizes ();
        if (rawSupportedSizes == null) {
            Log.w (TAG, "Device returned no supported preview sizes; using default");
            Camera.Size defaultSize = parameters.getPreviewSize ();
            return new Point (defaultSize.width, defaultSize.height);
        }

        // Sort by size, descending
        List<Camera.Size> supportedPreviewSizes = new ArrayList<> (rawSupportedSizes);
        Collections.sort (supportedPreviewSizes, new Comparator<Camera.Size> () {
            @Override
            public int compare(Camera.Size a, Camera.Size b) {
                int aPixels = a.height * a.width;
                int bPixels = b.height * b.width;
                if (bPixels < aPixels) {
                    return -1;
                }
                if (bPixels > aPixels) {
                    return 1;
                }
                return 0;
            }
        });

        if (Log.isLoggable (TAG, Log.INFO)) {
            StringBuilder previewSizesString = new StringBuilder ();
            for (Camera.Size supportedPreviewSize : supportedPreviewSizes) {
                previewSizesString.append (supportedPreviewSize.width)
                        .append ('x')
                        .append (supportedPreviewSize.height)
                        .append (' ');
            }
            Log.i (TAG, "Supported preview sizes: " + previewSizesString);
        }

        double screenAspectRatio = (double) screenResolution.x / (double) screenResolution.y;

        // Remove sizes that are unsuitable
        Iterator<Camera.Size> it = supportedPreviewSizes.iterator ();
        while (it.hasNext ()) {
            Camera.Size supportedPreviewSize = it.next ();
            int realWidth = supportedPreviewSize.width;
            int realHeight = supportedPreviewSize.height;
            if (realWidth * realHeight < MIN_PREVIEW_PIXELS) {
                it.remove ();
                continue;
            }

            boolean isCandidatePortrait = realWidth < realHeight;
            int maybeFlippedWidth = isCandidatePortrait ? realHeight : realWidth;
            int maybeFlippedHeight = isCandidatePortrait ? realWidth : realHeight;

            double aspectRatio = (double) maybeFlippedWidth / (double) maybeFlippedHeight;
            double distortion = Math.abs (aspectRatio - screenAspectRatio);
            if (distortion > MAX_ASPECT_DISTORTION) {
                it.remove ();
                continue;
            }

            if (maybeFlippedWidth == screenResolution.x && maybeFlippedHeight == screenResolution.y) {
                Point exactPoint = new Point (realWidth, realHeight);
                Log.i (TAG, "Found preview size exactly matching screen size: " + exactPoint);
                return exactPoint;
            }
        }

        // If no exact match, use largest preview size. This was not a great
        // idea on older devices because
        // of the additional computation needed. We're likely to get here on
        // newer Android 4+ devices, where
        // the CPU is much more powerful.
        if (!supportedPreviewSizes.isEmpty ()) {
            Camera.Size largestPreview = supportedPreviewSizes.get (0);
            Point largestSize = new Point (largestPreview.width, largestPreview.height);
            Log.i (TAG, "Using largest suitable preview size: " + largestSize);
            return largestSize;
        }

        // If there is nothing at all suitable, return current preview size
        Camera.Size defaultPreview = parameters.getPreviewSize ();
        Point defaultSize = new Point (defaultPreview.width, defaultPreview.height);
        Log.i (TAG, "No suitable preview sizes, using default: " + defaultSize);

        return defaultSize;
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidthPixels(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService (Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay ().getWidth ();
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeightPixels(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService (Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay ().getHeight ();
    }
}
