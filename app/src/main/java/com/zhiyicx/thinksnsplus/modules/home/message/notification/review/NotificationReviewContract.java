package com.zhiyicx.thinksnsplus.modules.home.message.notification.review;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.GroupOrFriendReviewBean;

/**
 * author: huwenyong
 * date: 2018/8/16 11:59
 * description:
 * version:
 */

public interface NotificationReviewContract {


    interface View extends ITSListView<GroupOrFriendReviewBean,Presenter>{

    }

    interface Presenter extends ITSListPresenter<GroupOrFriendReviewBean>{

    }

}
