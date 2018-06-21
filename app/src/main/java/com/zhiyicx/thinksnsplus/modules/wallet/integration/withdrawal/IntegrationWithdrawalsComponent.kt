package com.zhiyicx.thinksnsplus.modules.wallet.integration.withdrawal

import com.zhiyicx.common.dagger.scope.FragmentScoped
import com.zhiyicx.thinksnsplus.base.AppComponent
import com.zhiyicx.thinksnsplus.base.InjectComponent
import dagger.Component

/**
 * @Describe
 * @Author zl
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(IntegrationWithdrawalsPresenterModule::class))
interface IntegrationWithdrawalsComponent : InjectComponent<IntegrationWithdrawalsActivity>
