package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.maintain.upgradepay;
/*
 * 文件名: 升级支付
 * 创建者：zhangl
 * 时  间：2018/7/9
 * 描  述：
 * 版  权: 九曲互动
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.UpgradeTypeBean;

public class UpgradePayActivity extends TSActivity<UpgradePayPresenter, UpgradePayFragment> {
    public static final String UPGRADE_TYPE = "upgradeTypeBean";
    public static final String GRADE_ID = "group_id";

    @Override
    protected UpgradePayFragment getFragment() {
        return UpgradePayFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerUpgradePayComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .upgradePayPresenterModule(new UpgradePayPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }

    public static void startUpgradePayActivity(Context context, UpgradeTypeBean upgradeTypeBean, String groupId) {
        Intent intent = new Intent(context, UpgradePayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(UPGRADE_TYPE, upgradeTypeBean);
        bundle.putString(GRADE_ID, groupId);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mContanierFragment.onActivityResult(requestCode, resultCode, data);
    }
}
