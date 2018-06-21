package com.zhiyicx.thinksnsplus.modules.shortvideo.record;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;

/**
 * @Author Jliuer
 * @Date 2018/03/28/10:52
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class RecordActivity extends TSActivity<AppBasePresenter, RecordFragment> {

    @Override
    public void onBackPressed() {
        mContanierFragment.onBackPressed();
    }

    @Override
    protected RecordFragment getFragment() {
        return new RecordFragment();
    }

    @Override
    protected void componentInject() {

    }
}
