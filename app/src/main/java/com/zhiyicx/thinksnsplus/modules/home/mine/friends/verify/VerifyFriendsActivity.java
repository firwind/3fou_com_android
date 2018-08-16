package com.zhiyicx.thinksnsplus.modules.home.mine.friends.verify;
/*
 * 文件名：验证好友界面
 * 创建者：zl
 * 时  间：2018/8/16 0016
 * 描  述：
 * 版  权：九曲互动
 * 
 */
import android.content.Context;
import android.content.Intent;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.i.IntentKey;


public class VerifyFriendsActivity extends TSActivity<VerifyFriendsPresenter,VerifyFriendsFragment>{

    @Override
    protected VerifyFriendsFragment getFragment() {
        return VerifyFriendsFragment.getInstance(getIntent().getStringExtra(IntentKey.USER_ID));
    }

    @Override
    protected void componentInject() {
        DaggerVerifyFriendsComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .verifyFriendsPresenterModule(new VerifyFriendsPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }

    public static void startVerifyFriendsActivity(Context context,String userId){
        Intent intent = new Intent(context,VerifyFriendsActivity.class);
        intent.putExtra(IntentKey.USER_ID,userId);
        context.startActivity(intent);
    }
}
