package com.zhiyicx.thinksnsplus.modules.information.infomain.list;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tym.shortvideo.view.AutoPlayScrollListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.TouristConfig;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.DynamicListAdvert;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.i.IntentKey;
import com.zhiyicx.thinksnsplus.modules.home.HomeActivity;
import com.zhiyicx.thinksnsplus.modules.information.adapter.InfoBannerHeader;
import com.zhiyicx.thinksnsplus.modules.information.adapter.InfoListItem;
import com.zhiyicx.thinksnsplus.modules.information.adapter.VideoListItem;
import com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsActivity;
import com.zhiyicx.thinksnsplus.modules.information.infomain.InfoMainContract;
import com.zhiyicx.thinksnsplus.modules.information.videoinfodetails.VideoInfoDetailsActivity;
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.CustomWEBActivity;
import com.zhiyicx.thinksnsplus.modules.shortvideo.helper.ZhiyiVideoView;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cn.jzvd.JZMediaManager;
import cn.jzvd.JZVideoPlayer;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsFragment.BUNDLE_INFO;
import static com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsFragment.BUNDLE_INFO_TYPE;
import static com.zhiyicx.thinksnsplus.modules.information.infomain.container.InfoContainerFragment.RECOMMEND_INFO;
import static com.zhiyicx.thinksnsplus.modules.information.infomain.container.InfoContainerFragment.VIDEO_INFO_ID;

/**
 * @Author Jliuer
 * @Date 2017/03/03
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoListFragment extends TSListFragment<InfoMainContract.InfoListPresenter,
        BaseListBean> implements InfoMainContract.InfoListView, InfoBannerHeader.InfoBannerHeadlerClickEvent, ZhiyiVideoView.ShareInterface {

    /**
     * 推荐分类
     */
    private String mInfoType = RECOMMEND_INFO;

    private List<RealAdvertListBean> mListAdvert;

    private InfoBannerHeader mInfoBannerHeader;

    public static InfoListFragment newInstance(String params) {
        InfoListFragment fragment = new InfoListFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_INFO_TYPE, params);
        fragment.setArguments(args);
        return fragment;
    }

    @Inject
    InfoListPresenter mInfoListPresenter;

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected boolean needMusicWindowView() {
        return false;
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getActivity(), HomeActivity.class));
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<BaseListBean> data, boolean isLoadMore) {
        try {// 添加广告
            if (!data.isEmpty() && mListAdvert != null && mListAdvert.size() >= getPage()) {
                RealAdvertListBean realAdvertListBean = mListAdvert.get(getPage() - 1);
                DynamicListAdvert advert = realAdvertListBean.getAdvertFormat().getAnalog();
                long maxId = data.get(data.size() - 1).getMaxId();
                data.add(DynamicListAdvert.advert2Info(advert, maxId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onNetResponseSuccess(data, isLoadMore);
        if (mInfoBannerHeader == null) {
            return;
        }
        if (!isLoadMore && data.isEmpty()) {
            mInfoBannerHeader.getInfoBannerHeader().setVisibility(View.GONE);
        } else {
            mInfoBannerHeader.getInfoBannerHeader().setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCacheResponseSuccess(List<BaseListBean> data, boolean isLoadMore) {
        try {// 添加广告
            if (!data.isEmpty()) {
                RealAdvertListBean realAdvertListBean = mListAdvert.get(getPage() - 1);
                DynamicListAdvert advert = realAdvertListBean.getAdvertFormat().getAnalog();
                long max_id = data.get(data.size() - 1).getMaxId();
                data.add(DynamicListAdvert.advert2Info(advert, max_id));
            }
        } catch (Exception e) {
        }
        super.onCacheResponseSuccess(data, isLoadMore);
    }

    @Override
    protected MultiItemTypeAdapter getAdapter() {

        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter(getActivity(), mListDatas);
        adapter.addItemViewDelegate(isVideoInfo() ? getVideoInfoListDelegate() : getNormalInfoListDelegate());

        return adapter;
    }


    @Override
    public void onPause() {
        super.onPause();

        if (isVideoInfo()) {//暂停播放
            JZVideoPlayer.goOnPlayOnPause();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser && isVideoInfo()) {//暂停播放
            JZVideoPlayer.goOnPlayOnPause();
        }
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mInfoType = getArguments().getString(BUNDLE_INFO_TYPE, RECOMMEND_INFO);

        Observable.create(subscriber -> {
            DaggerInfoListComponent.builder()
                    .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                    .infoListPresenterModule(new InfoListPresenterModule(InfoListFragment.this))
                    .build()
                    .inject(InfoListFragment.this);
            subscriber.onCompleted();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new rx.Subscriber<Object>() {
                    @Override
                    public void onCompleted() {

                        if (null != mVideoListItem)
                            mVideoListItem.setPresenter(mInfoListPresenter);

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

        if(isVideoInfo()){
            mRvList.addOnScrollListener(new AutoPlayScrollListener() {
                @Override
                public int getPlayerViewId() {
                    return R.id.videoplayer;
                }

                @Override
                public boolean canAutoPlay() {
                    return false;
                }
            });
        }


    }

    @Override
    protected void initData() {
        if (mPresenter != null) {
            super.initData();
        }
    }

    private void initAdvert() {
        if (!com.zhiyicx.common.BuildConfig.USE_ADVERT) {
            return;
        }
        // 只有推荐才加载广告
        if (!mInfoType.equals(RECOMMEND_INFO)) {
            return;
        }
        List<RealAdvertListBean> advertList = mPresenter.getBannerAdvert();
        mListAdvert = mPresenter.getListAdvert();

        if (advertList == null || advertList.isEmpty()) {
            return;
        }

        List<String> advertTitle = new ArrayList<>();
        List<String> advertUrls = new ArrayList<>();
        List<String> advertLinks = new ArrayList<>();

        for (RealAdvertListBean advert : advertList) {
            advertTitle.add(advert.getTitle());
            advertUrls.add(advert.getAdvertFormat().getImage().getImage());
            advertLinks.add(advert.getAdvertFormat().getImage().getLink());
            if (advert.getType().equals("html")) {
                showStickyHtmlMessage((String) advert.getData());
            }
        }

        mInfoBannerHeader = new InfoBannerHeader(getActivity());
        mInfoBannerHeader.setHeadlerClickEvent(this);
        InfoBannerHeader.InfoBannerHeaderInfo headerInfo = mInfoBannerHeader.new
                InfoBannerHeaderInfo();
        headerInfo.setTitles(advertTitle);
        headerInfo.setLinks(advertLinks);
        headerInfo.setUrls(advertUrls);
        headerInfo.setDelay(4000);
        headerInfo.setOnBannerListener(position -> {

        });
        mInfoBannerHeader.setHeadInfo(headerInfo);
        mHeaderAndFooterWrapper.addHeaderView(mInfoBannerHeader.getInfoBannerHeader());
    }

    @Override
    public void headClick(String link, String title) {
        CustomWEBActivity.startToWEBActivity(getActivity(), link, title);
    }

    @Override
    public String getInfoType() {
        return mInfoType;
    }

    @Override
    public int isRecommend() {
        return mInfoType.equals(RECOMMEND_INFO) ? 1 : 0;
    }

    @Override
    public boolean isVideoInfo() {

        return getArguments().getString(BUNDLE_INFO_TYPE).equals(VIDEO_INFO_ID);//系统定死默认8为video
    }

    @Override
    public void showMessage(String message) {
        showMessageNotSticky(message);
    }

    @Override
    protected boolean setUseSatusbar() {
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

    @Subscriber(tag = EventBusTagConfig.EVENT_SEND_INFO_LIST_COLLECT)
    public void handleCollectInfo(InfoListDataBean info) {
        LogUtils.d("handleCollectInfo");
//        onCacheResponseSuccess(requestCacheData(mMaxId, false), false);
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_SEND_INFO_LIST_DELETE_UPDATE)
    public void handleDeleteInfo(InfoListDataBean info) {
        LogUtils.d("handleDeleteInfo");
        mListDatas.remove(mListDatas.indexOf(info));
        refreshData();
    }


    /**
     * 获取普通资讯列表的delegate
     *
     * @return
     */
    private ItemViewDelegate getNormalInfoListDelegate() {
        return new InfoListItem(false) {
            @Override
            public void itemClick(int position, ImageView imageView, TextView title, InfoListDataBean realData) {
                if (TouristConfig.INFO_DETAIL_CAN_LOOK || !mPresenter.handleTouristControl()) {
                    if (!AppApplication.sOverRead.contains(realData.getId())) {
                        AppApplication.sOverRead.add(realData.getId().intValue());
                    }
                    FileUtils.saveBitmapToFile(getActivity(), ConvertUtils.drawable2BitmapWithWhiteBg(getContext()
                            , imageView.getDrawable(), R.mipmap.icon), "info_share.jpg");
                    title.setTextColor(getResources()
                            .getColor(R.color.normal_for_assist_text));
                    Intent intent = new Intent(getActivity(), InfoDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(BUNDLE_INFO, realData);
                    bundle.putString(BUNDLE_INFO_TYPE, mInfoType);
                    intent.putExtra(BUNDLE_INFO, bundle);
                    startActivity(intent);
                }
            }
        };
    }


    private VideoListItem mVideoListItem = null;

    /**
     * 获取视频资讯列表的delegate
     *
     * @return
     */
    private ItemViewDelegate getVideoInfoListDelegate() {
        if (null == mVideoListItem) {
            mVideoListItem = new VideoListItem(mActivity) {
                @Override
                public void itemClick(int position, ViewHolder holder, InfoListDataBean realData) {
                    ZhiyiVideoView playView = (ZhiyiVideoView) holder.getView(R.id.videoplayer);
                    if (playView.currentState == ZhiyiVideoView.CURRENT_STATE_PLAYING) {
                        playView.startButton.callOnClick();
                    }
                    int videoState = playView.currentState;

                    playView.textureViewContainer.removeView(JZMediaManager.textureView);
                    playView.onStateNormal();

                    VideoInfoDetailsActivity.startVideoInfoDetailsActivity(mActivity, realData, videoState);
                }
            };
            mVideoListItem.setShareInterface(this);
        }
        return mVideoListItem;
    }


    @Override
    public void share(int position) {
        mPresenter.shareVideo((InfoListDataBean) mListDatas.get(position));

    }

    @Override
    public void shareWihtType(int position, SHARE_MEDIA type) {
        mPresenter.shareVideo((InfoListDataBean) mListDatas.get(position),type);
    }
}
