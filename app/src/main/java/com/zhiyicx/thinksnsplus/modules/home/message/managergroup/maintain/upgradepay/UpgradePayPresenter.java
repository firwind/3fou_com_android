package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.maintain.upgradepay;
/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/7/9
 * 描  述：
 * 版  权: 九曲互动
 */
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;

import javax.inject.Inject;

public class UpgradePayPresenter extends AppBasePresenter<UpgradePayContract.View> implements UpgradePayContract.Presenter {
    @Inject
    public UpgradePayPresenter(UpgradePayContract.View rootView) {
        super(rootView);
    }

}
