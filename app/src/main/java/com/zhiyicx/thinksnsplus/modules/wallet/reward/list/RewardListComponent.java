package com.zhiyicx.thinksnsplus.modules.wallet.reward.list;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Describe
 * @Author zl
 * @Date 2017/4/10
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = RewardListPresenterModule.class)
public interface RewardListComponent extends InjectComponent<RewardListActivity> {
}
