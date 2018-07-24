package com.zhiyicx.thinksnsplus.modules.currency.accountbook;

import dagger.Module;
import dagger.Provides;

/**
 * author: huwenyong
 * date: 2018/7/23 9:36
 * description:
 * version:
 */

@Module
public class AccountBookChildMoudle {

    private AccountBookChildContract.View mView;

    public AccountBookChildMoudle(AccountBookChildContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public AccountBookChildContract.View provideAccountBookChildContractView(){
        return mView;
    }

}
