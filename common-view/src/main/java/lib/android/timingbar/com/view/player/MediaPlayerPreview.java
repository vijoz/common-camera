package lib.android.timingbar.com.view.player;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.widget.*;
import lib.android.timingbar.com.view.R;
import lib.android.timingbar.com.view.player.util.CommonUtil;
import lib.android.timingbar.com.view.player.util.ConstUtils;
import lib.android.timingbar.com.view.player.util.PlayerCallback;

import java.util.Timer;
import java.util.TimerTask;

/**
 * MediaPlayerPreview
 * -----------------------------------------------------------------------------------------------------------------------------------
 * 视频播放器View
 *
 * @author rqmei on 2018/5/2
 */

public class MediaPlayerPreview extends FrameLayout implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, MediaPlayer.OnVideoSizeChangedListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnInfoListener, MediaPlayer.OnErrorListener {
    private Context mContext;
    //mediaPlayer最外层的Layout
    private FrameLayout mContainer;
    //
    private PlayerSurfaceView surfaceView;
    //视频状态控制View
    private View playerStateView, playerMiddleControl;
    PlayerManager playerManager;
    //播放状态控制栏的view
    SeekBar skbPlayerProgress;
    TextView tvPlayerPosition, tvPlayerDuration;
    ImageView ivPlayerMiddleControl, ivPlayerFront, ivPlayerStateControl, ivPlayerNext, ivPlayerScreen;
    ProgressBar proLoading;

    //播放视频的路径
    private String videoUrl = "";

    private Timer mUpdateProgressTimer;
    private TimerTask mUpdateProgressTimerTask;


    public MediaPlayerPreview(Context context) {
        this (context, null);
    }

    public MediaPlayerPreview(Context context, AttributeSet attrs) {
        this (context, attrs, 0);
    }

    public MediaPlayerPreview(Context context, AttributeSet attrs, int defStyleAttr) {
        super (context, attrs, defStyleAttr);
        this.mContext = context;
        initContainer ();
        addSurfaceView ();
    }

    /**
     * 初始化最外层的Layout
     */
    private void initContainer() {
        mContainer = new FrameLayout (mContext);
        mContainer.setBackgroundColor (Color.BLACK);
        mContainer.setKeepScreenOn (true);
        LayoutParams params = new LayoutParams (
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        this.addView (mContainer, params);
        playerManager = new PlayerManager (mContext);
    }

    private void addSurfaceView() {
        surfaceView = new PlayerSurfaceView (mContext);
        mContainer.removeView (surfaceView);
        LayoutParams params = new LayoutParams (
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER);
        mContainer.addView (surfaceView, 0, params);
        surfaceView.getHolder ().addCallback (this);
        addPlayerMiddleControl ();
    }

    private void addPlayerStateControl() {
        if (playerStateView == null) {
            playerStateView = LayoutInflater.from (mContext).inflate (R.layout.player_state_control, mContainer, false);
            mContainer.addView (playerStateView);
            skbPlayerProgress = playerStateView.findViewById (R.id.skb_player_progress);
            tvPlayerPosition = playerStateView.findViewById (R.id.tv_player_position);
            tvPlayerDuration = playerStateView.findViewById (R.id.tv_player_duration);
            ivPlayerFront = playerStateView.findViewById (R.id.iv_player_front);
            ivPlayerStateControl = playerStateView.findViewById (R.id.iv_player_state_control);
            ivPlayerNext = playerStateView.findViewById (R.id.iv_player_next);
            ivPlayerScreen = playerStateView.findViewById (R.id.iv_player_screen);
            skbPlayerProgress.setOnSeekBarChangeListener (onSeekBarChangeListener);

            ivPlayerFront.setOnClickListener (onClickListener);
            ivPlayerStateControl.setOnClickListener (onClickListener);
            ivPlayerNext.setOnClickListener (onClickListener);
            ivPlayerScreen.setOnClickListener (onClickListener);
        }

    }

    private void addPlayerMiddleControl() {
        if (playerMiddleControl == null) {
            playerMiddleControl = LayoutInflater.from (mContext).inflate (R.layout.player_middle_control, mContainer, false);
            ivPlayerMiddleControl = playerMiddleControl.findViewById (R.id.iv_middle_state);
            ivPlayerMiddleControl.setOnClickListener (onClickListener);
            proLoading = playerMiddleControl.findViewById (R.id.pro_loading);
            mContainer.addView (playerMiddleControl);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i ("MediaPlayerPreview", "surfaceCreated~~~~" + playerManager.player);
        if (surfaceView == null) {
            addSurfaceView ();
        }
        if (playerManager.player != null) {
            playerManager.player.setDisplay (holder);
            playerManager.player.setAudioStreamType (AudioManager.STREAM_MUSIC);
            playerManager.player.setOnVideoSizeChangedListener (this);
            playerManager.player.setOnBufferingUpdateListener (this);
            playerManager.player.setOnPreparedListener (this);
            playerManager.player.setOnInfoListener (this);
            playerManager.player.setOnSeekCompleteListener (this);
            playerManager.player.setOnErrorListener (this);
            playerManager.player.setOnCompletionListener (this);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i ("MediaPlayerPreview", "surfaceChanged~~~~");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i ("MediaPlayerPreview", "surfaceDestroyed~~~~");
        if (playerManager.player != null) {
            playerManager.player.setDisplay (null);
        }
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        Log.i ("MediaPlayerPreview", "onVideoSizeChanged~~~~width=" + width + ",height=" + height);
        surfaceView.adaptVideoSize (width, height);
        addPlayerStateControl ();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Log.i ("MediaPlayerPreview", "onBufferingUpdate~~~~" + percent);
        if (skbPlayerProgress != null) {
            skbPlayerProgress.setSecondaryProgress (percent);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.i ("MediaPlayerPreview", "onPrepared~~~~");
        playerManager.onPrepared ();
        tvPlayerDuration.setText (playerManager.getTotalTime ());
        // skbPlayerProgress.setProgress (0);
        tvPlayerPosition.setText ("00:00");
        startUpdateProgressTimer ();
        onPlayerControlView ();
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        Log.i ("MediaPlayerPreview", "onInfo~~~~" + playerManager.currentState + ",what=" + what + ",extra=" + extra);
        switch (what) {
            case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                // 播放器开始渲染
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_START://开始缓冲
                Log.i ("MediaPlayerPreview", "onInfo buffering start~~~~");
                playerManager.onBufferingPlaying ();
                onBufferingControlView ();
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END://结束缓冲
                Log.i ("MediaPlayerPreview", "onInfo buffering end~~~~" + playerManager.currentState);
                if (playerManager.isBufferingPlaying () && playerManager.isSeekComplet ()) {
                    playerManager.onResume ();
                    onPlayerControlView ();
                } else if (playerManager.isBufferingPaused ()) {
                    playerManager.onPause ();
                    onPauseControlView (R.mipmap.player_middle_pause);
                }
                break;
            case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                //视频不能seekTo
                break;
        }
        return false;
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        Log.i ("MediaPlayerPreview", "onSeekComplete~~~~" + playerManager.currentState + ",isSeekComplet=" + playerManager.isSeekComplet ());
        if (!playerManager.isSeekComplet ()) {
            playerManager.onResume ();
            onPlayerControlView ();
            playerManager.setSeekComplet (true);
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.i ("MediaPlayerPreview", "onError~~~~" + playerManager.currentState + ",what=" + what + ",extra" + extra);
        // 直播流播放时去调用mediaPlayer.getDuration会导致-38和-2147483648错误，忽略该错误
        switch (extra) {
            case MediaPlayer.MEDIA_ERROR_IO:
                if (!playerManager.isError ()) {
                    playerManager.currentState = ConstUtils.STATE_ERROR;
                    int currentPosition = playerManager.getCurrentPosition ();
                    Log.i ("MediaPlayerPreview", " onError MEDIA_ERROR_IO~~~~" + playerManager.currentState + ",currentPosition=" + currentPosition + "," + playerManager.player.getCurrentPosition ());
                    onPlay (this.videoUrl, currentPosition);
                }
                break;
            case -2147483648:
            case -38:
                Log.i ("MediaPlayerPreview", " onError忽略该错误~~~~");
                break;
            default:
                playerManager.onError ();
                onPauseControlView (R.mipmap.player_error);
                break;
        }
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.i ("MediaPlayerPreview", " onCompletion~~~~" + playerManager.currentState);
        if (playerManager.isPlaying ()) {
            onPauseControlView (R.mipmap.player_middle_pause);
            playerManager.onComplet ();
        }
    }

    /**
     * 根据视频路径和位置播放视频
     */
    public void onPlay(String videoUrl, int postion) {
        Log.i ("MediaPlayerPreview", " onPlay~~~~" + postion);
        if (!CommonUtil.isEmpty (videoUrl)) {
            playerManager.onReset ();
            if (playerMiddleControl == null) {
                addPlayerMiddleControl ();
            }
            onBufferingControlView ();
            this.videoUrl = videoUrl;
            playerManager.onPlay (videoUrl, postion);
        } else {
            Log.i ("MediaPlayerPreview", "onPlay() 视频路径为空");
        }
    }


    OnClickListener onClickListener = new OnClickListener () {
        @Override
        public void onClick(View v) {
            Log.i ("MediaPlayerPreview", " onClickListener播放器当前状态~~~" + playerManager.currentState);
            int id = v.getId ();
            if (id == R.id.iv_player_front) {
                //上一个视频
                if (playerManager.playerCallback != null) {
                    playerManager.playerCallback.setUp ();
                }
            } else if (id == R.id.iv_player_state_control || id == R.id.iv_middle_state) {
                //播放暂停控制
                if (playerManager.isPaused () || playerManager.isBufferingPlaying ()) {
                    //恢复播放
                    onPlayerControlView ();
                    playerManager.onResume ();
                } else if (playerManager.isPlaying ()) {
                    //暂停
                    onPauseControlView (R.mipmap.player_middle_pause);
                    playerManager.onPause ();
                } else if (playerManager.isError ()) {
                    onPlay (videoUrl, playerManager.getStartPostion ());
                }
            } else if (id == R.id.iv_player_next) {
                //下一个视频
                if (playerManager.playerCallback != null) {
                    playerManager.playerCallback.setNext ();
                }
            } else if (id == R.id.iv_player_screen) {
                //全屏、半屏控制
                if (!playerManager.isFullScreen ()) {
                    ivPlayerScreen.setImageResource (R.mipmap.player_shrink);
                    enterFullScreen ();
                } else {
                    ivPlayerScreen.setImageResource (R.mipmap.player_enlarge);
                    exitFullScreen ();
                }
            }
        }
    };

    /**
     * 视频播放时播放器的控制器显示效果
     */
    private void onPlayerControlView() {
        if (playerMiddleControl != null && playerStateView != null) {
            playerStateView.setVisibility (VISIBLE);
            playerMiddleControl.setVisibility (GONE);
        }
    }

    /**
     * 视频暂停、出错等状态的播放器控的制器显示效果
     *
     * @param player_middle_pause 代表视频当前播放状态的图标
     */
    private void onPauseControlView(int player_middle_pause) {
        Log.i ("MediaPlayerPreview", "player onPauseControlView=" + playerMiddleControl);
        if (playerMiddleControl != null) {
            playerMiddleControl.setVisibility (VISIBLE);
            playerStateView.setVisibility (GONE);
            proLoading.setVisibility (GONE);
            ivPlayerMiddleControl.setImageResource (player_middle_pause);
            ivPlayerMiddleControl.setVisibility (VISIBLE);
        }
    }

    /**
     * 视频缓冲时播放器的控制器显示效果
     */
    private void onBufferingControlView() {
        Log.i ("MediaPlayerPreview", "player onBufferingControlView=" + playerMiddleControl);
        if (playerMiddleControl != null) {
            playerMiddleControl.setVisibility (VISIBLE);
            proLoading.setVisibility (VISIBLE);
            ivPlayerMiddleControl.setVisibility (GONE);
            if (playerManager.isBufferingPlaying ()) {
                playerStateView.setVisibility (GONE);
            }
        }
    }

    /**
     * 全屏，将mContainer(内部包含mTextureView和mController)从当前容器中移除，并添加到android.R.content中.
     * 切换横屏时需要在manifest的activity标签下添加android:configChanges="orientation|keyboardHidden|screenSize"配置，
     * 以避免Activity重新走生命周期
     */
    public void enterFullScreen() {
        if (playerManager.isFullScreen ())
            return;
        // 隐藏ActionBar、状态栏，并横屏
        CommonUtil.hideActionBar (mContext);
        CommonUtil.scanForActivity (mContext)
                .setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        ViewGroup contentView = (ViewGroup) CommonUtil.scanForActivity (mContext)
                .findViewById (android.R.id.content);
        this.removeView (mContainer);
        LayoutParams params = new LayoutParams (
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        contentView.addView (mContainer, params);
        playerManager.currentMode = ConstUtils.MODE_FULL_SCREEN;
        Log.d ("MediaPlayerPreview", "MODE_FULL_SCREEN");
    }

    /**
     * 退出全屏，移除mTextureView和mController，并添加到非全屏的容器中。
     * 切换竖屏时需要在manifest的activity标签下添加android:configChanges="orientation|keyboardHidden|screenSize"配置，
     * 以避免Activity重新走生命周期.
     *
     * @return true退出全屏.
     */
    public boolean exitFullScreen() {
        if (playerManager.isFullScreen ()) {
            CommonUtil.showActionBar (mContext);
            CommonUtil.scanForActivity (mContext)
                    .setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            ViewGroup contentView = (ViewGroup) CommonUtil.scanForActivity (mContext)
                    .findViewById (android.R.id.content);
            contentView.removeView (mContainer);
            LayoutParams params = new LayoutParams (
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);

            this.addView (mContainer, params);
            playerManager.currentMode = ConstUtils.MODE_NORMAL;
            Log.d ("MediaPlayerPreview", "MODE_NORMAL");
            return true;
        }
        return false;
    }

    /**
     * 开始更新进度条
     */
    protected void startUpdateProgressTimer() {
        cancelUpdateProgressTimer ();
        if (mUpdateProgressTimer == null) {
            mUpdateProgressTimer = new Timer ();
        }
        if (mUpdateProgressTimerTask == null) {
            mUpdateProgressTimerTask = new TimerTask () {
                @Override
                public void run() {
                    post (new Runnable () {
                        @Override
                        public void run() {
                            if (playerManager.isPlaying ()) {
                                skbPlayerProgress.setProgress (playerManager.getProgress ());
                                tvPlayerPosition.setText (playerManager.getCurrentTime ());
                            }
                        }
                    });
                }
            };
        }

        mUpdateProgressTimer.schedule (mUpdateProgressTimerTask, 0, 800);
    }

    /**
     * 取消更新进度的计时器。
     */
    protected void cancelUpdateProgressTimer() {
        Log.i ("MediaPlayerPreview", "cancelUpdateProgressTimer~~~~");
        if (mUpdateProgressTimer != null) {
            mUpdateProgressTimer.cancel ();
            mUpdateProgressTimer = null;
        }
        if (mUpdateProgressTimerTask != null) {
            mUpdateProgressTimerTask.cancel ();
            mUpdateProgressTimerTask = null;
        }
    }

    /**
     * 视频进度条拖动事件监听
     *
     * @param isPause
     */

    SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener () {
        int isDrag = 1;
        int currentPosition = 0, trackProgress = 0;


        /**
         * 通知进度已经被修改
         * @param seekBar 当前被修改进度的SeekBar
         * @param progress 当前的进度值。此值的取值范围为0到max之间。Max为用户通过setMax(int)设置的值，默认为100
         * @param fromUser 如果是用户触发的改变则返回True
         */
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            Log.i ("MediaPlayerPreview", "MediaPlayerPreview onProgressChanged~~~=" + playerManager.isPlaying ());
            if (isDrag == 1) {
                if (playerManager.isPlaying ()) {
                    int duration = playerManager.getDuration ();
                    currentPosition = progress * duration / seekBar.getMax ();
                    Log.i ("MediaPlayerPreview", " onProgressChanged ~~~currentPosition11=" + currentPosition);
                    trackProgress = progress;
                } else if (!playerManager.isIdle ()) {
                    currentPosition = playerManager.getCurrentPosition ();
                    Log.i ("MediaPlayerPreview", " onProgressChanged~~~currentPosition22=" + currentPosition);
                    trackProgress = skbPlayerProgress.getProgress ();
                }
            }
        }

        /**
         * 通知用户已经开始一个触摸拖动手势。客户端可能需要使用这个来禁用seekbar的滑动功能。
         * @param seekBar
         */
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            isDrag = playerManager == null ? 1 : playerManager.canDrag ();
            Log.i ("MediaPlayerPreview", " onStartTrackingTouch~~~=" + isDrag);
            if (isDrag == 1) {//可以拖动进度条
                seekBar.setEnabled (true);
            } else {//不可以多动进度条
                seekBar.setEnabled (false);
            }
        }

        /**
         * 通知用户触摸手势已经结束。户端可能需要使用这个来启用seekbar的滑动功能。
         * @param seekBar
         */
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (!playerManager.isIdle () && isDrag == 1) {
                if (CommonUtil.isConnected (mContext) || trackProgress <= skbPlayerProgress.getSecondaryProgress ()) {
                    Log.i ("MediaPlayerPreview", " onStopTrackingTouch ~~~~currentPosition=" + currentPosition);
                    playerManager.onSeekTo (currentPosition);
                } else if (!CommonUtil.isConnected (mContext)) {
                    // TODO: 2018/5/4  没有网络
                    Log.i ("MediaPlayerPreview", " onStopTrackingTouch 没有网络~~~=");
                    playerManager.onError ();
                    onPauseControlView (R.mipmap.player_error);
                }
            }
        }
    };

    /**
     * 提供给外部监听视频播放状态的接口回调
     *
     * @param playerCallback
     */
    public void setPlayerCallback(PlayerCallback playerCallback) {
        playerManager.setPlayerCallback (playerCallback);
    }
    //****************************************************************提供给外部的播放器状态信息************************************************************************************************

    /**
     * 暂停播放
     */
    public void onPause() {
        if (playerManager != null) {
            playerManager.onPause ();
        }
    }

    /**
     * 恢复播放
     */
    public void onResume() {
        if (playerManager != null) {
            playerManager.onResume ();
        }
    }

    public boolean isPaused() {
        boolean isPaused = false;
        if (playerManager != null) {
            isPaused = playerManager.isPaused ();
        }
        return isPaused;
    }

    public void onRelease() {
        mContainer.setKeepScreenOn (false);
        cancelUpdateProgressTimer ();
        playerManager.onReleasePlayer ();
    }

    /**
     * @return 播放器当前的播放状态
     */
    public int getPlayerCurrentState() {
        return playerManager == null ? ConstUtils.STATE_IDLE : playerManager.currentState;
    }

    /**
     * @return 播放器当前的播放模式（全屏、半屏）
     */
    public int getPlayerCurrentMode() {
        return playerManager == null ? ConstUtils.STATE_IDLE : playerManager.currentMode;
    }

    /**
     * @return 视频当前播放进度
     */
    public int getCurrentPosition() {
        return playerManager == null ? -1 : playerManager.getCurrentPosition ();
    }

    /**
     * @return 视频总进度
     */
    public int getDuration() {
        return playerManager == null ? 0 : playerManager.getDuration ();
    }

    /**
     * 设置视频是否可以拖动观看
     *
     * @param canDrag 1：是；0否
     */
    public void setCanDrag(int canDrag) {
        playerManager.setCanDrag (canDrag);
    }
}
