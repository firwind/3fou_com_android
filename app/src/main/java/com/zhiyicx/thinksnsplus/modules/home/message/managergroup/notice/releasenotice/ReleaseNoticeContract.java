package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.notice.releasenotice;
/*
 * 文件名：
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/20 11:54
 * 修改者：
 * 修改备注：
 * 修改时间：
 * 版  权：互动科技
 */

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.NoticeItemBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.home.message.managergroup.notice.NoticeManagerContract;

import java.util.List;

public interface ReleaseNoticeContract {
    interface View extends IBaseView<Presenter> {
        void relaseSuccess();
        /**
         * 设置当前用户的用户信息
         *
         * @param userInfoBean
         */
        void setUserInfo(UserInfoBean userInfoBean);


    }

    interface Presenter extends IBasePresenter{
        void releaseNotice(String group_id,String title,String content,String author);
        /**
         * 从数据库获取当前用户的信息
         */
        void getUserInfoFromDB();
    }
}
