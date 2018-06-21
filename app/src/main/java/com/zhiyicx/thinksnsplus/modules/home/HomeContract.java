package com.zhiyicx.thinksnsplus.modules.home;

import android.content.Intent;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.CheckInBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.List;

/**
 * @Describe
 * @Author zl
 * @Date 2017/2/13
 * @Contact master.jungle68@gmail.com
 */

public interface HomeContract {
    /**
     * 对于经常使用的关于 UI 的方法可以定义到 BaseView 中,如显示隐藏进度条,和显示文字消息
     */
    interface View extends IBaseView<Presenter> {
        /**
         * 是否显示消息提示
         *
         * @param isVisiable
         */
        void setMessageTipVisable(boolean isVisiable);

        /**
         * 是否显示我的消息提示
         *
         * @param isVisiable
         */
        void setMineTipVisable(boolean isVisiable);

        /**
         * 选择 item
         *
         * @param positon
         */
        void checkBottomItem(int positon);

        /**
         * 签到
         *
         * @param data
         */
        void showCheckInPop(CheckInBean data);

        /**
         * 获取本地签到信息
         *
         * @return
         */
        CheckInBean getCheckInData();

        /**
         * 需要聊天消息通知栏提示
         * @return
         */
        boolean needShowChatNotofication();

        /**
         * 更新签到信息
         * @param data
         */
        void updateCheckInBean(CheckInBean data);
    }

    interface Presenter extends IBaseTouristPresenter {
        /**
         * 初始化 im
         */
        void initIM();

        /**
         * 签到
         */
        void checkIn();

        /**
         * 获取签到信息
         */
        void getCheckInInfo();
        /**
         * 获取签到信息,不显示
         */
        void getCheckInInfoData();

        double getWalletRatio();

        /**
         * 更新群信息
         * @param id
         * @param count
         * @param add
         * @return
         */
        boolean updateChatGroupMemberCount(String id,int count,boolean add);
    }
}
