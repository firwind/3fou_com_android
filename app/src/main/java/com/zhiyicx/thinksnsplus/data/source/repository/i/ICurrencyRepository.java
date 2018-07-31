package com.zhiyicx.thinksnsplus.data.source.repository.i;
/*
 * 文件名: 货币公用的Repository
 * 创建者：zhangl
 * 时  间：2018/7/19
 * 描  述：
 * 版  权: 九曲互动
 */

import android.content.Context;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.AccountBookListBean;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyAddress;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyBalanceBean;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.ExchangeCurrencyRate;
import com.zhiyicx.thinksnsplus.data.beans.TeamBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.WithdrawCurrencyBean;

import java.util.List;

import rx.Observable;

public interface ICurrencyRepository {
    /**
     * 获取币种
     * @return
     */
    Observable<List<CurrencyTypeBean>> getCurrencyType();
    /**
     * 获取币种
     * @return
     */
    Observable<List<TeamBean.TeamListBean>> getTeamList(String type,int grade);
    /**
     * 获取收益明细
     * @return
     */
    Observable<List<TeamBean.TeamListBean>> getEarningList(Context context,int id);

    /**
     * 获取首页币种列表
     * @return
     */
    Observable<List<CurrencyBalanceBean>> getMyCurrencyList();

    /**
     * 获取币种地址列表
     * @param currency
     * @return
     */
    Observable<List<CurrencyAddress>> getCurrencyAddressList(String currency);

    /**
     * 添加币种地址
     * @param currency
     * @param address
     * @param mark
     * @return
     */
    Observable<String> addCurrencyAddress(String currency,String address,String mark);

    /**
     * 编辑币种地址
     * @param address_id
     * @param address
     * @param mark
     * @return
     */
    Observable<String> editCurrencyAddress(String address_id,String address,String mark);

    /**
     * 删除币种地址
     * @param address_id
     * @return
     */
    Observable<String> deleteCurrencyAddress(String address_id);

    /**
     * 获取账本明细
     * @param offset
     * @param
     * @param target_type
     * @return
     */
    Observable<List<AccountBookListBean>> getAccountBookList(Long offset , Integer target_type);

    /**
     * 充币（获取钱包地址）
     * @param currency
     * @return
     */
    Observable<BaseJson<String>> rechargeCurrencyAddress(String currency);

    /**
     * 提币
     * @param currency
     * @param address
     * @param mark
     * @param isSave
     * @param money
     * @param remark
     * @return
     */
    Observable<String> withdrawCurrency(String currency, String address, String mark, boolean isSave,
                                          String money, String remark);

    /**
     * 获取转账手续费率
     * @param currency
     * @return
     */
    Observable<WithdrawCurrencyBean> getWithdrawRate(String currency);

    /**
     * 获取兑换比例
     * @param currency
     * @param currency2
     * @return
     */
    Observable<ExchangeCurrencyRate> getExchangeRate(String currency,String currency2);

    /**
     * 兑换币
     * @param currency
     * @param currency2
     * @param num
     * @param verifyCode
     * @param password
     * @return
     */
    Observable<String> exchangeCurrency(String currency,String currency2,String num,String phone,String verifyCode,String password);

}
