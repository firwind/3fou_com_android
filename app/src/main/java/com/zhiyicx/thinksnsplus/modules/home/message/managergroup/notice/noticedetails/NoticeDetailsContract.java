package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.notice.noticedetails;
/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/7/3
 * 描  述：
 * 版  权: 九曲互动
 */

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;

public class NoticeDetailsContract {
    interface View extends IBaseView<Presenter> {

    }

    interface Presenter extends IBasePresenter {
        void deleteNotice(String noticeId);
    }
}
