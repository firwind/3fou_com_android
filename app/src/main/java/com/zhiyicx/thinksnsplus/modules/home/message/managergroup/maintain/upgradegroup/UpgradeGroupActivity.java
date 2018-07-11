package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.maintain.upgradegroup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

import javax.inject.Inject;

import static com.zhiyicx.thinksnsplus.i.IntentKey.GROUP_ID;

/**
 * Created by zhang on 2018/6/28.
 */

public class UpgradeGroupActivity extends TSActivity<UpgradeGroupPresenter, UpgradeGroupFragment> {

    public static UpgradeGroupActivity mFlag = null;

    @Override
    protected UpgradeGroupFragment getFragment() {
        return UpgradeGroupFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerUpgradeGroupComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .upgradeGroupPresenterModule(new UpgradeGroupPresenterModule(mContanierFragment))
                .build()
                .inject(this);
        mFlag = this;
    }

    public static void startUpgradeGroupActivity(Context context, String groupId) {
        Intent intent = new Intent(context, UpgradeGroupActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(GROUP_ID, groupId);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
