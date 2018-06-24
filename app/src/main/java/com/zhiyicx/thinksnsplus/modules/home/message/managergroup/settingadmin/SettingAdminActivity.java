package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.settingadmin;
/*
 * 文件名：
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/23 11:04
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
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;

public class SettingAdminActivity extends TSActivity<SettingAdminPresenter,SettingAdminFragment>{
    public static final String GROUP_INFO_BEAN = "group_info_bean";
    @Override
    protected SettingAdminFragment getFragment() {

        return SettingAdminFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerSettingAdminComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .settingAdminPresenterModule(new SettingAdminPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }

    public static void  startSettingAdminActivty(Context context, ChatGroupBean chatGroupBean){
        Intent intent = new Intent(context,SettingAdminActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(GROUP_INFO_BEAN,chatGroupBean);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }



}
