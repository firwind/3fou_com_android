package com.zhiyicx.thinksnsplus.data.source.repository.i;

import com.zhiyicx.common.base.BaseJsonV2;
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
import retrofit2.http.POST;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PAHT_WALLET_BALANCE_TO_INTEGRATION;

/**
 * @Describe
 * @Author zl
 * @Date 2017/12/26
 * @Contact master.jungle68@gmail.com
 */
public interface IBillRepository {


    Observable<List<RechargeSuccessBean>> getBillList(int after, String action);

    Observable<List<RechargeSuccessBean>> dealRechargeList(Observable<List<RechargeSuccessBean>> data);

    Observable<WalletConfigBean> getWalletConfig();

    void getWalletConfigWhenStart(Long user_id);

    Observable<RechargeSuccessBean> rechargeSuccessCallBack(String charge);

    Observable<RechargeSuccessBean> rechargeSuccess(String charge);

    Observable<WithdrawResultBean> withdraw(double value, String type, String account);

    /**
     * @param after 获取更多数据，上一次获取列表的最后一条 ID
     * @return
     */
    Observable<List<WithdrawalsListBean>> getWithdrawListDetail(int after);

    /**
     * 获取支付信息
     *
     * @param channel 支付渠道
     * @param amount  支付金额
     * @return
     */
    Observable<PayStrV2Bean> getPayStr(String channel, double amount);

    /**
     * 获取ali支付信息V2
     * 不在调用 ping++
     *
     * @param channel 支付渠道
     * @param amount  支付金额
     * @return
     */
    Observable<BaseJsonV2<String>> getAliPayStr(String channel, double amount);

    /**
     * 获取升级群支付宝支付信息
     *
     * @param amount
     * @return
     */
    Observable<BaseJsonV2<String>> getUpgradeGroupAliPayStr(String groupId, String upGradeType, String channel, double amount, int fewmouths);

    /**
     * 获取wx支付信息V2
     *
     * @param channel
     * @param amount
     * @return
     */
    Observable<BaseJsonV2<WXPayInfo>> getWXPayStr(String channel, double amount);

    /**
     * 获取升级群微信支付信息
     *
     * @param amount
     * @return
     */
    Observable<BaseJsonV2<WXPayInfo>> getUpgradeGroupWXPayStr(String groupId, String upGradeType, String channel, double amount, int fewmouths);

    /**
     * 获取升级群支付宝信息
     *
     * @param amount
     * @return
     */
    Observable<String> getUpgradeGroupBalancePayStr(String groupId, String upGradeType, String channel, double amount, int fewmouths);

    /**
     * 支付宝支付验证，3个参数都是支付宝返回
     *
     * @param memo
     * @param result
     * @param resultStatus
     * @return
     */
    Observable<BaseJsonV2<String>> aliPayVerify(String memo, String result, String resultStatus);


    Observable<BaseJsonV2<String>> aliPayIntegrationVerify(String memo, String result, String resultStatus);

    /**
     * 微信支付验证，3个参数都是支付宝返回
     *
     * @param memo
     * @param result
     * @param resultStatus
     * @return
     */
    Observable<BaseJsonV2<String>> wxPayVerify(String memo, String result, String resultStatus);

    /**
     * 钱包余额转糖果
     *
     * @param amount 转账金额，分单位
     * @return
     */
    Observable<BaseJsonV2> balance2Integration(long amount);


    /*******************************************  糖果  *********************************************/
    /**
     * @return 糖果配置信息
     */
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
    Observable<PayStrV2Bean> getIntegrationPayStr(String type, long amount, String extra);

    Observable<BaseJsonV2<String>> getIntegrationAliPayStr(String channel, double amount);

    Observable<BaseJsonV2<WXPayInfo>> getIntegrationWXPayStr(String channel, double amount);

    /**
     * @param order
     * @return 取回凭据
     */
    Observable<RechargeSuccessV2Bean> integrationRechargeSuccess(String order);

    /**
     * @param limit  数据返回条数
     * @param after  翻页数据id
     * @param action 筛选类型 recharge - 充值记录 cash - 提现记录 默认为全部
     * @param type   筛选类型 1 - 收入 -1 - 支出 默认为全部
     * @return 糖果流水
     */
    Observable<List<RechargeSuccessV2Bean>> integrationOrdersSuccess(Integer limit, int after, String
            action, Integer type);


    /**
     * 发起糖果提现
     *
     * @param amount 提取糖果，发起该操作后会根据糖果兑换比例取人民币分单位整数后扣减相应糖果
     * @return
     */
    Observable<BaseJsonV2> integrationWithdrawals(Integer amount);


}
