package com.zhiyicx.thinksnsplus.modules.home.find.market.list;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.source.repository.MarketRepository;
import com.zhiyicx.thinksnsplus.modules.home.find.market.MarketContract;

import org.jetbrains.annotations.NotNull;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

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

    private Subscription mSubscription;

    @Inject
    public MarketListPresenter(MarketContract.MarketListView rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

        if(null != mSubscription && !mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();

        Observable observable = mRootView.isRankMarket()?mMarketRepository.getMarketCurrencyRankList():
                mMarketRepository.getMarketList(mRootView.getCurrency().currency_type,
                        String.valueOf(mRootView.getCurrency().type));

        mSubscription = observable.subscribe(
                new BaseSubscribeForV2<List>() {
                    @Override
                    protected void onSuccess(List data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.onResponseError(throwable,isLoadMore);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.onResponseError(e,isLoadMore);
                    }
                });

    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<BaseListBean> data, boolean isLoadMore) {
        return false;
    }
}
