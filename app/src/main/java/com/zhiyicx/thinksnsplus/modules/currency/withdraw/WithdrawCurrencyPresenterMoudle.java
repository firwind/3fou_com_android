package com.zhiyicx.thinksnsplus.modules.currency.withdraw;

import dagger.Module;
import dagger.Provides;

/**
 * author: huwenyong
 * date: 2018/7/18 13:55
 * description:
 * version:
 */

@Module
public class WithdrawCurrencyPresenterMoudle {

    private WithdrawCurrencyContract.View mView;

    public WithdrawCurrencyPresenterMoudle(WithdrawCurrencyContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public WithdrawCurrencyContract.View provideWithdrawCurrencyContractView(){
        return mView;
    }

}
