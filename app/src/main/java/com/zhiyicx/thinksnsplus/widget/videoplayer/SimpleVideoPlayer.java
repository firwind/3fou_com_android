package com.zhiyicx.thinksnsplus.widget.videoplayer;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;

/**
 * author: huwenyong
 * date: 2018/8/28 16:24
 * description:
 * version:
 */

public class SimpleVideoPlayer {

    private static SimpleExoPlayer mExoPlayer;
    private static SimpleVideoPlayer instance;

    private boolean isPlaying = false;

    public static SimpleVideoPlayer getInstance(Activity mActivity){
        if(instance == null){
            synchronized (SimpleVideoPlayer.class){
                if(instance == null){
                    instance = new SimpleVideoPlayer();
                    mExoPlayer = createVideoPlayer(mActivity);
                }
            }
        }
        return instance;
    }

    /**
     * 开始播放
     */
    public void toPlayVideo(Context mContext, String videoUrl, PlayerView mPlayerView,VideoListener mVideoListener){

        mExoPlayer.stop(true);

        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext,
                Util.getUserAgent(mContext, "com.huwy.medialib"));
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(videoUrl));

        // Prepare the player with the source.
        mExoPlayer.prepare(videoSource);
        mExoPlayer.setPlayWhenReady(true);
        mExoPlayer.addVideoListener(mVideoListener);
        // Bind the player to the view.
        mPlayerView.setUseController(false);
        mPlayerView.setPlayer(mExoPlayer);
    }

    /**
     * 停止播放
     */
    public void onStop(){
        if(null != mExoPlayer)
            mExoPlayer.setPlayWhenReady(false);
    }

    /**
     * 继续播放
     */
    public void onResume(){
        if(null != mExoPlayer)
            mExoPlayer.setPlayWhenReady(true);
    }

    /**
     * 暂停或者继续播放
     */
    public void onPlayOrPause(){

        if(null != mExoPlayer)
            mExoPlayer.setPlayWhenReady(!mExoPlayer.getPlayWhenReady());

    }

    /**
     * 暴露给外部的播放器
     * @return
     */
    public SimpleExoPlayer getSimpleExoPlayer(){
        return mExoPlayer;
    }

    /**
     * 释放播放器
     */
    public void onDestroy(){
        if(null != mExoPlayer)
            mExoPlayer.release();

        mExoPlayer = null;
        instance = null;
    }

    /**
     * 创建播放器
     * @return
     */
    private static SimpleExoPlayer createVideoPlayer(Context mContext){

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        SimpleExoPlayer mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);
        mSimpleExoPlayer.setPlayWhenReady(true);//设置自动开始播放
        mSimpleExoPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);//设置重复模式
        mSimpleExoPlayer.addListener(new Player.DefaultEventListener() {
            @Override
            public void onLoadingChanged(boolean isLoading) {
                super.onLoadingChanged(isLoading);
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                super.onPlayerError(error);
            }
        });
        return mSimpleExoPlayer;
    }

}
