package com.zhiyicx.thinksnsplus.modules.chat.record;


import android.content.Context;
import android.content.Intent;

import com.hyphenate.chat.EMConversation;
import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.DaggerAppComponent;
import com.zhiyicx.thinksnsplus.i.IntentKey;

/**
 * author: huwenyong
 * date: 2018/6/28 9:33
 * description:
 * version:
 */

public class ChatRecordActivity extends TSActivity<ChatRecordPresenter,ChatRecordFragment> {


    public static void startChatRecordActivity(Context mContext, String conversationId){
        Intent intent = new Intent(mContext,ChatRecordActivity.class);
        intent.putExtra(IntentKey.CONVERSATION_ID,conversationId);
        mContext.startActivity(intent);
    }

    @Override
    protected ChatRecordFragment getFragment() {
        return ChatRecordFragment.newInstance(getIntent().getStringExtra(IntentKey.CONVERSATION_ID));
    }

    @Override
    protected void componentInject() {
        DaggerChatRecordComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .chatRecordPresenterMoudle(new ChatRecordPresenterMoudle(mContanierFragment))
                .build()
                .inject(this);
    }
}
