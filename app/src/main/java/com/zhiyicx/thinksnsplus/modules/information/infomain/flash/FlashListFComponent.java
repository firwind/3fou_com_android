package com.zhiyicx.thinksnsplus.modules.information.infomain.flash;
/*
 * 文件名：
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/12 14:12
 * 修改者：
 * 修改备注：
 * 修改时间：
 * 版  权：互动科技
 */

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;
import com.zhiyicx.thinksnsplus.modules.information.infomain.list.InfoListPresenterModule;

import dagger.Component;

@FragmentScoped
@Component(dependencies = AppComponent.class, modules = FlashListPresenterModule.class)
public interface FlashListFComponent extends InjectComponent<FlashListFragment> {
}
