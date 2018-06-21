package com.zhiyicx.thinksnsplus.utils;

import android.content.Context;

import com.zhiyicx.common.utils.SharePreferenceUtils;

import java.util.HashSet;

/**
 * @author zl
 * @describe
 * @date 2018/4/12
 * @contact master.jungle68@gmail.com
 */
public class TSImHelperUtils {
//    private static final String SHAREPREFERENCE_IMHELPERS_DELETED_ID = "imhlepers_deleted_ids";

    /**
     * 记录当前小助手的用户的消息是清空过历史了的，再次创建小助手聊天的时候就不要再加入聊天提示信息了
     *
     * @param context
     * @param helperId
     */
    public static void saveDeletedHistoryMessageHelper(Context context, String helperId, String currenUserId) {
        HashSet<String> helpers = getDeletedHistoryMessageHelpers(context, currenUserId);
        helpers.add(helperId);
        SharePreferenceUtils.saveObject(context, currenUserId, helpers);
    }

    /**
     * @param context
     * @param helperId
     * @return 检查当前小助手是否有过清空
     */
    public static boolean getMessageHelperIsDeletedHistory(Context context, String helperId, String currenUserId) {
        HashSet<String> helpers = getDeletedHistoryMessageHelpers(context, currenUserId);
        return helpers.contains(helperId);
    }

    /**
     * @return 删除过小助手历史消息的小助手 ids
     */
    public static HashSet<String> getDeletedHistoryMessageHelpers(Context context, String currenUserId) {
        HashSet<String> helpers = SharePreferenceUtils.getObject(context, currenUserId);
        if (helpers == null) {
            helpers = new HashSet<>();
        }
        return helpers;
    }
}
