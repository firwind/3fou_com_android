package com.zhiyicx.thinksnsplus.modules.home.message.messagegroup.newgroup;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupParentBean;

/**
 * @Author Jliuer
 * @Date 2018/05/03/15:41
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface NewMessageGroupContract {

    interface View extends ITSListView<GroupParentBean,Presenter>{
        boolean isOnlyOfficial();
    }

    interface Presenter extends ITSListPresenter<GroupParentBean>{

    }
}
