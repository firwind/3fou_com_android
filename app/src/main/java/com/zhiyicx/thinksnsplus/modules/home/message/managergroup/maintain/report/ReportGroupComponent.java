package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.maintain.report;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;

import dagger.Component;

/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/6/28
 * 描  述：
 * 版  权: 九曲互动
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = ReportGroupPresenterModule.class)
public interface ReportGroupComponent {

}
