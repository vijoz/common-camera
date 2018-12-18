package lib.android.timingbar.com.camera;

import android.content.Context;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.yanzhenjie.zbar.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * CameraScanAnalysis
 * -----------------------------------------------------------------------------------------------------------------------------------
 * 二维码扫描 预览回调处理
 *
 * @author rqmei on 2018/4/4
 */
class CameraScanAnalysis implements Camera.PreviewCallback {
    /**
     * 线程池接口
     */
    private ExecutorService executorService = Executors.newSingleThreadExecutor ();
    /**
     * 图像扫描仪
     */
    private ImageScanner mImageScanner;
    /**
     * 自定义扫描结果回调
     */
    public ScanCallback mCallback;
    /**
     * 存储二维码图片的Image
     */
    private Image barcode;
    /**
     * 是否开始扫描
     */
    private boolean allowAnalysis = true;
    private Handler mHandler;
    private CameraManager mCameraManager;
    Context context;

    public interface PreviewInterface {
        void onPreviewFrame(Image barcode);
    }

    PreviewInterface event;

    CameraScanAnalysis(Context context, PreviewInterface event) {
        this.context = context;
        this.event = event;
        mImageScanner = new ImageScanner ();
        mCameraManager = new CameraManager (context);
        mImageScanner.setConfig (0, Config.X_DENSITY, 3);
        mImageScanner.setConfig (0, Config.Y_DENSITY, 3);
        mImageScanner.setConfig (0, Config.ENABLE, 0); //Disable all the Symbols
        //只开启QRCODE二维码扫描
        mImageScanner.setConfig (Symbol.QRCODE, Config.ENABLE, 1);

        mHandler = new Handler (Looper.getMainLooper ()) {
            @Override
            public void handleMessage(Message msg) {
                if (mCallback != null)
                    mCallback.onScanResult ((String) msg.obj);
            }
        };
    }

    void setScanCallback(ScanCallback callback) {
        this.mCallback = callback;
    }

    void onStop() {
        this.allowAnalysis = false;
    }

    void onStart() {
        this.allowAnalysis = true;
    }

    public CameraManager getCameraManager() {
        return this.mCameraManager;
    }

    /**
     * 接收每一帧的预览数据
     *
     * @param data
     * @param camera
     */
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (allowAnalysis) {
            allowAnalysis = false;
            Camera.Size size = camera.getParameters ().getPreviewSize ();
            // 这里需要将获取的data翻转一下，因为相机默认拿的的横屏的数据
            byte[] rotatedData = new byte[data.length];
            for (int y = 0; y < size.height; y++) {
                for (int x = 0; x < size.width; x++)
                    rotatedData[x * size.height + size.height - y - 1] = data[x + y * size.width];
            }
            // 宽高也要调整
            int tmp = size.width;
            size.width = size.height;
            size.height = tmp;
            barcode = new Image (size.width, size.height, "Y800");
            barcode.setData (rotatedData);
            if (event != null) {
                event.onPreviewFrame (barcode);
            }
          /*  //获取扫描图片的大小
            Camera.Size size = camera.getParameters ().getPreviewSize ();
            //构造存储图片的Image  
            barcode = new Image (size.width, size.height, "Y800");
            //设置Image的数据资源
            barcode.setData (data);
            // barcode.setCrop(startX, startY, width, height);*/
            executorService.execute (mAnalysisTask);
        }
    }


    private Runnable mAnalysisTask = new Runnable () {
        @Override
        public void run() {
            //获取扫描结果的代码  
            int result = mImageScanner.scanImage (barcode);
            Log.i ("CameraScanAnalysis", "mAnalysisTask 扫描基恩=" + result);
            String resultStr = null;
            if (result != 0) {
                //开始解析扫描图片  
                SymbolSet symSet = mImageScanner.getResults ();
                for (Symbol sym : symSet)
                    resultStr = sym.getData ();
            }

            if (!TextUtils.isEmpty (resultStr)) {
                Message message = mHandler.obtainMessage ();
                message.obj = resultStr;
                message.sendToTarget ();
            } else
                allowAnalysis = true;
        }
    };
}