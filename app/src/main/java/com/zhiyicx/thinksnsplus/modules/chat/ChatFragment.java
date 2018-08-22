package com.zhiyicx.thinksnsplus.modules.chat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.hyphenate.easeui.widget.EaseChatPrimaryMenu;
import com.hyphenate.easeui.widget.EaseChatPrimaryMenuBase;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.easeui.widget.presenter.EaseChatRowPresenter;
import com.hyphenate.util.PathUtil;
import com.zhiyi.richtexteditorlib.view.dialogs.LinkDialog;
import com.zhiyicx.baseproject.em.manager.eventbus.TSEMRefreshEvent;
import com.zhiyicx.baseproject.em.manager.eventbus.TSEMessageEvent;
import com.zhiyicx.baseproject.em.manager.util.TSEMConstants;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.baseproject.widget.popwindow.NoticePopupWindow;
import com.zhiyicx.baseproject.widget.popwindow.PermissionPopupWindow;
import com.zhiyicx.common.utils.AndroidBug5497Workaround;
import com.zhiyicx.common.utils.ColorPhrase;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.TSEaseChatFragment;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.config.JpushMessageTypeConfig;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.beans.JpushMessageBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.chat.call.BaseCallActivity;
import com.zhiyicx.thinksnsplus.modules.chat.info.ChatInfoActivity;
import com.zhiyicx.thinksnsplus.modules.chat.item.ChatConfig;
import com.zhiyicx.thinksnsplus.modules.chat.item.presenter.TSChatCallPresneter;
import com.zhiyicx.thinksnsplus.modules.chat.item.presenter.TSChatFilePresenter;
import com.zhiyicx.thinksnsplus.modules.chat.item.presenter.TSChatLocationPresenter;
import com.zhiyicx.thinksnsplus.modules.chat.item.presenter.TSChatPicturePresenter;
import com.zhiyicx.thinksnsplus.modules.chat.item.presenter.TSChatTextPresenter;
import com.zhiyicx.thinksnsplus.modules.chat.item.presenter.TSChatTipTextPresenter;
import com.zhiyicx.thinksnsplus.modules.chat.item.presenter.TSChatVideoPresenter;
import com.zhiyicx.thinksnsplus.modules.chat.item.presenter.TSChatVoicePresenter;
import com.zhiyicx.thinksnsplus.modules.chat.location.SendLocationActivity;
import com.zhiyicx.thinksnsplus.modules.chat.video.ImageGridActivity;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.CircleDetailActivity;
import com.zhiyicx.thinksnsplus.modules.home.message.managergroup.notice.NoticeManagerActivity;
import com.zhiyicx.thinksnsplus.utils.NotificationUtil;
import com.zhiyicx.thinksnsplus.widget.chat.TSChatInputMenu;
import com.zhiyicx.thinksnsplus.widget.chat.TSChatPrimaryMenu;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.hyphenate.easeui.EaseConstant.EXTRA_CHAT_TYPE;
import static com.hyphenate.easeui.EaseConstant.EXTRA_IS_ADD_GROUP;
import static com.hyphenate.easeui.EaseConstant.EXTRA_TO_USER_ID;
import static com.hyphenate.easeui.widget.chatrow.EaseChatRow.TipMsgType.OPEN_MUTE;
import static com.zhiyicx.thinksnsplus.modules.chat.edit.name.EditGroupNameFragment.GROUP_ORIGINAL_ID;
import static com.zhiyicx.thinksnsplus.modules.chat.edit.name.EditGroupNameFragment.IS_GROUP_OWNER;

/**
 * @author Catherine
 * @describe 使用环信UI的聊天页面
 * @date 2018/1/2
 * @contact email:648129313@qq.com
 */

public class ChatFragment extends TSEaseChatFragment<ChatContract.Presenter>
        implements TSEaseChatFragment.EaseChatFragmentHelper, ChatContract.View, EaseChatRow.OnTipMsgClickListener {

    private static final int REQUEST_CODE_SELECT_VIDEO = 11;
    private static final int REQUEST_CODE_SELECT_FILE = 12;
    private static final int REQUEST_CODE_GROUP_DETAIL = 13;
    private static final int REQUEST_CODE_CONTEXT_MENU = 14;
    private static final int REQUEST_CODE_SELECT_AT_USER = 15;
    public static final int REQUEST_CODE_SELECT_AMAP = 16;


    private static final int MESSAGE_TYPE_SENT_VOICE_CALL = 1;
    private static final int MESSAGE_TYPE_RECV_VOICE_CALL = 2;
    private static final int MESSAGE_TYPE_SENT_VIDEO_CALL = 3;
    private static final int MESSAGE_TYPE_RECV_VIDEO_CALL = 4;
    private static final int MESSAGE_TYPE_RECALL = 9;

    /**
     * 群聊天室的提示消息
     */
    private static final int MESSAGE_TYPE_TIP = 10;

    static final int ITEM_TAKE_PICTURE_TS = 31;
    static final int ITEM_PICTURE_TS = 32;
    static final int ITEM_LOCATION_TS = 33;
    private static final int ITEM_VIDEO_TS = 34;
    private static final int ITEM_VOICE_CALL_TS = 35;
    private static final int ITEM_VIDEO_CALL_TS = 36;
    @BindView(R.id.input_menu)
    TSChatInputMenu inputMenu;
    @BindView(R.id.card_community)
    CardView mCardCommunity;

    Unbinder unbinder;
    View mView;
    private ActionPopupWindow mActionPopupWindow;


    public static ChatFragment instance(Bundle bundle) {
        ChatFragment chatFragment = new ChatFragment();
        chatFragment.setArguments(bundle);
        return chatFragment;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);

    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected boolean usePermisson() {
        return true;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected int getToolBarLayoutId() {
        return R.layout.ease_ts_title_bar;
    }

    @Override
    protected int setRightImg() {
        return R.mipmap.topbar_more_black;
    }

    @Override
    protected int setLeftImg() {
        return R.mipmap.topbar_back;
    }

    @Override
    protected void initEMView(View rootView) {

        super.initEMView(rootView);

        // 适配手机无法显示输入焦点
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            AndroidBug5497Workaround.assistActivity(mActivity);
        }

    }

    @Override
    public void onTipMsgClick(EaseChatRow.TipMsgType tipMsgType) {
        LinkDialog dialog = createLinkDialog();
        dialog.setListener(new LinkDialog.OnDialogClickListener() {
            @Override
            public void onConfirmButtonClick(String name, String url) {
                if (url.length() < 2) {
                    dialog.setErrorMessage(getString(R.string.chat_edit_group_name_alert));
                    return;
                }
                ChatGroupBean groupBean = mPresenter.getChatGroupInfoFromLocal();
                groupBean.setName(url);
                mPresenter.updateGroupName(groupBean);
                dialog.dismiss();
            }

            @Override
            public void onCancelButtonClick() {
                dialog.dismiss();
            }
        });
        dialog.show(getFragmentManager(), LinkDialog.Tag);
    }

    @Override
    public void onOpenMuteClick(EaseChatRow.TipMsgType tipMsgType, String str) {
        /*if (tipMsgType == OPEN_MUTE) {
            View view = inputMenu.setPrimaryMenuView();
            EaseChatPrimaryMenu menu = (EaseChatPrimaryMenu) view.findViewById(R.id.primary_menu);
            RelativeLayout mute = menu.getmOpenMute();
            mute.setVisibility(View.VISIBLE);
        }*/
    }

    @Override
    public void onNoticeSingleClick(String noticeJson) {

    }

    @Override
    protected void setUpView() {
        setChatFragmentHelper(this);

        //初始化为无法聊天的状态
        setTalkingState(false,"正在连接服务器");

        if (chatType == EaseConstant.CHATTYPE_SINGLE) {
            setCenterText(mPresenter.getUserInfoFromLocal().getName());
            //从服务器更新群信息
            mPresenter.getUserInfoFromServer();
        } else if (chatType == EaseConstant.CHATTYPE_GROUP) {
            setCenterText(mPresenter.getChatGroupName());

            /*if(null == EMClient.getInstance().groupManager().getGroup(toChatUsername))
                setToolBarRightImage(0);*/
            //获取禁言状态
            mPresenter.getCurrentTalkingState(toChatUsername);
            mPresenter.getCommunityInfo(toChatUsername);
        }
        if (chatType != EaseConstant.CHATTYPE_CHATROOM) {
            onConversationInit();
            onMessageListInit(this);
        }
        setRefreshLayoutListener();
        // show forward message if the message is not null
        String forwardMsgId = fragmentArgs.getString("forward_msg_id");
        if (forwardMsgId != null) {
            forwardMessage(forwardMsgId);
        }
    }

    @Override
    public void handleNotRoamingMessageWithUserInfo() {
        if(isMessageListInited)
            messageList.refresh();

    }

    /**
     * 设置禁言状态
     * @param isTalking
     */
    @Override
    public void setTalkingState(boolean isTalking,String content) {
        if(chatType == EaseConstant.CHATTYPE_SINGLE){
            ((EaseChatPrimaryMenu)inputMenu.getPrimaryMenu()).setTalkingState(isTalking||mPresenter.isImHelper(), content);
        }else {
            ((EaseChatPrimaryMenu)inputMenu.getPrimaryMenu()).setTalkingState(isTalking, content);
        }
    }

    @Override
    public void updateUserInfo(UserInfoBean userInfoBean) {
        setCenterText(userInfoBean.getName());

        setTalkingState(userInfoBean.isIs_my_friend(),getString(R.string.chat_no_talking_not_friend));

    }

    @Override
    public void updateChatGroupInfo(ChatGroupBean chatGroupBean) {
        if(null != chatGroupBean)
            setCenterText(mPresenter.getChatGroupName());
    }

    @Override
    public void getCommunityInfo(CircleInfo data) {

        if (data!=null) {
            mCardCommunity.setVisibility(View.VISIBLE);//显示频道
            ImageView mCloseView = (ImageView) mCardCommunity.findViewById(R.id.iv_close_community);
            // 封面
            ImageView mCircleCover = (ImageView) mCardCommunity.findViewById(R.id.iv_circle_cover);
            TextView mCommunityName = (TextView) mCardCommunity.findViewById(R.id.tv_circle_name);
            TextView mShareNum = (TextView) mCardCommunity.findViewById(R.id.tv_circle_feed_count);
            TextView mFollow = (TextView) mCardCommunity.findViewById(R.id.tv_circle_follow_count);
            TextView mRurnInfo = (TextView) mCardCommunity.findViewById(R.id.tv_turn_into);
            // 设置封面
            Glide.with(getContext())
                    .load(data.getAvatar())
                    .error(R.drawable.shape_default_image)
                    .placeholder(R.drawable.shape_default_image)
                    .into(mCircleCover);
            mCommunityName.setText(data.getName());
            String feedCountNumber = ConvertUtils.numberConvert(data.getPosts_count());
            String feedContent = getContext().getString(R.string.circle_post) + " " + "<" +
                    feedCountNumber + ">";
            CharSequence feedString = ColorPhrase.from(feedContent).withSeparator("<>")
                    .innerColor(ContextCompat.getColor(getContext(), R.color.themeColor))
                    .outerColor(ContextCompat.getColor(getContext(), R.color.normal_for_assist_text))
                    .format();
            mShareNum.setText(feedString);
            // 设置订阅人数
            String followCountNumber = ConvertUtils.numberConvert(data.getUsers_count());
            String followContent = getContext().getString(R.string.circle_member) + " " + "<" +
                    followCountNumber + ">";
            CharSequence followString = ColorPhrase.from(followContent).withSeparator("<>")
                    .innerColor(ContextCompat.getColor(getContext(), R.color.themeColor))
                    .outerColor(ContextCompat.getColor(getContext(), R.color.normal_for_assist_text))
                    .format();
            mFollow.setText(followString);
            mCloseView.setOnClickListener(v -> mCardCommunity.setVisibility(View.GONE));
            mRurnInfo.setOnClickListener(v -> CircleDetailActivity.startCircleDetailActivity(getContext(),data.getId()));

        }else {
            mCardCommunity.setVisibility(View.GONE);
        }
    }

    @Override
    public void getCommunityError() {
        mCardCommunity.setVisibility(View.GONE);
    }

    @Override
    protected void handleNotRoamingMessageList(List<EMMessage> list) {
        super.handleNotRoamingMessageList(list);
        mPresenter.handleNotRoamingMessageList(list);
    }


    @Override
    public void onResume() {
        super.onResume();

        setNeedRefreshToLast(false);

        if (chatType == EaseConstant.CHATTYPE_SINGLE) {
            setCenterText(mPresenter.getUserInfoFromLocal().getName());
            setTalkingState(mPresenter.getUserInfoFromLocal().isIs_my_friend(),getString(R.string.chat_no_talking_not_friend));
        } else if (chatType == EaseConstant.CHATTYPE_GROUP) {
            EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
            if (group != null && group.isMsgBlocked()) {
                mToolbarCenter.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ico_newslist_shield, 0);
            } else if (group != null && !group.isMsgBlocked()) {
                mToolbarCenter.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }

            setCenterText(mPresenter.getChatGroupName());
        }
        //setUpView();
    }

    @Override
    protected void setLeftClick() {
        onBackPressed();
    }

    @Override
    protected void setRightClick() {
        /*if(chatType == EaseConstant.CHATTYPE_GROUP &&
                null == EMClient.getInstance().groupManager().getGroup(toChatUsername))
            return;*/
        toGroupDetails();
    }

    @Override
    public void onSetMessageAttributes(EMMessage message) {

    }

    @Override
    protected void toGroupDetails() {
        onEnterToChatDetails();
    }

    @Override
    public void onEnterToChatDetails() {
        // 跳转群组详情
        Intent intent = new Intent(getActivity(), ChatInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TO_USER_ID, toChatUsername);
        bundle.putInt(EXTRA_CHAT_TYPE, chatType);
        bundle.putBoolean(EXTRA_IS_ADD_GROUP, true);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onAvatarClick(String username) {

    }

    @Override
    public void onAvatarLongClick(String username) {

    }

    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        return false;
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {
        String[] items = message.getType() == EMMessage.Type.TXT ?
                new String[]{"复制","删除"}:new String[]{"删除"};
        new AlertDialog.Builder(mActivity)
                .setItems(items,
                        (dialog, which) -> {
                            if(which == 0){
                                if(EMMessage.Type.TXT == message.getType()){
                                    DeviceUtils.copyTextToBoard(mActivity,
                                            ((EMTextMessageBody) message.getBody()).getMessage());
                                }else {
                                    EMClient.getInstance().chatManager().getConversation(message.conversationId())
                                            .removeMessage(message.getMsgId());
                                    messageList.refresh();
                                }
                            }else {
                                EMClient.getInstance().chatManager().getConversation(message.conversationId())
                                        .removeMessage(message.getMsgId());
                                messageList.refresh();
                            }
                        })
                .create()
                .show();
    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        LogUtils.d("Cathy", "onExtendMenuItemClick" + itemId);
        switch (itemId) {
            case ITEM_TAKE_PICTURE_TS:
                // 拍照
                mRxPermissions
                        .requestEach(Manifest.permission.CAMERA)
                        .subscribe(permission -> {
                            if (permission.granted) {
                                // 权限被允许
                                selectPicFromCamera();
                            } else if (permission.shouldShowRequestPermissionRationale) {
                                // 权限没有被彻底禁止
                            } else {
                                // 权限被彻底禁止
                                initPermissionPopUpWindow(getString(com.zhiyicx.baseproject.R.string.setting_permission_hint));
                            }
                        });
                break;
            case ITEM_PICTURE_TS:
                // 相册
                selectPicFromLocal();
                break;
            case ITEM_LOCATION_TS:
                // 位置
                Intent intentMap = new Intent(new Intent(getActivity(), SendLocationActivity.class));
                intentMap.putExtras(new Bundle());
                startActivityForResult(intentMap, REQUEST_CODE_MAP);
                mIsNeedRefreshToLast = false;
                break;
            case ITEM_VIDEO_TS:
                // 发送视频文件
                Intent intent = new Intent(getActivity(), ImageGridActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
                mIsNeedRefreshToLast = false;

                break;
            case ITEM_VIDEO_CALL_TS:
                // 视频通话
                mRxPermissions
                        .requestEach(Manifest.permission.CAMERA)
                        .subscribe(permission -> {
                            if (permission.granted) {
                                // 权限被允许
                                startVideoCall();
                            } else if (permission.shouldShowRequestPermissionRationale) {
                                // 权限没有被彻底禁止
                            } else {
                                // 权限被彻底禁止
                                initPermissionPopUpWindow(getString(com.zhiyicx.baseproject.R.string.setting_permission_hint));
                            }
                        });
                break;
            case ITEM_VOICE_CALL_TS:
                // 语音通话
                mRxPermissions
                        .requestEach(Manifest.permission.RECORD_AUDIO)
                        .subscribe(permission -> {
                            if (permission.granted) {
                                // 权限被允许
                                startVoiceCall();
                            } else if (permission.shouldShowRequestPermissionRationale) {
                                // 权限没有被彻底禁止
                            } else {
                                // 权限被彻底禁止
                                initPermissionPopUpWindow(getString(com.zhiyicx.baseproject.R.string.setting_permission_hint));
                            }
                        });
                break;
            default:
                break;
        }
        inputMenu.hideExtendMenuContainer();
        return true;
    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return new CustomChatRowProvider();
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_chat;
    }

    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        mPresenter.dealMessages(messages);
    }

    @Override
    public String getChatId() {
        return toChatUsername;
    }

    @Override
    public void onMessageReceivedWithUserInfo(List<EMMessage> messages) {
        for (EMMessage message : messages) {

            String username = null;
            // group message
            if (message.getChatType() == EMMessage.ChatType.GroupChat || message.getChatType() == EMMessage.ChatType.ChatRoom) {
                username = message.getTo();
            } else {
                // single chat message
                username = message.getFrom();
            }


            // if the message is for current conversation
            if (username.equals(toChatUsername) || message.getTo().equals(toChatUsername)
                    || message.conversationId().equals(toChatUsername)) {


                // 从左到右依次：用户加入，用户退出，创建群聊，屏蔽/接收群消息，该群消息作用对象是否是自己,群信息修改
                boolean isUserJoin, isUserExit, isCreate, isBlock, userTag , groupNotice,isGroupChange;

                isUserJoin = TSEMConstants.TS_ATTR_JOIN.equals(message.ext().get("type"));
                isUserExit = TSEMConstants.TS_ATTR_EIXT.equals(message.ext().get("type"));
                isCreate = message.getBooleanAttribute(TSEMConstants.TS_ATTR_GROUP_CRATE, false);
                isBlock = message.getBooleanAttribute(TSEMConstants.TS_ATTR_BLOCK, false);
                groupNotice = message.getBooleanAttribute(TSEMConstants.TS_ATTR_RELEASE_NOTICE,false);
                isGroupChange = TSEMConstants.TS_ATTR_GROUP_CHANGE.equals(message.ext().get("type"));

                userTag = AppApplication.getMyUserIdWithdefault() == message.getLongAttribute(TSEMConstants.TS_ATTR_TAG, -1L);

                if (isCreate || isBlock) {
                    if (!userTag) {
                        return;
                    }
                }

                //当最后一个item不可见时，不滑动到底部
                if(messageList.getListView().getLastVisiblePosition()
                        != messageList.getChildCount()-1){
                    messageList.refresh();
                }else {
                    messageList.refreshSelectLast();
                }

                EaseUI.getInstance().getNotifier().vibrateAndPlayTone(message);
                conversation.markMessageAsRead(message.getMsgId());

                if (isUserExit || isUserJoin) {
                    // 只有群聊中才会有 成员 加入or退出的消息
                    mPresenter.updateChatGroupMemberCount(message.conversationId(), 1, isUserJoin);
                    setCenterText(mPresenter.getChatGroupName());
                }
                //如果群信息改变，则向服务器请求群信息
                if(isGroupChange)
                    mPresenter.getChatGroupInfoFromServer();

                if (groupNotice){
                    ToastUtils.showLongToast("收到公告");
                }

            } else {
                if ("admin".equals(message.getFrom())) {
                    continue;
                }
                JpushMessageBean jpushMessageBean = new JpushMessageBean();
                jpushMessageBean.setType(JpushMessageTypeConfig.JPUSH_MESSAGE_TYPE_IM);
                jpushMessageBean.setExtras(message.getChatType().name());
                String content = message.getBody().toString();
                if (message.getBody() instanceof EMTextMessageBody) {
                    content = ((EMTextMessageBody) message.getBody()).getMessage();
                } else if (message.getBody() instanceof EMImageMessageBody) {
                    content = "[图片]";
                } else if (message.getBody() instanceof EMVoiceMessageBody) {
                    content = "[语音]";
                }

                content = mPresenter.getUserInfoFromLocal(message.getFrom()) + ":" + content;
                jpushMessageBean.setMessage(content);
                NotificationUtil.showChatNotifyMessageExceptCurrentConversation(mActivity, jpushMessageBean, message.conversationId());
            }
        }
    }

    @Override
    protected void registerExtendMenuItem() {
        //use the menu in base class
//        super.registerExtendMenuItem();
        //extend menu items
        // 图片
        inputMenu.registerExtendMenuItem(R.string.attach_picture, R.mipmap.ico_chat_picture, ITEM_PICTURE_TS, extendMenuItemClickListener);
        // 拍照
        inputMenu.registerExtendMenuItem(R.string.attach_take_pic, R.mipmap.ico_chat_takephoto, ITEM_TAKE_PICTURE_TS, extendMenuItemClickListener);
        // 视频 -- 需求取消  2018-1-31 15:26:14
//        inputMenu.registerExtendMenuItem(R.string.attach_video, R.mipmap.ico_chat_video, ITEM_VIDEO_TS, extendMenuItemClickListener);
        // 位置
        inputMenu.registerExtendMenuItem(R.string.attach_location, R.mipmap.ico_chat_location, ITEM_LOCATION_TS, extendMenuItemClickListener);
        // 目前仅有单聊才有音视频通话：2018.04.13备注：本版本去掉音视频通话；
//        if (chatType == EaseConstant.CHATTYPE_SINGLE) {
//            // 语音电话
//            inputMenu.registerExtendMenuItem(R.string.attach_voice_call, R.mipmap.ico_chat_voicecall, ITEM_VOICE_CALL_TS,
//                    extendMenuItemClickListener);
//            // 视频通话
//            inputMenu.registerExtendMenuItem(R.string.attach_video_call, R.mipmap.ico_chat_videocall, ITEM_VIDEO_CALL_TS,
//                    extendMenuItemClickListener);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    private final class CustomChatRowProvider implements EaseCustomChatRowProvider {
        @Override
        public int getCustomChatRowTypeCount() {
            //here the number is the message type in EMMessage::Type
            //which is used to count the number of different chat row
            return 11;
        }

        @Override
        public int getCustomChatRowType(EMMessage message) {
            boolean isGroupChange = TSEMConstants.TS_ATTR_GROUP_CHANGE.equals(message.ext().get("type"))
                    || TSEMConstants.TS_ATTR_EIXT.equals(message.ext().get("type"))
                    || TSEMConstants.TS_ATTR_JOIN.equals(message.ext().get("type"))
                    || TSEMConstants.TS_ATTR_RELEASE_NOTICE.equals(message.ext().get("type"));

            if (message.getType() == EMMessage.Type.TXT) {
                //voice call
                if (message.getBooleanAttribute(ChatConfig.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE_CALL : MESSAGE_TYPE_SENT_VOICE_CALL;
                } else if (message.getBooleanAttribute(ChatConfig.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
                    //video call
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO_CALL : MESSAGE_TYPE_SENT_VIDEO_CALL;
                }
                //messagee recall
                else if (message.getBooleanAttribute(ChatConfig.MESSAGE_TYPE_RECALL, false)) {
                    return MESSAGE_TYPE_RECALL;
                } else if ("admin".equals(message.getUserName()) || isGroupChange) {
                    return MESSAGE_TYPE_TIP;
                }
            }
            return 0;
        }

        @Override
        public EaseChatRowPresenter getCustomChatRow(EMMessage message, int position, BaseAdapter adapter) {
            EaseChatRowPresenter presenter;
            // voice call or video call
            if (message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_VOICE_CALL, false) ||
                    message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
                presenter = new TSChatCallPresneter();
                return presenter;
            } else {
                boolean admin;
                boolean isGroupChange = TSEMConstants.TS_ATTR_GROUP_CHANGE.equals(message.ext().get("type"))
                        || TSEMConstants.TS_ATTR_GROUP_CHANGE.equals(message.ext().get("type"))
                        || TSEMConstants.TS_ATTR_EIXT.equals(message.ext().get("type"))
                        || TSEMConstants.TS_ATTR_JOIN.equals(message.ext().get("type"));

                admin = "admin".equals(message.getUserName());
                if (admin || isGroupChange) {
                    presenter = new TSChatTipTextPresenter();
                } else {
                    presenter = new TSChatTextPresenter();
                }
            }
            return presenter;
        }

        @Override
        public EaseChatRowPresenter getCustomChatRow(EMMessage message, int position, BaseAdapter adapter, ChatUserInfoBean userInfoBean) {
            EaseChatRowPresenter presenter;
            if (message.getType() == EMMessage.Type.TXT) {
                // voice call or video call
                if (message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_VOICE_CALL, false) ||
                        message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
                    presenter = new TSChatCallPresneter();
                } else {
                    boolean admin;
                    boolean isGroupChange = TSEMConstants.TS_ATTR_GROUP_CHANGE.equals(message.ext().get("type"))
                            || TSEMConstants.TS_ATTR_GROUP_CHANGE.equals(message.ext().get("type"))
                            || TSEMConstants.TS_ATTR_EIXT.equals(message.ext().get("type"))
                            || TSEMConstants.TS_ATTR_JOIN.equals(message.ext().get("type"));
                    // admin 是管理员
                    admin = "admin".equals(message.getUserName());
                    if (admin || isGroupChange) {
                        presenter = new TSChatTipTextPresenter();
                    } else {
                        presenter = new TSChatTextPresenter();
                    }
                }
            } else if (message.getType() == EMMessage.Type.IMAGE) {
                presenter = new TSChatPicturePresenter();
            } else if (message.getType() == EMMessage.Type.VOICE) {
                presenter = new TSChatVoicePresenter();
            } else if (message.getType() == EMMessage.Type.LOCATION) {
                presenter = new TSChatLocationPresenter();
            } else if (message.getType() == EMMessage.Type.VIDEO) {
                presenter = new TSChatVideoPresenter();
            } else if (message.getType() == EMMessage.Type.FILE) {
                presenter = new TSChatFilePresenter();
            }else
                presenter = null;

            if(null != presenter)
                presenter.setMessageSendErrorCallback((code, msg) -> {
                mActivity.runOnUiThread(() -> {
                    if(code == 215){//禁言
                        setTalkingState(false,"您已被禁言");
                    }
                    //ToastUtils.showToast(mActivity,msg);
                });
                });
            return presenter;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SELECT_VIDEO: //send the video
                    if (data != null) {
                        int duration = data.getIntExtra("dur", 0);
                        String videoPath = data.getStringExtra("path");
                        File file = new File(PathUtil.getInstance().getImagePath(), "thvideo" + System.currentTimeMillis());
                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            Bitmap ThumbBitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
                            ThumbBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.close();
                            sendVideoMessage(videoPath, file.getAbsolutePath(), duration);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * make a voice call
     */
    protected void startVoiceCall() {
        if (!EMClient.getInstance().isConnected()) {
            ToastUtils.showToast(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT);
        } else {
            BaseCallActivity.startVoiceCallActivity(mActivity, toChatUsername, false);
            inputMenu.hideExtendMenuContainer();
            mIsNeedRefreshToLast = false;
        }
    }

    /**
     * make a video call
     */
    protected void startVideoCall() {
        if (!EMClient.getInstance().isConnected()) {
            ToastUtils.showToast(R.string.not_connect_to_server);
        } else {
            BaseCallActivity.startVideoCallActivity(mActivity, toChatUsername, false);
            inputMenu.hideExtendMenuContainer();
            mIsNeedRefreshToLast = false;

        }
    }

    @Subscriber(mode = ThreadMode.MAIN, tag = EventBusTagConfig.EVENT_IM_DELETE_QUIT)
    public void deleteGroup(String id) {
        mActivity.finish();
    }

    @Subscriber(mode = ThreadMode.MAIN, tag = EventBusTagConfig.EVENT_IM_COMMUNITY)
    public void updateCommunityInfo(String str){
        mPresenter.getCommunityInfo(toChatUsername);
    }
    /*@Subscriber(mode = ThreadMode.MAIN, tag = EventBusTagConfig.EVENT_IM_GROUP_CREATE_FROM_SINGLE)
    public void closeCurrent(ChatGroupBean chatGroupBean) {
        if (!chatGroupBean.getId().equals(toChatUsername)) {
            getActivity().finish();
        }
    }*/

    /*OnResume中 已经重新设置了
    @Subscriber(tag = EventBusTagConfig.EVENT_IM_GROUP_UPDATE_GROUP_INFO)
    public void updateCurrent(ChatGroupBean chatGroupBean) {
        if (chatGroupBean.getId().equals(toChatUsername)) {
            setCenterText(getString(R.string.chat_group_name_default, chatGroupBean.getName(), chatGroupBean.getAffiliations_count()));
        }
    }*/


    private NoticePopupWindow mNoticePop;

    /**
     * 获取公告信息
     *
     * @param event
     */
    @Subscriber(mode = ThreadMode.MAIN)
    public void onTSEMessageEventEventBus(TSEMessageEvent event) {
        String userName = event.getMessage().getStringAttribute(TSEMConstants.TS_USER_NAME, null);
        String time = event.getMessage().getStringAttribute(TSEMConstants.TS_TIME, null);
        String title = event.getMessage().getStringAttribute(TSEMConstants.TS_TITLE, null);
        String content = event.getMessage().getStringAttribute(TSEMConstants.TS_CONTENT, null);
        String groupId = event.getMessage().getStringAttribute(TSEMConstants.TS_GROUP_ID, null);
        boolean isOwner = event.getMessage().getBooleanAttribute(TSEMConstants.TS_GROUP_OWNER, false);
        EMCmdMessageBody body = (EMCmdMessageBody) event.getMessage().getBody();
        switch (body.action()) {
            case TSEMConstants.TS_ATTR_RELEASE_NOTICE://获取发布公告
                if (mNoticePop != null) {
                    mNoticePop.show();
                    return;
                }
                mNoticePop = NoticePopupWindow.builder()
                        .titleStr(title)
                        .desStr(content)
                        .item1Str(getString(R.string.get_it))
                        .nameStr(userName)
                        .timeStr(TimeUtils.getTimeFriendlyNormal(time))
                        .isOutsideTouch(true)
                        .item1Color(R.color.white)
                        .isFocus(true)
                        .animationStyle(R.style.style_actionPopupAnimation)
                        .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                        .buildNoticePopWindowItem1ClickListener(new NoticePopupWindow.NoticePopWindowItem1ClickListener() {
                            @Override
                            public void onClicked() {
                                mNoticePop.hide();
                            }

                            @Override
                            public void onMore() {
                                Intent intentName = new Intent(getContext(), NoticeManagerActivity.class);
                                intentName.putExtra(GROUP_ORIGINAL_ID, groupId);
                                intentName.putExtra(IS_GROUP_OWNER, isOwner);
                                startActivity(intentName);

                            }
                        })
                        .with(getActivity())
                        .parentView(getView())
                        .build();
                mNoticePop.show();
                break;
        }
    }

    private void initPermissionPopUpWindow(String item1) {
        mActionPopupWindow = PermissionPopupWindow.builder()
                .permissionName(getString(com.zhiyicx.baseproject.R.string.camera_permission))
                .with(getActivity())
                .bottomStr(getString(com.zhiyicx.baseproject.R.string.cancel))
                .item1Str(item1)
                .item2Str(getString(com.zhiyicx.baseproject.R.string.setting_permission))
                .item2ClickListener(() -> {
                    DeviceUtils.openAppDetail(getContext());
                    mActionPopupWindow.hide();
                })
                .bottomClickListener(() -> mActionPopupWindow.hide())
                .isFocus(true)
                .isOutsideTouch(true)
                .build();
        mActionPopupWindow.show();
    }

    private LinkDialog createLinkDialog() {
        return LinkDialog.createLinkDialog()
                .setNeedMaxInputFomatFilter(true)
                .setUrlHinit(getString(R.string.chat_edit_group_name_alert))
                .setTitleStr(getString(R.string.chat_edit_group_name))
                .setNameVisible(false);
    }

    @Override
    public void onDestroyView() {
        dismissPop(mActionPopupWindow);
        super.onDestroyView();
        unbinder.unbind();
    }
}
