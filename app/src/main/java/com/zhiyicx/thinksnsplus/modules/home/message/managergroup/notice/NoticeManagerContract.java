package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.notice;
/*
 * 文件名：
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/19 14:37
 * 修改者：
 * 修改备注：
 * 修改时间：
 * 版  权：互动科技
 */

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.NoticeItemBean;

import java.util.List;

public interface NoticeManagerContract {
    interface View extends ITSListView<NoticeItemBean,Presenter>{
        void getNoticeItemBeanList(List<NoticeItemBean> noticeItemBeans);
        String getGroupId();
        void refreshNoticeListData();
    }

    interface Presenter extends ITSListPresenter<NoticeItemBean>{

    }
}
