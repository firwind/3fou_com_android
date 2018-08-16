package com.zhiyicx.thinksnsplus.modules.home.message.notification;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.NotificationBean;
import com.zhiyicx.thinksnsplus.data.beans.UnReadNotificaitonBean;

/**
 * author: huwenyong
 * date: 2018/8/15 10:49
 * description:
 * version:
 */

public interface NotificationContract {

    interface View extends ITSListView<NotificationBean,Presenter>{
        Fragment getCurrentFragment();
    }

    interface Presenter extends ITSListPresenter<NotificationBean>{

        UnReadNotificaitonBean getUnreadNotiBean();
    }

}
