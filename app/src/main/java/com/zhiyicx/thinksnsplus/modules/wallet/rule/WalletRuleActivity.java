package com.zhiyicx.thinksnsplus.modules.wallet.rule;

import com.zhiyicx.baseproject.base.TSActivity;

/**
 * @Describe 钱包充值提现规则页
 * @Author zl
 * @Date 2017/5/22
 * @Contact master.jungle68@gmail.com
 */
public class WalletRuleActivity extends TSActivity {


    @Override
    protected void componentInject() {

    }

    @Override
    protected WalletRuleFragment getFragment() {
        return WalletRuleFragment.newInstance(getIntent().getExtras());
    }

}
