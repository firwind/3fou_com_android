package com.tym.shortvideo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.Surface;


import com.tym.shortvideo.interfaces.SingleCallback;
import com.tym.shortvideo.media.MediaPlayerWrapper;
import com.tym.shortvideo.media.VideoInfo;
import com.tym.shortvideo.filter.helper.SlideGpuFilterGroup;
import com.tym.shortvideo.filter.helper.VideoDrawer;
import com.tym.shortvideo.utils.CameraUtils;
import com.zhiyicx.common.utils.DeviceUtils;

import java.io.IOException;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author Jliuer
 * @Date 18/04/28 9:50
 * @Email Jliuer@aliyun.com
 * @Description 播放视频的view 单个视频循环播放
 */
public class VideoPreviewView extends GLSurfaceView implements GLSurfaceView.Renderer, MediaPlayerWrapper.IMediaCallback {
    private MediaPlayerWrapper mMediaPlayer;
    private VideoDrawer mDrawer;

    /**
     * 视频播放状态的回调
     */
    private MediaPlayerWrapper.IMediaCallback callback;

    public VideoPreviewView(Context context) {
        super(context, null);
    }

    public VideoPreviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setEGLContextClientVersion(2);
        setRenderer(this);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
        setPreserveEGLContextOnPause(false);
        setCameraDistance(100);
        mDrawer = new VideoDrawer(context, getResources());

        //初始化Drawer和VideoPlayer
        mMediaPlayer = new MediaPlayerWrapper();
        mMediaPlayer.setOnCompletionListener(this);
    }

    /**
     * 设置视频的播放地址
     */
    public void setVideoPath(List<String> paths) {
        mMediaPlayer.setDataSource(paths);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mDrawer.onSurfaceCreated(gl, config);
        SurfaceTexture surfaceTexture = mDrawer.getSurfaceTexture();
        surfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                requestRender();
            }
        });
        Surface surface = new Surface(surfaceTexture);
        mMediaPlayer.setSurface(surface);
        try {
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (CameraUtils.getCurrentRatio() == CameraUtils.Ratio.RATIO_4_3_2_1_1
                || CameraUtils.getCurrentRatio() == CameraUtils.Ratio.RATIO_16_9_2_1_1) {
            int w, h;
            w = h = DeviceUtils.getScreenWidth(getContext());
            if (CameraUtils.getCurrentRatio() == CameraUtils.Ratio.RATIO_16_9_2_1_1) {
                // 不知为何， 16:9 转 1:1 计算得到的值并不好用，相差了 0.02f
                h = (int) (0.98 * DeviceUtils.getScreenWidth(getContext()));
            }

            setMeasuredDimension(w, h);
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mDrawer.onSurfaceChanged(gl, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        mDrawer.onDrawFrame(gl);
    }

    public void onDestroy() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        mMediaPlayer.release();
    }

    public void onTouch(final MotionEvent event) {
        queueEvent(new Runnable() {
            @Override
            public void run() {
                mDrawer.onTouch(event);
            }
        });
    }

    public void setOnFilterChangeListener(SlideGpuFilterGroup.OnFilterChangeListener listener) {
        mDrawer.setOnFilterChangeListener(listener);
    }

    @Override
    public void onVideoPrepare() {
        if (callback != null) {
            callback.onVideoPrepare();
        }
    }

    @Override
    public void onVideoStart() {
        if (callback != null) {
            callback.onVideoStart();
        }
    }

    @Override
    public void onVideoPause() {
        if (callback != null) {
            callback.onVideoPause();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (callback != null) {
            callback.onCompletion(mp);
        }
    }

    @Override
    public void onVideoChanged(final VideoInfo info) {
        queueEvent(new Runnable() {
            @Override
            public void run() {
                mDrawer.onVideoChanged(info);
            }
        });
        if (callback != null) {
            callback.onVideoChanged(info);
        }
    }

    /**
     * isPlaying now
     */
    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    /**
     * pause play
     */
    public void pause() {
        mMediaPlayer.pause();
    }

    /**
     * start play video
     */
    public void start() {
        mMediaPlayer.start();
    }

    /**
     * 跳转到指定的时间点，只能跳到关键帧
     */
    public void seekTo(int time) {
        mMediaPlayer.seekTo(time);
    }

    /**
     * 获取当前视频的长度
     */
    public int getVideoDuration() {
        return mMediaPlayer.getCurVideoDuration();
    }

    public int getTotalVIdeoDuration() {
        return mMediaPlayer.getVideoDuration();
    }

    public int getVideoWidth() {
        return mMediaPlayer.getVideoInfo().get(0).getWidth();
    }

    public int getVideoHeight() {
        return mMediaPlayer.getVideoInfo().get(0).getHeight();
    }

    /**
     * 获取视频信息
     *
     * @return
     */
    public List<VideoInfo> getVideoInfo() {
        return mMediaPlayer.getVideoInfo();
    }


    /**
     * 切换美颜状态
     */
    public void switchBeauty() {
        mDrawer.switchBeauty();
    }


    public void setIMediaCallback(MediaPlayerWrapper.IMediaCallback callback) {
        this.callback = callback;
    }

    public void takePic(SingleCallback<Bitmap, Integer> singleCallback) {
        mDrawer.takePic(singleCallback);
    }
}
