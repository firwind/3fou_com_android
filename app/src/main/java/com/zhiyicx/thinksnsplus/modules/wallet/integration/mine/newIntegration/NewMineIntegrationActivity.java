package com.zhiyicx.thinksnsplus.modules.wallet.integration.mine.newIntegration;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * author: huwenyong
 * date: 2018/8/3 19:10
 * description:
 * version:
 */

public class NewMineIntegrationActivity extends TSActivity<NewMineIntegrationPresenter,NewMineIntegrationFragment>{
    @Override
    protected NewMineIntegrationFragment getFragment() {
        return NewMineIntegrationFragment.newInstance();
    }

    @Override
    protected void componentInject() {
        DaggerNewMineIntegrationComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .newMineIntegrationPresenterMoudle(new NewMineIntegrationPresenterMoudle(mContanierFragment))
                .build()
                .inject(this);
    }
}
