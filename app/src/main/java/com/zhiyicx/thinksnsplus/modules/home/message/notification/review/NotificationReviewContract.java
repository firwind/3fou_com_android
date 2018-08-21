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
        boolean isFriendReview();
    }

    interface Presenter extends ITSListPresenter<GroupOrFriendReviewBean>{
        //同意或者拒绝申请
        void requestAgreeOrInjectApply(GroupOrFriendReviewBean bean,boolean isAgree);
        //清空验证信息
        void clearApplyList();

    }

}
