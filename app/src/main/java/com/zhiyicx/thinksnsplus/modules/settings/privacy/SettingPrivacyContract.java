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
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

public interface SettingPrivacyContract {
    interface View extends IBaseView<Presenter>{
        void settingSuccess(int state);
        ChatGroupBean getChatGroupBean();
    }

    interface Presenter extends IBasePresenter{
        void settingAddFriendOrGroupWay(int setState);
        UserInfoBean getCurrentUser();
    }
}
