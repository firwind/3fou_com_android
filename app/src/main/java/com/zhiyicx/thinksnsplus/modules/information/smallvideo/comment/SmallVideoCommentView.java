package com.zhiyicx.thinksnsplus.modules.information.smallvideo.comment;

import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;

import java.util.List;

/**
 * author: huwenyong
 * date: 2018/8/30 10:42
 * description:
 * version:
 */

public interface SmallVideoCommentView {

    DynamicDetailBeanV2 getCurrentDynamic();

    void onNetResponseSuccess(List<DynamicCommentBean> list,boolean isLoadMore);

    void onNetResponseError();

    void refreshData();

    List<DynamicCommentBean> getListDatas();

}
