package com.zhiyicx.thinksnsplus.modules.chat.select.addgroup;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;
import com.zhiyicx.thinksnsplus.modules.home.message.messagegroup.MessageGroupPresenterModule;

import dagger.Component;

/*
 * 文件名：
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/25 17:14
 * 修改者：
 * 修改备注：
 * 修改时间：
 * 版  权：互动科技
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = AddGroupPresenterModule.class)
public interface AddGroupComponent extends InjectComponent<AddGroupActivity> {
}
