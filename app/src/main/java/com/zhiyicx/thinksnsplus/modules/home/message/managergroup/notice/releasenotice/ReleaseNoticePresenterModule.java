package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.notice.releasenotice;

import com.zhiyicx.thinksnsplus.modules.feedback.FeedBackContract;

import dagger.Module;
import dagger.Provides;

/*
 * 文件名：
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/20 12:01
 * 修改者：
 * 修改备注：
 * 修改时间：
 * 版  权：互动科技
 */
@Module
public class ReleaseNoticePresenterModule {
    ReleaseNoticeContract.View mView;

    public ReleaseNoticePresenterModule(ReleaseNoticeContract.View view) {
        mView = view;
    }

    @Provides
    ReleaseNoticeContract.View provideReleaseNoticeContractView() {
        return mView;
    }
}
