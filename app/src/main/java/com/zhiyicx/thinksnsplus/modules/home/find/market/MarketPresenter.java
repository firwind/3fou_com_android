package com.zhiyicx.thinksnsplus.modules.home.find.market;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.source.repository.MarketRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * author: huwenyong
 * date: 2018/7/2 9:46
 * description:
 * version:
 */
@FragmentScoped
public class MarketPresenter extends AppBasePresenter<MarketContract.MarketView> implements MarketContract.MarketPresenter{

    @Inject
    MarketRepository mMarketRepository;

    @Inject
    public MarketPresenter(MarketContract.MarketView rootView) {
        super(rootView);
    }

    @Override
    public void getMarketCurrencyList() {
        addSubscrebe(mMarketRepository.getMarketCurrencyList()
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<List<String>>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<List<String>> data) {
                        mRootView.getCurrencyListSuccess(data.getData());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.showSnackErrorMessage(e.getMessage());
                    }
                }));
    }
}
