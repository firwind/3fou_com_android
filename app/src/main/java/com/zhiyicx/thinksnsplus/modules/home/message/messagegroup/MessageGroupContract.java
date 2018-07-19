package com.zhiyicx.thinksnsplus.modules.home.message.messagegroup;

import com.hyphenate.chat.EMGroup;
import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;

/**
 * @Author Jliuer
 * @Date 2018/05/03/15:41
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface MessageGroupContract {

    interface View extends ITSListView<ChatGroupBean,Presenter>{
        String getsearchKeyWord();
        //boolean isOnlyOfficialGroup();
        void checkGroupExist(String id);
    }

    interface Presenter extends ITSListPresenter<ChatGroupBean>{

        void checkGroupExist(ChatGroupBean chatGroupBean);
    }
}
