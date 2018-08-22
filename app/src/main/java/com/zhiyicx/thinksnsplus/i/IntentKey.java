package com.zhiyicx.thinksnsplus.i;

/**
 * <pre>
 *     @author : huwenyong
 *     @date : 2018/6/23 11:17
 *     desc :
 *     version : 1.0
 * <pre>
 */

public interface IntentKey {

    String USER_ID = "user_id";
    String GROUP_ID = "group_id";
    String GROUP_INFO = "group_info";
    String IS_TOURIST_LOGIN = "is_tourist_login";//游客登陆
    String CONVERSATION_ID = "conversation_id";//会话id
    String CONVERSATION_TYPE = "conversation_type";//会话type
    String CURRENCY_TYPE = "currency_type";//行情type
    String CURRENCY_IN_MARKET = "currency_in_market";//在交易所的货币

    String IS_SELECT = "is_select";//是否选择

    String IS_ONLY_OFFICIAL = "is_only_official";//是否只显示官方群

    String IS_GET_SCAN_RESULT = "is_get_scan_result";

    String RESULT_SCAN = "result_scan";//
    String RESULT_CURRENCY_ADDRESS = "result_currency_address";

    String SEARCH_TAG = "search_tag";

    String PASSWORD_TYPE = "password_type";

    String NOTIFICATION_REVIEW_TYPE = "notification_review_type";

    int TYPE_PASSWORD_LOGIN = 0;
    int TYPE_PASSWORD_PAY = 1;

    int NOTIFICATION_REVIEW_FRIEND = 0;
    int NOTIFICATION_REVIEW_GROUP = 1;

    int REQ_CODE_SELECT_CURRENCY_ADDRESS = 200;
    int REQ_CODE_GET_SCAN_RESULT = 201;

}
