package com.zhiyicx.thinksnsplus.modules.chat.member;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.List;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/23
 * @contact email:648129313@qq.com
 */

public interface GroupMemberListContract {
    interface View extends ITSListView<UserInfoBean,Presenter> {
        ChatGroupBean getGroupData();
    }

    interface Presenter extends ITSListPresenter<UserInfoBean> {
    }

    interface Repository {
    }
}
