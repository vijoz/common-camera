package lib.android.timingbar.com.view.player.util;

/**
 * PlayerCallback
 * -----------------------------------------------------------------------------------------------------------------------------------
 * 播放器各种状态的回调
 *
 * @author rqmei on 2018/5/2
 */

public interface PlayerCallback {
    /**
     * 上一个视频
     */
    void setUp();

    /**
     * 下一个视频
     */
    void setNext();

    /**
     * 视频播放暂停
     */
    void onPlayPause();

    /**
     * 视频播放开始
     */
    void onPlayStart();

    /**
     * 视频播放出错
     */
    void onPlayError();

    /**
     * 视频播放完成
     */
    void onPlayComplet();
}
