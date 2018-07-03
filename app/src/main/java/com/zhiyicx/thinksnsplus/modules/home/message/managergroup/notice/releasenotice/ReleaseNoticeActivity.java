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
import com.zhiyicx.thinksnsplus.data.beans.NoticeItemBean;
import com.zhiyicx.thinksnsplus.modules.home.message.managergroup.notice.NoticeManagerActivity;

import static com.zhiyicx.thinksnsplus.modules.home.message.managergroup.notice.noticedetails.NoticeDetailsActivity.ITEM_NOTICE_BEAN;

public class ReleaseNoticeActivity extends TSActivity<ReleaseNoticePresenter, ReleaseNoticeFragment> {
    public static String GROUP_INFO_ID = "group_id";

    public static Intent newIntent(Context context, String group_id, NoticeItemBean noticeItemBean) {
        Intent intent = new Intent(context, ReleaseNoticeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(GROUP_INFO_ID,group_id);
        bundle.putParcelable(ITEM_NOTICE_BEAN,noticeItemBean);
        intent.putExtras(bundle);
        return intent;
    }


    @Override
    protected ReleaseNoticeFragment getFragment() {
//        getIntent().getStringExtra(GROUP_INFO_ID),getIntent().getParcelableExtra(ITEM_NOTICE_BEAN)
        return ReleaseNoticeFragment.newInstance(getIntent().getExtras());
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
