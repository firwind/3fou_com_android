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


public class VerifyFriendsActivity extends TSActivity<VerifyFriendsPresenter,VerifyFriendsFragment>{
    public static final String FRIENDS_ID = "friend_id";
    @Override
    protected VerifyFriendsFragment getFragment() {
        return VerifyFriendsFragment.getInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerVerifyFriendsComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .verifyFriendsPresenterModule(new VerifyFriendsPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }

    public static void startVerifyFriendsActivity(Context context,String friendsId){
        Intent intent = new Intent(context,VerifyFriendsActivity.class);
        intent.putExtra(FRIENDS_ID,friendsId);
        context.startActivity(intent);
    }
}
