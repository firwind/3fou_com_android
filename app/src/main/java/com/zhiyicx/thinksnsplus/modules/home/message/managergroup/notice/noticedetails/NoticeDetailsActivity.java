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
import com.zhiyicx.thinksnsplus.data.beans.NoticeItemBean;

public class NoticeDetailsActivity extends TSActivity{
    public static final String ITEM_NOTICE_BEAN = "itemNoticeBean";
    @Override
    protected Fragment getFragment() {
        return NoticeDetailsFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
    }

    public static Intent newNoticeDetailsIntent(Context context, NoticeItemBean itemBean){
        Intent intent = new Intent(context,NoticeDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ITEM_NOTICE_BEAN,itemBean);
        intent.putExtras(bundle);
        return intent;
    }
}
