package com.zhiyicx.thinksnsplus.modules.settings.privacy;
/*
 * 文件名：
 * 创建者：zl
 * 时  间：2018/8/14 0014
 * 描  述：
 * 版  权：九曲互动
 * 
 */

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;

public interface SettingPricacyContract {
    interface View extends IBaseView<Presenter>{
        void settingSuccess(int state);
    }

    interface Presenter extends IBasePresenter{
        void settingAddFriendWay(int setState);
    }
}
