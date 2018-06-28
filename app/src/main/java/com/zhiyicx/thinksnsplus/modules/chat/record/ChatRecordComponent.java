package com.zhiyicx.thinksnsplus.modules.chat.record;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * author: huwenyong
 * date: 2018/6/28 11:17
 * description:
 * version:
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = ChatRecordPresenterMoudle.class)
public interface ChatRecordComponent extends InjectComponent<ChatRecordActivity>{
}
