package com.tym.shortvideo.view;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.common.utils.log.LogUtils;

import java.util.concurrent.TimeUnit;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerManager;
import cn.jzvd.JZVideoPlayerStandard;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * @author Jliuer
 * @Date 18/04/03 13:49
 * @Email Jliuer@aliyun.com
 * @Description 监听recycleView滑动状态，自动播放可见区域内的第一个视频,暂停不可见的播放器
 */
public abstract class AutoPlayScrollListener extends RecyclerView.OnScrollListener {

    private int firstVisibleItem = 0;
    private int firstCompoleteVisibleItem = 0;
    private int lastCompoleteVisibleItem = 0;
    private int lastVisibleItem = 0;
    private int visibleCount = 0;

    private final int ALL_INVISIBLE = 0;
    private final int ALL_VISIBLE = 1;
    private final int PART_VISIBLE = 2;

    private boolean palyDelay = true;

    /**
     * 被处理的视频状态标签
     */
    public enum VideoTagEnum {

        /**
         * 自动播放视频
         */
        TAG_AUTO_PLAY_VIDEO,

        /**
         * 暂停视频
         */
        TAG_PAUSE_VIDEO
    }

    @Override
    public void onScrollStateChanged(final RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        switch (newState) {
            case RecyclerView.SCROLL_STATE_IDLE:
                if (canAutoPlay()) {
                    if (palyDelay) {
                        Observable.timer(500, TimeUnit.MILLISECONDS)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<Long>() {
                                    @Override
                                    public void call(Long aLong) {
                                        autoPlayVideo(recyclerView, VideoTagEnum.TAG_AUTO_PLAY_VIDEO);
                                    }
                                });
                    } else {
                        autoPlayVideo(recyclerView, VideoTagEnum.TAG_AUTO_PLAY_VIDEO);
                    }
                }
            default: // TODO 滑出屏幕暂停

                if (JZVideoPlayerManager.getCurrentJzvd() != null) {

                    boolean isPlay = JZVideoPlayerManager.getCurrentJzvd().currentState == JZVideoPlayerStandard.CURRENT_STATE_PLAYING
                            || JZVideoPlayerManager.getCurrentJzvd().currentState == JZVideoPlayerStandard.CURRENT_STATE_PREPARING
                            || JZVideoPlayerManager.getCurrentJzvd().currentState == JZVideoPlayerStandard.CURRENT_STATE_PREPARING_CHANGING_URL;

                    if (PART_VISIBLE == checkVisibility(JZVideoPlayerManager.getCurrentJzvd())) {
                        if (JZVideoPlayerManager.getCurrentJzvd().currentState == JZVideoPlayerStandard.CURRENT_STATE_PLAYING) {
                            JZVideoPlayerManager.getCurrentJzvd().startButton.callOnClick();
                        } else {
                            JZVideoPlayer.releaseAllVideos();
                        }
                    }
                }
                break;
        }

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
            firstVisibleItem = linearManager.findFirstVisibleItemPosition();
            firstCompoleteVisibleItem = linearManager.findFirstCompletelyVisibleItemPosition();
            lastVisibleItem = linearManager.findLastVisibleItemPosition();
            lastCompoleteVisibleItem = linearManager.findLastCompletelyVisibleItemPosition();
            visibleCount = lastVisibleItem - firstVisibleItem + 1;
        }

    }

    /**
     * 循环遍历 可见区域的播放器
     * 然后通过 getLocalVisibleRect(rect)方法计算出哪个播放器完全显示出来
     * <p>
     * getLocalVisibleRect相关链接：http://www.cnblogs.com/ai-developers/p/4413585.html
     *
     * @param recyclerView
     * @param videoTagEnum 视频需要进行状态
     */
    private void autoPlayVideo(RecyclerView recyclerView, VideoTagEnum videoTagEnum) {

        if (videoTagEnum == VideoTagEnum.TAG_PAUSE_VIDEO) {

            return;
        }

        // 当前正在播放的 view
        JZVideoPlayerStandard currentPlayView = null;
        // 准备播放的 view
        JZVideoPlayerStandard nextplayView = null;
        for (int i = 0; i < visibleCount; i++) {
            if (recyclerView != null && recyclerView.getChildAt(i) != null && recyclerView
                    .getChildAt(i).findViewById(getPlayerViewId()) != null) {
                JZVideoPlayerStandard playView = (JZVideoPlayerStandard) recyclerView.getChildAt
                        (i).findViewById(getPlayerViewId());
                Rect playViewRect = new Rect();
                playView.getLocalVisibleRect(playViewRect);
                if (playView.currentState == JZVideoPlayerStandard.CURRENT_STATE_PLAYING) {
                    currentPlayView = playView;
                    int current = recyclerView.getChildAdapterPosition(recyclerView.getChildAt(i));
                    // 当前正在播放的 view 少于 1/3 的可见高度
                    if (playViewRect.bottom - playViewRect.top < (float) currentPlayView
                            .getHeight() / 3f) {
                        currentPlayView.startButton.callOnClick();
                        JZVideoPlayerStandard view = null;
                        if (current + 1 == firstCompoleteVisibleItem) {
                            View itemView = recyclerView.getChildAt(i + 1);
                            if (itemView != null) {
                                view = (JZVideoPlayerStandard) itemView.findViewById
                                        (getPlayerViewId());
                            }
                        } else if (current - 1 == lastCompoleteVisibleItem) {
                            View itemView = recyclerView.getChildAt(i - 1);
                            if (itemView != null) {
                                view = (JZVideoPlayerStandard) itemView.findViewById
                                        (getPlayerViewId());
                            }
                        }
                        // 滑动方向上的第一个完全可见的 view
                        if (view != null) {
                            view.startVideo();
                        }

                    }
                }
                int videoheight = playView.getHeight();
                if (playViewRect.top == 0 && playViewRect.bottom == videoheight && nextplayView
                        == null) {
                    nextplayView = playView;
                }

            }
        }
        if (currentPlayView == null && nextplayView != null) {
            if (nextplayView.currentState == JZVideoPlayerStandard.CURRENT_STATE_PAUSE) {
                nextplayView.startButton.callOnClick();
            } else {
                nextplayView.startVideo();
            }
        }

    }

    /**
     * 视频状态处理
     *
     * @param handleVideoTag     视频需要进行状态
     * @param homeGSYVideoPlayer JZVideoPlayer播放器
     */
    private void handleVideo(VideoTagEnum handleVideoTag, JZVideoPlayerStandard
            homeGSYVideoPlayer) {
        switch (handleVideoTag) {
            case TAG_AUTO_PLAY_VIDEO:
                if (homeGSYVideoPlayer.currentState != JZVideoPlayerStandard
                        .CURRENT_STATE_PLAYING) {
                    // 进行播放
                    homeGSYVideoPlayer.startVideo();
                }
                break;
            case TAG_PAUSE_VIDEO:
                if (homeGSYVideoPlayer.currentState != JZVideoPlayerStandard.CURRENT_STATE_PAUSE) {
                    // 模拟点击,暂停视频
                    homeGSYVideoPlayer.startButton.callOnClick();
                }
                break;
            default:
                break;
        }
    }

    private int checkVisibility(View view) {
        Rect rect = new Rect();
        boolean b = view.getLocalVisibleRect(rect);
        if (b) {
            if (rect.width() == view.getMeasuredWidth() && rect.height() == view.getMeasuredHeight()) {
                return ALL_VISIBLE;
            }
            return PART_VISIBLE;
        }
        return ALL_INVISIBLE;
    }

    public abstract int getPlayerViewId();

    public abstract boolean canAutoPlay();
}