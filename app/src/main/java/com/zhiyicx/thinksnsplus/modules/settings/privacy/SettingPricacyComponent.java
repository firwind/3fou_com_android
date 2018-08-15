package com.zhiyicx.thinksnsplus.modules.settings.privacy;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;
import com.zhiyicx.thinksnsplus.modules.settings.SettingsPresenterModule;

import dagger.Component;

/*
 * 文件名：
 * 创建者：zl
 * 时  间：2018/8/14 0014
 * 描  述：
 * 版  权：九曲互动
 * 
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = SettingPricacyPresenterModule.class)
public interface SettingPricacyComponent extends InjectComponent<SettingPricacyActivity>{

}
