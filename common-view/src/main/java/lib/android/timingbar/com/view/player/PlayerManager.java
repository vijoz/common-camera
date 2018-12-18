package lib.android.timingbar.com.view.player;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import lib.android.timingbar.com.view.player.util.ConstUtils;
import lib.android.timingbar.com.view.player.util.PlayerCallback;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;

/**
 * PlayerManager
 * -----------------------------------------------------------------------------------------------------------------------------------
 * MediaPlayer管理类
 *
 * @author rqmei on 2018/5/2
 */

public class PlayerManager {
    Context context;
    MediaPlayer player;
    //播放器当前处于的状态
    public int currentState = ConstUtils.STATE_IDLE;
    //当前所处的模式
    public int currentMode = ConstUtils.MODE_NORMAL;
    public PlayerCallback playerCallback;
    /**
     * 是否完成视频跳转
     */
    private boolean isSeekComplet = true;
    //开始视频播放的进度
    private int startPostion = 0;

    /**
     * 视频时候可以拖动播放
     * 1：是；0：否
     */
    private int canDrag = 1;

    public PlayerManager(Context context) {
        this.context = context;
    }

    /**
     * 打开多媒体MediaPlayer
     */
    @SuppressLint("PrivateApi")
    private synchronized void openMediaPlayer() {
        player = new MediaPlayer ();
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {
            return;
        }
        try {
            Class<?> cMediaTimeProvider = Class.forName ("android.media.MediaTimeProvider");
            Class<?> cSubtitleController = Class.forName ("android.media.SubtitleController");
            AtomicReference<? extends Class<?>> iSubtitleControllerAnchor = new AtomicReference<Class<?>> (Class.forName ("android.media.SubtitleController$Anchor"));
            Class<?> iSubtitleControllerListener = Class.forName ("android.media.SubtitleController$Listener");
            Constructor constructor = cSubtitleController.getConstructor (Context.class, cMediaTimeProvider, iSubtitleControllerListener);
            Object subtitleInstance = constructor.newInstance (context, null, null);
            Field f = cSubtitleController.getDeclaredField ("mHandler");
            f.setAccessible (true);
            try {
                f.set (subtitleInstance, new Handler ());
            } catch (IllegalAccessException e) {
                Log.e ("PlayerManager", "openMediaPlayer IllegalAccessException" + e.getMessage ());
            } finally {
                f.setAccessible (false);
            }
            Method setsubtitleanchor = player.getClass ().getMethod ("setSubtitleAnchor", cSubtitleController, iSubtitleControllerAnchor.get ());
            setsubtitleanchor.invoke (player, subtitleInstance, null);
        } catch (Exception e) {
            Log.e ("PlayerManager", "openMediaPlayer Exception" + e);
        }
    }

    /**
     * 重置播放器
     */
    public void onReset() {
        if (player == null) {
            openMediaPlayer ();
            player.reset ();
            currentState = ConstUtils.STATE_IDLE;
        } else {
            if (isPlaying ()) {
                player.stop ();
                currentState = ConstUtils.STATE_PAUSED;
            }
            player.reset ();
            currentState = ConstUtils.STATE_IDLE;
        }
    }

    /**
     * 根据视频路径实现播放
     */
    public void onPlay(String videoUrl, int startPostion) {
        if (isIdle ()) {
            Log.i ("PlayerManager", "onPlay startPostion~~" + startPostion);
            this.startPostion = startPostion;
            try {
                player.setDataSource (videoUrl);
                player.prepareAsync ();
                currentState = ConstUtils.STATE_PREPARING;
            } catch (IOException e) {
                Log.e ("PlayerManager", "onPlay MediaPlayer设置视频源出错~" + e.getMessage ());
            }
        } else {
            Log.i ("PlayerManager", "onPlay MediaPlayer未处于闲置状态~~");
        }
    }

    /**
     * 视频加载完毕，可以准备播放了
     */
    public void onPrepared() {
        currentState = ConstUtils.STATE_PREPARED;
        if (this.startPostion < 1000) {
            onStart ();
        } else {
            onSeekTo (startPostion);
        }
    }

    /**
     * 跳转到指定位置继续播放
     */
    public void onSeekTo(int position) {
        if (player != null) {
            isSeekComplet = false;
            player.seekTo (position);
            Log.i ("PlayerManager", "onSeekTo  currentState=" + currentState);
        }
    }

    /**
     * 开始播放
     */
    private void onStart() {
        player.start ();
        if (playerCallback != null) {
            playerCallback.onPlayStart ();
        }
        currentState = ConstUtils.STATE_PLAYING;
    }

    /**
     * 正在缓冲(播放器正在播放时，缓冲区数据不足，进行缓冲，此时暂停播放器，继续缓冲，缓冲区数据足够后恢复播放
     */

    public void onBufferingPlaying() {
        if ((isPaused () && isSeekComplet ()) || isBufferingPaused ()) {
            currentState = ConstUtils.STATE_BUFFERING_PAUSED;
        } else {
            currentState = ConstUtils.STATE_BUFFERING_PLAYING;
        }
    }


    /**
     * 暂停
     */
    public void onPause() {
        if (isPlaying ()) {
            player.pause ();
            if (playerCallback != null) {
                playerCallback.onPlayPause ();
            }
            currentState = ConstUtils.STATE_PAUSED;
        }
    }

    /**
     * 恢复播放
     */
    public void onResume() {
        if ((isPaused () || isPrepared () || isBufferingPlaying ()) && player != null) {
            onStart ();
        }
    }

    /**
     * 指定位置恢复播放
     */
    public void onResume(int position) {
        if ((isPaused () || isPrepared () || isBufferingPlaying ()) && player != null) {
            onSeekTo (position);
        }
    }

    /**
     * 播放出错
     */
    public void onError() {
        startPostion = player.getCurrentPosition ();
        if (!isPlaying ()) {
            onReleasePlayer ();
        }
        if (playerCallback != null) {
            playerCallback.onPlayError ();
        }
        currentState = ConstUtils.STATE_ERROR;
    }

    /**
     * 播放完成
     */
    public void onComplet() {
        if (playerCallback != null) {
            playerCallback.onPlayComplet ();
        }
        currentState = ConstUtils.STATE_COMPLETED;
    }

    public void onReleasePlayer() {
        if (player != null) {
            player.stop ();
            try {
                player.release ();
            } catch (Exception e) {
                // TODO: handle exception
                Log.e ("PlayerManager", "releasePlayer释放player异常==" + e.getMessage ());
            }
            player = null;
            currentState = ConstUtils.STATE_IDLE;
        }
    }

    public boolean isIdle() {
        return currentState == ConstUtils.STATE_IDLE;
    }

    public boolean isPreparing() {
        return currentState == ConstUtils.STATE_PREPARING;
    }

    public boolean isPrepared() {
        return currentState == ConstUtils.STATE_PREPARED;
    }

    public boolean isBufferingPlaying() {
        return currentState == ConstUtils.STATE_BUFFERING_PLAYING;
    }

    public boolean isBufferingPaused() {
        return currentState == ConstUtils.STATE_BUFFERING_PAUSED;
    }

    public boolean isPlaying() {
        return currentState == ConstUtils.STATE_PLAYING;
    }

    public boolean isPaused() {
        return currentState == ConstUtils.STATE_PAUSED;
    }

    public boolean isError() {
        return currentState == ConstUtils.STATE_ERROR;
    }

    public boolean isCompleted() {
        return currentState == ConstUtils.STATE_COMPLETED;
    }

    public boolean isFullScreen() {
        return currentMode == ConstUtils.MODE_FULL_SCREEN;
    }

    public boolean isSeekComplet() {
        return isSeekComplet;
    }

    public void setSeekComplet(boolean seekComplet) {
        isSeekComplet = seekComplet;
    }

    public void setStartPostion(int startPostion) {
        this.startPostion = startPostion;
    }

    public int getStartPostion() {
        return startPostion;
    }

    public int canDrag() {
        return canDrag;
    }

    public void setCanDrag(int canDrag) {
        this.canDrag = canDrag;
    }

    /**
     * @return 视频播放的进度
     */
    public int getProgress() {
        int position = getCurrentPosition ();
        int duration = player.getDuration ();
        return 100 * position / duration;
    }

    /**
     * @return 当前播放进度对应的时间
     */
    @SuppressLint("DefaultLocale")
    public String getCurrentTime() {
        long position = getCurrentPosition () / 1000;
        return String.format ("%02d:%02d", position / 60, position % 60);
    }

    /**
     * @return 视频总进度对应的时间
     */
    @SuppressLint("DefaultLocale")
    public String getTotalTime() {
        long duration = player.getDuration () / 1000;
        return String.format ("%02d:%02d", duration / 60, duration % 60);
    }

    public int getCurrentPosition() {
        int currentPotistion = player.getCurrentPosition ();
        if (!isSeekComplet ()) {
            Log.i ("PlayerManager", "getCurrentPosition  currentPotistion=" + currentPotistion);
            currentPotistion = currentPotistion < startPostion ? startPostion : currentPotistion;
        }
        return currentPotistion;
    }

    public int getDuration() {
        return player.getDuration ();
    }

    /**
     * 提供给外部监听视频播放状态的接口回调
     *
     * @param playerCallback
     */
    public void setPlayerCallback(PlayerCallback playerCallback) {
        this.playerCallback = playerCallback;
    }
}
