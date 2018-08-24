package com.zhiyicx.thinksnsplus.modules.chat.select.community;
/*
 * 文件名：关联社区
 * 创建者：zl
 * 时  间：2018/8/21 0021
 * 描  述：
 * 版  权：九曲互动
 * 
 */

import android.content.Context;
import android.content.Intent;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

import javax.inject.Inject;

import static com.zhiyicx.thinksnsplus.i.IntentKey.GROUP_ID;

public class RelevanceCommunityActivity extends TSActivity<RelevanceCommunityPresenter, RelevanceCommunityFragment> {
    public static String COMMUNITY_ID = "communityId";
    @Override
    protected RelevanceCommunityFragment getFragment() {
        return RelevanceCommunityFragment.getInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerRelevanceCommunityComponent.builder()
                .appComponent((AppApplication.AppComponentHolder.getAppComponent()))
                .relevanceCommunityPresenterModule(new RelevanceCommunityPresenterModule(mContanierFragment))
                .build().inject(this);
    }

    public static void startRelevanceCommunityActivity(Context context, String groupId,String communityId) {
        Intent intent = new Intent(context, RelevanceCommunityActivity.class);
        intent.putExtra(GROUP_ID, groupId);
        intent.putExtra(COMMUNITY_ID, communityId);
        context.startActivity(intent);
    }
}
