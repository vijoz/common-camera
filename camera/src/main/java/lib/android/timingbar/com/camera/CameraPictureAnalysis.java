package lib.android.timingbar.com.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * CameraPictureAnalysis
 * -----------------------------------------------------------------------------------------------------------------------------------
 * 拍照结果回调处理
 *
 * @author rqmei on 2018/4/26
 */

public class CameraPictureAnalysis implements Camera.PictureCallback {
    Context context;
    PictureCallback pictureCallback;
    CameraManager cameraManager;
    /**
     * 保存图片的手机路径
     */
    static String bitmapPath = "";

    public CameraPictureAnalysis(Context context, CameraManager cameraManager) {
        this.context = context;
        this.cameraManager = cameraManager;
    }

    private int _displaypixels = 720 * 1280;

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Log.i ("CameraPictureAnalysis", "CameraPictureAnalysis onPictureTaken拍照结果回调");
        _displaypixels = CameraConfiguration.getScreenWidthPixels (context) * CameraConfiguration.getScreenHeightPixels (context);
        if (null != data) {
            // 解析生成相机返回的图片
            // begin处理图片内存溢出
            BitmapFactory.Options opts = new BitmapFactory.Options ();
            BitmapFactory.decodeByteArray (data, 0, data.length, opts);
            opts.inJustDecodeBounds = true;
            opts.inSampleSize = computeSampleSize (opts, -1, _displaypixels);
            opts.inJustDecodeBounds = false;
            // end
            Bitmap b = BitmapFactory.decodeByteArray (data, 0, data.length, opts);// data是字节数据，将其解析成位 ?
            // 保存图片到sdcard
            if (null != b) {
                Camera.CameraInfo info = new Camera.CameraInfo ();
                Camera.getCameraInfo (cameraManager.mCameraId, info);
                Log.i ("CameraPictureAnalysis", "CameraPictureAnalysis orientation=" + info.orientation);
                // 设置FOCUS_MODE_CONTINUOUS_VIDEO)之后，myParam.set("rotation", 90)失效。
                // 图片竟然不能旋转了，故这里要旋转下
                Bitmap rotaBitmap = getRotateBitmap (b, info.orientation);
                Bitmap bitmap = null;
                if (cameraManager.isSaveBtimap ()) {
                    saveBitmap (rotaBitmap);
                } else {
                    bitmap = ThumbnailUtils.extractThumbnail (rotaBitmap, 213, 213);
                }
                if (pictureCallback != null) {
                    pictureCallback.onPictureTakenResult (bitmap);
                }
            }
        }
    }

    //
    //图片内存溢出处理
    //-----------------------------------

    /****
     * 处理图片bitmap size exceeds VM budget （Out Of Memory 内存溢出）
     */
    private int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize (options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    /**
     * 图片内存溢出处理
     *
     * @param options
     * @param minSideLength
     * @param maxNumOfPixels
     * @return
     */
    private int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil (Math.sqrt (w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min (Math.floor (w / minSideLength), Math.floor (h / minSideLength));

        if (upperBound < lowerBound) {
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * 旋转Bitmap
     *
     * @param b
     * @param rotateDegree
     * @return
     */
    public Bitmap getRotateBitmap(Bitmap b, float rotateDegree) {
        Matrix matrix = new Matrix ();
        matrix.postRotate ((float) rotateDegree);
        Bitmap rotaBitmap = Bitmap.createBitmap (b, 0, 0, b.getWidth (), b.getHeight (), matrix, false);
        return rotaBitmap;
    }

    /**
     * 保存图片到手机的缓存文件夹中
     *
     * @param b
     */
    private void saveBitmap(Bitmap b) {
        bitmapPath = getCacheFile (context) + "/face1.jpg";
        try {
            FileOutputStream fout = new FileOutputStream (bitmapPath);
            BufferedOutputStream bos = new BufferedOutputStream (fout);
            // 生成缩略图
            Bitmap bitmap = ThumbnailUtils.extractThumbnail (b, 213, 213);
            bitmap.compress (Bitmap.CompressFormat.JPEG, 50, bos);
            bos.flush ();
            bos.close ();
            Log.i ("CameraPictureAnalysis", "saveBitmap成功，图片路径=============" + bitmapPath);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.i ("CameraPictureAnalysis", "saveBitmap:失败＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
            e.printStackTrace ();
        }

    }

    public void setPictureCallback(PictureCallback pictureCallback) {
        this.pictureCallback = pictureCallback;
    }

    /**
     * 返回缓存文件夹
     */
    public static File getCacheFile(Context context) {
        if (Environment.getExternalStorageState ().equals (Environment.MEDIA_MOUNTED)) {
            File file = null;
            file = context.getExternalCacheDir ();//获取系统管理的sd卡缓存文件
            if (file == null) {//如果获取的为空,就是用自己定义的缓存文件夹做缓存路径
                file = new File ("/mnt/sdcard/" + context.getPackageName ());
                if (!file.exists ()) {
                    file.mkdirs ();
                }
            }
            return file;
        } else {
            return context.getCacheDir ();
        }
    }

    /**
     * @return 保存图片的路径
     */
    public static String getBitmapPath() {
        return bitmapPath;
    }
}
