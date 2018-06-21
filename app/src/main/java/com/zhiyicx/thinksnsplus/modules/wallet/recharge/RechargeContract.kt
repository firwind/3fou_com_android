package com.zhiyicx.thinksnsplus.modules.wallet.recharge

import android.app.Activity
import com.zhiyicx.baseproject.base.IBaseTouristPresenter
import com.zhiyicx.common.mvp.i.IBaseView
import com.zhiyicx.thinksnsplus.data.beans.PayStrV2Bean
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean
import com.zhiyicx.tspay.TSPayClient

/**
 * @Describe
 * @Author zl
 * @Date 2017/4/26
 * @Contact master.jungle68@gmail.com
 */

interface RechargeContract {

    interface View : IBaseView<Presenter> {
        val money: Double
        fun getCurrentActivity(): Activity
        fun payCredentialsResult(payStrBean: PayStrV2Bean)
        fun payCredentialsResult(payStrBean: String)
        fun configSureBtn(enable: Boolean)
        fun rechargeSuccess(rechargeSuccessBean: RechargeSuccessBean)
        fun initmRechargeInstructionsPop()

        fun useInputMonye(): Boolean
    }

    interface Presenter : IBaseTouristPresenter {
        fun getPayStr(@TSPayClient.PayKey channel: String, amount: Double)
        fun getAliPayStr(@TSPayClient.PayKey channel: String, amount: Double)
        fun getWXPayStr(@TSPayClient.PayKey channel: String, amount: Double)
        fun rechargeSuccess(charge: String)
        fun rechargeSuccessCallBack(charge: String)
    }
}
