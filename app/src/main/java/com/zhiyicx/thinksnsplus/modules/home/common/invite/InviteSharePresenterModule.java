package com.zhiyicx.thinksnsplus.modules.home.common.invite;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 *     @author : huwenyong
 *     @date : 2018/6/24 9:41
 *     desc :
 *     version : 1.0
 * <pre>
 */
@Module
public class InviteSharePresenterModule {

    private InviteShareContract.View mView;

    public InviteSharePresenterModule(InviteShareContract.View mView) {
        this.mView = mView;
    }

    @Provides
    InviteShareContract.View provideInviteShareContractView(){
        return mView;
    }
}
