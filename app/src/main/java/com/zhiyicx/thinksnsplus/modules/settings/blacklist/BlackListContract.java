package com.zhiyicx.thinksnsplus.modules.settings.blacklist;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.List;

/**
 * @Describe
 * @Author zl
 * @Date 2018/4/17
 * @Contact master.jungle68@gmail.com
 */

public interface BlackListContract {
    interface View extends ITSListView<UserInfoBean, Presenter> {

        /**
         * 移除黑名单成功回调
         *
         * @param position 当前列表位置
         */
        void removeSuccess(int position);
    }

    interface Presenter extends ITSListPresenter<UserInfoBean> {

        /**
         * 移除黑名单
         *
         * @param position 当前列表位置
         */
        void removeBlackList(int position);
    }

}
