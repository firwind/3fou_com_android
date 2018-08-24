package com.zhiyicx.thinksnsplus.modules.chat.select.organization;
/*
 * 文件名：
 * 创建者：zl
 * 时  间：2018/8/20 0020
 * 描  述：
 * 版  权：九曲互动
 * 
 */

import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.OrganizationBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.List;

public class SelectOrganizationContract {
    interface View extends ITSListView<OrganizationBean, Presenter> {
        String getSearchKeyWord();
        void createConversionResult(List<ChatUserInfoBean> list, EMConversation.EMConversationType type, int chatType, String id);

        /**
         * 获取组织ID
         * @return
         */
        int getOrganizationId();

        String getGroupId();

    }

    interface Presenter extends ITSListPresenter<OrganizationBean> {
        /**
         * 创建会话
         *
         * @param list 用户列表
         */
        void createConversation(List<UserInfoBean> list);

        void changOrganization();
    }
}
