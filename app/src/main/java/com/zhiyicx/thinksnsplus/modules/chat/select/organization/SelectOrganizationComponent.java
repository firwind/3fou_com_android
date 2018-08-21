package com.zhiyicx.thinksnsplus.modules.chat.select.organization;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;
import com.zhiyicx.thinksnsplus.modules.chat.select.SelectFriendsPresenterModule;

import dagger.Component;

/*
 * 文件名：
 * 创建者：zl
 * 时  间：2018/8/20 0020
 * 描  述：
 * 版  权：九曲互动
 * 
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = SelectOrganizationPresenterModule.class)
public interface SelectOrganizationComponent extends InjectComponent<SelectOrganizationActivity> {

}
