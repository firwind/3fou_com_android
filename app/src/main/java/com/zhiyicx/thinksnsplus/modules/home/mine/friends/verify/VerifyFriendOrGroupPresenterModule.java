package com.zhiyicx.thinksnsplus.modules.home.mine.friends.verify;

import dagger.Module;
import dagger.Provides;

/*
 * 文件名：
 * 创建者：zl
 * 时  间：2018/8/16 0016
 * 描  述：
 * 版  权：九曲互动
 * 
 */
@Module
public class VerifyFriendOrGroupPresenterModule {
    private final VerifyFriendOrGroupContract.View mView;

    public VerifyFriendOrGroupPresenterModule(VerifyFriendOrGroupContract.View view) {
        this.mView = view;
    }

    @Provides
    VerifyFriendOrGroupContract.View provideVerifyFriendsContractView() {
        return mView;
    }
}
