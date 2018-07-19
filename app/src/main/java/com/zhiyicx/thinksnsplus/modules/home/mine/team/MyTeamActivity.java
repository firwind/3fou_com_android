package com.zhiyicx.thinksnsplus.modules.home.mine.team;
/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/7/18
 * 描  述：
 * 版  权: 九曲互动
 */

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class MyTeamActivity extends TSActivity<MyTeamPresenter, MyTeamFragment> {
    @Override
    protected MyTeamFragment getFragment() {
        return MyTeamFragment.instance(getIntent().getExtras());
    }
    @Override
    protected void componentInject() {
        DaggerMyTeamComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .myTeamPresenterModule(new MyTeamPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
    public static final void startMyTeamActivity(Context context){
        Intent intent = new Intent(context,MyTeamActivity.class);
        context.startActivity(intent);
    }
}
