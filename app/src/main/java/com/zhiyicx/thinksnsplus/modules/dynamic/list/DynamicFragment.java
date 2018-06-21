package com.zhiyicx.thinksnsplus.modules.dynamic.list;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tym.shortvideo.view.AutoPlayScrollListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.config.TouristConfig;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.Toll;
import com.zhiyicx.baseproject.widget.InputLimitView;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.baseproject.widget.popwindow.PayPopWindow;
import com.zhiyicx.common.BuildConfig;
import com.zhiyicx.common.utils.AndroidBug5497Workaround;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.TextViewUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicListAdvert;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.report.ReportResourceBean;
import com.zhiyicx.thinksnsplus.i.OnUserInfoClickListener;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailActivity;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicBannerHeader;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListBaseItem;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForAdvert;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForEightImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForFiveImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForFourImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForNineImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForOneImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForSevenImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForShorVideo;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForSixImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForThreeImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForTwoImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForZeroImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.tollcomment.DynamicCommentTollActivity;
import com.zhiyicx.thinksnsplus.modules.gallery.GalleryActivity;
import com.zhiyicx.thinksnsplus.modules.home.HomeFragment;
import com.zhiyicx.thinksnsplus.modules.home.main.MainFragment;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.modules.report.ReportActivity;
import com.zhiyicx.thinksnsplus.modules.report.ReportType;
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.CustomWEBActivity;
import com.zhiyicx.thinksnsplus.modules.shortvideo.helper.ZhiyiVideoView;
import com.zhiyicx.thinksnsplus.modules.wallet.sticktop.StickTopActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.sticktop.StickTopFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.widget.comment.DynamicListCommentView;
import com.zhiyicx.thinksnsplus.widget.comment.DynamicNoPullRecycleView;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import cn.jzvd.JZMediaManager;
import cn.jzvd.JZVideoPlayerManager;
import cn.jzvd.JZVideoPlayerStandard;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.zhiyicx.baseproject.config.ApiConfig.DYNAMIC_TYPE_HOTS;
import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.thinksnsplus.data.beans.DynamicListAdvert.DEFAULT_ADVERT_FROM_TAG;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA_POSITION;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA_TYPE;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_VIDEO_STATE;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.LOOK_COMMENT_MORE;
import static com.zhiyicx.thinksnsplus.modules.dynamic.tollcomment.DynamicCommentTollFragment.TOLL_DYNAMIC_COMMENT;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA_POSITION;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA_TYPE;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_VIDEO_STATE;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.LOOK_COMMENT_MORE;
import static com.zhiyicx.thinksnsplus.modules.dynamic.tollcomment.DynamicCommentTollFragment.TOLL_DYNAMIC_COMMENT;

/**
 * @Describe 动态列表
 * @Author zl
 * @Date 2017/1/17
 * @Contact master.jungle68@gmail.com
 */
public class DynamicFragment extends TSListFragment<DynamicContract.Presenter, DynamicDetailBeanV2>
        implements DynamicNoPullRecycleView.OnCommentStateClickListener<DynamicCommentBean>,
        InputLimitView.OnSendClickListener, DynamicContract.View, DynamicListCommentView
                .OnCommentClickListener, DynamicListCommentView.OnMoreCommentClickListener,
        DynamicListBaseItem.OnReSendClickListener, DynamicListBaseItem.OnMenuItemClickLisitener,
        DynamicListBaseItem.OnImageClickListener, OnUserInfoClickListener,
        MultiItemTypeAdapter.OnItemClickListener, TextViewUtils.OnSpanTextClickListener,
        DynamicBannerHeader.DynamicBannerHeadlerClickEvent, ZhiyiVideoView.ShareInterface {

    protected static final String BUNDLE_DYNAMIC_TYPE = "dynamic_type";

    /**
     * item 间距单位 dp , 由 5L 修改至 10L by tym 2018-5-15 14:55:58
     */
    public static final long ITEM_SPACING = 5L;

    @BindView(R.id.ilv_comment)
    InputLimitView mIlvComment;
    @BindView(R.id.v_shadow)
    View mVShadow;

    /**
     * 仅用于构造
     */
    @Inject
    DynamicPresenter mDynamicPresenter;
    private String mDynamicType = ApiConfig.DYNAMIC_TYPE_NEW;

    private ActionPopupWindow mDeletCommentPopWindow;
    private ActionPopupWindow mOtherDynamicPopWindow;

    private ActionPopupWindow mMyDynamicPopWindow;
    private ActionPopupWindow mReSendCommentPopWindow;
    private ActionPopupWindow mReSendDynamicPopWindow;
    private PayPopWindow mPayImagePopWindow;
    /**
     * 是否已经加载过网络数据了
     */
    private boolean mIsLoadedNetData = false;

    /**
     * 当前评论的动态位置
     */
    private int mCurrentPostion;

    /**
     * 被评论者的 id
     */
    private long mReplyToUserId;

    private DynamicBannerHeader mDynamicBannerHeader;
    private List<RealAdvertListBean> mListAdvert;
    private List<RealAdvertListBean> mHeaderAdvert;

    public void setOnCommentClickListener(OnCommentClickListener onCommentClickListener) {
        mOnCommentClickListener = onCommentClickListener;
    }

    OnCommentClickListener mOnCommentClickListener;

    public static DynamicFragment newInstance(String dynamicType, OnCommentClickListener l) {
        DynamicFragment fragment = new DynamicFragment();
        fragment.setOnCommentClickListener(l);
        Bundle args = new Bundle();
        args.putString(BUNDLE_DYNAMIC_TYPE, dynamicType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void headClick(int position) {
        toAdvert(mHeaderAdvert.get(position).getAdvertFormat().getImage().getLink(),
                mHeaderAdvert.get(position).getTitle());
    }

    private void toAdvert(String link, String title) {
        CustomWEBActivity.startToWEBActivity(getActivity(), link, title);
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return DYNAMIC_TYPE_HOTS.equals(mDynamicType);
    }

    @Override
    protected boolean isNeedRequestNetDataWhenCacheDataNull() {
        return DYNAMIC_TYPE_HOTS.equals(mDynamicType);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mDynamicBannerHeader != null) {
            mDynamicBannerHeader.startBanner();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mPresenter != null && !mIsLoadedNetData) {
            startRefrsh();
            mIsLoadedNetData = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mListDatas.isEmpty()) {
            refreshData();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mDynamicBannerHeader != null) {
            mDynamicBannerHeader.stopBanner();
        }
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_list_with_input;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDynamicType = getArguments().getString(BUNDLE_DYNAMIC_TYPE);

    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        // 刷新控件调白
        mRefreshlayout.setBackgroundResource(R.color.white);
        mRvList.setBackgroundResource(R.color.bgColor);
        initInputView();
        AndroidBug5497Workaround.assistActivity(mActivity);
        Observable.create(subscriber -> {
            // 在 super.initData();之前，因为initdata 会使用到 presenter
            DaggerDynamicComponent
                    .builder()
                    .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                    .dynamicPresenterModule(new DynamicPresenterModule(DynamicFragment.this))
                    .build()
                    .inject(DynamicFragment.this);
            subscriber.onCompleted();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        initData();
                        initAdvert();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Object o) {
                    }
                });

        // 自动播放 - 滑出屏幕暂停也在这里面
        mRvList.addOnScrollListener(new AutoPlayScrollListener() {
            @Override
            public int getPlayerViewId() {
                return R.id.videoplayer;
            }

            @Override
            public boolean canAutoPlay() {
                // NetUtils.isWifiConnected(getContext().getApplicationContext())
                // 暂时关闭滑动自动播放
                return false;
            }
        });
    }


    @Override
    protected boolean setUseCenterLoading() {
        return true;
    }

    @Override
    protected boolean setUseCenterLoadingAnimation() {
        return true;
    }

    @Override
    protected int getstatusbarAndToolbarHeight() {
        return 0;
    }

    /**
     * 初始化广告数据
     */
    private void initAdvert() {
        if (!com.zhiyicx.common.BuildConfig.USE_ADVERT) {
            return;
        }
        if (!mDynamicType.equals(DYNAMIC_TYPE_HOTS)) {
            return;
        }
        mHeaderAdvert = mPresenter.getBannerAdvert();

        if (mHeaderAdvert == null || mHeaderAdvert.isEmpty()) {
            return;
        }

        List<String> advertTitle = new ArrayList<>();
        List<String> advertUrls = new ArrayList<>();
        List<String> advertLinks = new ArrayList<>();
        mListAdvert = mPresenter.getListAdvert();

        for (RealAdvertListBean advert : mHeaderAdvert) {
            advertTitle.add(advert.getTitle());
            advertUrls.add(advert.getAdvertFormat().getImage().getImage());
            advertLinks.add(advert.getAdvertFormat().getImage().getLink());
            if ("html".equals(advert.getType())) {
                showStickyHtmlMessage((String) advert.getData());
            }
        }
        if (advertUrls.isEmpty()) {
            return;
        }
        mDynamicBannerHeader = new DynamicBannerHeader(mActivity);
        mDynamicBannerHeader.setHeadlerClickEvent(this);
        DynamicBannerHeader.DynamicBannerHeaderInfo headerInfo = mDynamicBannerHeader.new
                DynamicBannerHeaderInfo();
        headerInfo.setTitles(advertTitle);
        headerInfo.setLinks(advertLinks);
        headerInfo.setUrls(advertUrls);
        headerInfo.setDelay(4000);
        headerInfo.setOnBannerListener(position -> {

        });
        mDynamicBannerHeader.setHeadInfo(headerInfo);
        mHeaderAndFooterWrapper.addHeaderView(mDynamicBannerHeader.getDynamicBannerHeader());
    }

    /**
     * 初始化输入框
     */
    private void initInputView() {
        mVShadow.setOnClickListener(v -> closeInputView());
        mIlvComment.setOnSendClickListener(this);
    }

    @Override
    protected float getItemDecorationSpacing() {
        return ITEM_SPACING;
    }

    @Override
    protected MultiItemTypeAdapter getAdapter() {
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter<>(getContext(), mListDatas);
        setAdapter(adapter, new DynamicListItemForZeroImage(getContext()));
        setAdapter(adapter, new DynamicListItemForOneImage(getContext()));
        setAdapter(adapter, new DynamicListItemForTwoImage(getContext()));
        setAdapter(adapter, new DynamicListItemForThreeImage(getContext()));
        setAdapter(adapter, new DynamicListItemForFourImage(getContext()));
        setAdapter(adapter, new DynamicListItemForFiveImage(getContext()));
        setAdapter(adapter, new DynamicListItemForSixImage(getContext()));
        setAdapter(adapter, new DynamicListItemForSevenImage(getContext()));
        setAdapter(adapter, new DynamicListItemForEightImage(getContext()));
        setAdapter(adapter, new DynamicListItemForNineImage(getContext()));
        setAdapter(adapter, new DynamicListItemForAdvert(getContext()));
        setAdapter(adapter, new DynamicListItemForShorVideo(getContext(), this) {
            @Override
            protected String videoFrom() {
                return mDynamicType;
            }
        });
        adapter.setOnItemClickListener(this);
        return adapter;
    }

    protected void setAdapter(MultiItemTypeAdapter adapter, DynamicListBaseItem
            dynamicListBaseItem) {
        dynamicListBaseItem.setOnImageClickListener(this);
        dynamicListBaseItem.setOnSpanTextClickListener(this);
        dynamicListBaseItem.setOnUserInfoClickListener(this);
        dynamicListBaseItem.setOnMenuItemClickLisitener(this);
        dynamicListBaseItem.setOnReSendClickListener(this);
        dynamicListBaseItem.setOnMoreCommentClickListener(this);
        dynamicListBaseItem.setOnCommentClickListener(this);
        dynamicListBaseItem.setOnCommentStateClickListener(this);
        adapter.addItemViewDelegate(dynamicListBaseItem);
    }

    /**
     * 由于热门和关注和最新的 max_id 不同，所以特殊处理 ,热门分页使用 offset
     *
     * @param data
     * @return
     */
    @Override
    protected Long getMaxId(@NotNull List<DynamicDetailBeanV2> data) {
        if (mDynamicType.equals(DYNAMIC_TYPE_HOTS)) {
            if (DEFAULT_PAGE_SIZE == null) {
                return (long) mPage * DEFAULT_PAGE_DB_SIZE;
            } else {
                return (long) mPage * DEFAULT_PAGE_SIZE;
            }
        } else {
            if (mListDatas.size() > 0) {
                return mListDatas.get(mListDatas.size() - 1).getId();
            } else {
                return DEFAULT_PAGE_MAX_ID;
            }
        }
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<DynamicDetailBeanV2> data, boolean isLoadMore) {
        mIsLoadedNetData = true;
        try {// 添加广告
            if (!data.isEmpty() && mListAdvert != null && mListAdvert.size() >= getPage()) {

                RealAdvertListBean realAdvertListBean = mListAdvert.get(getPage() - 1);
                DynamicListAdvert advert = realAdvertListBean.getAdvertFormat().getAnalog();
                long maxId = data.get(data.size() - 1).getMaxId();
                data.add(DynamicListAdvert.advert2Dynamic(advert, maxId));
            }
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
        super.onNetResponseSuccess(data, isLoadMore);
    }

    @Override
    public void onCacheResponseSuccess(List<DynamicDetailBeanV2> data, boolean isLoadMore) {
        try {// 添加广告
            RealAdvertListBean realAdvertListBean = mListAdvert.get(getPage() - 1);
            DynamicListAdvert advert = realAdvertListBean.getAdvertFormat().getAnalog();
            long maxId = data.get(data.size() - 1).getMaxId();
            data.add(DynamicListAdvert.advert2Dynamic(advert, maxId));
        } catch (Exception ignore) {
        }
        super.onCacheResponseSuccess(data, isLoadMore);
        closeLoadingView();
    }

    /**
     * scan imags
     *
     * @param dynamicBean
     * @param position
     */
    @Override
    public void onImageClick(ViewHolder holder, DynamicDetailBeanV2 dynamicBean, int position) {
        long start = System.currentTimeMillis();
        if (!TouristConfig.DYNAMIC_BIG_PHOTO_CAN_LOOK && mPresenter.handleTouristControl()) {
            return;
        }
        // 广告
        if (dynamicBean.getFeed_from() == DEFAULT_ADVERT_FROM_TAG) {
            toAdvert(dynamicBean.getDeleted_at(), dynamicBean.getFeed_content());
            return;
        }
        int dynamicPosition = holder.getAdapterPosition() - mHeaderAndFooterWrapper
                .getHeadersCount();

        DynamicDetailBeanV2.ImagesBean img = dynamicBean.getImages().get(position);
        boolean canLook = !(img.isPaid() != null && !img.isPaid() && img.getType().equals(Toll
                .LOOK_TOLL_TYPE));
        if (!canLook) {
            initImageCenterPopWindow(dynamicPosition, position, dynamicBean
                            .getImages().get(position).getAmount(),
                    dynamicBean.getImages().get(position).getPaid_node(), R.string.buy_pay_desc,
                    true);
            return;
        }

        List<DynamicDetailBeanV2.ImagesBean> tasks = dynamicBean.getImages();
        List<ImageBean> imageBeanList = new ArrayList<>();
        ArrayList<AnimationRectBean> animationRectBeanArrayList
                = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            DynamicDetailBeanV2.ImagesBean task = tasks.get(i);
            int id = UIUtils.getResourceByName("siv_" + i, "id", getContext());
            ImageView imageView = holder.getView(id);
            ImageBean imageBean = new ImageBean();
            imageBean.setImgUrl(task.getImgUrl());
            Toll toll = new Toll();
            toll.setPaid(task.isPaid());
            toll.setToll_money(task.getAmount());
            toll.setToll_type_string(task.getType());
            toll.setPaid_node(task.getPaid_node());
            imageBean.setToll(toll);
            imageBean.setDynamicPosition(dynamicPosition);
            imageBean.setFeed_id(dynamicBean.getId());
            imageBean.setWidth(task.getWidth());
            imageBean.setHeight(task.getHeight());
            imageBean.setListCacheUrl(task.getGlideUrl());
            imageBean.setStorage_id(task.getFile());
            imageBean.setImgMimeType(task.getImgMimeType());
            imageBeanList.add(imageBean);
            AnimationRectBean rect = AnimationRectBean.buildFromImageView(imageView);
            animationRectBeanArrayList.add(rect);
        }

        GalleryActivity.startToGallery(mActivity, position, imageBeanList, animationRectBeanArrayList);
    }

    @Override
    public void setSpanText(int position, int note, long amount, TextView view, boolean
            canNotRead) {
        position -= mHeaderAndFooterWrapper.getHeadersCount();
        initImageCenterPopWindow(position, position, amount,
                note, R.string.buy_pay_words_desc, false);
    }

    /**
     * scan user Info
     *
     * @param userInfoBean
     */
    @Override
    public void onUserInfoClick(UserInfoBean userInfoBean) {
        // 游客处理
        if (!TouristConfig.USER_INFO_CAN_LOOK && mPresenter.handleTouristControl()) {
            return;
        }
        // 广告
        if (userInfoBean.getUser_id().intValue() == -1) {
            return;
        }
        PersonalCenterFragment.startToPersonalCenter(getContext(), userInfoBean);
    }

    @Override
    public String getDynamicType() {
        return mDynamicType;
    }

    @Override
    public void closeInputView() {
        if (mIlvComment.getVisibility() == View.VISIBLE) {
            mIlvComment.setVisibility(View.GONE);
            DeviceUtils.hideSoftKeyboard(getActivity(), mIlvComment.getEtContent());
        }
        mVShadow.setVisibility(View.GONE);
        if (mOnCommentClickListener != null) {
            mOnCommentClickListener.onButtonMenuShow(true);
        }
    }

    @Override
    public void showNewDynamic(int position) {
        if (position == -1) {
            // 添加一条新动态
            refreshData();
            // 回到顶部
            mRvList.scrollToPosition(0);
            // viewpager切换到关注列表
            Fragment parentFragmentMain = getParentFragment();
            if (parentFragmentMain != null && parentFragmentMain instanceof MainFragment) {
                MainFragment mainFragment = (MainFragment) parentFragmentMain;
                mainFragment.setPagerSelection(MainFragment.PAGER_FOLLOW_DYNAMIC_LIST_POSITION);
                // 主页切换到首页
                Fragment parentFragmentHome = mainFragment.getParentFragment();
                if (parentFragmentHome != null && parentFragmentHome instanceof HomeFragment) {
                    HomeFragment homeFragment = (HomeFragment) parentFragmentHome;
                    homeFragment.setPagerSelection(HomeFragment.PAGE_HOME);
                }
            }
        } else {
            refreshData();
        }
    }

    /**
     * dynamic resend click
     *
     * @param position
     */
    @Override
    public void onReSendClick(int position) {
        initReSendDynamicPopupWindow(position);
        mReSendDynamicPopWindow.show();
    }


    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        position -= mHeaderAndFooterWrapper.getHeadersCount();
        if (!TouristConfig.DYNAMIC_DETAIL_CAN_LOOK && mPresenter.handleTouristControl()) { // 游客处理
            return;
        }
        DynamicDetailBeanV2 detailBeanV2 = mListDatas.get(position);
        // 是广告
        if (detailBeanV2.getFeed_from() == DEFAULT_ADVERT_FROM_TAG) {
            toAdvert(detailBeanV2.getDeleted_at(), detailBeanV2.getFeed_content());
            return;
        }
        boolean canNotLookWords = detailBeanV2.getPaid_node() != null &&
                !detailBeanV2.getPaid_node().isPaid()
                && detailBeanV2.getUser_id().intValue() != AppApplication.getMyUserIdWithdefault();
        if (canNotLookWords) {
            initImageCenterPopWindow(position, position,
                    detailBeanV2.getPaid_node().getAmount(),
                    detailBeanV2.getPaid_node().getNode(), R.string.buy_pay_words_desc, false);
            return;
        }

        goDynamicDetail(position, false, (ViewHolder) holder);

    }


    @Override
    public void paySuccess() {

    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }

    @Override
    public void onMenuItemClick(View view, int dataPosition, int viewPosition) {
        dataPosition -= mHeaderAndFooterWrapper.getHeadersCount();
        switch (viewPosition) {
            // 0 1 2 3 代表 view item 位置
            case 0:

                boolean isAdvert = mListDatas.get(dataPosition).getFeed_from() == DEFAULT_ADVERT_FROM_TAG;
                // 喜欢
                // 还未发送成功的动态列表不查看详情
                boolean isLoginAndDiggClick = !TouristConfig.DYNAMIC_CAN_DIGG && mPresenter.handleTouristControl();
                if (isAdvert || isLoginAndDiggClick || mListDatas.get(dataPosition).getId() == null || mListDatas.get
                        (dataPosition).getId() == 0) {
                    return;
                }
                handleLike(dataPosition);
                break;

            case 1:
                // 评论
                boolean isLogainAndCanComment = !TouristConfig.DYNAMIC_CAN_COMMENT && mPresenter.handleTouristControl();
                // 还未发送成功的动态列表不查看详情
                if (isLogainAndCanComment ||
                        mListDatas.get(dataPosition).getId() == null || mListDatas.get
                        (dataPosition).getId() == 0) {
                    return;
                }
                showCommentView();
                mIlvComment.setEtContentHint(getString(R.string.default_input_hint));
                mCurrentPostion = dataPosition;
                // 0 代表评论动态
                mReplyToUserId = 0;
                break;

            case 2:
                // 浏览
                onItemClick(null, null, dataPosition + mHeaderAndFooterWrapper.getHeadersCount());
                break;

            case 3:
                // 更多
                Bitmap shareBitMap = null;
                try {
                    ImageView imageView = (ImageView) layoutManager.findViewByPosition
                            (dataPosition + mHeaderAndFooterWrapper.getHeadersCount())
                            .findViewById(R.id.siv_0);
                    if (imageView == null) {
                        imageView = (ImageView) layoutManager.findViewByPosition
                                (dataPosition + mHeaderAndFooterWrapper.getHeadersCount())
                                .findViewById(R.id.thumb);
                    }
                    shareBitMap = ConvertUtils.drawable2BitmapWithWhiteBg(getContext(), imageView
                            .getDrawable(), R.mipmap.icon);
                } catch (Exception e) {
                }
                if (AppApplication.getmCurrentLoginAuth() != null && mListDatas.get(dataPosition)
                        .getUser_id() == AppApplication.getMyUserIdWithdefault()) {
                    initMyDynamicPopupWindow(mListDatas.get(dataPosition), dataPosition,
                            mListDatas.get(dataPosition)
                                    .isHas_collect(), shareBitMap);
                    mMyDynamicPopWindow.show();
                } else {
                    // 不是自己
                    initOtherDynamicPopupWindow(mListDatas.get(dataPosition), dataPosition,
                            mListDatas.get(dataPosition)
                                    .isHas_collect(), shareBitMap);
                    mOtherDynamicPopWindow.show();
                }

                break;
            default:
                onItemClick(null, null, dataPosition + mHeaderAndFooterWrapper.getHeadersCount());

        }
    }

    /**
     * 喜欢
     *
     * @param dataPosition
     */
    private void handleLike(int dataPosition) {
        // 先更新界面，再后台处理
        mListDatas.get(dataPosition).setHas_digg(!mListDatas.get(dataPosition)
                .isHas_digg());
        mListDatas.get(dataPosition).setFeed_digg_count(mListDatas.get(dataPosition)
                .isHas_digg() ?
                mListDatas.get(dataPosition).getFeed_digg_count() + 1 : mListDatas.get
                (dataPosition).getFeed_digg_count() - 1);
        refreshData();
        mPresenter.handleLike(mListDatas.get(dataPosition).isHas_digg(),
                mListDatas.get(dataPosition).getId(), dataPosition);
    }

    @Override
    public void onCommentUserInfoClick(UserInfoBean userInfoBean) {
        onUserInfoClick(userInfoBean);
    }

    /**
     * comment has been clicked
     *
     * @param dynamicBean current dynamic
     * @param position    this position of comment
     */
    @Override
    public void onCommentContentClick(DynamicDetailBeanV2 dynamicBean, int position) {
        if (!TouristConfig.DYNAMIC_CAN_COMMENT && mPresenter.handleTouristControl()) {
            return;
        }
        mCurrentPostion = mPresenter.getCurrenPosiotnInDataList(dynamicBean.getFeed_mark());
        if (dynamicBean.getComments().get(position).getUser_id() == AppApplication
                .getMyUserIdWithdefault()) {
            initDeletCommentPopupWindow(dynamicBean, mCurrentPostion, position);
            mDeletCommentPopWindow.show();

        } else {
            showCommentView();
            mReplyToUserId = dynamicBean.getComments().get(position).getUser_id();
            String contentHint = getString(R.string.default_input_hint);
            if (dynamicBean.getComments().get(position).getUser_id() != AppApplication
                    .getMyUserIdWithdefault()) {
                contentHint = getString(R.string.reply, dynamicBean.getComments().get(position)
                        .getCommentUser().getName());
            }
            mIlvComment.setEtContentHint(contentHint);
        }

    }

    /**
     * 评论长按
     *
     * @param dynamicBean
     * @param position
     */
    @Override
    public void onCommentContentLongClick(DynamicDetailBeanV2 dynamicBean, int position) {
        if (!TouristConfig.DYNAMIC_CAN_COMMENT && mPresenter.handleTouristControl()) {
            return;
        }
        mCurrentPostion = mPresenter.getCurrenPosiotnInDataList(dynamicBean.getFeed_mark());
        // 举报
        if (dynamicBean.getComments().get(position).getUser_id() != AppApplication
                .getMyUserIdWithdefault()) {
            ReportActivity.startReportActivity(mActivity, new ReportResourceBean(dynamicBean
                    .getComments().get
                            (position).getCommentUser(), dynamicBean.getComments().get
                    (position).getComment_id().toString(),
                    null, null, dynamicBean.getComments().get(position).getComment_content(),
                    ReportType.COMMENT));

        } else {

        }
    }


    private void showCommentView() {
        showBottomView(false);

    }

    /**
     * comment send
     *
     * @param text
     */
    @Override
    public void onSendClick(View v, final String text) {
        com.zhiyicx.imsdk.utils.common.DeviceUtils.hideSoftKeyboard(getContext(), v);
        mIlvComment.setVisibility(View.GONE);
        mVShadow.setVisibility(View.GONE);
        mPresenter.sendCommentV2(mCurrentPostion, mReplyToUserId, text);
        showBottomView(true);
    }

    /**
     * 重发评论
     *
     * @param dynamicCommentBean
     * @param position
     */
    @Override
    public void onCommentStateClick(DynamicCommentBean dynamicCommentBean, int position) {
        initReSendCommentPopupWindow(dynamicCommentBean, mListDatas.get(mPresenter
                .getCurrenPosiotnInDataList(dynamicCommentBean.getFeed_mark())).getId());
        mReSendCommentPopWindow.show();
    }

    @Override
    public void onMoreCommentClick(View view, DynamicDetailBeanV2 dynamicBean) {


        if (!TouristConfig.MORE_COMMENT_CAN_LOOK && mPresenter.handleTouristControl()) {
            return;
        }
        int dataPosition = mPresenter.getCurrenPosiotnInDataList(dynamicBean.getFeed_mark());
        DynamicDetailBeanV2 detailBeanV2 = mListDatas.get(dataPosition);
        boolean canNotLookWords = detailBeanV2.getPaid_node() != null &&
                !detailBeanV2.getPaid_node().isPaid()
                && detailBeanV2.getUser_id().intValue() != AppApplication.getMyUserIdWithdefault();
        if (canNotLookWords) {
            initImageCenterPopWindow(dataPosition, dataPosition,
                    detailBeanV2.getPaid_node().getAmount(),
                    detailBeanV2.getPaid_node().getNode(), R.string.buy_pay_words_desc, false);
            return;
        }
        int viewPosition = dataPosition + mHeaderAndFooterWrapper.getHeadersCount();
        goDynamicDetail(dataPosition, true, (ViewHolder) mRvList.findViewHolderForAdapterPosition(viewPosition));
    }

    @Override
    public void share(int position) {
        if (mListDatas.get(position).getId() > 0) {
            Bitmap shareBitMap = getShareBitmap(position, R.id.thumb);
            mPresenter.shareDynamic(mListDatas.get(position), shareBitMap);
        }
        showBottomView(true);

    }

    @Override
    public void shareWihtType(int position, SHARE_MEDIA type) {
        if (mListDatas.get(position).getId() > 0) {
            mPresenter.shareDynamic(mListDatas.get(position), getShareBitmap(position, R.id.thumb),
                    type);
        }
    }

    /**
     * 初始化评论删除选择弹框
     *
     * @param dynamicBean     curent dynamic
     * @param dynamicPositon  dynamic comment position
     * @param commentPosition current comment position
     */
    private void initDeletCommentPopupWindow(final DynamicDetailBeanV2 dynamicBean, final int
            dynamicPositon, final int commentPosition) {
        mDeletCommentPopWindow = ActionPopupWindow.builder()
                .item1Str(BuildConfig.USE_TOLL && dynamicBean.getState() == DynamicDetailBeanV2
                        .SEND_SUCCESS && !dynamicBean
                        .getComments().get(commentPosition).getPinned() && dynamicBean.getComments().get(commentPosition).getComment_id() != null ?
                        getString(R
                                .string.dynamic_list_top_comment) : null)
                .item2Str(getString(R.string.dynamic_list_delete_comment))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(() -> {
                    mDeletCommentPopWindow.hide();
                    boolean sourceIsMine = AppApplication.getMyUserIdWithdefault() == dynamicBean.getUser_id();
                    StickTopFragment.startSticTopActivity(getActivity(), StickTopFragment.TYPE_DYNAMIC, dynamicBean.getId(), dynamicBean
                            .getComments().get(commentPosition).getComment_id(), sourceIsMine);
                    showBottomView(true);
                })
                .item2ClickListener(() -> {
                    mDeletCommentPopWindow.hide();
                    showDeleteTipPopupWindow(getString(R.string.delete_comment), () -> {
                        mPresenter.deleteCommentV2(dynamicBean, dynamicPositon, dynamicBean
                                        .getComments().get(commentPosition).getComment_id() != null ? dynamicBean
                                        .getComments().get(commentPosition).getComment_id() : 0,
                                commentPosition);
                        showBottomView(true);
                    }, true);

                })
                .bottomClickListener(() -> {
                    mDeletCommentPopWindow.hide();
                    showBottomView(true);
                })
                .build();
    }

    /**
     * 初始化他人动态操作选择弹框
     *
     * @param dynamicBean curent dynamic
     * @param position    curent dynamic postion
     */
    private void initOtherDynamicPopupWindow(final DynamicDetailBeanV2 dynamicBean, final int
            position,
                                             boolean isCollected, final Bitmap shareBitmap) {
        boolean isAdvert = dynamicBean.getFeed_from() == DEFAULT_ADVERT_FROM_TAG;
        mOtherDynamicPopWindow = ActionPopupWindow.builder()
                // 广告不处理
                .item3Str(isAdvert ? "" : getString(R.string.report))
                .item2Str(isAdvert ? "" : getString(isCollected ? R.string.dynamic_list_uncollect_dynamic : R
                        .string.dynamic_list_collect_dynamic))
                .item1Str(getString(R.string.dynamic_list_share_dynamic))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item3ClickListener(() -> {                    // 举报帖子
                    if (mPresenter.handleTouristControl()) {
                        return;
                    }

                    String img = "";
                    if (dynamicBean.getImages() != null && !dynamicBean.getImages().isEmpty()) {
                        img = ImageUtils.imagePathConvertV2(dynamicBean.getImages().get(0)
                                        .getFile(), getResources()
                                        .getDimensionPixelOffset(R.dimen.report_resource_img),
                                getResources()
                                        .getDimensionPixelOffset(R.dimen.report_resource_img),
                                100);
                    }
                    ReportResourceBean reportResourceBean = new ReportResourceBean(dynamicBean
                            .getUserInfoBean(), String.valueOf(dynamicBean
                            .getId()),
                            "", img, dynamicBean.getFeed_content(), ReportType.DYNAMIC);
                    reportResourceBean.setDesCanlook(dynamicBean.getPaid_node() == null ||
                            dynamicBean
                                    .getPaid_node().isPaid());
                    ReportActivity.startReportActivity(mActivity, reportResourceBean);
                    mOtherDynamicPopWindow.hide();
                    showBottomView(true);
                })
                .item2ClickListener(() -> {// 收藏
                    if (!TouristConfig.DYNAMIC_CAN_COLLECT && mPresenter.handleTouristControl
                            ()) {
                        return;
                    }
                    mPresenter.handleCollect(dynamicBean);
                    mOtherDynamicPopWindow.hide();
                    showBottomView(true);
                })
                .item1ClickListener(() -> {// 分享
                    mPresenter.shareDynamic(dynamicBean, shareBitmap);
                    mOtherDynamicPopWindow.hide();
                    showBottomView(true);
                })
                .bottomClickListener(() -> {
                    mOtherDynamicPopWindow.hide();
                    showBottomView(true);
                })
                .build();
    }

    /**
     * 初始化我的动态操作弹窗
     *
     * @param dynamicBean curent dynamic
     * @param position    curent dynamic postion
     */
    private void initMyDynamicPopupWindow(final DynamicDetailBeanV2 dynamicBean, final int position,
                                          boolean isCollected, final Bitmap shareBitMap) {

        Long feed_id = dynamicBean.getMaxId();
        boolean feedIdIsNull = feed_id == null || feed_id == 0;

        mMyDynamicPopWindow = ActionPopupWindow.builder()
                .item1Str(getString(feedIdIsNull ? R.string.empty : R.string
                        .dynamic_list_share_dynamic))
                .item2Str(getString(feedIdIsNull ? R.string.empty :
                        (isCollected ? R.string.dynamic_list_uncollect_dynamic : R.string
                                .dynamic_list_collect_dynamic)))
                // 付费评论功能 移除
//                .item3Str(BuildConfig.USE_TOLL ? getString(R.string.dynamic_comment_toll) :
// null)// 付费评论功能 移除
//                .item4Str(BuildConfig.USE_TOLL && !getDynamicType().equals(ApiConfig
// .DYNAMIC_TYPE_FOLLOWS) && !feedIdIsNull ? getString(R.string
// .dynamic_list_top_dynamic) : null)
                .item4Str(BuildConfig.USE_TOLL && !feedIdIsNull && dynamicBean.getTop() != DynamicDetailBeanV2.TOP_SUCCESS ? getString(R.string
                        .dynamic_list_top_dynamic) : null)
                .item5Str(getString(R.string.dynamic_list_delete_dynamic))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(() -> {// 分享
                    mPresenter.shareDynamic(dynamicBean, shareBitMap);
                    mMyDynamicPopWindow.hide();


                })
                .item2ClickListener(() -> {// 收藏
                    mPresenter.handleCollect(dynamicBean);
                    mMyDynamicPopWindow.hide();
                    showBottomView(true);
                })
                .item3ClickListener(() -> {// 申请评论收费
                    Intent intent = new Intent(getActivity(), DynamicCommentTollActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(TOLL_DYNAMIC_COMMENT, dynamicBean);
                    intent.putExtra(TOLL_DYNAMIC_COMMENT, bundle);
                    startActivity(intent);
                    mMyDynamicPopWindow.hide();
                })
                .item4ClickListener(() -> {// 申请置顶
                    StickTopFragment.startSticTopActivity(getContext(), StickTopFragment
                            .TYPE_DYNAMIC, dynamicBean.getId());
                    mMyDynamicPopWindow.hide();
                })
                .item5ClickListener(() -> {// 删除
                    mMyDynamicPopWindow.hide();
                    showDeleteTipPopupWindow(getString(R.string.dynamic_list_delete_dynamic), ()
                            -> {
                        mPresenter.deleteDynamic(dynamicBean, position);
                        showBottomView(true);
                    }, true);

                })
                .bottomClickListener(() -> {//取消
                    mMyDynamicPopWindow.hide();
                    showBottomView(true);
                })
                .build();
    }

    /**
     * 初始化重发动态选择弹框
     */
    private void initReSendDynamicPopupWindow(final int position) {
        mReSendDynamicPopWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.resend))
                .item1Color(ContextCompat.getColor(getContext(), R.color.themeColor))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(() -> {
                    mReSendDynamicPopWindow.hide();
                    mListDatas.get(position).setState(DynamicDetailBeanV2.SEND_ING);
                    refreshData();
                    mPresenter.reSendDynamic(position);
                })
                .bottomClickListener(() -> mReSendDynamicPopWindow.hide())
                .build();
    }

    /**
     * @param dynamicPosition 动态位置
     * @param imagePosition   图片位置
     * @param amout           费用
     * @param note            支付节点
     * @param strRes          文字说明
     * @param isImage         是否是图片收费
     */
    private void initImageCenterPopWindow(final int dynamicPosition, final int imagePosition,
                                          long amout,
                                          final int note, int strRes, final boolean isImage) {

        mPayImagePopWindow = PayPopWindow.builder()
                .with(getActivity())
                .isWrap(true)
                .isFocus(true)
                .isOutsideTouch(true)
                .buildLinksColor1(R.color.themeColor)
                .buildLinksColor2(R.color.important_for_content)
                .contentView(R.layout.ppw_for_center)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .buildDescrStr(String.format(getString(strRes) + getString(R
                        .string.buy_pay_member), amout, mPresenter.getGoldName()))
                .buildLinksStr(getString(R.string.buy_pay_member))
                .buildTitleStr(getString(R.string.buy_pay))
                .buildItem1Str(getString(R.string.buy_pay_in))
                .buildItem2Str(getString(R.string.buy_pay_out))
                .buildMoneyStr(String.format(getString(R.string.buy_pay_integration), amout))
                .buildCenterPopWindowItem1ClickListener(() -> {
                    mPresenter.payNote(dynamicPosition, imagePosition, note, isImage);
                    mPayImagePopWindow.hide();
                })
                .buildCenterPopWindowItem2ClickListener(() -> mPayImagePopWindow.hide())
                .buildCenterPopWindowLinkClickListener(new PayPopWindow
                        .CenterPopWindowLinkClickListener() {
                    @Override
                    public void onLongClick() {

                    }

                    @Override
                    public void onClicked() {

                    }
                })
                .build();
        mPayImagePopWindow.show();

    }

    /**
     * 初始化重发评论选择弹框
     */
    private void initReSendCommentPopupWindow(final DynamicCommentBean commentBean, final long
            feed_id) {
        mReSendCommentPopWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.dynamic_list_resend_comment))
                .item1Color(ContextCompat.getColor(getContext(), R.color.themeColor))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(() -> {
                    mReSendCommentPopWindow.hide();
                    mPresenter.reSendComment(commentBean, feed_id);
                    showBottomView(true);
                })
                .bottomClickListener(() -> {
                    mReSendCommentPopWindow.hide();
                    showBottomView(true);
                })
                .build();
    }

    private void showBottomView(boolean isShow) {
        if (isShow) {
            mVShadow.setVisibility(View.GONE);
            mIlvComment.setVisibility(View.GONE);
            mIlvComment.clearFocus();
            mIlvComment.setSendButtonVisiable(false);
            DeviceUtils.hideSoftKeyboard(getActivity(), mIlvComment.getEtContent());
        } else {
            mVShadow.setVisibility(View.VISIBLE);
            mIlvComment.setVisibility(View.VISIBLE);
            mIlvComment.getFocus();
            mIlvComment.setSendButtonVisiable(true);
            DeviceUtils.showSoftKeyboard(getActivity(), mIlvComment.getEtContent());
        }
        if (mOnCommentClickListener != null) {
            mOnCommentClickListener.onButtonMenuShow(isShow);
        }
    }

    private Bitmap getShareBitmap(int position, int id) {
        Bitmap shareBitMap = null;
        try {
            ImageView imageView = (ImageView) layoutManager.findViewByPosition
                    (position + mHeaderAndFooterWrapper.getHeadersCount()).findViewById(id);
            shareBitMap = ConvertUtils.drawable2BitmapWithWhiteBg(getContext(), imageView
                    .getDrawable(), R.mipmap.icon);
        } catch (Exception e) {
        }
        return shareBitMap;
    }

    private void goDynamicDetail(int position, boolean isLookMoreComment, ViewHolder holder) {
        // 还未发送成功的动态列表不查看详情
        if (mListDatas.get(position).getId() == null || mListDatas.get(position).getId() <= 0) {
            return;
        }

        Intent intent = new Intent(getActivity(), DynamicDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(DYNAMIC_DETAIL_DATA, mListDatas.get(position));
        bundle.putString(DYNAMIC_DETAIL_DATA_TYPE, getDynamicType());
        bundle.putInt(DYNAMIC_DETAIL_DATA_POSITION, position);

        bundle.putBoolean(LOOK_COMMENT_MORE, isLookMoreComment);
        mPresenter.handleViewCount(mListDatas.get(position).getId(), position);

        if (isLookMoreComment) {
            ZhiyiVideoView.releaseAllVideos();
            intent.putExtras(bundle);
            startActivity(intent);
            return;
        }
        ZhiyiVideoView playView = null;
        try {
            playView = holder.getView(R.id.videoplayer);
        } catch (Exception ignore) {

        }

        if (playView != null && JZVideoPlayerManager.getFirstFloor() != null) {
            playView.mVideoFrom = mDynamicType;
            if (playView.currentState == ZhiyiVideoView.CURRENT_STATE_PLAYING) {
                playView.startButton.callOnClick();
            }
            bundle.putInt(DYNAMIC_VIDEO_STATE, playView.currentState);
            playView.textureViewContainer.removeView(JZMediaManager.textureView);
            playView.onStateNormal();
        }

        intent.putExtras(bundle);
        startActivity(intent);

    }

    public interface OnCommentClickListener {
        void onButtonMenuShow(boolean isShow);
    }

    @Override
    public void onDestroyView() {
        dismissPop(mDeletCommentPopWindow);
        dismissPop(mOtherDynamicPopWindow);
        dismissPop(mMyDynamicPopWindow);
        dismissPop(mReSendCommentPopWindow);
        dismissPop(mReSendDynamicPopWindow);
        dismissPop(mPayImagePopWindow);
        super.onDestroyView();

    }

}
