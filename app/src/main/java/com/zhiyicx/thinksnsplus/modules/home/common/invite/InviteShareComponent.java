package com.zhiyicx.thinksnsplus.modules.home.common.invite;

import com.zhiyicx.baseproject.impl.share.ShareModule;
import com.zhiyicx.baseproject.impl.share.ShareWithoutViewModule;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * <pre>
 *     @author : huwenyong
 *     @date : 2018/6/24 9:39
 *     desc :
 *     version : 1.0
 * <pre>
 */
@FragmentScoped
@Component(dependencies = AppComponent.class,modules = {InviteSharePresenterModule.class,ShareWithoutViewModule.class})
public interface InviteShareComponent extends InjectComponent<InviteShareActivity>{
}
