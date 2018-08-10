package com.zhiyicx.thinksnsplus.modules.home.message.homepage;

import android.support.v4.app.Fragment;

import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.SharePreferenceTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.source.local.AllAdvertListBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.beans.HomeMessageIndexBean;
import com.zhiyicx.thinksnsplus.data.source.repository.MarketRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * author: huwenyong
 * date: 2018/8/9 9:59
 * description:
 * version:
 */

public class MessageHomePagePresenter extends AppBasePresenter<MessageHomePageContract.View> implements MessageHomePageContract.Presenter{

    @Inject
    MarketRepository mMarketRepository;
    @Inject
    AllAdvertListBeanGreenDaoImpl mAllAdvertListBeanGreenDao;

    @Inject
    public MessageHomePagePresenter(MessageHomePageContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        mMarketRepository.getHomeMessageIndexData()
                .map(homeMessageIndexBean -> {
                    homeMessageIndexBean.setBanners(mAllAdvertListBeanGreenDao.getInfoBannerAdvert().getMRealAdvertListBeen());
                    //将首页数据缓存到本地
                    SharePreferenceUtils.saveObject( ((Fragment)mRootView).getContext(),
                            SharePreferenceTagConfig.SHAREPREFERENCE_TAG_HOME_INDEX_CACHE,homeMessageIndexBean);
                    return homeMessageIndexBean;
                })
                .subscribe(new BaseSubscribeForV2<HomeMessageIndexBean>() {
                    @Override
                    protected void onSuccess(HomeMessageIndexBean data) {
                        mRootView.onNetResponseSuccess(data.getNews(),isLoadMore);
                        mRootView.setHeaderData(data);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.onResponseError(null,isLoadMore);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.onResponseError(throwable,isLoadMore);
                    }
                });
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        HomeMessageIndexBean indexBean = SharePreferenceUtils.getObject( ((Fragment)mRootView).getContext(),
                SharePreferenceTagConfig.SHAREPREFERENCE_TAG_HOME_INDEX_CACHE);
        if(null != indexBean){
            mRootView.onCacheResponseSuccess(indexBean.getNews(),isLoadMore);
            mRootView.setHeaderData(indexBean);
        }
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<InfoListDataBean> data, boolean isLoadMore) {
        return false;
    }
}
