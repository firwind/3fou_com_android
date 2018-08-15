package com.zhiyicx.thinksnsplus.modules.chat.select.addgroup;
/*
 * 文件名：添加群聊
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/25 16:52
 * 修改者：
 * 修改备注：
 * 修改时间：
 * 版  权：互动科技
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.home.message.messagegroup.MessageGroupPresenterModule;

import javax.inject.Inject;

public class AddGroupActivity extends TSActivity<AddGroupPresenter, AddGroupFragment> {
    @Override
    protected AddGroupFragment getFragment() {
        return AddGroupFragment.instance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerAddGroupComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .addGroupPresenterModule(new AddGroupPresenterModule(mContanierFragment))
                .build().inject(this);
    }

    public static void startAddGroupActivity(Context context) {
        Intent intent = new Intent(context, AddGroupActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
