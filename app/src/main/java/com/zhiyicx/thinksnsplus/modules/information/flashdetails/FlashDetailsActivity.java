package com.zhiyicx.thinksnsplus.modules.information.flashdetails;
/*
 * 文件名：
 * 创建者：Administrator
 * 时  间：2018/8/9 0009
 * 描  述：
 * 版  权：九曲互动
 * 
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.baseproject.impl.share.ShareWithoutViewModule;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;

import static com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsFragment.BUNDLE_INFO;
import static com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsFragment.BUNDLE_INFO_TYPE;

public class FlashDetailsActivity extends TSActivity<FlashDetailsPresenter, FlashDetailsFragment> {

    @Override
    protected FlashDetailsFragment getFragment() {
        return FlashDetailsFragment.newInstance(getIntent().getBundleExtra(BUNDLE_INFO));
    }

    @Override
    protected void componentInject() {
        DaggerFlashDetailsComponent.builder().appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .flashDetailsPresenterModule(new FlashDetailsPresenterModule(mContanierFragment))
                .shareWithoutViewModule(new ShareWithoutViewModule(this))
                .build()
                .inject(this);

    }

    public static void startActivity(Context context, InfoListDataBean dataBean) {
        Intent intent = new Intent(context, FlashDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_INFO, dataBean);
        intent.putExtra(BUNDLE_INFO, bundle);
        context.startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UmengSharePolicyImpl.onActivityResult(requestCode, resultCode, data, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UmengSharePolicyImpl.onDestroy(this);
    }

}
