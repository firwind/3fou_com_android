package com.zhiyicx.thinksnsplus.modules.home.mine.friends.verify;
/*
 * 文件名：
 * 创建者：zl
 * 时  间：2018/8/16 0016
 * 描  述：
 * 版  权：九曲互动
 * 
 */

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;

public class VerifyFriendOrGroupContract {
    interface View extends IBaseView<Presenter> {
        boolean isGroupVerify();
    }

    interface Presenter extends IBasePresenter {
        void addFriendOrGroup(String id,String information);
    }
}
