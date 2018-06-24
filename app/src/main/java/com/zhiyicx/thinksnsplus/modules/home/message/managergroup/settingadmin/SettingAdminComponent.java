package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.settingadmin;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;
import com.zhiyicx.thinksnsplus.modules.home.message.managergroup.notice.NoticeManagerActivity;

import dagger.Component;

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
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = SettingAdminPresenterModule.class)
public interface SettingAdminComponent extends InjectComponent<SettingAdminActivity> {
}
