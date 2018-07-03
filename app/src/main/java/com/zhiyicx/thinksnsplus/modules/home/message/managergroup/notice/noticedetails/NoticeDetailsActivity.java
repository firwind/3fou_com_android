package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.notice.noticedetails;
/*
 * 文件名：公告详情
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/21 13:36
 * 修改者：
 * 修改备注：
 * 修改时间：
 * 版  权：互动科技
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.NoticeItemBean;

public class NoticeDetailsActivity extends TSActivity<NoticeDetailsPresenter, NoticeDetailsFragment> {
    public static final String ITEM_NOTICE_BEAN = "itemNoticeBean";
    public static final String IS_GROUP = "is_group";
    public static final String GROUP_ID = "group_id";

    @Override
    protected NoticeDetailsFragment getFragment() {
        return NoticeDetailsFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerNoticeDetailsComponent.builder().appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .noticeDetailsPresenterModule(new NoticeDetailsPresenterModule(mContanierFragment))
                .build().inject(this);
    }

    public static Intent newNoticeDetailsIntent(Context context, NoticeItemBean itemBean,boolean isGroup,String group_id) {
        Intent intent = new Intent(context, NoticeDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ITEM_NOTICE_BEAN, itemBean);
        bundle.putBoolean(IS_GROUP,isGroup);
        bundle.putString(GROUP_ID,group_id);
        intent.putExtras(bundle);
        return intent;
    }
}
