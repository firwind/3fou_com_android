package com.zhiyicx.thinksnsplus.modules.information.infomain.smallvideo;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.source.local.AllAdvertListBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.InfoListDataBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseDynamicRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseInfoRepository;
import com.zhiyicx.thinksnsplus.modules.information.infomain.InfoMainContract;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * author: huwenyong
 * date: 2018/8/27 10:02
 * description:
 * version:
 */
@FragmentScoped
public class SmallVideoListPresenter extends AppBasePresenter<InfoMainContract.SmallVideoListView> implements InfoMainContract.SmallVideoListPresenter {

    InfoListDataBeanGreenDaoImpl mInfoListDataBeanGreenDao;

    BaseInfoRepository mBaseInfoRepository;

    @Inject
    BaseDynamicRepository mDynamicRepository;

    @Inject
    public SmallVideoListPresenter(InfoMainContract.SmallVideoListView smallVideoListView
            , InfoListDataBeanGreenDaoImpl infoListDataBeanGreenDao
            , AllAdvertListBeanGreenDaoImpl allAdvertListBeanGreenDao
            , BaseInfoRepository baseInfoRepository) {
        super(smallVideoListView);
        mInfoListDataBeanGreenDao = infoListDataBeanGreenDao;
        mBaseInfoRepository = baseInfoRepository;
    }

    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore) {

        mDynamicRepository.getSmallVideoList(maxId)
                .subscribe(new BaseSubscribeForV2<DynamicBeanV2>() {
                    @Override
                    protected void onSuccess(DynamicBeanV2 data) {
                        mRootView.onNetResponseSuccess(data.getFeeds(),isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.showMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.onResponseError(throwable, isLoadMore);
                    }

                });

    }

    @Override
    public void requestCacheData(Long maxId, final boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(null,isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<DynamicDetailBeanV2> data, boolean isLoadMore) {
        return false;
    }


    @Override
    protected boolean useEventBus() {
        return true;
    }

}
