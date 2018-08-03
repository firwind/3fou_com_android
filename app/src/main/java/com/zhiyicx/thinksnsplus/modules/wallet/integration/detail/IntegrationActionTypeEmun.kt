package com.zhiyicx.thinksnsplus.modules.wallet.integration.detail

/**
 * @Describe 糖果操作类型 doc {@see https://slimkit.github.io/plus-docs/v2/core/currency}
 * @Author zl
 * @Date 2018/1/24
 * @Contact master.jungle68@gmail.com
 */
object IntegrationActionTypeEmun {
    /**
     * target_type	string	操作类型 目前有：
     * default - 默认操作、
     * commodity - 购买糖果商品、
     * user - 用户到用户流程（如采纳、付费置顶等）、
     * task - 糖果任务、
     * recharge - 充值、
     * cash - 糖果提取
     */
    val DEFAULT = "default"
    val COMMODITY = "commodity"
    val TASK = "task"
    val RECHARGE = "recharge"
    val CASH = "cash"

}
