package com.zhiyicx.thinksnsplus.modules.information.infomain.flash;
/*
 * 文件名：
 * 创建者：zl
 * 时  间：2018/8/9 0009
 * 描  述：
 * 版  权：九曲互动
 * 
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.TouristConfig;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.modules.information.adapter.FlashListItem;
import com.zhiyicx.thinksnsplus.modules.information.adapter.InfoBannerHeader;
import com.zhiyicx.thinksnsplus.modules.information.flashdetails.FlashDetailsActivity;
import com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsActivity;
import com.zhiyicx.thinksnsplus.modules.information.infomain.InfoMainContract;
import com.zhiyicx.thinksnsplus.modules.information.infomain.list.InfoListPresenterModule;
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.CustomWEBActivity;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsFragment.BUNDLE_INFO;
import static com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsFragment.BUNDLE_INFO_TYPE;
import static com.zhiyicx.thinksnsplus.modules.information.infomain.container.InfoContainerFragment.RECOMMEND_INFO;

public class FlashListFragment extends TSListFragment<InfoMainContract.FlashListPresenter,
        BaseListBean> implements InfoMainContract.FlashListView, InfoBannerHeader.InfoBannerHeadlerClickEvent {

    /**
     * 推荐分类
     */
    private String mInfoType = RECOMMEND_INFO;

    private List<RealAdvertListBean> mListAdvert;

    private InfoBannerHeader mInfoBannerHeader;

    @Inject
    FlashListPresenter mInfoListPresenter;
    private MultiItemTypeAdapter adapter;

    public static FlashListFragment newInstance(String params) {
        FlashListFragment fragment = new FlashListFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_INFO_TYPE, params);
        fragment.setArguments(args);
        return fragment;
    }

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
    public void onNetResponseSuccess(@NotNull List<BaseListBean> data, boolean isLoadMore) {

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

        super.onCacheResponseSuccess(data, isLoadMore);
    }
    int mPosition;
    @Override
    protected MultiItemTypeAdapter getAdapter() {
        adapter = new MultiItemTypeAdapter(getActivity(), mListDatas);
        adapter.addItemViewDelegate(new FlashListItem(false,getContext()) {
            @Override
            public void itemClick(int position, TextView textView, TextView title, InfoListDataBean realData) {
                if (TouristConfig.INFO_DETAIL_CAN_LOOK || !mPresenter.handleTouristControl()) {
                    if (!AppApplication.sOverRead.contains(realData.getId())) {
                        AppApplication.sOverRead.add(realData.getId().intValue());
                    }
                    title.setTextColor(getResources()
                            .getColor(R.color.normal_for_assist_text));
                    FlashDetailsActivity.startActivity(getContext(),realData);
                }
            }

            @Override
            public void bullClick(int position, InfoListDataBean realData) {
                mPresenter.commitBull(realData);
                mPosition = position;
            }

            @Override
            public void bearNewsClick(int position, InfoListDataBean realData) {
                mPresenter.commitBearNews(realData);
                mPosition = position;
            }

        });

        return adapter;
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
    public void commitResult(InfoListDataBean bean) {
        mListDatas.set(mPosition,bean);
        refreshData();
    }

    @Override
    public void headClick(String link, String title) {
        CustomWEBActivity.startToWEBActivity(getActivity(), link, title);
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

    @Override
    protected float getItemDecorationSpacing() {
        return 0;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mInfoType = getArguments().getString(BUNDLE_INFO_TYPE, RECOMMEND_INFO);
        Observable.create(subscriber -> {
            DaggerFlashListFComponent.builder()
                    .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                    .flashListPresenterModule(new FlashListPresenterModule(FlashListFragment.this))
                    .build()
                    .inject(FlashListFragment.this);
            subscriber.onCompleted();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new rx.Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        initData();
//                        initAdvert();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });
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

    public void setInfoType(String infoType) {
        mInfoType = infoType;
    }
}
