package com.zhiyicx.thinksnsplus.modules.wallet.integration.mine.newIntegration;

import dagger.Module;
import dagger.Provides;

/**
 * author: huwenyong
 * date: 2018/8/3 19:07
 * description:
 * version:
 */
@Module
public class NewMineIntegrationPresenterMoudle {

    private NewMineIntegrationContract.View mView;

    public NewMineIntegrationPresenterMoudle(NewMineIntegrationContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public NewMineIntegrationContract.View provideNewMineIntegrationContractView(){
        return mView;
    }

}
