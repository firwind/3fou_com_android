package com.zhiyicx.thinksnsplus.modules.currency.withdraw;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;

import javax.inject.Inject;

/**
 * author: huwenyong
 * date: 2018/7/18 13:51
 * description:
 * version:
 */

public class WithdrawCurrencyPresenter extends AppBasePresenter<WithdrawCurrencyContract.View> implements WithdrawCurrencyContract.Presenter{
    @Inject
    public WithdrawCurrencyPresenter(WithdrawCurrencyContract.View rootView) {
        super(rootView);
    }
}
