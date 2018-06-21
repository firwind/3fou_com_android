package com.zhiyicx.thinksnsplus.modules.chat.edit.owner;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.home.mine.friends.IBaseFriendsRepository;

import java.util.List;

/**
 * Created by Catherine on 2018/1/22.
 */

public interface EditGroupOwnerContract {

    interface View extends ITSListView<UserInfoBean, Presenter> {
        /**
         *
         * @return 群数据
         */
        ChatGroupBean getGroupData();

        /**
         *
         * @param chatGroupBean 更新群内容
         */
        void updateGroup(ChatGroupBean chatGroupBean);

        /**
         *
         * @return 搜索输入内容
         */
        String getsearchKeyWord();
    }

    interface Presenter extends ITSListPresenter<UserInfoBean> {
        /**
         * 检查用户是否为群主
         * @param userInfoBean 用户信息
         * @return
         */
        boolean checkNewOwner(UserInfoBean userInfoBean);

        /**
         *
         * @param key 搜索
         * @return
         */
        List<UserInfoBean> getSearchResult(String key);

        /**
         * 更新群内容
         * @param chatGroupBean  新的内容
         */
        void updateGroup(ChatGroupBean chatGroupBean);
    }

    interface Repository extends IBaseFriendsRepository{
    }
}
