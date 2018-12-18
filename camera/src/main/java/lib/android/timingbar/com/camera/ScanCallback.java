package lib.android.timingbar.com.camera;


/**
 * ScanCallback
 * -----------------------------------------------------------------------------------------------------------------------------------
 * <p>
 * 二维码扫描结果
 *
 * @author rqmei on 2018/4/4
 */

public interface ScanCallback {
    /**
     * 扫描成功
     *
     * @param content 扫描结果f
     */
    void onScanResult(String content);

    /**
     * 打开相机失败
     */
    void openCameraFail();
    
}
