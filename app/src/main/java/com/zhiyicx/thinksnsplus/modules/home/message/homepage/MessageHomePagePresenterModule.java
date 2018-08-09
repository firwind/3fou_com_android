package com.zhiyicx.thinksnsplus.modules.home.message.homepage;

import dagger.Module;
import dagger.Provides;

/**
 * author: huwenyong
 * date: 2018/8/9 10:00
 * description:
 * version:
 */
@Module
public class MessageHomePagePresenterModule {

    private MessageHomePageContract.View mView;

    public MessageHomePagePresenterModule(MessageHomePageContract.View mView) {
        this.mView = mView;
    }


    @Provides
    public MessageHomePageContract.View provideMessageHomePageContractView(){
        return mView;
    }

}
