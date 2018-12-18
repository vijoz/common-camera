package com.timingbar.android.library.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.timingbar.android.library.R;
import lib.android.timingbar.com.base.activity.BaseActivity;
import lib.android.timingbar.com.view.player.MediaPlayerPreview;
import lib.android.timingbar.com.view.player.util.ConstUtils;
import lib.android.timingbar.com.view.player.util.PlayerCallback;

/**
 * PlayerActivity
 * -----------------------------------------------------------------------------------------------------------------------------------
 * 播放多媒体视频
 *
 * @author rqmei on 2018/5/2
 */

public class PlayerActivity extends BaseActivity {
    @BindView(R.id.media_player)
    MediaPlayerPreview mediaPlayer;


    @Override
    public int getLayoutResId() {
        return R.layout.player;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mediaPlayer.onPlay ("https://1256136530.vod2.myqcloud.com/c20c3a87vodtransgzp1256136530/ae0d1d4b7447398155040609194/v.f20.mp4", 1000 * 12);
        mediaPlayer.setPlayerCallback (playerCallback);
        mediaPlayer.setCanDrag (0);
    }

    @Override
    protected void onResume() {
        super.onResume ();
        if (mediaPlayer.isPaused ()) {
            mediaPlayer.onResume ();
        }
    }

    @Override
    protected void onPause() {
        super.onPause ();
        mediaPlayer.onPause ();
    }

    @Override
    protected void onStop() {
        super.onStop ();
    }

    @Override
    protected void onDestroy() {
        Log.i ("PlayerActivity", "onDestroy()==" + mediaPlayer);
        mediaPlayer.onRelease ();
        super.onDestroy ();

    }

    @Override
    public void onBackPressed() {
        if (mediaPlayer.getPlayerCurrentMode () == ConstUtils.MODE_FULL_SCREEN) {
            mediaPlayer.exitFullScreen ();
        } else {
            finish ();
        }
    }

    PlayerCallback playerCallback = new PlayerCallback () {
        @Override
        public void setUp() {
            mediaPlayer.onPlay ("https://1256136530.vod2.myqcloud.com/c20c3a87vodtransgzp1256136530/ae0d1d4c7447398155040609195/v.f20.mp4", 12000);
        }

        @Override
        public void setNext() {
            mediaPlayer.onPlay ("https://1256136530.vod2.myqcloud.com/c20c3a87vodtransgzp1256136530/ae315e477447398155040629512/v.f20.mp4", 0);
        }

        @Override
        public void onPlayPause() {

        }

        @Override
        public void onPlayStart() {

        }

        @Override
        public void onPlayError() {

        }

        @Override
        public void onPlayComplet() {
            mediaPlayer.onPlay ("https://1256136530.vod2.myqcloud.com/c20c3a87vodtransgzp1256136530/ae315f327447398155040629586/v.f20.mp4", 12000);
        }
    };


}
