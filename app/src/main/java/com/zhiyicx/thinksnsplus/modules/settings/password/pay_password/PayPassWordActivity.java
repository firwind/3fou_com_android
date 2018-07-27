package com.zhiyicx.thinksnsplus.modules.settings.password.pay_password;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.modules.settings.password.PassWordManagerFragment;

/**
 * Created by Administrator on 2018/7/27 0027.
 */

public class PayPassWordActivity extends TSActivity<BasePresenter,PayPassWordFragment> {
    @Override
    protected PayPassWordFragment getFragment() {
        return PayPassWordFragment.newInstance();
    }

    @Override
    protected void componentInject() {

    }

}
