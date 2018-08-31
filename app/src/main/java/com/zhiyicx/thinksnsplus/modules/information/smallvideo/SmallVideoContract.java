package com.zhiyicx.thinksnsplus.modules.information.smallvideo;

import android.graphics.Bitmap;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.List;

/**
 * author: huwenyong
 * date: 2018/8/27 18:58
 * description:
 * version:
 */

public interface SmallVideoContract {

    interface View extends ITSListView<DynamicDetailBeanV2,Presenter> {
        List<DynamicDetailBeanV2> getInitVideoList();
        Long getUserId();
    }

    interface Presenter extends ITSListPresenter<DynamicDetailBeanV2> {

        //处理喜欢和取消喜欢
        void handleLike(DynamicDetailBeanV2 bean);
        //处理关注和取消关注
        void handleFollow(UserInfoBean userInfoBean);
        //动态分享
        void shareDynamic(DynamicDetailBeanV2 dynamicBean, Bitmap bitmap);
    }

}
