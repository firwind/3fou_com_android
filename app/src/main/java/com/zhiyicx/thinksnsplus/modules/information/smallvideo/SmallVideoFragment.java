package com.zhiyicx.thinksnsplus.modules.information.smallvideo;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.video.VideoListener;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.SmallVideoDataBean;
import com.zhiyicx.thinksnsplus.i.IntentKey;
import com.zhiyicx.thinksnsplus.modules.information.smallvideo.comment.SmallVideoCommentDialog;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.widget.dialog.EditTextDialog;
import com.zhiyicx.thinksnsplus.widget.dialog.HBaseDialog;
import com.zhiyicx.thinksnsplus.widget.verticalviewpager.OnViewPagerListener;
import com.zhiyicx.thinksnsplus.widget.verticalviewpager.ViewPagerLayoutManager;
import com.zhiyicx.thinksnsplus.widget.videoplayer.SimpleVideoPlayer;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * author: huwenyong
 * date: 2018/8/27 18:58
 * description:
 * version:
 */

public class SmallVideoFragment extends TSListFragment<SmallVideoContract.Presenter, DynamicDetailBeanV2>
        implements SmallVideoContract.View, OnViewPagerListener, SmallVideoCommentDialog.OnInputDialogShowListener {

    @BindView(R.id.iv_back)
    ImageView mIvBack;

    private TextView mTvComment;
    private ImageView mIvFollow;
    private SmallVideoCommentDialog mSmallVideoCommentDialog;//评论弹窗
    private EditTextDialog mEditTextDialog;//输入弹窗
    private long mUserId;
    private int currentPosition = 0;

    public static SmallVideoFragment newInstance(List<DynamicDetailBeanV2> list, int position) {

        SmallVideoFragment fragment = new SmallVideoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(IntentKey.DATA, (ArrayList<? extends Parcelable>) list);
        bundle.putInt(IntentKey.POSITION, position);
        fragment.setArguments(bundle);

        return fragment;
    }

    public static SmallVideoFragment newInstance(Bundle bundle) {
        SmallVideoFragment fragment = new SmallVideoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRefreshEnable() {
        return false;
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

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_small_video_play;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        currentPosition = getArguments().getInt(IntentKey.POSITION);
        mUserId = getArguments().getLong(IntentKey.USER_ID);
        mIvBack.setOnClickListener(v -> mActivity.finish());
    }

    @Override
    protected boolean getEnalbeAutoLoadMore() {
        return true;
    }

    @Override
    protected boolean getEnableScrollContentWhenLoaded() {
        return false;
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
    public void onDestroyView() {
        super.onDestroyView();

        //通知上一个页面更新数据
        if(0 == mUserId)
            EventBus.getDefault().post(new SmallVideoDataBean(mListDatas,currentPosition),
                    EventBusTagConfig.EVENT_UPDATE_SMALL_VIDEO_LIST);
    }

    @Override
    public void onResume() {
        super.onResume();
        SimpleVideoPlayer.getInstance(mActivity).onResume();
        for (int i = 0; i < mRvList.getChildCount(); i++) {
            if (null != mRvList.getChildAt(i) && null != mRvList.getChildAt(i).findViewById(R.id.iv_play))
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
                        SimpleVideoPlayer.getInstance(mActivity).getSimpleExoPlayer().getPlayWhenReady() ?
                                View.INVISIBLE : View.VISIBLE);
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

    @Override
    public Long getUserId() {
        return mUserId;
    }

    /**
     * 评论弹窗
     *
     * @param detailBeanV2
     */
    private void showSmallVideoCommentDialog(DynamicDetailBeanV2 detailBeanV2) {

        if (null == mSmallVideoCommentDialog) {
            mSmallVideoCommentDialog = new SmallVideoCommentDialog(mActivity, true);
            mSmallVideoCommentDialog.setOnInputDialogShowListener(this);
            mSmallVideoCommentDialog.setPresenter(((SmallVideoPresenter) mPresenter));
            mSmallVideoCommentDialog.getDialog().setOnDismissListener(dialog -> {
                //刷新评论数
                //refreshData(currentPosition);
                mTvComment.setText(String.valueOf(detailBeanV2.getFeed_comment_count()));
            });
            ((SmallVideoPresenter) mPresenter).setSmallVideoCommentView(mSmallVideoCommentDialog);
        }
        mSmallVideoCommentDialog.setCurrentDynamicDetailData(detailBeanV2);
        mSmallVideoCommentDialog.showDialog();
    }

    /**
     * 输入弹窗
     *
     * @param hint
     * @param listener
     */
    @Override
    public void onInputDilaogShow(String hint, EditTextDialog.OnInputOkListener listener) {
        if (null == mEditTextDialog)
            mEditTextDialog = new EditTextDialog(mActivity, true);
        mEditTextDialog.setOnInputOkListener(listener);
        mEditTextDialog.setHintText(hint);
        mEditTextDialog.showDialog();
    }


    /**
     * 适配器
     *
     * @return
     */
    private CommonAdapter<DynamicDetailBeanV2> getSmallVideoAdapter() {

        return new CommonAdapter<DynamicDetailBeanV2>(mActivity,
                R.layout.item_small_video_play, mListDatas) {
            @Override
            protected void convert(ViewHolder holder, DynamicDetailBeanV2 smallVideoListBean, int position) {

                ImageUtils.loadUserHead(smallVideoListBean.getUserInfoBean(),
                        ((UserAvatarView) holder.getView(R.id.user_avatar)), false);
                if (null != smallVideoListBean.getUserInfoBean())
                    holder.getTextView(R.id.tv_user_name).setText(smallVideoListBean.getUserInfoBean().getName());

                holder.getView(R.id.iv_follow).setVisibility(
                        AppApplication.getMyUserIdWithdefault() == smallVideoListBean.getUser_id() ? View.INVISIBLE : View.VISIBLE);

                //视频缩略图
                holder.getImageViwe(R.id.iv_thumb).setVisibility(View.VISIBLE);
                ImageUtils.loadVerticalVideoThumbDefault(holder.getImageViwe(R.id.iv_thumb),
                        ImageUtils.getVideoUrl(smallVideoListBean.getVideo() != null ? smallVideoListBean.getVideo().getCover_id() : 0));
                //评论数
                holder.getTextView(R.id.tv_comment).setText(String.valueOf(smallVideoListBean.getFeed_comment_count()));
                //点赞数
                holder.getTextView(R.id.tv_dig).setText(String.valueOf(smallVideoListBean.getFeed_digg_count()));
                //内容
                holder.getTextView(R.id.tv_content).setText(smallVideoListBean.getFeed_content());
                //播放按钮
                holder.getView(R.id.iv_play).setVisibility(View.INVISIBLE);


                //是否已经点赞
                holder.getTextView(R.id.tv_dig).setSelected(smallVideoListBean.getHas_digg());
                holder.getImageViwe(R.id.iv_follow).setSelected(smallVideoListBean.getUserInfoBean().isFollower());

                //关注监听
                holder.getView(R.id.iv_follow).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.handleFollow(smallVideoListBean.getUserInfoBean());
                        //presenter中已经做了处理
                        //smallVideoListBean.getUserInfoBean().setFollowing(smallVideoListBean.getUserInfoBean().isFollowing());
                        holder.getImageViwe(R.id.iv_follow).setSelected(smallVideoListBean.getUserInfoBean().isFollower());
                    }
                });

                //点赞监听
                holder.getView(R.id.tv_dig).setOnClickListener(v -> {
                    //后台处理
                    mPresenter.handleLike(smallVideoListBean);

//                    smallVideoListBean.setHas_digg(smallVideoListBean.getHas_digg());
                    smallVideoListBean.setFeed_digg_count(smallVideoListBean.getHas_digg() ? smallVideoListBean.getFeed_digg_count() + 1 :
                            smallVideoListBean.getFeed_digg_count() - 1);
                    holder.getTextView(R.id.tv_dig).setSelected(smallVideoListBean.getHas_digg());
                    holder.getTextView(R.id.tv_dig).setText(String.valueOf(smallVideoListBean.getFeed_digg_count()));
                });

                //跳转 个人中心  这里暂时屏蔽掉，从这里进入个人主页，再进视频，再返回这里，会有问题
                /*holder.getView(R.id.user_avatar).setOnClickListener(v ->
                        PersonalCenterFragment.startToPersonalCenter(mActivity,smallVideoListBean.getUserInfoBean()));*/

                //分享
                holder.getView(R.id.tv_share).setOnClickListener(v ->
                        mPresenter.shareDynamic(smallVideoListBean,null/*getSharBitmap(holder.getImageViwe(R.id.iv_thumb)))*/));

                //评论
                holder.getView(R.id.tv_comment).setOnClickListener(v -> showSmallVideoCommentDialog(smallVideoListBean));

                if(currentPosition == position){
                    mTvComment = holder.getTextView(R.id.tv_comment);
                    mIvFollow = holder.getImageViwe(R.id.iv_follow);
                }

                //视频处理
                if (currentPosition == position && null != smallVideoListBean.getVideo()) {

                    //如果当前视频正在播放
                    if (((FrameLayout) holder.getView(R.id.video_container)).getChildCount() > 0 &&
                            SimpleVideoPlayer.getInstance(mActivity).getCurrentVideoUrl()
                                    .equals(ImageUtils.getVideoUrl(mListDatas.get(currentPosition).getVideo().getVideo_id()))) {

                    } else {

                        ((FrameLayout) holder.getView(R.id.video_container)).removeAllViews();

                        PlayerView playerView = new PlayerView(mActivity);
                        playerView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT));
                        ((FrameLayout) holder.getView(R.id.video_container)).addView(playerView);
                        SimpleVideoPlayer.getInstance(mActivity).toPlayVideo(mActivity,
                                TextUtils.isEmpty(ImageUtils.getVideoUrl(mListDatas.get(currentPosition).getVideo().getVideo_id())) ? mListDatas.get(currentPosition).getVideo().getUrl() :
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
            }
        };

    }

}
