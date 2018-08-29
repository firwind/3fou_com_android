package com.zhiyicx.thinksnsplus.modules.information.smallvideo;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;

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
    }

    interface Presenter extends ITSListPresenter<DynamicDetailBeanV2> {
        void handleLike(DynamicDetailBeanV2 bean);
    }

}
