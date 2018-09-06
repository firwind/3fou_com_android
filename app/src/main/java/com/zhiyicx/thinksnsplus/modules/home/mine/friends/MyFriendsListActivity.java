package com.zhiyicx.thinksnsplus.modules.home.mine.friends;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author Catherine
 * @describe 我的好友页面
 * @date 2017/12/22
 * @contact email:648129313@qq.com
 */

public class MyFriendsListActivity extends TSActivity<MyFriendsListPresenter, MyFriendsListFragment>{
    public static final String IS_SHOW_TOOR_BAR = "isShowToolbar";
    @Override
    protected MyFriendsListFragment getFragment() {
        return MyFriendsListFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        /*DaggerMyFriendsListComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .myFriendsListPresenterModule(new MyFriendsListPresenterModule(mContanierFragment))
                .build()
                .inject(this);*/
    }

    public static void startMyFriendsListActivity(Context context,boolean isShowToolbar){
        Intent intent = new Intent(context,MyFriendsListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_SHOW_TOOR_BAR,isShowToolbar);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
