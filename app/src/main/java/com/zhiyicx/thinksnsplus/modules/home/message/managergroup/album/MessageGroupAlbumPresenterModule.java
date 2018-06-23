package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.album;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 *     @author : huwenyong
 *     @date : 2018/6/22 17:52
 *     desc :
 *     version : 1.0
 * <pre>
 */
@Module
public class MessageGroupAlbumPresenterModule {

    private final MessageGroupAlbumContract.View mView;


    public MessageGroupAlbumPresenterModule(MessageGroupAlbumContract.View mView) {
        this.mView = mView;
    }

    @Provides
    MessageGroupAlbumContract.View provideMessageGroupAlbumContractView(){
        return mView;
    }

}
