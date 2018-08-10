package com.zhiyicx.thinksnsplus.modules.chat.card;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.i.IntentKey;

/**
 * author: huwenyong
 * date: 2018/8/10 9:16
 * description:
 * version:
 */

public class ChatGroupCardActivity extends TSActivity{

    @Override
    protected Fragment getFragment() {
        return ChatGroupCardFragment.newInstance(getIntent().getParcelableExtra(IntentKey.GROUP_INFO));
    }

    @Override
    protected void componentInject() {

    }

    public static void startChatGroupCardActivity(Context mContext, ChatGroupBean chatGroupBean){

        Intent intent = new Intent(mContext,ChatGroupCardActivity.class);
        intent.putExtra(IntentKey.GROUP_INFO, (Parcelable) chatGroupBean);
        mContext.startActivity(intent);

    }

}
