package com.zhiyicx.thinksnsplus.modules.settings.password.pay_password;

import dagger.Module;
import dagger.Provides;

/**
 * author: huwenyong
 * date: 2018/7/30 18:18
 * description:
 * version:
 */
@Module
public class PayPasswordModule {

    private PayPasswordContract.View mView;

    public PayPasswordModule(PayPasswordContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public PayPasswordContract.View providePayPasswordContractView(){
        return mView;
    }

}
