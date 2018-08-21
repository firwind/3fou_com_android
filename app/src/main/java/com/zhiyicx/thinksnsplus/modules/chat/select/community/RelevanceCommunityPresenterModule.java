package com.zhiyicx.thinksnsplus.modules.chat.select.community;

import dagger.Module;
import dagger.Provides;

/*
 * 文件名：
 * 创建者：zl
 * 时  间：2018/8/21 0021
 * 描  述：
 * 版  权：九曲互动
 * 
 */
@Module
public class RelevanceCommunityPresenterModule {
    private RelevanceCommunityContract.View mView;

    public RelevanceCommunityPresenterModule(RelevanceCommunityContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public RelevanceCommunityContract.View provideRelevanceCommunityContractView() {
        return mView;
    }
}
