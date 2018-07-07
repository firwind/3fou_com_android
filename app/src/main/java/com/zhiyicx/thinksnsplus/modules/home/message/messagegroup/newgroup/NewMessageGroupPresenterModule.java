package com.zhiyicx.thinksnsplus.modules.home.message.messagegroup.newgroup;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2018/05/03/15:48
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class NewMessageGroupPresenterModule {

    NewMessageGroupContract.View mView;

    public NewMessageGroupPresenterModule(NewMessageGroupContract.View view) {
        mView = view;
    }

    @Provides
    public NewMessageGroupContract.View providesMessageGroupContractView(){
        return mView;
    }
}
