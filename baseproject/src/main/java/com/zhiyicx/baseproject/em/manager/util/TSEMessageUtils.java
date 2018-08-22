package com.zhiyicx.baseproject.em.manager.util;

import android.content.Context;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.PathUtil;
import com.zhiyicx.baseproject.em.manager.eventbus.TSEMRefreshEvent;
import com.zhiyicx.baseproject.em.manager.eventbus.TSEMessageEvent;
import com.zhiyicx.common.utils.log.LogUtils;

import org.simple.eventbus.EventBus;

/**
 * @author Jliuer
 * @Date 18/02/06 11:43
 * @Email Jliuer@aliyun.com
 * @Description 消息处理工具类，主要做谢谢EMMessage对象的处理
 */
public class TSEMessageUtils {

    /**
     * 发送一条撤回消息的透传，这里需要和接收方协商定义，通过一个透传，并加上扩展去实现消息的撤回
     *
     * @param message  需要撤回的消息
     * @param callBack 发送消息的回调，通知调用方发送撤回消息的结果
     */
    public static void sendRecallMessage(EMMessage message, final EMCallBack callBack) {
        boolean result = false;
        // 获取当前时间，用来判断后边撤回消息的时间点是否合法，这个判断不需要在接收方做，
        // 因为如果接收方之前不在线，很久之后才收到消息，将导致撤回失败
        long currTime = TSEMDateUtil.getCurrentMillisecond();
        long msgTime = message.getMsgTime();
        // 判断当前消息的时间是否已经超过了限制时间，如果超过，则不可撤回消息
        if (currTime < msgTime || (currTime - msgTime > TSEMConstants.ML_TIME_RECALL)) {
            callBack.onError(TSEMConstants.ML_ERROR_I_RECALL_TIME, TSEMConstants.ML_ERROR_S_RECALL_TIME);
            return;
        }
        // 获取消息 id，作为撤回消息的参数
        String msgId = message.getMsgId();
        // 创建一个CMD 类型的消息，将需要撤回的消息通过这条CMD消息发送给对方
        EMMessage cmdMessage = EMMessage.createSendMessage(EMMessage.Type.CMD);
        // 判断下消息类型，如果是群聊就设置为群聊
        if (message.getChatType() == EMMessage.ChatType.GroupChat) {
            cmdMessage.setChatType(EMMessage.ChatType.GroupChat);
        }
        // 设置消息接收者
        cmdMessage.setTo(message.getTo());
        // 创建CMD 消息的消息体 并设置 action 为 recall
        String action = TSEMConstants.TS_ATTR_RECALL;
        EMCmdMessageBody body = new EMCmdMessageBody(action);
        cmdMessage.addBody(body);
        // 设置消息的扩展为要撤回的 msgId
        cmdMessage.setAttribute(TSEMConstants.ML_ATTR_MSG_ID, msgId);
        // 确认无误，开始发送撤回消息的透传
        cmdMessage.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                callBack.onSuccess();
            }

            @Override
            public void onError(int i, String s) {
                callBack.onError(i, s);
            }

            @Override
            public void onProgress(int i, String s) {
            }
        });
        // 准备工作完毕，发送消息
        EMClient.getInstance().chatManager().sendMessage(cmdMessage);
    }

    /**
     * 收到撤回消息，这里需要和发送方协商定义，通过一个透传，并加上扩展去实现消息的撤回
     *
     * @param cmdMessage 收到的透传消息，包含需要撤回的消息的 msgId
     * @return 返回撤回结果是否成功
     */
    public static boolean receiveRecallMessage(Context context, EMMessage cmdMessage) {
        boolean result = false;
        // 从cmd扩展中获取要撤回消息的id
        String msgId = cmdMessage.getStringAttribute(TSEMConstants.ML_ATTR_MSG_ID, null);
        if (msgId == null) {
            LogUtils.d("recall - 3 %s", msgId);
            return result;
        }
        // 根据得到的msgId 去本地查找这条消息，如果本地已经没有这条消息了，就不用撤回
        EMMessage message = EMClient.getInstance().chatManager().getMessage(msgId);
        if (message == null) {
            LogUtils.d("recall - 3 message is null %s", msgId);
            return result;
        }

        // 设置扩展为撤回消息类型，是为了区分消息的显示
        message.setAttribute(TSEMConstants.TS_ATTR_RECALL, true);
        // 更新消息
        result = EMClient.getInstance().chatManager().updateMessage(message);
        return result;
    }

    /**
     * 发送输入状态，这里通过cmd消息来进行发送，告知对方自己正在输入
     *
     * @param to 接收方的名字
     */
    public static void sendInputStatusMessage(String to) {
        EMMessage cmdMessage = EMMessage.createSendMessage(EMMessage.Type.CMD);
        cmdMessage.setTo(to);
        // 创建CMD 消息的消息体 并设置 action 为输入状态
        EMCmdMessageBody body = new EMCmdMessageBody(TSEMConstants.ML_ATTR_INPUT_STATUS);
        cmdMessage.addBody(body);
        // 确认无误，开始表示发送输入状态的透传
        EMClient.getInstance().chatManager().sendMessage(cmdMessage);
    }

    /**
     * 获取图片消息的缩略图本地保存的路径
     *
     * @param fullSizePath 缩略图的原始路径
     * @return 返回本地路径
     */
    public static String getThumbImagePath(String fullSizePath) {
        String thumbImageName = TSEMCryptoUtil.cryptoStr2SHA1(fullSizePath);
        String path = PathUtil.getInstance().getHistoryPath() + "/" + "thumb_" + thumbImageName;
        return path;
    }

    /**
     * @param to       发送到 ...
     * @param content  发送的内容
     * @param isJoin
     * @param callBack
     */
    public static void sendGroupMemberJoinOrExitMessage(final String to, String content, boolean isJoin, final EMCallBack callBack) {

        EMMessage message = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
        // 消息体
        EMTextMessageBody textBody = new EMTextMessageBody(content);
        message.addBody(textBody);
        message.setTo(to);
        message.setFrom("admin");
        message.setChatType(EMMessage.ChatType.GroupChat);
        long currTime = TSEMDateUtil.getCurrentMillisecond();
        message.setMsgTime(currTime);
        // 设置消息的扩展
        message.setAttribute("type", TSEMConstants.TS_ATTR_JOIN);
        message.setAttribute(TSEMConstants.TS_ATTR_JOIN, isJoin);
        message.setAttribute(TSEMConstants.TS_ATTR_EIXT, !isJoin);

        TSEMRefreshEvent event = new TSEMRefreshEvent();
        event.setMessage(message);
        event.setType(TSEMRefreshEvent.TYPE_USER_EXIT);
        event.setStringExtra(content);
        EventBus.getDefault().post(event);
    }

    /**
     * 发送一条退出了群聊（解散，被移除）的透传，这里需要和接收方协商定义
     *
     * @param groupId
     * @param groupName
     */
    public static void sendEixtGroupMessage(String groupId, String groupName, String action) {
        long currTime = TSEMDateUtil.getCurrentMillisecond();
        EMMessage cmdMessage = EMMessage.createSendMessage(EMMessage.Type.CMD);
        cmdMessage.setChatType(EMMessage.ChatType.GroupChat);
        // 创建CMD 消息的消息体 并设置 action 为 disband
        EMCmdMessageBody body = new EMCmdMessageBody(action);
        cmdMessage.addBody(body);

        cmdMessage.setMsgTime(currTime);
        cmdMessage.setAttribute(TSEMConstants.TS_ATTR_ID, groupId);
        cmdMessage.setAttribute(TSEMConstants.TS_ATTR_NAME, groupName);

        TSEMessageEvent event = new TSEMessageEvent();
        event.setMessage(cmdMessage);
        event.setStatus(cmdMessage.status());
        EventBus.getDefault().post(event);
    }

    /**
     * 发送一条公告信息的透传，这里需要和接收方协商定义
     *
     * @param userName
     * @param time
     */
    public static void sendNoticeGroupMessage(String userName, String time, String content, String title, String groupId, boolean isOwner, String action) {
        long currTime = TSEMDateUtil.getCurrentMillisecond();
        EMMessage cmdMessage = EMMessage.createSendMessage(EMMessage.Type.CMD);
        cmdMessage.setChatType(EMMessage.ChatType.GroupChat);
        // 创建CMD 消息的消息体 并设置 action 为 disband
        EMCmdMessageBody body = new EMCmdMessageBody(action);
        cmdMessage.addBody(body);
        cmdMessage.setMsgTime(currTime);
        cmdMessage.setTo(groupId);
        // 设置消息的扩展
        cmdMessage.setAttribute("type", TSEMConstants.TS_ATTR_RELEASE_NOTICE);
        cmdMessage.setAttribute(TSEMConstants.TS_USER_NAME, userName);
        cmdMessage.setAttribute(TSEMConstants.TS_TIME, time);
        cmdMessage.setAttribute(TSEMConstants.TS_CONTENT, content);
        cmdMessage.setAttribute(TSEMConstants.TS_TITLE, title);
        cmdMessage.setAttribute(TSEMConstants.TS_GROUP_ID, groupId);
        cmdMessage.setAttribute(TSEMConstants.TS_GROUP_OWNER, isOwner);

        TSEMessageEvent event = new TSEMessageEvent();
        event.setMessage(cmdMessage);
        event.setStatus(cmdMessage.status());
        EventBus.getDefault().post(event);
        EMClient.getInstance().chatManager().sendMessage(cmdMessage);
    }

    /**
     * 创建群聊 快速编辑群名称
     *
     * @param content 内容
     * @param groupId 群 id
     * @param tagUser 谁 接收这条消息
     */
    public static void sendCreateGroupMessage(String content, String groupId, long tagUser) {
        EMMessage message = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
        message.addBody(new EMTextMessageBody(content));
        message.setFrom("admin");
        long currTime = TSEMDateUtil.getCurrentMillisecond();
        message.setMsgTime(currTime);
        message.setTo(groupId);
        message.setMsgId(TSEMConstants.TS_ATTR_GROUP_CRATE);
        message.setAttribute(TSEMConstants.TS_ATTR_GROUP_CRATE, true);
        message.setAttribute(TSEMConstants.TS_ATTR_TAG, tagUser);
        message.setChatType(EMMessage.ChatType.GroupChat);
        EMClient.getInstance().chatManager().saveMessage(message);
    }


    /**
     * 本地创建admin消息
     * @param content
     * @param groupId
     */
    public static EMMessage createLocalMessageFromAdmin(String content,String groupId,String type){

        //如果是自己退出了群聊，本地给自己发送一条消息
        EMMessage message = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
        message.addBody(new EMTextMessageBody(content));
        message.setTo(groupId);
        message.setFrom("admin");
        message.setChatType(EMMessage.ChatType.GroupChat);
        message.setMsgTime(System.currentTimeMillis());
        // 设置消息的扩展
        message.setAttribute("type", type);
        return message;

    }

    /**
     * 删除某条消息
     *
     * @param conversationId
     * @param msgId
     */
    public static void deleteMessage(String conversationId, String msgId) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(conversationId);
        conversation.removeMessage(msgId);
    }

    /**
     * 发送已经成为好友的消息
     * @param userId
     */
    public static void sendAgreeFriendApplyMessage(String userId){
        //创建会话，发送一条消息
        EMClient.getInstance().chatManager().getConversation(userId,
                EMConversation.EMConversationType.Chat, true);
        EMMessage message = EMMessage.createTxtSendMessage("我们已经是好友啦，一起来聊天吧！", userId);
        EMClient.getInstance().chatManager().sendMessage(message);
        EMClient.getInstance().chatManager().saveMessage(message);
    }


    /**
     * 保存退出群聊的信息到本地
     * @param chatId
     */
    public static void saveExitGroupInLocal(String chatId){

        //如果是自己退出了群聊，本地给自己发送一条消息
        EMMessage message = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
        message.addBody(new EMTextMessageBody("你已经退出了群聊"));
        message.setTo(chatId);
        message.setFrom("admin");
        message.setChatType(EMMessage.ChatType.GroupChat);
        message.setMsgTime(System.currentTimeMillis());
        // 设置消息的扩展
        message.setAttribute("type", TSEMConstants.TS_ATTR_JOIN);
        message.setAttribute(TSEMConstants.TS_ATTR_JOIN, false);
        message.setAttribute(TSEMConstants.TS_ATTR_EIXT, true);
        EMClient.getInstance().chatManager().getConversation(chatId).insertMessage(message);

    }

}
