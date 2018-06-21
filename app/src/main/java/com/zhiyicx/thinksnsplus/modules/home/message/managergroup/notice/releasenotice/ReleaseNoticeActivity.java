package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.notice.releasenotice;
/*
 * 文件名：
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/20 9:48
 * 修改者：
 * 修改备注：
 * 修改时间：
 * 版  权：互动科技
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.home.message.managergroup.notice.NoticeManagerActivity;

public class ReleaseNoticeActivity extends TSActivity<ReleaseNoticePresenter, ReleaseNoticeFragment> {
    public static String GROUP_INFO_ID = "group_id";

    public static Intent newIntent(Context context, String group_id) {
        Intent intent = new Intent(context, ReleaseNoticeActivity.class);
        intent.putExtra(GROUP_INFO_ID, group_id);
        return intent;
    }

    @Override
    protected ReleaseNoticeFragment getFragment() {
        return ReleaseNoticeFragment.newInstance(getIntent().getStringExtra(GROUP_INFO_ID));
    }

    @Override
    protected void componentInject() {
        DaggerReleaseNoticeComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .releaseNoticePresenterModule(new ReleaseNoticePresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
}
