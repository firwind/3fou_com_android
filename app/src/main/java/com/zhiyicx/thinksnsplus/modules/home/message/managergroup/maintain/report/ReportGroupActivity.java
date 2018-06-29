package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.maintain.report;
/*
 * 文件名:举报群
 * 创建者：zhangl
 * 时  间：2018/6/28
 * 描  述：只有用户才能举报。群主不能举报
 * 版  权: 九曲互动
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

import static com.zhiyicx.thinksnsplus.i.IntentKey.GROUP_ID;

public class ReportGroupActivity extends TSActivity<ReportGroupPresenter, ReportGroupFragment> {

    @Override
    protected ReportGroupFragment getFragment() {
        return ReportGroupFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerReportGroupComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .reportGroupPresenterModule(new ReportGroupPresenterModule(mContanierFragment))
                .build()
                .inject(this);

    }

    public static void startReportGroupActivity(Context context,String groupId){
        Intent intent = new Intent(context,ReportGroupActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(GROUP_ID, groupId);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
