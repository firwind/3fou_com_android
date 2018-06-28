package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.maintain.report;
/*
 * 文件名:举报群
 * 创建者：zhangl
 * 时  间：2018/6/28
 * 描  述：只有用户才能举报。群主不能举报
 * 版  权: 九曲互动
 */

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class ReportGroupActivity extends TSActivity<ReportGroupPresenter,ReportGroupFragment>{

    @Override
    protected ReportGroupFragment getFragment() {
        return null;
    }

    @Override
    protected void componentInject() {
        DaggerReportGroupComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                ;

    }
}
