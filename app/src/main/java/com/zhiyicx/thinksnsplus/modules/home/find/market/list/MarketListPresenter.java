package com.zhiyicx.thinksnsplus.modules.home.find.market.list;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.StockCertificateBean;
import com.zhiyicx.thinksnsplus.data.source.local.CurrencyRankBean;
import com.zhiyicx.thinksnsplus.data.source.repository.MarketRepository;
import com.zhiyicx.thinksnsplus.modules.home.find.market.MarketContract;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * author: huwenyong
 * date: 2018/6/28 19:31
 * description:
 * version:
 */
@FragmentScoped
public class MarketListPresenter extends AppBasePresenter<MarketContract.MarketListView> implements MarketContract.MarektListPresenter {

    @Inject
    public MarketRepository mMarketRepository;

    @Inject
    public MarketListPresenter(MarketContract.MarketListView rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

        if (mRootView.isRankMarket()) {

            addSubscrebe(mMarketRepository.getMarketCurrencyRankList().subscribe(
                    new BaseSubscribeForV2<Object>() {
                        @Override
                        protected void onSuccess(Object data) {
                            mRootView.onNetResponseSuccess((List<BaseListBean>) data, isLoadMore);
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            mRootView.onResponseError(e,isLoadMore);
                        }
                    }));

        }

    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<BaseListBean> data, boolean isLoadMore) {
        return false;
    }
}
