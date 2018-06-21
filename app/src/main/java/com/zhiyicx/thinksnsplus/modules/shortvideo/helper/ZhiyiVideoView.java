package com.zhiyicx.thinksnsplus.modules.shortvideo.helper;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Shader;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tym.shortvideo.view.ZhiyiResizeTextureView;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.DrawableProvider;
import com.zhiyicx.common.utils.FastBlur;
import com.zhiyicx.common.utils.StatusBarUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;

import java.lang.reflect.Constructor;

import cn.jzvd.JZMediaManager;
import cn.jzvd.JZUserActionStandard;
import cn.jzvd.JZUtils;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerManager;
import cn.jzvd.JZVideoPlayerStandard;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Author Jliuer
 * @Date 2018/03/29/15:32
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class ZhiyiVideoView extends JZVideoPlayerStandard {

    protected static final int MEDIA_INFO_VIDEO_ROTATION_CHANGED = 10001;
    protected static final int MEDIA_INFO_BUFFERING_START = 701;
    protected static final int MEDIA_INFO_BUFFERING_END = 702;

    public ImageView mShareImageView;
    public ImageView mDefaultStartImageView;
    public ImageView mVideoLoadingImageView;

    public LinearLayout mShareLineLinearLayout;

    public LinearLayout mShareLinearLayout;

    public TextView mShareToQQ;
    public TextView mShareToQQZone;
    public TextView mShareToWX;
    public TextView mShareToWXZone;
    public TextView mShareToWeiBo;

    public TextView mShareTextView;

    public ActionPopupWindow mWarnPopupWindow;

    public String mVideoFrom;

    public ZhiyiVideoView(Context context) {
        super(context);
    }

    public ZhiyiVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onPrepared() {
        super.onPrepared();
//        if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
//            JZMediaManager.instance().jzMediaInterface.setVolume(1f, 1f);
//        } else {
//            JZMediaManager.instance().jzMediaInterface.setVolume(0f, 0f);
//        }
    }

    /**
     * 退出全屏模式的时候开启静音模式
     */
    @Override
    public void playOnThisJzvd() {
        super.playOnThisJzvd();
        StatusBarUtils.statusBarLightMode(JZUtils.scanForActivity(getContext()),StatusBarUtils.STATUS_TYPE_FLYME);
//        JZMediaManager.instance().jzMediaInterface.setVolume(0f, 0f);
    }

    @Override
    public void init(Context context) {
        super.init(context);
        mShareImageView = (ImageView) findViewById(R.id.share);
        mDefaultStartImageView = (ImageView) findViewById(R.id.first_start);
        mVideoLoadingImageView = (ImageView) findViewById(R.id.iv_loading);
        mShareLineLinearLayout = (LinearLayout) findViewById(R.id.ll_share_line_container);
        mShareLinearLayout = (LinearLayout) findViewById(R.id.ll_share_container);

        mShareToQQ = (TextView) findViewById(R.id.share_qq);
        mShareToQQZone = (TextView) findViewById(R.id.share_qq_zone);
        mShareToWX = (TextView) findViewById(R.id.share_wx);
        mShareToWXZone = (TextView) findViewById(R.id.share_wx_zone);
        mShareToWeiBo = (TextView) findViewById(R.id.share_weibo);
        mShareTextView = (TextView) findViewById(R.id.share_text);

        mShareImageView.setOnClickListener(this);
        mShareToQQ.setOnClickListener(this);
        mShareToQQZone.setOnClickListener(this);
        mShareToWXZone.setOnClickListener(this);
        mShareToWeiBo.setOnClickListener(this);
        mShareToWX.setOnClickListener(this);

    }

    @Override
    public void initTextureView() {
        removeTextureView();
        JZMediaManager.textureView = new ZhiyiResizeTextureView(getContext());
        JZMediaManager.textureView.setSurfaceTextureListener(JZMediaManager.instance());
    }

    @Override
    public int getLayoutId() {
        return R.layout.zhiyi_layout_standard;
    }

    @Override
    public void startVideo() {
        if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
            LogUtils.d(TAG, "startVideo [" + this.hashCode() + "] ");
            initTextureView();
            addTextureView();
            AudioManager mAudioManager = (AudioManager) getContext().getSystemService(Context
                    .AUDIO_SERVICE);
            mAudioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager
                    .STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            JZUtils.scanForActivity(getContext()).getWindow().addFlags(WindowManager.LayoutParams
                    .FLAG_KEEP_SCREEN_ON);

            JZMediaManager.setDataSource(dataSourceObjects);
            JZMediaManager.setCurrentDataSource(JZUtils.getCurrentFromDataSource
                    (dataSourceObjects, currentUrlMapIndex));
            JZMediaManager.instance().positionInList = positionInList;
            onStatePreparing();
        } else {
            JZVideoPlayerManager.completeAll();
            Log.d(TAG, "startVideo [" + this.hashCode() + "] ");
            initTextureView();
            addTextureView();
            AudioManager mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
            mAudioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            JZUtils.scanForActivity(getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            JZMediaManager.setDataSource(dataSourceObjects);
            JZMediaManager.setCurrentDataSource(JZUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex));
            JZMediaManager.instance().positionInList = positionInList;
            onStatePreparing();
            JZVideoPlayerManager.setFirstFloor(this);
        }
    }

    @Override
    public void onInfo(int what, int extra) {
        super.onInfo(what, extra);

        if (what == MEDIA_INFO_VIDEO_ROTATION_CHANGED) {
            // 在 ijk中 10001 是角度信息，IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED
            JZMediaManager.textureView.setRotation(extra);
        }
        if (what == MEDIA_INFO_BUFFERING_START) {
            // 在 ijk中 701 是开始缓冲，IMediaPlayer.MEDIA_INFO_BUFFERING_START
            currentState = CURRENT_STATE_PREPARING;
            changeUiToPreparing();
        }
        if (what == MEDIA_INFO_BUFFERING_END) {
            // 在 ijk中 702 是缓冲完成，IMediaPlayer.MEDIA_INFO_BUFFERING_END
            currentState = CURRENT_STATE_PLAYING;
            changeUiToPlayingClear();
        }
    }

    @Override
    public void onAutoCompletion() {
        if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
            onStateAutoComplete();
            mShareImageView.setVisibility(GONE);
            mShareTextView.setVisibility(GONE);
            replayTextView.setVisibility(GONE);
            mShareLineLinearLayout.setVisibility(VISIBLE);
            mShareLinearLayout.setVisibility(VISIBLE);
        } else {
            super.onAutoCompletion();
            mShareImageView.setVisibility(VISIBLE);
        }
        mDefaultStartImageView.setVisibility(GONE);
        thumbImageView.setBackgroundColor(Color.parseColor("#80000000"));
    }

    @Override
    public void onStatePreparingChangingUrl(int urlMapIndex, long seekToInAdvance) {
        super.onStatePreparingChangingUrl(urlMapIndex, seekToInAdvance);
        mDefaultStartImageView.setVisibility(GONE);
        loadingProgressBar.setVisibility(GONE);
        mVideoLoadingImageView.setVisibility(VISIBLE);
        ((AnimationDrawable) mVideoLoadingImageView.getDrawable()).start();
    }

    @Override
    public void changeUiToNormal() {
        super.changeUiToNormal();
        mShareImageView.setVisibility(GONE);

        // 左下角的开始按钮
//        mDefaultStartImageView.setVisibility(VISIBLE);
        mDefaultStartImageView.setVisibility(GONE);

        mShareLineLinearLayout.setVisibility(GONE);
        mShareLinearLayout.setVisibility(GONE);
    }

    @Override
    public void changeUiToPreparing() {
        super.changeUiToPreparing();
        mShareImageView.setVisibility(GONE);
        mDefaultStartImageView.setVisibility(GONE);
        mShareLineLinearLayout.setVisibility(GONE);
        mShareLinearLayout.setVisibility(GONE);
        thumbImageView.setVisibility(progressBar.getProgress() == 0 ? VISIBLE : GONE);
    }

    @Override
    public void changeUiToPlayingShow() {
        super.changeUiToPlayingShow();
        mShareImageView.setVisibility(GONE);
        mDefaultStartImageView.setVisibility(GONE);
        mShareLineLinearLayout.setVisibility(GONE);
        mShareLinearLayout.setVisibility(GONE);
    }

    @Override
    public void changeUiToPlayingClear() {
        super.changeUiToPlayingClear();
        mShareImageView.setVisibility(GONE);
        mDefaultStartImageView.setVisibility(GONE);
        mShareLineLinearLayout.setVisibility(GONE);
        mShareLinearLayout.setVisibility(GONE);
    }

    @Override
    public void changeUiToPauseShow() {
        super.changeUiToPauseShow();
        mShareImageView.setVisibility(GONE);
        mDefaultStartImageView.setVisibility(GONE);
        mShareLineLinearLayout.setVisibility(GONE);
        mShareLinearLayout.setVisibility(GONE);
    }

    @Override
    public void changeUiToPauseClear() {
        super.changeUiToPauseClear();
        mDefaultStartImageView.setVisibility(GONE);
        mShareImageView.setVisibility(GONE);
        mShareLineLinearLayout.setVisibility(GONE);
        mShareLinearLayout.setVisibility(GONE);
    }

    @Override
    public void changeUiToError() {
        super.changeUiToError();
        mDefaultStartImageView.setVisibility(GONE);
        mShareImageView.setVisibility(GONE);
        mShareLineLinearLayout.setVisibility(GONE);
        mShareLinearLayout.setVisibility(GONE);
    }

    @Override
    public void changeUiToComplete() {
        super.changeUiToComplete();
        mDefaultStartImageView.setVisibility(GONE);
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                mShareImageView.setVisibility(VISIBLE);
                mShareLineLinearLayout.setVisibility(GONE);
                mShareLinearLayout.setVisibility(GONE);
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                mShareImageView.setVisibility(GONE);
                mShareLineLinearLayout.setVisibility(VISIBLE);
                mShareLinearLayout.setVisibility(VISIBLE);
                break;
            case SCREEN_WINDOW_TINY:
                break;
            default:
        }
    }

    @Override
    public void setAllControlsVisiblity(int topCon, int bottomCon, int startBtn, int loadingPro, int thumbImg, int bottomPro, int retryLayout) {
        super.setAllControlsVisiblity(topCon, bottomCon, startBtn, loadingPro, thumbImg, bottomPro, retryLayout);
        loadingProgressBar.setVisibility(GONE);
        mVideoLoadingImageView.setVisibility(loadingPro);
        if (loadingPro == GONE || loadingPro == INVISIBLE) {
            ((AnimationDrawable) mVideoLoadingImageView.getDrawable()).stop();
            return;
        }
        ((AnimationDrawable) mVideoLoadingImageView.getDrawable()).start();
    }

    @Override
    public void updateStartImage() {
        if (currentState == CURRENT_STATE_PLAYING) {
            startButton.setVisibility(VISIBLE);
            startButton.setImageResource(R.mipmap.icon_video_suspend);
            replayTextView.setVisibility(GONE);
        } else if (currentState == CURRENT_STATE_ERROR) {
            startButton.setVisibility(GONE);
            replayTextView.setVisibility(GONE);
        } else if (currentState == CURRENT_STATE_AUTO_COMPLETE) {
            startButton.setVisibility(VISIBLE);
            startButton.setImageResource(R.mipmap.ico_video_replay);
            replayTextView.setVisibility(VISIBLE);
        } else {

            // 中间的开始按钮
//            if (currentState != CURRENT_STATE_PAUSE) {
//                startButton.setVisibility(GONE);
//            }


            if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
                startButton.setImageResource(R.mipmap.ico_video_play_fullscreen);
                mDefaultStartImageView.setImageResource(R.mipmap.ico_video_play_fullscreen);
            } else {
                startButton.setImageResource(R.mipmap.ico_video_play_list);
                mDefaultStartImageView.setImageResource(R.mipmap.ico_video_play_list);
            }
            replayTextView.setVisibility(GONE);
        }
        mShareTextView.setVisibility(replayTextView.getVisibility());
    }

    @Override
    public void onStatePrepared() {
        if (seekToInAdvance != 0) {
            JZMediaManager.seekTo(seekToInAdvance);
            seekToInAdvance = 0;
        }
    }

    @Override
    public void onVideoSizeChanged() {
        LogUtils.i(TAG, "onVideoSizeChanged " + " [" + this.hashCode() + "] ");
        if (JZMediaManager.textureView != null) {
            if (videoRotation != 0) {
                JZMediaManager.textureView.setRotation(videoRotation);
            }
            JZMediaManager.textureView.setVideoSize(JZMediaManager.instance().currentVideoWidth,
                    JZMediaManager.instance().currentVideoHeight);
        }
    }

    @Override
    public void startWindowFullscreen() {
        LogUtils.d("startWindowFullscreen:::" + " [" + this.hashCode() + "] ");
        hideSupportActionBar(getContext());
        //.getWindow().getDecorView();
        ViewGroup vp = (ViewGroup) (JZUtils.scanForActivity(getContext()))
                .findViewById(Window.ID_ANDROID_CONTENT);
        View old = vp.findViewById(cn.jzvd.R.id.jz_fullscreen_id);
        if (old != null) {
            vp.removeView(old);
        }
        textureViewContainer.removeView(JZMediaManager.textureView);
        try {
            Constructor<ZhiyiVideoView> constructor = (Constructor<ZhiyiVideoView>)
                    ZhiyiVideoView.this.getClass().getConstructor(Context.class);
            ZhiyiVideoView jzVideoPlayer = constructor.newInstance(getContext());
            jzVideoPlayer.setId(cn.jzvd.R.id.jz_fullscreen_id);
            jzVideoPlayer.setShareInterface(mShareInterface);
            LayoutParams lp = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            vp.addView(jzVideoPlayer, lp);
            // 三个 tag 依次
            // 1.隐藏虚拟按键(导航栏)。有些手机会用虚拟按键来代替物理按键。
            // 2.隐藏了系统栏和其他UI控件
            // 3.Activity全屏显示，且状态栏被隐藏覆盖掉
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                jzVideoPlayer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN);
            }else{
                jzVideoPlayer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);
            }
            jzVideoPlayer.setUp(dataSourceObjects, currentUrlMapIndex, JZVideoPlayerStandard
                    .SCREEN_WINDOW_FULLSCREEN, objects);
            jzVideoPlayer.setState(currentState);
            jzVideoPlayer.positionInList = this.positionInList;

            jzVideoPlayer.addTextureView();
            JZVideoPlayer firstVideoView = JZVideoPlayerManager.getFirstFloor();
            BitmapDrawable drawable = (BitmapDrawable) firstVideoView.getBackground();

            Observable.just(drawable.getBitmap())
                    .subscribeOn(Schedulers.io())
                    .map(bg -> {
                        Bitmap overlay = Bitmap.createScaledBitmap(bg, DeviceUtils.getScreenHeight(getContext()),
                                DeviceUtils.getScreenHeight(getContext()), false);
                        Bitmap bitmap = FastBlur.blurBitmap(overlay, overlay.getWidth(), overlay.getHeight());
                        return new BitmapDrawable(getResources(), bitmap);
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(jzVideoPlayer::setBackground, Throwable::printStackTrace);

            JZVideoPlayerManager.setSecondFloor(jzVideoPlayer);

            int orientation;
            float rotation = 0;
            if (JZMediaManager.textureView instanceof ZhiyiResizeTextureView) {
                ZhiyiResizeTextureView resizeTextureView = (ZhiyiResizeTextureView) JZMediaManager.textureView;
                rotation = resizeTextureView.getRotation();
            }
            if (JZMediaManager.instance().currentVideoWidth > JZMediaManager.instance()
                    .currentVideoHeight && rotation == 0) {
                // 横屏
                orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            } else {
                orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            }
            JZUtils.setRequestedOrientation(getContext(), orientation);


            onStateNormal();
            jzVideoPlayer.progressBar.setSecondaryProgress(progressBar.getSecondaryProgress());
            jzVideoPlayer.startProgressTimer();
            CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        JZMediaManager.instance().jzMediaInterface.setVolume(1f, 1f);
    }



    @Override
    public void startWindowTiny() {
        // 彻底做掉小窗播放
        //super.startWindowTiny();
    }

    @Override
    public void showWifiDialog() {
        if (JZVideoPlayerManager.getCurrentJzvd() != null) {
            if (JZMediaManager.isPlaying()) {
                JZVideoPlayerManager.getCurrentJzvd().startButton.callOnClick();
            }
        }
        if (mWarnPopupWindow == null) {
            mWarnPopupWindow = ActionPopupWindow.builder()
                    .item1Str(getResources().getString(R.string.info_publish_hint))
                    .desStr(getResources().getString(R.string.tips_not_wifi))
                    .item2Str(getResources().getString(R.string.keepon))
                    .bottomStr(getResources().getString(R.string.giveup))
                    .isOutsideTouch(true)
                    .isFocus(true)
                    .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                    .with(JZUtils.scanForActivity(getContext()))
                    .item2ClickListener(() -> {
                        mWarnPopupWindow.dismiss();
                        onEvent(JZUserActionStandard.ON_CLICK_START_WIFIDIALOG);
                        startVideo();
                        WIFI_TIP_DIALOG_SHOWED = true;
                    })
                    .bottomClickListener(() -> {
                        mWarnPopupWindow.dismiss();
                        clearFloatScreen();
                    })
                    .build();
        }
        mWarnPopupWindow.show();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (mShareInterface == null) {
            return;
        }
        int i = v.getId();
        SHARE_MEDIA type = null;
        switch (i) {
            case R.id.share:
                mShareInterface.share(positionInList);
                break;
            case R.id.share_qq:
                type = SHARE_MEDIA.QQ;
                break;
            case R.id.share_qq_zone:
                type = SHARE_MEDIA.QZONE;
                break;
            case R.id.share_wx:
                type = SHARE_MEDIA.WEIXIN;
                break;
            case R.id.share_wx_zone:
                type = SHARE_MEDIA.WEIXIN_CIRCLE;
                break;
            case R.id.share_weibo:
                type = SHARE_MEDIA.SINA;
                break;
            default:
        }

        if (type != null) {
            mShareInterface.shareWihtType(positionInList, type);
        }

    }

    @Override
    public void setBufferProgress(int bufferProgress) {
        super.setBufferProgress(bufferProgress);
    }

    protected ShareInterface mShareInterface;

    public void setShareInterface(ShareInterface shareInterface) {
        mShareInterface = shareInterface;
    }

    public interface ShareInterface {
        void share(int position);

        void shareWihtType(int position, SHARE_MEDIA type);
    }
}
