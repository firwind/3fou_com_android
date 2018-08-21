package com.zhiyicx.thinksnsplus.modules.chat.select.community;
/*
 * 文件名：
 * 创建者：zl
 * 时  间：2018/8/21 0021
 * 描  述：
 * 版  权：九曲互动
 * 
 */

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;

public class RelevanceCommunityContract {

    interface View extends ITSListView<CircleInfo, Presenter> {
        String getSearchKeyWord();
        String getGroupId();
    }

    interface Presenter extends ITSListPresenter<CircleInfo> {
        /**
         * 关联社区
         * @param
         * @param communityId
         */
        void relevanceCommunity(long communityId);
    }
}
