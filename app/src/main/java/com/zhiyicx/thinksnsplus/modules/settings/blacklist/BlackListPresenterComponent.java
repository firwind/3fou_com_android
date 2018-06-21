package com.zhiyicx.thinksnsplus.modules.settings.blacklist;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Describe
 * @Author zl
 * @Date 2018/4/17
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = BlackListPresenterModule.class)
public interface BlackListPresenterComponent extends InjectComponent<BlackListFragment> {
}
