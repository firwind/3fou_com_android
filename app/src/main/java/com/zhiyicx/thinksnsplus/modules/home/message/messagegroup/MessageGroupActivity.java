package com.zhiyicx.thinksnsplus.modules.home.message.messagegroup;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @Author Jliuer
 * @Date 2018/05/03/15:36
 * @Email Jliuer@aliyun.com
 * @Description 群
 */
public class MessageGroupActivity extends TSActivity<MessageGroupPresenter, MessageGroupListFragment> {

    @Override
    protected MessageGroupListFragment getFragment() {
        return new MessageGroupListFragment();
    }

    @Override
    protected void componentInject() {
        DaggerMessageGroupComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .messageGroupPresenterModule(new MessageGroupPresenterModule(mContanierFragment))
                .build().inject(this);
    }
}
