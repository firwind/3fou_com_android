package com.zhiyicx.thinksnsplus.modules.home.message.messagegroup;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2018/05/03/15:48
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = MessageGroupPresenterModule.class)
public interface MessageGroupComponent extends InjectComponent<MessageGroupActivity> {
}
