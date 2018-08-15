package com.zhiyicx.thinksnsplus.modules.settings.privacy;
/*
 * 文件名：
 * 创建者：zl
 * 时  间：2018/8/14 0014
 * 描  述：
 * 版  权：九曲互动
 * 
 */

import com.zhiyicx.common.mvp.BasePresenter;

import javax.inject.Inject;

public class SettingPricacyPresenter extends BasePresenter<SettingPricacyContract.View> implements SettingPricacyContract.Presenter{
    @Inject
    public SettingPricacyPresenter(SettingPricacyContract.View rootView) {
        super(rootView);
    }

    @Override
    public void settingAddFriendWay() {

    }
}
