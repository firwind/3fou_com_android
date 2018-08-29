package com.zhiyicx.thinksnsplus.modules.information.smallvideo;

import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.video.VideoListener;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.i.IntentKey;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.widget.verticalviewpager.OnViewPagerListener;
import com.zhiyicx.thinksnsplus.widget.verticalviewpager.ViewPagerLayoutManager;
import com.zhiyicx.thinksnsplus.widget.videoplayer.SimpleVideoPlayer;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * author: huwenyong
 * date: 2018/8/27 18:58
 * description:
 * version:
 */

public class SmallVideoFragment extends TSListFragment<SmallVideoContract.Presenter, DynamicDetailBeanV2> implements SmallVideoContract.View, OnViewPagerListener {

    /*@BindView(R.id.iv_back)
    ImageView mIvBack;*/


    private int currentPosition = 0;

    public static SmallVideoFragment newInstance(List<DynamicDetailBeanV2> list, int position) {

        SmallVideoFragment fragment = new SmallVideoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(IntentKey.DATA, (ArrayList<? extends Parcelable>) list);
        bundle.putInt(IntentKey.POSITION, position);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected boolean setStatusbarGrey() {
        return false;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

   /* @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_small_video_play;
    }*/

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);

        currentPosition = getArguments().getInt(IntentKey.POSITION);

    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public void onCacheResponseSuccess(List<DynamicDetailBeanV2> data, boolean isLoadMore) {
        super.onCacheResponseSuccess(data, isLoadMore);
        if(data.size() > currentPosition)
            mRvList.scrollToPosition(currentPosition);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        ViewPagerLayoutManager layoutManager = new ViewPagerLayoutManager(getContext(), OrientationHelper.VERTICAL);
        layoutManager.setOnViewPagerListener(this);
        return layoutManager;
    }

    @Override
    public void onResume() {
        super.onResume();
        SimpleVideoPlayer.getInstance(mActivity).onResume();
        for (int i = 0; i < mRvList.getChildCount(); i++) {
            if(null != mRvList.getChildAt(i) && null != mRvList.getChildAt(i).findViewById(R.id.iv_play))
                mRvList.getChildAt(i).findViewById(R.id.iv_play).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        CommonAdapter<DynamicDetailBeanV2> mAdapter = getSmallVideoAdapter();

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                SimpleVideoPlayer.getInstance(mActivity).onPlayOrPause();
                view.findViewById(R.id.iv_play).setVisibility(
                        SimpleVideoPlayer.getInstance(mActivity).getSimpleExoPlayer().getPlayWhenReady()?
                                View.INVISIBLE:View.VISIBLE);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        return mAdapter;
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }
        };
    }

    @Override
    public void onStop() {
        super.onStop();
        SimpleVideoPlayer.getInstance(mActivity).onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SimpleVideoPlayer.getInstance(mActivity).onDestroy();
    }

    @Override
    public void onInitComplete() {

    }

    @Override
    public void onPageRelease(boolean isNext, int position) {

    }

    @Override
    public void onPageSelected(int position, boolean isBottom, View view) {

        if (this.currentPosition == position)
            return;

        this.currentPosition = position;
        SimpleVideoPlayer.getInstance(mActivity).onStop();

        refreshData();

    }

    @Override
    public List<DynamicDetailBeanV2> getInitVideoList() {
        return getArguments().getParcelableArrayList(IntentKey.DATA);
    }


    private CommonAdapter<DynamicDetailBeanV2> getSmallVideoAdapter(){

        return new CommonAdapter<DynamicDetailBeanV2>(mActivity,
                R.layout.item_small_video_play, mListDatas) {
            @Override
            protected void convert(ViewHolder holder, DynamicDetailBeanV2 smallVideoListBean, int position) {

                ImageUtils.loadUserHead(smallVideoListBean.getUserInfoBean(),
                        ((UserAvatarView)holder.getView(R.id.user_avatar)), false);
                if(null != smallVideoListBean.getUserInfoBean())
                    holder.getTextView(R.id.tv_user_name).setText(smallVideoListBean.getUserInfoBean().getName());
                holder.getImageViwe(R.id.iv_thumb).setVisibility(View.VISIBLE);
                //视频缩略图
                ImageUtils.loadVerticalVideoThumbDefault(holder.getImageViwe(R.id.iv_thumb),
                        ImageUtils.getVideoUrl(smallVideoListBean.getVideo() != null?smallVideoListBean.getVideo().getCover_id():0));
                //评论数
                holder.getTextView(R.id.tv_comment).setText(String.valueOf(smallVideoListBean.getFeed_comment_count()) );
                //点赞数
                holder.getTextView(R.id.tv_dig).setText(String.valueOf(smallVideoListBean.getFeed_digg_count()) );
                //是否已经点赞
                holder.getTextView(R.id.tv_dig).setSelected(smallVideoListBean.getHas_digg());
                //内容
                holder.getTextView(R.id.tv_content).setText(smallVideoListBean.getFeed_content());
                //播放按钮
                holder.getView(R.id.iv_play).setVisibility(View.INVISIBLE);
                //视频处理
                if(currentPosition == position && null != smallVideoListBean.getVideo()){

                    ((FrameLayout)holder.getView(R.id.video_container)).removeAllViews();

                    PlayerView playerView = new PlayerView(mActivity);
                    playerView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
                    ((FrameLayout)holder.getView(R.id.video_container)).addView(playerView);
                    SimpleVideoPlayer.getInstance(mActivity).toPlayVideo(mActivity,
                            ImageUtils.getVideoUrl(mListDatas.get(currentPosition).getVideo().getVideo_id()),
                            playerView, new VideoListener() {
                                @Override
                                public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {

                                }

                                @Override
                                public void onRenderedFirstFrame() {
                                    holder.getImageViwe(R.id.iv_thumb).setVisibility(View.INVISIBLE);
                                }
                            });
                }
            }
        };

    }


}
