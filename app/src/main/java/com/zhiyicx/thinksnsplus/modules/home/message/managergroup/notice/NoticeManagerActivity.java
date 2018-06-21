package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.notice;
/*
 * 文件名：群公告
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/19 13:56
 * 修改者：
 * 修改备注：
 * 修改时间：
 * 版  权：互动科技
 */

import android.content.Context;
import android.content.Intent;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

import static com.zhiyicx.thinksnsplus.modules.chat.edit.name.EditGroupNameFragment.GROUP_ORIGINAL_ID;
import static com.zhiyicx.thinksnsplus.modules.chat.edit.name.EditGroupNameFragment.IS_GROUP_OWNER;


public class NoticeManagerActivity extends TSActivity<NoticeManagerPresenter,NoticeManagerFragment>{

    @Override
    protected NoticeManagerFragment getFragment() {
        return NoticeManagerFragment.newInstance(getIntent().getStringExtra(GROUP_ORIGINAL_ID),getIntent().getBooleanExtra(IS_GROUP_OWNER,false));
    }

    @Override
    protected void componentInject() {
        DaggerNoticeManagerComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .noticeManagerPresenterModule( new NoticeManagerPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
}
