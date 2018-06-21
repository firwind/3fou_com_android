package com.zhiyicx.thinksnsplus.modules.home.message.messagegroup;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2018/05/03/15:48
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class MessageGroupPresenterModule {

    MessageGroupContract.View mView;

    public MessageGroupPresenterModule(MessageGroupContract.View view) {
        mView = view;
    }

    @Provides
    public MessageGroupContract.View providesMessageGroupContractView(){
        return mView;
    }
}
