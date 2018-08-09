package com.zhiyicx.thinksnsplus.data.source.remote;
/*
 * 文件名:货币相关接口  我的团队
 * 创建者：zhangl
 * 时  间：2018/7/19
 * 描  述：
 * 版  权: 九曲互动
 */

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyAddress;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyBalanceBean;

import java.util.List;

import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

import com.zhiyicx.thinksnsplus.data.beans.AccountBookListBean;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.ExchangeCurrencyRate;
import com.zhiyicx.thinksnsplus.data.beans.SystemConversationBean;
import com.zhiyicx.thinksnsplus.data.beans.TeamBean;
import com.zhiyicx.thinksnsplus.data.beans.WithdrawCurrencyBean;

import java.util.List;


public interface CurrencyClient {

    /**
     * 获取钱包首页币种列表
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_MY_CURRENCY_LIST)
    Observable<List<CurrencyBalanceBean>> getMyCurrencyList();

    /**
     * 获取币种地址列表
     * @param currency
     * @return
     */
    @GET(ApiConfig.APP_PATH_CURRENCY_ADDRESS_LIST)
    Observable<List<CurrencyAddress>> getCurrencyAddressList(@Query("currency") String currency);

    /**
     * 增加钱包地址
     * @param currency
     * @param address
     * @param mark
     * @return
     */
    @POST(ApiConfig.APP_PATH_CURRENCY_ADDRESS_MANAGE)
    Observable<String> addCurrencyAddress(@Query("currency")String currency,@Query("address")String address,@Query("mark")String mark);

    /**
     * 编辑钱包地址
     * @param address_id
     * @param address
     * @param mark
     * @return
     */
    @GET(ApiConfig.APP_PATH_CURRENCY_ADDRESS_MANAGE)
    Observable<String> editCurrencyAddress(@Query("address_id")String address_id,@Query("address")String address,@Query("mark")String mark);

    /**
     * 删除钱包地址
     * @param address_id
     * @return
     */
    @DELETE(ApiConfig.APP_PATH_CURRENCY_ADDRESS_MANAGE)
    Observable<String> deleteCurrencyAddress(@Query("address_id")String address_id);

    /**
     * 充币
     * @param currency
     * @return
     */
    @POST(ApiConfig.APP_PATH_CURRENCY_RECHARGE)
    Observable<BaseJson<String>> rechargeCurrency(@Query("currency")String currency);


    /**
     * 获取团队列表
     * @param type 获取币种
     * @param grade 推荐人等级
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_MY_TEAM_LIST)
    Observable<List<TeamBean.TeamListBean>> getTeamList(@Query ("type") String type, @Query("grade") int grade);
    /**
     * 获取团队收益明细
     * @param pid 获取id
     * @param currency 币种
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_DETAIL_LOG)
    Observable<List<TeamBean.TeamListBean>> getDetailLog(@Query ("pid") int pid, @Query("currency") String currency);

    /**
     * 账本
     * @param page
     * @param limit
     * @param target_type
     * @return
     */
    @GET(ApiConfig.APP_PATH_CURRENCY_ACCOUNT_BOOK_LIST)
    Observable<List<AccountBookListBean>> getAccountBookList(@Query("page") Integer page, @Query("limit") Integer limit ,
                                                             @Query("target_type") Integer target_type,@Query("currency")String currency);


    @GET(ApiConfig.APP_PATH_GET_TEAM_CURRENCY_LIST)
    Observable<List<CurrencyTypeBean>> getTeamCurrencyList();

    /**
     * 提币
     * @param currency
     * @param address
     * @param mark
     * @param isSave
     * @param remark
     * @return
     */
    @POST(ApiConfig.APP_PATH_WITHDRAW_CURRENCY)
    Observable<String> withdrawCurrency(@Query("currency")String currency,@Query("to_address")String address,
                                        @Query("mark")String mark,@Query("is_mark")String isSave,
                                        @Query("money")String money,@Query("remark")String remark);

    /**
     * 获取提币手续费
     * @param currency
     * @return
     */
    @POST(ApiConfig.APP_PATH_GET_WITHDRAW_RATE)
    Observable<WithdrawCurrencyBean> getWithdrawRate(@Query("currency")String currency);

    /**
     * 获取兑换比例
     * @param currency
     * @param currency2
     * @return
     */
    @POST(ApiConfig.APP_PATH_GET_EXCHANGE_RATE)
    Observable<ExchangeCurrencyRate> getExchangeRate(@Query("currency")String currency, @Query("currency2")String currency2);

    /**
     * 兑换
     * @param currency
     * @param currency2
     * @param number
     * @param phone
     * @param verify_code
     * @param password
     * @return
     */
    @POST(ApiConfig.APP_PATH_EXCHANGE_CURRENCY)
    Observable<String> exchangeCurrency(@Query("currency")String currency,@Query("currency2")String currency2,
                                        @Query("number2")String number,@Query("phone")String phone,
                                        @Query("verifiable_code")String verify_code,@Query("pay_password")String password);

}
