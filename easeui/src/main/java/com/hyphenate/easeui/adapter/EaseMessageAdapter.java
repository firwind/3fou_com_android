/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyphenate.easeui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.hyphenate.easeui.model.styles.EaseMessageListItemStyle;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.widget.EaseChatMessageList.MessageListItemClickListener;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.easeui.widget.presenter.EaseChatBigExpressionPresenter;
import com.hyphenate.easeui.widget.presenter.EaseChatFilePresenter;
import com.hyphenate.easeui.widget.presenter.EaseChatImagePresenter;
import com.hyphenate.easeui.widget.presenter.EaseChatLocationPresenter;
import com.hyphenate.easeui.widget.presenter.EaseChatRowPresenter;
import com.hyphenate.easeui.widget.presenter.EaseChatTextPresenter;
import com.hyphenate.easeui.widget.presenter.EaseChatVideoPresenter;
import com.hyphenate.easeui.widget.presenter.EaseChatVoicePresenter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class EaseMessageAdapter extends BaseAdapter {

    private final static String TAG = "msg";

    private Context context;
    private static final String HANDLER_TYPE = "hanlder_type";
    private static final String HANDLER_POSITION = "hanlder_positon";
    private static final int HANDLER_MESSAGE_REFRESH_LIST = 0;
    private static final int HANDLER_MESSAGE_SELECT_LAST = 1;
    private static final int HANDLER_MESSAGE_SEEK_TO = 2;

    private static final int MESSAGE_TYPE_RECV_TXT = 0;
    private static final int MESSAGE_TYPE_SENT_TXT = 1;
    private static final int MESSAGE_TYPE_SENT_IMAGE = 2;
    private static final int MESSAGE_TYPE_SENT_LOCATION = 3;
    private static final int MESSAGE_TYPE_RECV_LOCATION = 4;
    private static final int MESSAGE_TYPE_RECV_IMAGE = 5;
    private static final int MESSAGE_TYPE_SENT_VOICE = 6;
    private static final int MESSAGE_TYPE_RECV_VOICE = 7;
    private static final int MESSAGE_TYPE_SENT_VIDEO = 8;
    private static final int MESSAGE_TYPE_RECV_VIDEO = 9;
    private static final int MESSAGE_TYPE_SENT_FILE = 10;
    private static final int MESSAGE_TYPE_RECV_FILE = 11;
    private static final int MESSAGE_TYPE_SENT_EXPRESSION = 12;
    private static final int MESSAGE_TYPE_RECV_EXPRESSION = 13;


    public int itemTypeCount;

    // reference to conversation object in chatsdk
    private EMConversation conversation;
    private List<EMMessage> messages = new ArrayList<>();
    /**
     * 当前聊天的用户列表
     */

    private String toChatUsername;

    private MessageListItemClickListener itemClickListener;
    private EaseCustomChatRowProvider customRowProvider;
    private EaseChatRow.OnTipMsgClickListener mOnTipMsgClickListener;

    private boolean showUserNick;
    private boolean showAvatar;
    private Drawable myBubbleBg;
    private Drawable otherBuddleBg;

    private ListView listView;
    private EaseMessageListItemStyle itemStyle;
    private Subscription mRefershSub;

    public EaseMessageAdapter(Context context, String username, int chatType, ListView listView, EaseChatRow.OnTipMsgClickListener
            onTipMsgClickListener) {
        this.context = context;
        this.listView = listView;
        toChatUsername = username;
        this.mOnTipMsgClickListener = onTipMsgClickListener;
        this.conversation = EMClient.getInstance().chatManager().getConversation(username, EaseCommonUtils.getConversationType(chatType), true);
    }


    private void refreshList(final Bundle data) {
        // you should not call getAllMessages() in UI thread
        // otherwise there is problem when refreshing UI and there is new message arrive
        if (mRefershSub != null && !mRefershSub.isUnsubscribed()) {
            mRefershSub.unsubscribe();
        }
        final int what = data.getInt(HANDLER_TYPE);
        final int position = data.getInt(HANDLER_POSITION);

        mRefershSub = Observable.just("")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String wahtTmp) {
                        List<EMMessage> var = conversation.getAllMessages();

                        if (what == HANDLER_MESSAGE_SEEK_TO) {
                            messages.addAll(0, var.subList(0, position + 1));
                            conversation.markAllMessagesAsRead();
                        } else {
                            messages.clear();
                            messages.addAll(var);
                            conversation.markAllMessagesAsRead();
                        }
                        return wahtTmp;

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String wahtData) {
                        if (listView == null) {
                            // 页面被释放
                            return;
                        }
                        switch (what) {
                            case HANDLER_MESSAGE_SELECT_LAST:
                                if (messages != null && messages.size() > 0) {
                                    listView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            listView.setSelection(messages.size() - 1);
                                        }
                                    });
                                }
                                break;
                            case HANDLER_MESSAGE_REFRESH_LIST:
                                notifyDataSetChanged();
                                break;
                            case HANDLER_MESSAGE_SEEK_TO:
                                listView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        listView.setSelection(position + 1 > getCount() - 1 ? position : (position + 1));
                                    }
                                });
                                break;
                            default:
                                notifyDataSetChanged();
                                break;
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }


    public void refresh() {
        Bundle bundle = new Bundle();
        bundle.putInt(HANDLER_TYPE, HANDLER_MESSAGE_REFRESH_LIST);
        refreshList(bundle);
    }

    /**
     * refresh and select the last
     */
    public void refreshSelectLast() {
        Bundle bundle = new Bundle();
        bundle.putInt(HANDLER_TYPE, HANDLER_MESSAGE_SELECT_LAST);
        refreshList(bundle);

    }

    /**
     * refresh and seek to the position
     */
    public void refreshSeekTo(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(HANDLER_TYPE, HANDLER_MESSAGE_SEEK_TO);
        bundle.putInt(HANDLER_POSITION, position);
        refreshList(bundle);
    }

    @Override
    public EMMessage getItem(int position) {
        if (position < messages.size()) {
            return messages.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * get count of messages
     */
    @Override
    public int getCount() {
        return messages.size();
    }

    /**
     * get number of message type, here 14 = (EMMessage.Type) * 2
     */
    @Override
    public int getViewTypeCount() {
        if (customRowProvider != null && customRowProvider.getCustomChatRowTypeCount() > 0) {
            return customRowProvider.getCustomChatRowTypeCount() + 14;
        }
        return 14;
    }

    /**
     * get type of item
     */
    @Override
    public int getItemViewType(int position) {
        EMMessage message = getItem(position);
        if (message == null) {
            return -1;
        }

        if (customRowProvider != null && customRowProvider.getCustomChatRowType(message) > 0) {
            return customRowProvider.getCustomChatRowType(message) + 13;
        }

        if (message.getType() == EMMessage.Type.TXT) {
            if (message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)) {
                return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_EXPRESSION : MESSAGE_TYPE_SENT_EXPRESSION;
            }
            return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_TXT : MESSAGE_TYPE_SENT_TXT;
        }
        if (message.getType() == EMMessage.Type.IMAGE) {
            return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_IMAGE : MESSAGE_TYPE_SENT_IMAGE;

        }
        if (message.getType() == EMMessage.Type.LOCATION) {
            return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_LOCATION : MESSAGE_TYPE_SENT_LOCATION;
        }
        if (message.getType() == EMMessage.Type.VOICE) {
            return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE : MESSAGE_TYPE_SENT_VOICE;
        }
        if (message.getType() == EMMessage.Type.VIDEO) {
            return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO : MESSAGE_TYPE_SENT_VIDEO;
        }
        if (message.getType() == EMMessage.Type.FILE) {
            return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_FILE : MESSAGE_TYPE_SENT_FILE;
        }

        return -1;// invalid
    }

    protected EaseChatRowPresenter createChatRowPresenter(EMMessage message, int position) {
        ChatUserInfoBean chatUserInfoBean = new ChatUserInfoBean();
        if (customRowProvider != null && customRowProvider.getCustomChatRow(message, position, this, chatUserInfoBean) != null) {
            return customRowProvider.getCustomChatRow(message, position, this, chatUserInfoBean);
        }

        EaseChatRowPresenter presenter = null;

        switch (message.getType()) {
            case TXT:
                if (message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)) {
                    presenter = new EaseChatBigExpressionPresenter();
                } else {
                    presenter = new EaseChatTextPresenter();
                }
                break;
            case LOCATION:
                presenter = new EaseChatLocationPresenter();
                break;
            case FILE:
                presenter = new EaseChatFilePresenter();
                break;
            case IMAGE:
                presenter = new EaseChatImagePresenter();
                break;
            case VOICE:
                presenter = new EaseChatVoicePresenter();
                break;
            case VIDEO:
                presenter = new EaseChatVideoPresenter();
                break;
            default:
                break;
        }

        return presenter;
    }


    @Override
    @SuppressLint("NewApi")
    public View getView(final int position, View convertView, ViewGroup parent) {
        EMMessage message = getItem(position);
        ChatUserInfoBean chatUserInfoBean = new ChatUserInfoBean();
        EaseChatRowPresenter presenter = null;


        if (convertView == null) {
            presenter = createChatRowPresenter(message, position);
            // 这个需要快速编辑群名字
            if ("TSChatTipTextPresenter".equals(presenter.getClass().getSimpleName())) {
                convertView = presenter.createChatRow(context, message, position, this, mOnTipMsgClickListener);
            } else {
                convertView = presenter.createChatRow(context, message, position, this, chatUserInfoBean);
            }
            convertView.setTag(presenter);
        } else {
            presenter = (EaseChatRowPresenter) convertView.getTag();
        }
        presenter.setup(message, position, itemClickListener, itemStyle);
        return convertView;
    }


    public void setItemStyle(EaseMessageListItemStyle itemStyle) {
        this.itemStyle = itemStyle;
    }


    public void setItemClickListener(MessageListItemClickListener listener) {
        itemClickListener = listener;
    }

    public void setCustomChatRowProvider(EaseCustomChatRowProvider rowProvider) {
        customRowProvider = rowProvider;
    }


    public boolean isShowUserNick() {
        return showUserNick;
    }


    public boolean isShowAvatar() {
        return showAvatar;
    }


    public Drawable getMyBubbleBg() {
        return myBubbleBg;
    }


    public Drawable getOtherBubbleBg() {
        return otherBuddleBg;
    }

}
