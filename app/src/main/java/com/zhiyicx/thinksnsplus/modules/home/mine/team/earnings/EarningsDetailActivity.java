package com.zhiyicx.thinksnsplus.modules.home.mine.team.earnings;
/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/7/21
 * 描  述：
 * 版  权: 九曲互动
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class EarningsDetailActivity extends TSActivity<EarningsDetailPresenter, EarningsDetailFragment> {
    public static String MEMBER_ID = "memberId";
    @Override
    protected EarningsDetailFragment getFragment() {
        return EarningsDetailFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerEarningsDetailComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .earningsDetailModule(new EarningsDetailModule(mContanierFragment))
                .build().inject(this);
    }

    public static void startEarningsDetailActivity(Context context,int id){
        Intent intent = new Intent(context,EarningsDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(MEMBER_ID,id);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
