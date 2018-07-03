package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.notice.noticedetails;

import dagger.Module;
import dagger.Provides;

/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/7/3
 * 描  述：
 * 版  权: 九曲互动
 */
@Module
public class NoticeDetailsPresenterModule {
    NoticeDetailsContract.View mView;
    public NoticeDetailsPresenterModule(NoticeDetailsContract.View view){
        mView = view;
    }
    @Provides
    NoticeDetailsContract.View provideNoticeDetailsContractView() {
        return mView;
    }
}
