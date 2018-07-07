package com.zhiyicx.thinksnsplus.modules.home.message.messagegroup.newgroup;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;
import com.zhiyicx.thinksnsplus.modules.home.message.messagegroup.MessageGroupActivity;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2018/05/03/15:48
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = {NewMessageGroupPresenterModule.class})
public interface NewMessageGroupComponent extends InjectComponent<NewMessageGroupListFragment> {
}
