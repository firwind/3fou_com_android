package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.jurisdiction;
/*
 * 文件名：
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/22 18:00
 * 修改者：
 * 修改备注：
 * 修改时间：
 * 版  权：互动科技
 */

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.chat.select.SelectFriendsContract;

import java.util.List;

public interface JurisdictionContract {
    interface View extends ITSListView<UserInfoBean, Presenter> {
        /**
         * 返回搜索结果
         *
         * @param userInfoBeans 用户列表
         */
        void getUserListByKeyResult(List<UserInfoBean> userInfoBeans);
        ChatGroupBean getGroupData();
        /**
         * 获取搜索输入的数据
         *
         * @return
         */
        String getSearchKeyWord();

        void addGroupRuloSuccess();
        String setAddRuloName();
        String setGroupId();
    }

    interface Presenter extends ITSListPresenter<UserInfoBean> {
        /**
         * 处理群成员变化
         */
        void dealGroupMember(List<UserInfoBean> list);

        /**
         * 禁言
         * @param im_group_id  群ID
         * @param user_id  用户id   以逗号隔开
         * @param times  禁言时长
         * @param members  用户昵称
         */
        void openBannedPost(String im_group_id, String user_id, String times, String members);
        /**
         * 添加群角色
         * @param im_group_id  群ID
         * @param admin_id  群主ID
         * @param admin_type  管理员角色（1.普通管理员，2.主持人，3.角色）
         * @param
         */
        void addGroupRole(List<UserInfoBean> mSelectedList);

        /**
         * 解除禁言
         * @param im_group_id
         * @param user_id
         * @param members
         */
        void removeBannedPost(String im_group_id, String user_id, String members);
    }
}
