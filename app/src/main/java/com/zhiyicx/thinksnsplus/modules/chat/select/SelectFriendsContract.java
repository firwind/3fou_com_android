package com.zhiyicx.thinksnsplus.modules.chat.select;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.home.mine.friends.IBaseFriendsRepository;

import java.util.List;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/11
 * @contact email:648129313@qq.com
 */

public interface SelectFriendsContract {

    interface View extends ITSListView<UserInfoBean, Presenter> {
        /**
         * 返回搜索结果
         *
         * @param userInfoBeans 用户列表
         */
        void getFriendsListByKeyResult(List<UserInfoBean> userInfoBeans);

        void createConversionResult(List<ChatUserInfoBean> list, EMConversation.EMConversationType type, int chatType, String id);

        boolean getIsDeleteMember();

        ChatGroupBean getGroupData();

        void dealGroupMemberResult();

        /**
         * 获取搜索输入的数据
         *
         * @return
         */
        String getSearchKeyWord();

        void nextCreateGroup(List<UserInfoBean> list);
    }

    interface Presenter extends ITSListPresenter<UserInfoBean> {

        /**
         * 创建会话
         *
         * @param list 用户列表
         */
        void createConversation(List<UserInfoBean> list);

        /**
         * 处理群成员变化
         */
        void dealGroupMember(List<UserInfoBean> list);
    }

    interface Repository extends IBaseFriendsRepository {

    }
}
