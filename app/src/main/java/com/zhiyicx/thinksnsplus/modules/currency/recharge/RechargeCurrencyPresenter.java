package com.zhiyicx.thinksnsplus.modules.currency.recharge;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.source.repository.CurrencyRepository;

import javax.inject.Inject;

/**
 * author: huwenyong
 * date: 2018/7/18 10:17
 * description:
 * version:
 */

public class RechargeCurrencyPresenter extends AppBasePresenter<RechargeCurrencyContract.View>
        implements RechargeCurrencyContract.Presenter{

    @Inject
    CurrencyRepository mCurrencyRepository;

    @Inject
    public RechargeCurrencyPresenter(RechargeCurrencyContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestCurrencyAddress() {
        mCurrencyRepository.rechargeCurrencyAddress(mRootView.getCurrency())
                .subscribe(new BaseSubscribeForV2<BaseJson<String>>() {
                    @Override
                    protected void onSuccess(BaseJson<String> data) {
                        mRootView.setCurrencyAddress(data.getData());
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.setCurrencyAddress(null);
                    }

                });
    }
}
