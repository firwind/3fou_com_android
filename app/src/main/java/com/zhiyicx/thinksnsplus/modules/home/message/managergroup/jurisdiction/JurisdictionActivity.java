package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.jurisdiction;
/*
 * 文件名：成员权限
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/22 17:54
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
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;


import static com.zhiyicx.thinksnsplus.modules.chat.select.SelectFriendsFragment.BUNDLE_GROUP_EDIT_DATA;
import static com.zhiyicx.thinksnsplus.modules.chat.select.SelectFriendsFragment.BUNDLE_GROUP_IS_DELETE;

public class JurisdictionActivity extends TSActivity<JurisdictionPresenter, JurisdictionFragment> {

    @Override
    protected JurisdictionFragment getFragment() {
        return JurisdictionFragment.instance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerJurisdictionComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .jurisdictionPresenterModule(new JurisdictionPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }

    public static void startSelectFriendActivity(Context context, ChatGroupBean groupBean) {
        Intent intent = new Intent(context, JurisdictionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_GROUP_EDIT_DATA, groupBean);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
