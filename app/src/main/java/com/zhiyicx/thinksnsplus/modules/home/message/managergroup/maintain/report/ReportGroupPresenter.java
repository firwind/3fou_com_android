package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.maintain.report;
/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/6/28
 * 描  述：
 * 版  权: 九曲互动
 */

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;

import javax.inject.Inject;

public class ReportGroupPresenter extends AppBasePresenter<ReportGroupContract.View> implements ReportGroupContract.Presenter {
    @Inject
    public ReportGroupPresenter(ReportGroupContract.View rootView) {
        super(rootView);
    }
}
