package com.zhiyicx.thinksnsplus.modules.chat.info;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.github.tamir7.contacts.Contact;
import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

import static com.hyphenate.easeui.EaseConstant.CHATTYPE_SINGLE;
import static com.hyphenate.easeui.EaseConstant.EXTRA_CHAT_TYPE;
import static com.hyphenate.easeui.EaseConstant.EXTRA_IS_ADD_GROUP;
import static com.hyphenate.easeui.EaseConstant.EXTRA_TO_USER_ID;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/22
 * @contact email:648129313@qq.com
 */

public class ChatInfoActivity extends TSActivity<ChatInfoPresenter, ChatInfoFragment>{

    @Override
    protected ChatInfoFragment getFragment() {
        return ChatInfoFragment.newInstance(getIntent().getStringExtra(EXTRA_TO_USER_ID),
                getIntent().getIntExtra(EXTRA_CHAT_TYPE,CHATTYPE_SINGLE));
    }

    @Override
    protected void componentInject() {
        DaggerChatInfoComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .chatInfoPresenterModule(new ChatInfoPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }

    public static void startChatInfoActivity(Context context,String groupId,int chatType){
        Intent intent = new Intent(context, ChatInfoActivity.class);
        intent.putExtra(EXTRA_TO_USER_ID, groupId);
        intent.putExtra(EXTRA_CHAT_TYPE, chatType);
        context.startActivity(intent);
    }
}
