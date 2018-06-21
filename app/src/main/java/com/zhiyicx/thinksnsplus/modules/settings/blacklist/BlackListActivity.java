package com.zhiyicx.thinksnsplus.modules.settings.blacklist;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListPresenter;
/**
 * @Describe 黑名单列表
 * @Author zl
 * @Date 2018/4/17
 * @Contact master.jungle68@gmail.com
 */
public class BlackListActivity extends TSActivity<BlackListPresenter, BlackListFragment> {

    @Override
    protected void componentInject() {
    }

    @Override
    protected BlackListFragment getFragment() {
        return BlackListFragment.initFragment(getIntent().getExtras());
    }
}
