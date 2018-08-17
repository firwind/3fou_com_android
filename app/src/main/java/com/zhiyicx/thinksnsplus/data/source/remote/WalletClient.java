package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.IntegrationRuleBean;
import com.zhiyicx.thinksnsplus.data.beans.PayStrV2Bean;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessV2Bean;
import com.zhiyicx.thinksnsplus.data.beans.WXPayInfo;
import com.zhiyicx.thinksnsplus.data.beans.WalletConfigBean;
import com.zhiyicx.thinksnsplus.data.beans.WithdrawResultBean;
import com.zhiyicx.thinksnsplus.data.beans.WithdrawalsListBean;
import com.zhiyicx.thinksnsplus.data.beans.integration.IntegrationConfigBean;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PAHT_INTEGRATION_CONFIG;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PAHT_INTEGRATION_ORDERS;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PAHT_INTEGRATION_RECHARGE;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PAHT_INTEGRATION_RECHARGE_SUCCESS;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PAHT_INTEGRATION_RECHARGE_V2;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PAHT_INTEGRATION_VERIFY_V2;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PAHT_INTEGRATION_WITHDRAWALS;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PAHT_UPGRADE_GROUP_V2;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PAHT_WALLET_BALANCE_TO_INTEGRATION;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PAHT_WALLET_CONFIG;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PAHT_WALLET_RECHARGE;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PAHT_WALLET_RECHARGE_SUCCESS;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PAHT_WALLET_RECHARGE_SUCCESS_CALLBACK;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PAHT_WALLET_RECHARGE_SUCCESS_LIST;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PAHT_WALLET_RECHARGE_V2;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PAHT_WALLET_VERIFY_V2;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PAHT_WALLET_WITHDRAW;


/**
 * @Describe
 * @Author zl
 * @Date 2017/5/27
 * @Contact master.jungle68@gmail.com
 */
public interface WalletClient {

    @GET(APP_PAHT_WALLET_CONFIG)
    Observable<WalletConfigBean> getWalletConfig();

    /**
     * 提现
     *
     * @param value   用户需要提现的金额
     * @param type    用户提现账户方式
     * @param account 用户提现账户
     * @return
     */
    @FormUrlEncoded
    @POST(APP_PAHT_WALLET_WITHDRAW)
    Observable<WithdrawResultBean> withdraw(@Field("value") long value, @Field("type") String type, @Field("account") String account);

    /**
     * 提现明细
     *
     * @param limit 可以设置获取数量
     * @param after 获取更多数据，上一次获取列表的最后一条 ID
     * @return
     */
    @GET(APP_PAHT_WALLET_WITHDRAW)
    Observable<List<WithdrawalsListBean>> getWithdrawList(@Query("limit") Integer limit, @Query("after") int after);

    /**
     * 取回凭据
     *
     * @param order
     * @return
     */
    @GET(APP_PAHT_WALLET_RECHARGE_SUCCESS)
    Observable<RechargeSuccessBean> rechargeSuccess(@Path("order") String order);

    /**
     * @param limit
     * @param after
     * @param action income - 收入 expenses - 支出
     * @return
     */
    @GET(APP_PAHT_WALLET_RECHARGE_SUCCESS_LIST)
    Observable<List<RechargeSuccessBean>> getRechargeSuccessList(@Query("limit") Integer limit, @Query("after") int after, @Query("action") String
            action);

    @GET(APP_PAHT_WALLET_RECHARGE_SUCCESS_CALLBACK)
    Observable<RechargeSuccessBean> rechargeSuccessCallBack(@Path("charge") String charge);

    /**
     * 发起充值
     */
    @FormUrlEncoded
    @POST(APP_PAHT_WALLET_RECHARGE)
    Observable<PayStrV2Bean> getPayStr(@Field("type") String channel, @Field("amount") long amount, @Field("extra") String extra);

    /**
     * 发起充值V2 2018-5-21 09:55:47 by tym
     */
    @FormUrlEncoded
    @POST(APP_PAHT_WALLET_RECHARGE_V2)
    Observable<BaseJsonV2<String>> getAliPayStr(@Field("type") String channel, @Field("amount") long amount, @Field("from") String from);

    @FormUrlEncoded
    @POST(APP_PAHT_INTEGRATION_RECHARGE_V2)
    Observable<BaseJsonV2<String>> getIntegrationAliPayStr(@Field("type") String channel, @Field("amount") long amount, @Field("from") String from);

    @FormUrlEncoded
    @POST(APP_PAHT_WALLET_RECHARGE_V2)
    Observable<BaseJsonV2<WXPayInfo>> getWXPayStr(@Field("type") String channel, @Field("amount") long amount, @Field("from") String from);

    /**
     * 升级群微信支付
     *
     * @param upgradeType
     * @param channel
     * @param amount
     * @param from
     * @param fewmouths
     * @return
     */
    @FormUrlEncoded
    @POST(APP_PAHT_UPGRADE_GROUP_V2)
    Observable<BaseJsonV2<WXPayInfo>> getUpgradeGroupWXPayStr(
                                                            @Field("im_group_id") String groupId,
                                                            @Field("type") String upgradeType,
                                                            @Field("paytype") String channel,
                                                            @Field("amount") long amount,
                                                            @Field("from") String from,
                                                            @Field("fewmouths") int fewmouths);

    /**
     * 升级群支付宝支付
     *
     * @param amount
     * @return
     */

    @FormUrlEncoded
    @POST(APP_PAHT_UPGRADE_GROUP_V2)
    Observable<BaseJsonV2<String>> getUpgradeGroupAliPayStr(
            @Field("im_group_id") String groupId,
            @Field("type") String upgradeType,
            @Field("paytype") String channel,
            @Field("amount") long amount,
            @Field("from") String from,
            @Field("fewmouths") int fewmouths);
    /**
     * 升级群余额支付
     *
     * @param amount
     * @return
     */

    @FormUrlEncoded
    @POST(APP_PAHT_UPGRADE_GROUP_V2)
    Observable<String> getUpgradeGroupBalancePayStr(
            @Field("im_group_id") String groupId,
            @Field("type") String upgradeType,
            @Field("paytype") String channel,
            @Field("amount") long amount,
            @Field("from") String from,
            @Field("fewmouths") int fewmouths);

    @FormUrlEncoded
    @POST(APP_PAHT_INTEGRATION_RECHARGE_V2)
    Observable<BaseJsonV2<WXPayInfo>> getIntegrationWXPayStr(@Field("type") String channel, @Field("amount") long amount, @Field("from") String from);


    /**
     * 支付宝充值余额验证V2 2018-5-21 09:55:47 by tym
     * 三个参数信息是支付宝返回来的
     */
    @FormUrlEncoded
    @POST(APP_PAHT_WALLET_VERIFY_V2)
    Observable<BaseJsonV2<String>> aliPayVerify(@Field("memo") String memo, @Field("result") String result, @Field("resultStatus") String resultStatus);

    /**
     * 支付宝充值糖果验证V2 2018-5-21 09:55:47 by tym
     * 三个参数信息是支付宝返回来的
     */
    @FormUrlEncoded
    @POST(APP_PAHT_INTEGRATION_VERIFY_V2)
    Observable<BaseJsonV2<String>> aliPayIntegrationVerify(@Field("memo") String memo, @Field("result") String result, @Field("resultStatus") String resultStatus);

    /**
     * 钱包余额转糖果
     *
     * @param amount 转账金额，分单位
     * @return
     */
    @FormUrlEncoded
    @POST(APP_PAHT_WALLET_BALANCE_TO_INTEGRATION)
    Observable<BaseJsonV2> balance2Integration(@Field("amount") long amount);


    /*******************************************  糖果  *********************************************/
    /**
     * @return 糖果配置信息
     */
    @GET(APP_PAHT_INTEGRATION_CONFIG)
    Observable<IntegrationConfigBean> getIntegrationConfig();

    /**
     * 获取糖果充值信息
     */
    /**
     * @param type   充值方式 （见「启动信息接口」或者「钱包信息」）
     * @param amount 用户充值金额，单位为真实货币「分」单位，充值完成后会根据糖果兑换比例增加相应数量的糖果
     * @param extra  object,array 拓展信息字段，见 支付渠道-extra-参数说明
     * @return
     */
    @FormUrlEncoded
    @POST(APP_PAHT_INTEGRATION_RECHARGE)
    Observable<PayStrV2Bean> getIntegrationPayStr(@Field("type") String type, @Field("amount") long amount, @Field("extra") String extra);


    /**
     * 取回凭据
     *
     * @param order
     * @return
     */
    @GET(APP_PAHT_INTEGRATION_RECHARGE_SUCCESS)
    Observable<RechargeSuccessV2Bean> integrationRechargeSuccess(@Path("order") String order);

    /**
     * 糖果流水
     *
     * @param limit  数据返回条数
     * @param after  翻页数据id
     * @param action 筛选类型 recharge - 充值记录 cash - 提现记录 默认为全部
     * @param type   筛选类型 1 - 收入 -1 - 支出 默认为全部
     * @return
     */
    @GET(APP_PAHT_INTEGRATION_ORDERS)
    Observable<List<RechargeSuccessV2Bean>> integrationOrdersSuccess(@Query("limit") Integer limit, @Query("after") int after, @Query("action") String
            action, @Query("type") Integer type);


    /**
     * 发起糖果提现
     *
     * @param amount 提取糖果，发起该操作后会根据糖果兑换比例取人民币分单位整数后扣减相应糖果
     * @return
     */
    @FormUrlEncoded
    @POST(APP_PAHT_INTEGRATION_WITHDRAWALS)
    Observable<BaseJsonV2> integrationWithdrawals(@Field("amount") Integer amount);

    /**
     * 获取糖果规则
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_INTEGRATION_RULES)
    Observable<List<IntegrationRuleBean>> getIntegrationRules();

    /**
     * 获取糖果红包数量
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_INTEGRATION_RED_PACKET_NUM)
    Observable<BaseJsonV2<String>> getIntegrationRedPacketNum();

    /**
     * 领取糖果红包
     * @return
     */
    @GET(ApiConfig.APP_PATH_RECEIVE_INTEGRATION_RED_PACKET)
    Observable<String> receiveIntegrationRedPacket();
    /**
     * 兑换FCC
     * @return
     */
    @POST(ApiConfig.APP_PATH_RECEIVE_CONVERSION_FCC)
    Observable<String> conversionFcc(@Query("amount") String amount);

}
