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

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.home.mine.team.team.MyTeamListFragment;
import com.zhiyicx.thinksnsplus.modules.home.mine.team.team.MyTeamListPresenter;


public class MyTeamActivity extends TSActivity<MyTeamPresenter, MyTeamFragment> {
    @Override
    protected MyTeamFragment getFragment() {
        return MyTeamFragment.newInstance(getIntent().getExtras());
    }
    @Override
    protected void componentInject() {
//        DaggerMyTeamListComponent.builder()
//                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
//                .myTeamListPresenterModule(new MyTeamListPresenterModule(mContanierFragment))
//                .build()
//                .inject(this);
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
