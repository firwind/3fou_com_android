package com.zhiyicx.thinksnsplus.modules.settings.privacy;
/*
 * 文件名：
 * 创建者：zl
 * 时  间：2018/8/14 0014
 * 描  述：
 * 版  权：九曲互动
 * 
 */

import android.os.Bundle;
import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;

public class SettingPricacyFragment extends TSFragment<SettingPricacyContract.Presenter> implements SettingPricacyContract.View{

    public static SettingPricacyFragment getInstance(Bundle bundle){
        SettingPricacyFragment fragment = new SettingPricacyFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getBodyLayoutId() {
        return 0;
    }
}
