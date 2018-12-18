package lib.android.timingbar.com.base.app;

/**
 * IAppDelegate
 * -----------------------------------------------------------------------------------------------------------------------------------
 *
 * @author rqmei on 2018/9/12
 */

public interface IAppDelegate {
    /**
     * 创建
     *
     */
    void onCreate();

    /**
     * 内存泄漏处理(比如清空数据)
     *
     */
    void onTerminate();
}
