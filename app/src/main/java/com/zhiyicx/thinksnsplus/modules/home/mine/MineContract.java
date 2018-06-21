package com.zhiyicx.thinksnsplus.modules.home.mine;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.baseproject.base.SystemConfigBean;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.UserCertificationInfo;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.List;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/9
 * @contact email:450127106@qq.com
 */

public interface MineContract {
    interface View extends IBaseView<MineContract.Presenter> {
        /**
         * 设置当前用户的用户信息
         *
         * @param userInfoBean
         */
        void setUserInfo(UserInfoBean userInfoBean);

        /**
         * 新的关注
         *
         * @param count
         */
        void setNewFollowTip(int count);

        /**
         * 新的好友
         *
         * @param count
         */
        void setNewFriendsTip(int count);

        /**
         * 是否有新系统消息
         */
        void setNewSystemInfo(boolean isShow);

        /**
         * 更新认证信息
         *
         * @param info
         */
        void updateCertification(UserCertificationInfo info);

    }

    interface Presenter extends IBaseTouristPresenter {
        /**
         * 从数据库获取当前用户的信息
         */
        void getUserInfoFromDB();

        /**
         * 更新用户信息
         */
        void updateUserInfo();

        /**
         * 更新用户认证信息
         */
        void getCertificationInfo();

        /**
         * 更新最新消息
         */
        void updateUserNewMessage();
    }

}
