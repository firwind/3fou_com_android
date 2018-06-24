package com.zhiyicx.thinksnsplus.modules.home.common.invite;

import android.content.Context;
import android.content.Intent;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.baseproject.impl.share.ShareWithoutViewModule;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * <pre>
 *     @author : huwenyong
 *     @date : 2018/6/24 9:31
 *     desc :
 *     version : 1.0
 * <pre>
 */

public class InviteShareActivity extends TSActivity<InviteSharePresenter,InviteShareFragment>{

    public static Intent newIntent(Context mContext){
        Intent intent = new Intent(mContext,InviteShareActivity.class);
        return intent;
    }

    @Override
    protected InviteShareFragment getFragment() {
        return InviteShareFragment.newInstance();
    }

    @Override
    protected void componentInject() {
        DaggerInviteShareComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .inviteSharePresenterModule(new InviteSharePresenterModule(mContanierFragment))
                .shareWithoutViewModule(new ShareWithoutViewModule(this))
                .build()
                .inject(this);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UmengSharePolicyImpl.onActivityResult(requestCode, resultCode, data, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UmengSharePolicyImpl.onDestroy(this);
    }


}
