package com.zhiyicx.thinksnsplus.modules.currency.recharge;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;

import javax.inject.Inject;

/**
 * author: huwenyong
 * date: 2018/7/18 10:17
 * description:
 * version:
 */

public class RechargeCurrencyPresenter extends AppBasePresenter<RechargeCurrencyContract.View> implements RechargeCurrencyContract.Presenter{
    @Inject
    public RechargeCurrencyPresenter(RechargeCurrencyContract.View rootView) {
        super(rootView);
    }
}
