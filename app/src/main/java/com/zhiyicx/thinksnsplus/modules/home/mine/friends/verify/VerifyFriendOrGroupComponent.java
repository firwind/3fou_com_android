package com.zhiyicx.thinksnsplus.modules.home.mine.friends.verify;
/*
 * 文件名：
 * 创建者：zl
 * 时  间：2018/8/16 0016
 * 描  述：
 * 版  权：九曲互动
 * 
 */

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;


import dagger.Component;

@FragmentScoped
@Component(dependencies = AppComponent.class, modules = VerifyFriendOrGroupPresenterModule.class)
public interface VerifyFriendOrGroupComponent extends InjectComponent<VerifyFriendOrGroupActivity>{
}
