package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.maintain.upgradepay;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/7/9
 * 描  述：
 * 版  权: 九曲互动
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = UpgradePayPresenterModule.class)
public interface UpgradePayComponent extends InjectComponent<UpgradePayActivity>{

}
