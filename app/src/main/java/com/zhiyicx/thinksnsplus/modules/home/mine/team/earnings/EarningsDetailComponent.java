package com.zhiyicx.thinksnsplus.modules.home.mine.team.earnings;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/7/21
 * 描  述：
 * 版  权: 九曲互动
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = EarningsDetailModule.class)
public interface EarningsDetailComponent extends InjectComponent<EarningsDetailActivity> {

}
