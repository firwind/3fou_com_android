package com.zhiyicx.tspay;

import android.app.Activity;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringDef;
import android.support.v4.util.ArrayMap;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Describe
 * @Author zl
 * @Date 2017/5/15
 * @Contact master.jungle68@gmail.com
 */
public class TSPayClient {
    /**
     * 手机支付宝 APP 支付
     */
    public static final String CHANNEL_ALIPAY = "alipay";
    public static final String CHANNEL_ALIPAY_V2 = "AlipayOrder";
    public static final String CHANNEL_ALIPAY_SUCCESS = "9000";

    /**
     * 手机支付宝扫码支付
     */
    public static final String CHANNEL_ALIQRPAY = "alipay_qr";
    /**
     * 手机网页发起支付宝支付
     */
    public static final String CHANNEL_ALIWAPPAY = "alipay_wap";
    /**
     * 微信 APP 支付
     * 充值用 wx \ 提现用 wechat
     */
    public static final String CHANNEL_WXPAY = "wechat";
    public static final String CHANNEL_WX = "wx";
    public static final String CHANNEL_WXPAY_V2 = "WechatOrder";
    /**
     * 余额支付
     */
    public static final String CHANNEL_BALANCE = "balance";
    public static final String CHANNEL_BALANCE_V2 = "BalanceOrder";
    /**
     * 手机网页发起微信支付
     */
    public static final String CHANNEL_WXWAPPAY = "wx_wap";

    public static final ArrayMap<String, Integer> PAY_KEYS_TYPE;

    static {
        PAY_KEYS_TYPE = new ArrayMap<>();
        PAY_KEYS_TYPE.put(CHANNEL_ALIPAY, R.string.alipay);
        PAY_KEYS_TYPE.put(CHANNEL_ALIQRPAY, R.string.alipay);
        PAY_KEYS_TYPE.put(CHANNEL_ALIWAPPAY, R.string.alipay);
        PAY_KEYS_TYPE.put(CHANNEL_ALIPAY_V2, R.string.alipay);
        PAY_KEYS_TYPE.put(CHANNEL_WXPAY, R.string.wxpay);
        PAY_KEYS_TYPE.put(CHANNEL_WXPAY_V2, R.string.wxpay);
        PAY_KEYS_TYPE.put(CHANNEL_WX, R.string.wxpay);
        PAY_KEYS_TYPE.put(CHANNEL_WXWAPPAY, R.string.wxpay);
    }

    @StringDef({CHANNEL_ALIPAY, CHANNEL_ALIQRPAY, CHANNEL_ALIWAPPAY, CHANNEL_WXPAY,CHANNEL_WX, CHANNEL_WXWAPPAY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PayKey {
    }

    public static void pay(String payCredentials, Activity activity) {
//        Pingpp.createPayment(activity, payCredentials);
    }
}
