package com.zhiyicx.thinksnsplus.modules.chat.select.addgroup;

import com.hyphenate.chat.EMGroup;
import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupServerBean;

/**
 * @Author Jliuer
 * @Date 2018/05/03/15:41
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface AddGroupContract {

    interface View extends ITSListView<ChatGroupServerBean,Presenter>{
        String getsearchKeyWord();
        void checkGroupExist(String id, EMGroup data);
        void checkIsAddGroup(String id, EMGroup data,boolean isExist);
    }

    interface Presenter extends ITSListPresenter<ChatGroupServerBean>{
       void checkIsAddGroup(String id, EMGroup data);
        void checkGroupExist(String id);

    }
}
