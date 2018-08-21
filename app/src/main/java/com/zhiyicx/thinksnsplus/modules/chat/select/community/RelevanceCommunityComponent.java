package com.zhiyicx.thinksnsplus.modules.chat.select.community;
/*
 * 文件名：
 * 创建者：zl
 * 时  间：2018/8/21 0021
 * 描  述：
 * 版  权：九曲互动
 * 
 */

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

@FragmentScoped
@Component(dependencies = AppComponent.class, modules = RelevanceCommunityPresenterModule.class)
public interface RelevanceCommunityComponent extends InjectComponent<RelevanceCommunityActivity> {
}
