package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.settingadmin;

import dagger.Module;
import dagger.Provides;

/*
 * 文件名：
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/23 11:17
 * 修改者：
 * 修改备注：
 * 修改时间：
 * 版  权：互动科技
 */
@Module
public class SettingAdminPresenterModule {
    private final SettingAdminContract.View mView;
    public SettingAdminPresenterModule(SettingAdminContract.View view) {
        mView = view;
    }

    @Provides
    SettingAdminContract.View provideNoticeManagerContractView() {
        return mView;
    }
}
