package com.zhiyicx.thinksnsplus.data.source.repository.i;
/*
 * 文件名: 货币公用的Repository
 * 创建者：zhangl
 * 时  间：2018/7/19
 * 描  述：
 * 版  权: 九曲互动
 */

import android.content.Context;

import com.zhiyicx.thinksnsplus.data.beans.CurrencyTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.TeamBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.List;

import rx.Observable;

public interface ICurrencyRepository {
    /**
     * 获取币种
     * @return
     */
    Observable<List<CurrencyTypeBean>> getCurrencyType(Context context);
    /**
     * 获取币种
     * @return
     */
    Observable<TeamBean> getTeamList(Context context);
    /**
     * 获取收益明细
     * @return
     */
    Observable<List<TeamBean.TeamListBean>> getEarningList(Context context,int id);

    /**
     * 获取首页币种列表
     * @return
     */
    Observable<String> getMyCurrencyList();

    /**
     * 获取币种地址列表
     * @param currency
     * @return
     */
    Observable<String> getCurrencyAddressList(String currency);

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

}
