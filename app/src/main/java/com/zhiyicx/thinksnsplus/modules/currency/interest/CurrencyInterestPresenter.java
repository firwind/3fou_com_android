package com.zhiyicx.thinksnsplus.modules.currency.interest;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyBalanceBean;
import com.zhiyicx.thinksnsplus.data.source.repository.CurrencyRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * author: huwenyong
 * date: 2018/7/31 15:49
 * description:
 * version:
 */

public class CurrencyInterestPresenter extends AppBasePresenter<CurrencyInterestContract.View>
        implements CurrencyInterestContract.Presenter{

    @Inject
    CurrencyRepository mCurrencyRepository;

    @Inject
    public CurrencyInterestPresenter(CurrencyInterestContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        addSubscrebe(mCurrencyRepository.getMyCurrencyList().subscribe(new BaseSubscribeForV2<List<CurrencyBalanceBean>>() {
            @Override
            protected void onSuccess(List<CurrencyBalanceBean> list) {
                mRootView.onNetResponseSuccess(list, isLoadMore);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mRootView.onResponseError(e, isLoadMore);
            }

        }));
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<CurrencyBalanceBean> data, boolean isLoadMore) {
        return false;
    }
}
