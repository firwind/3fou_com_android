package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.notice;

import dagger.Module;
import dagger.Provides;

/*
 * 文件名：
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/19 14:35
 * 修改者：
 * 修改备注：
 * 修改时间：
 * 版  权：互动科技
 */
@Module
public class NoticeManagerPresenterModule {
    private final NoticeManagerContract.View mView;

    public NoticeManagerPresenterModule(NoticeManagerContract.View view) {
        mView = view;
    }

    @Provides
    NoticeManagerContract.View provideNoticeManagerContractView() {
        return mView;
    }
}
