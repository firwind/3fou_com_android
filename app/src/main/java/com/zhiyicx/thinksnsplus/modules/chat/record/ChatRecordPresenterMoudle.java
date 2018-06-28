package com.zhiyicx.thinksnsplus.modules.chat.record;

import dagger.Module;
import dagger.Provides;

/**
 * author: huwenyong
 * date: 2018/6/28 11:15
 * description:
 * version:
 */
@Module
public class ChatRecordPresenterMoudle {

    private ChatRecordContract.View mView;

    public ChatRecordPresenterMoudle(ChatRecordContract.View mView) {
        this.mView = mView;
    }

    @Provides
    ChatRecordContract.View provideChatRecordContractView(){
        return mView;
    }

}
