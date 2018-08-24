package com.zhiyicx.thinksnsplus.modules.information.videoinfodetails;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;
import com.jakewharton.rxbinding.view.RxView;
import com.trycatch.mysnackbar.Prompt;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.baseproject.widget.DynamicDetailMenuView;
import com.zhiyicx.baseproject.widget.InputLimitView;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.BuildConfig;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.FastBlur;
import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseWebLoad;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsCountBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.report.ReportResourceBean;
import com.zhiyicx.thinksnsplus.i.IntentKey;
import com.zhiyicx.thinksnsplus.i.OnUserInfoClickListener;
import com.zhiyicx.thinksnsplus.modules.information.adapter.InfoDetailCommentEmptyItem;
import com.zhiyicx.thinksnsplus.modules.information.adapter.InfoDetailCommentItem;
import com.zhiyicx.thinksnsplus.modules.information.adapter.InfoDetailHeaderView;
import com.zhiyicx.thinksnsplus.modules.information.adapter.VideoInfoDetailHeaderView;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.modules.report.ReportActivity;
import com.zhiyicx.thinksnsplus.modules.report.ReportType;
import com.zhiyicx.thinksnsplus.modules.shortvideo.helper.ZhiyiVideoView;
import com.zhiyicx.thinksnsplus.modules.wallet.reward.RewardType;
import com.zhiyicx.thinksnsplus.modules.wallet.sticktop.StickTopFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import cn.jzvd.JZMediaManager;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerManager;
import cn.jzvd.JZVideoPlayerStandard;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;
import static cn.jzvd.JZVideoPlayer.URL_KEY_DEFAULT;
import static com.zhiyicx.baseproject.widget.DynamicDetailMenuView.ITEM_POSITION_0;
import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_UPDATE_LIST_DELETE;
import static com.zhiyicx.thinksnsplus.modules.home.message.messagecomment.MessageCommentAdapter.BUNDLE_SOURCE_ID;
import static com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsFragment.BUNDLE_INFO;

/**
 * @Author Jliuer
 * @Date 2017/03/08
 * @Email Jliuer@aliyun.com
 * @Description 视频详情
 */
public class VideoInfoDetailsFragment extends TSListFragment<VideoInfoDetailsConstract.Presenter,
        InfoCommentListBean> implements VideoInfoDetailsConstract.View, InputLimitView
        .OnSendClickListener, MultiItemTypeAdapter.OnItemClickListener, OnUserInfoClickListener {

    @BindView(R.id.tv_toolbar_center)
    TextView mTvToolbarCenter;
    @BindView(R.id.tv_toolbar_left)
    TextView mTvToolbarLeft;
    @BindView(R.id.tv_toolbar_right)
    TextView mTvToolbarRight;
    @BindView(R.id.ilv_comment)
    InputLimitView mIlvComment;
    @BindView(R.id.videoplayer)
    ZhiyiVideoView videoView;

    private VideoInfoDetailHeaderView mInfoDetailHeader;

    private ActionPopupWindow mDeletCommentPopWindow;

    /**
     * 传入的资讯信息
     */
    private InfoListDataBean mInfoMation;

    //上一个界面传过来的视频播放 state
    private int mVideoState;

    private int mReplyUserId;// 被评论者的 id ,评论动态 id = 0

    private boolean mIsClose;

    public static VideoInfoDetailsFragment newInstance(InfoListDataBean data,int videoState) {
        VideoInfoDetailsFragment fragment = new VideoInfoDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(IntentKey.DATA,data);
        bundle.putInt(IntentKey.VIDEO_STATE,videoState);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isNeedRefreshAnimation() {
        return false;
    }

    @Override
    protected boolean isRefreshEnable() {
        return false;
    }
    @Override
    protected boolean setUseCenterLoading() {
        return true;
    }
    @Override
    protected int getstatusbarAndToolbarHeight() {
        return 0;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected MultiItemTypeAdapter getAdapter() {
        MultiItemTypeAdapter multiItemTypeAdapter = new MultiItemTypeAdapter<>(getActivity(),
                mListDatas);
        InfoDetailCommentItem infoDetailCommentItem = new InfoDetailCommentItem(new
                ItemOnCommentListener());
        infoDetailCommentItem.setOnUserInfoClickListener(this);
        multiItemTypeAdapter.addItemViewDelegate(infoDetailCommentItem);
        multiItemTypeAdapter.addItemViewDelegate(new InfoDetailCommentEmptyItem());
        multiItemTypeAdapter.setOnItemClickListener(this);
        return multiItemTypeAdapter;
    }


    @Override
    public void updateInfoHeader(InfoListDataBean infoDetailBean) {
        closeLoadingView();
        this.mInfoMation = infoDetailBean;
        mInfoDetailHeader.setDetail(infoDetailBean);
        mInfoDetailHeader.setRelateInfo(infoDetailBean);
        onNetResponseSuccess(infoDetailBean.getCommentList(), false);
    }

    @Override
    public void setDigg(boolean isDigged, int count) {

    }


    @Override
    protected void snackViewDismissWhenTimeOut(Prompt prompt) {
        super.snackViewDismissWhenTimeOut(prompt);
        if (getActivity() != null && Prompt.SUCCESS == prompt && mIsClose) {
            getActivity().finish();
        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mIlvComment.setEtContentHint(getString(R.string.default_input_hint));
        mInfoMation = (InfoListDataBean) getArguments().getParcelable(IntentKey.DATA);
        mVideoState = getArguments().getInt(IntentKey.VIDEO_STATE,-1);
        if (mInfoMation == null) {
            mInfoMation = new InfoListDataBean();
            Long ids = getArguments().getLong(BUNDLE_SOURCE_ID);
            mInfoMation.setId(ids);
        }

        mIlvComment.setSendButtonVisiable(true);
        mTvToolbarCenter.setVisibility(View.VISIBLE);
        mTvToolbarCenter.setText(mInfoMation.getUser_name());
        mTvToolbarLeft.setOnClickListener(v -> {
            mActivity.finish();
        });

        initVideoPlayer();
        initHeaderView();
        initListener();

        mInfoDetailHeader.getDigView().setOnClickListener(v -> mPresenter.handleLike(mInfoMation));

        mInfoDetailHeader.setInfoReviewIng(mInfoMation.getAudit_status() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public void loadAllError() {
        hideRefreshState(false);
    }

    @Override
    protected void setLoadingViewHolderClick() {
        super.setLoadingViewHolderClick();
        requestNetData(mMaxId, false);
    }


    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_video_info_detail;
    }

    @Override
    public Long getNewsId() {
        return (long) mInfoMation.getId();
    }


    @Override
    public InfoListDataBean getCurrentInfo() {
        return mInfoMation;
    }


    @Override
    public void onNetResponseSuccess(@NotNull List<InfoCommentListBean> data, boolean isLoadMore) {
        if (!isLoadMore) {
            if (data.isEmpty()) { // 空白展位图
                InfoCommentListBean emptyData = new InfoCommentListBean();
                data.add(emptyData);
            }
        }
        super.onNetResponseSuccess(data, isLoadMore);
    }

    @Override
    public void onSendClick(View v, String text) {
        DeviceUtils.hideSoftKeyboard(getContext(), v);
        mIlvComment.setVisibility(View.GONE);
        mPresenter.sendComment(mReplyUserId, text);
    }

    @Override
    public void refreshData() {
        super.refreshData();
        mInfoMation.setComment_count(TextUtils.isEmpty(mListDatas.get(0).getComment_content()) ? 0 : mListDatas.size());
        mInfoDetailHeader.updateCommentView(mInfoMation);
    }

    /**
     * 初始化播放器
     */
    private void initVideoPlayer(){

        String imageUrl = ImageUtils.imagePathConvertV2(mInfoMation.getImage().getId(), 0, 0, ImageZipConfig.IMAGE_100_ZIP);
        String videoUrl = ImageUtils.getVideoUrl(mInfoMation.getVideo());

        Glide.with(mActivity)
                .load(imageUrl)
                .signature(new StringSignature(imageUrl + mInfoMation.getCreated_at()))
                .placeholder(R.mipmap.default_image_for_video)
                .error(R.mipmap.default_image_for_video)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean
                            isFromMemoryCache, boolean isFirstResource) {
                        Observable.just(resource)
                                .subscribeOn(Schedulers.io())
                                .map(glideDrawable -> {
                                    Bitmap bitmap = FastBlur.blurBitmapForShortVideo(ConvertUtils.drawable2Bitmap(resource), resource
                                            .getIntrinsicWidth(), resource
                                            .getIntrinsicHeight());
                                    return new BitmapDrawable(mActivity.getResources(), bitmap);
                                })
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(background -> {
                                    if (videoView != null) {
                                        // 防止被回收
                                        videoView.setBackground(background);
                                    }
                                }, Throwable::printStackTrace);

                        return false;
                    }
                })
                .into(videoView.thumbImageView);

        boolean isListToDetail = false;
        if (JZVideoPlayerManager.getFirstFloor() != null
                && !JZVideoPlayerManager.getCurrentJzvd().equals(videoView)) {


            LinkedHashMap<String, Object> map = (LinkedHashMap) JZVideoPlayerManager.getFirstFloor().dataSourceObjects[0];
            if (map != null) {
                isListToDetail = videoUrl.equals(map.get(URL_KEY_DEFAULT).toString());
            }

            if (isListToDetail) {

                videoView.setUp(videoUrl, JZVideoPlayerStandard.SCREEN_WINDOW_LIST);
                videoView.positionInList = 0;
                videoView.setState(mVideoState);
                videoView.positionInList = JZVideoPlayerManager.getFirstFloor().positionInList;
                if(null != JZMediaManager.textureView.getParent())
                    ((ZhiyiVideoView) JZVideoPlayerManager.getFirstFloor()).removeTextureView();
                videoView.addTextureView();
                if (JZVideoPlayerManager.getFirstFloor() instanceof ZhiyiVideoView) {
                    ZhiyiVideoView firstFloor = (ZhiyiVideoView) JZVideoPlayerManager.getFirstFloor();
                    videoView.mVideoFrom = firstFloor.mVideoFrom;
                }
                JZVideoPlayerManager.setFirstFloor(videoView);
                videoView.startProgressTimer();
                if (mVideoState == ZhiyiVideoView.CURRENT_STATE_PAUSE) {
                    videoView.startButton.callOnClick();
                }
            } else {
                videoView.setUp(videoUrl, JZVideoPlayerStandard.SCREEN_WINDOW_LIST);
                videoView.positionInList = 0;
            }

        } else {
            videoView.setUp(videoUrl, JZVideoPlayerStandard.SCREEN_WINDOW_LIST);
            videoView.positionInList = 0;
        }


    }

    private void initHeaderView() {
        mInfoDetailHeader = new VideoInfoDetailHeaderView(getContext());
        mHeaderAndFooterWrapper.addHeaderView(mInfoDetailHeader.getInfoDetailHeader());
        View mFooterView = new View(getContext());
        mFooterView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        mHeaderAndFooterWrapper.addFootView(mFooterView);
        mRvList.setAdapter(mHeaderAndFooterWrapper);
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }

    private void initListener() {
        mIlvComment.setOnSendClickListener(this);
    }

    public void showCommentView() {
        // 评论
        mIlvComment.setVisibility(View.VISIBLE);
        mIlvComment.setSendButtonVisiable(true);
        mIlvComment.getFocus();
        DeviceUtils.showSoftKeyboard(getActivity(), mIlvComment.getEtContent());
    }

    /**
     * 初始化评论删除选择弹框
     */
    private void initDeleteCommentPopupWindow(final InfoCommentListBean data) {
        mDeletCommentPopWindow = ActionPopupWindow.builder()
                .item1Str(BuildConfig.USE_TOLL && data.getId() != -1L && !data.getPinned() ? getString(R.string.dynamic_list_top_comment) : null)
                .item1Color(ContextCompat.getColor(getContext(), R.color.themeColor))
                .item2Str(getString(R.string.dynamic_list_delete_comment))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(() -> {
                    // 跳转置顶页面
                    mDeletCommentPopWindow.hide();
                    boolean sourceIsMine = AppApplication.getMyUserIdWithdefault() == data.getUser_id();

                    StickTopFragment.startSticTopActivity(getActivity(), StickTopFragment.TYPE_INFO, mInfoMation.getId(), data.getId(), sourceIsMine);
                    mDeletCommentPopWindow.hide();
                })
                .item2ClickListener(() -> {
                    mDeletCommentPopWindow.hide();
                    showDeleteTipPopupWindow(getString(R.string.delete_comment), () -> {
                        mPresenter.deleteComment(data);
                    }, true);

                })
                .bottomClickListener(() -> mDeletCommentPopWindow.hide())
                .build();
    }


    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        comment(position);
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        goReportComment(position);
        return true;
    }

    @Override
    public void onUserInfoClick(UserInfoBean userInfoBean) {
        PersonalCenterFragment.startToPersonalCenter(getContext(), userInfoBean);
    }


    class ItemOnCommentListener implements InfoDetailCommentItem.OnCommentItemListener {
        @Override
        public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
            comment(position);
        }

        @Override
        public void onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
            goReportComment(position);
        }

        @Override
        public void onUserInfoClick(UserInfoBean userInfoBean) {
            PersonalCenterFragment.startToPersonalCenter(getContext(), userInfoBean);
        }
    }

    /**
     * 评论
     *
     * @param position
     */
    private void comment(int position) {
        position = position - mHeaderAndFooterWrapper.getHeadersCount();// 减去 header
        InfoCommentListBean infoCommentListBean = mListDatas.get(position);
        if (infoCommentListBean != null && !TextUtils.isEmpty(infoCommentListBean.getComment_content())) {
            if (infoCommentListBean.getUser_id() == AppApplication.getMyUserIdWithdefault()) {// 自己的评论
                if (mListDatas.get(position).getId() != -1) {
                    initDeleteCommentPopupWindow(infoCommentListBean);
                    mDeletCommentPopWindow.show();
                }
//                else {
//
//                    return;
//                }
            } else {
                mReplyUserId = (int) infoCommentListBean.getUser_id();
                showCommentView();
                String contentHint = getString(R.string.default_input_hint);
                if (infoCommentListBean.getReply_to_user_id() != mInfoMation.getId()) {
                    contentHint = getString(R.string.reply, infoCommentListBean.getFromUserInfoBean().getName());
                }
                mIlvComment.setEtContentHint(contentHint);
            }
        }
    }

    /**
     * 举报
     *
     * @param position
     */
    private void goReportComment(int position) {
        // 减去 header
        position = position - mHeaderAndFooterWrapper.getHeadersCount();
        // 举报
        if (mListDatas.get(position).getUser_id() != AppApplication.getMyUserIdWithdefault()) {
            ReportActivity.startReportActivity(mActivity, new ReportResourceBean(mListDatas.get(position).getFromUserInfoBean(), mListDatas.get
                    (position).getId().toString(),
                    null, null, mListDatas.get(position).getComment_content(), ReportType.COMMENT));

        } else {

        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dismissPop(mDeletCommentPopWindow);
    }
}
