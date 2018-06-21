package com.zhiyicx.baseproject.em.manager.util;


import android.text.TextUtils;

import com.hyphenate.chat.EMConversation;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Jliuer
 * @Date 18/02/01 11:06
 * @Email Jliuer@aliyun.com
 * @Description
 * 会话扩展处理类，用来处理会话对象的扩展信息，
 * 包括：
 * TODO 群组@，
 * 会话置顶，
 * 会话最后操作时间，
 * 会话草稿，
 */
public class TSEmConversationExtUtils {

    /**
     * 会话实体类构造函数，根据传入的会话对象去
     *
     * @param conversation
     */
    public TSEmConversationExtUtils(EMConversation conversation) {

    }

    /**
     * 设置会话置顶状态
     *
     * @param conversation 要置顶的会话对象
     * @param pushpin      设置会话是否置顶
     */
    public static void setConversationPushpin(EMConversation conversation, boolean pushpin) {
        // 获取当前会话对象的扩展
        String ext = conversation.getExtField();
        JSONObject jsonObject = null;
        try {
            // 判断扩展内容是否为空
            if (TextUtils.isEmpty(ext)) {
                jsonObject = new JSONObject();
            } else {
                jsonObject = new JSONObject(ext);
            }
            // 将扩展信息设置给外层的 JSONObject 对象
            jsonObject.put(TSEMConstants.TS_ATTR_PUSHPIN, pushpin);
            // 将扩展信息保存到 Conversation 对象的扩展中去
            conversation.setExtField(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前会话是否置顶
     *
     * @param conversation 需要操作的会话对象
     * @return 返回当前会话是否置顶
     */
    public static boolean getConversationPushpin(EMConversation conversation) {
        // 获取当前会话对象的扩展
        String ext = conversation.getExtField();
        // 判断扩展内容是否为空
        if (TextUtils.isEmpty(ext)) {
            return false;
        }
        try {
            // 根据扩展获取Json对象，然后获取置顶的属性，
            JSONObject jsonObject = new JSONObject(ext);
            return jsonObject.optBoolean(TSEMConstants.TS_ATTR_PUSHPIN);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 设置会话的最后时间
     *
     * @param conversation 要设置的会话对象
     */
    public static void setConversationLastTime(EMConversation conversation) {
        // 获取当前会话对象的扩展
        String ext = conversation.getExtField();
        JSONObject jsonObject = null;
        try {
            // 判断扩展内容是否为空
            if (TextUtils.isEmpty(ext)) {
                jsonObject = new JSONObject();
            } else {
                jsonObject = new JSONObject(ext);
            }
            // 根据当前会话消息数是否为零来设置会话的最后时间
            if (conversation.getAllMessages().size() == 0) {
                jsonObject.put(TSEMConstants.TS_ATTR_LAST_TIME, System.currentTimeMillis());
            } else {
                jsonObject.put(TSEMConstants.TS_ATTR_LAST_TIME, conversation.getLastMessage().getMsgTime());
            }
            // 将扩展信息保存到 Conversation 对象的扩展中去
            conversation.setExtField(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取会话的最后时间
     *
     * @param conversation 需要获取的会话对象
     * @return 返回此会话最后的时间
     */
    public static long getConversationLastTime(EMConversation conversation) {
        // 获取当前会话对象的扩展
        String ext = conversation.getExtField();
        JSONObject jsonObject = null;
        try {
            // 判断扩展内容是否为空
            if (TextUtils.isEmpty(ext)) {
                jsonObject = new JSONObject();
            } else {
                jsonObject = new JSONObject(ext);
            }
            // 根据扩展的key获取扩展的值
            return jsonObject.optLong(TSEMConstants.TS_ATTR_LAST_TIME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }

    /**
     * 设置当前会话草稿
     *
     * @param conversation 需要设置的会话对象
     * @param draft        需要设置的草稿内容
     */
    public static void setConversationDraft(EMConversation conversation, String draft) {
        // 获取当前会话对象的扩展
        String ext = conversation.getExtField();
        JSONObject jsonObject = null;
        try {
            // 判断扩展内容是否为空
            if (TextUtils.isEmpty(ext)) {
                jsonObject = new JSONObject();
            } else {
                jsonObject = new JSONObject(ext);
            }
            // 将扩展信息设置给 JSONObject 对象
            jsonObject.put(TSEMConstants.ML_ATTR_DRAFT, draft);
            // 将扩展信息保存到 EMConversation 对象扩展中去
            conversation.setExtField(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前会话的草稿内容
     *
     * @param conversation 当前会话
     * @return 返回草稿内容
     */
    public static String getConversationDraft(EMConversation conversation) {
        // 获取当前会话对象的扩展
        String ext = conversation.getExtField();
        JSONObject jsonObject = null;
        try {
            // 判断扩展内容是否为空
            if (TextUtils.isEmpty(ext)) {
                jsonObject = new JSONObject();
            } else {
                jsonObject = new JSONObject(ext);
            }
            // 根据扩展的key获取扩展的值
            return jsonObject.optString(TSEMConstants.ML_ATTR_DRAFT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 标记会话为未读状态
     *
     * @param conversation 需要标记的会话
     * @param unread       设置未读状态
     */
    public static void setConversationUnread(EMConversation conversation, boolean unread) {
        // 获取当前会话对象的扩展
        String ext = conversation.getExtField();
        JSONObject jsonObject = null;
        try {
            // 判断扩展内容是否为空
            if (TextUtils.isEmpty(ext)) {
                jsonObject = new JSONObject();
            } else {
                jsonObject = new JSONObject(ext);
            }
            // 将扩展信息设置给 JSONObject 对象
            jsonObject.put(TSEMConstants.ML_ATTR_UNREAD, unread);
            // 将扩展信息保存到 EMConversation 对象扩展中去
            conversation.setExtField(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取 conversation 对象扩展中的未读状态
     *
     * @param conversation 当前会话
     * @return 返回未读状态
     */
    public static boolean getConversationUnread(EMConversation conversation) {
        // 获取当前会话对象的扩展
        String ext = conversation.getExtField();
        JSONObject jsonObject = null;
        try {
            // 判断扩展内容是否为空
            if (TextUtils.isEmpty(ext)) {
                jsonObject = new JSONObject();
            } else {
                jsonObject = new JSONObject(ext);
            }
            // 根据扩展的key获取扩展的值
            return jsonObject.optBoolean(TSEMConstants.ML_ATTR_UNREAD);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
