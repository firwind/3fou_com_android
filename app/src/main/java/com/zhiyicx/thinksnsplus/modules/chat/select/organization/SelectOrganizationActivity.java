package com.zhiyicx.thinksnsplus.modules.chat.select.organization;
/*
 * 文件名：选择群组织
 * 创建者：zl
 * 时  间：2018/8/20 0020
 * 描  述：
 * 版  权：九曲互动
 * 
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.io.Serializable;
import java.util.List;

public class SelectOrganizationActivity extends TSActivity<SelectOrganizationPresenter, SelectOrganizationFragment> {
    public static final String GROUP_INFO_BEAN = "userInfoBean";
    public static final String GROUP_INFO = "groupInfo";
    public static final String GROUP_ID = "groupId";
    public static final String GROUP_ORGANIZATION_ID = "organization_id";


    @Override
    protected SelectOrganizationFragment getFragment() {
        return SelectOrganizationFragment.instance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerSelectOrganizationComponent.builder()
                .appComponent((AppApplication.AppComponentHolder.getAppComponent()))
                .selectOrganizationPresenterModule(new SelectOrganizationPresenterModule(mContanierFragment))
                .build().inject(this);
    }

    public static void startSelectOrganizationActivity(Context context, List<UserInfoBean> list) {
        Intent intent = new Intent(context, SelectOrganizationActivity.class);
        intent.putExtra(GROUP_INFO_BEAN, (Serializable) list);
        context.startActivity(intent);
    }
    public static void startSelectOrganizationActivity(Context context,int orginizationId,String groupId) {
        Intent intent = new Intent(context, SelectOrganizationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(GROUP_ORGANIZATION_ID,orginizationId);
        bundle.putString(GROUP_ID,groupId);
        intent.putExtra(GROUP_INFO,bundle);
        context.startActivity(intent);
    }
}
