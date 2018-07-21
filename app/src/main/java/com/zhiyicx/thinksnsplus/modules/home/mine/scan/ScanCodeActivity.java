package com.zhiyicx.thinksnsplus.modules.home.mine.scan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.i.IntentKey;

/**
 * @author Catherine
 * @describe 扫码的页面哦
 * @date 2017/12/8
 * @contact email:648129313@qq.com
 */

public class ScanCodeActivity extends TSActivity<ScanCodePresenter, ScanCodeFragment>{

    @Override
    protected ScanCodeFragment getFragment() {
        return ScanCodeFragment.newInsatnce(getIntent().getBooleanExtra(IntentKey.IS_GET_SCAN_RESULT,false));
    }

    @Override
    protected void componentInject() {
        DaggerScanCodeComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .scanCodePresenterModule(new ScanCodePresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }


    /**
     * 支持activity扫描返回结果
     * @param mActivity
     * @param requestCode
     */
    public static void startActivityForResult(Activity mActivity, int requestCode){
        Intent intent = new Intent(mActivity,ScanCodeActivity.class);
        intent.putExtra(IntentKey.IS_GET_SCAN_RESULT,true);
        mActivity.startActivityForResult(intent,requestCode);
    }


}
