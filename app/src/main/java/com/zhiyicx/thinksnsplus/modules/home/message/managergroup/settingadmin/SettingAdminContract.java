package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.settingadmin;
/*
 * 文件名：
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/23 11:08
 * 修改者：
 * 修改备注：
 * 修改时间：
 * 版  权：互动科技
 */

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupHankBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.List;

public interface SettingAdminContract {

    interface View extends ITSListView<GroupHankBean,Presenter> {
        ChatGroupBean getGroupInfo();//获取群ID
        void getUserInfoBeans(List<UserInfoBean> userInfoBeans);

    }

    interface Presenter extends ITSListPresenter<GroupHankBean> {
        void deleteRole(UserInfoBean userInfoBean,String role_type);
    }
}
