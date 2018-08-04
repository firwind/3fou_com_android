package com.zhiyicx.thinksnsplus.modules.wallet.integration.mine.newIntegration;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * author: huwenyong
 * date: 2018/8/3 19:09
 * description:
 * version:
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = {NewMineIntegrationPresenterMoudle.class})
public interface NewMineIntegrationComponent extends InjectComponent<NewMineIntegrationActivity>{
}
