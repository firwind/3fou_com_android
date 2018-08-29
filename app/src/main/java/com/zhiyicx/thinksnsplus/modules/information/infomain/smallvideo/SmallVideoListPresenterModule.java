package com.zhiyicx.thinksnsplus.modules.information.infomain.smallvideo;

import com.zhiyicx.thinksnsplus.modules.information.infomain.InfoMainContract;

import dagger.Module;
import dagger.Provides;

/**
 * author: huwenyong
 * date: 2018/8/27 10:02
 * description:
 * version:
 */
@Module
public class SmallVideoListPresenterModule {

    InfoMainContract.SmallVideoListView mVideoListView;

    public SmallVideoListPresenterModule(InfoMainContract.SmallVideoListView mVideoListView) {
        this.mVideoListView = mVideoListView;
    }

    @Provides
    InfoMainContract.SmallVideoListView provideSmallVideoListView() {
        return mVideoListView;
    }


}
