package com.zhiyicx.thinksnsplus.modules.information.infomain.list;

import com.zhiyicx.baseproject.impl.share.ShareModule;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2017/03/14
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(modules = {InfoListPresenterModule.class,ShareModule.class},dependencies = AppComponent.class)
public interface InfoListComponent extends InjectComponent<InfoListFragment> {
}
