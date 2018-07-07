package com.zhiyicx.thinksnsplus.modules.home.message.messagegroup.newgroup;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;

/**
 * @Author Jliuer
 * @Date 2018/05/03/15:41
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface NewMessageGroupContract {

    interface View extends ITSListView<GroupParentBean,Presenter>{
        String getsearchKeyWord();
        void checkGroupExist(String id);
    }

    interface Presenter extends ITSListPresenter<GroupParentBean>{
        void checkGroupExist(ChatGroupBean chatGroupBean);
    }
}
