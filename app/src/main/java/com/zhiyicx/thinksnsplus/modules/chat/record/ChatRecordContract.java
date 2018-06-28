package com.zhiyicx.thinksnsplus.modules.chat.record;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.ChatRecord;

/**
 * author: huwenyong
 * date: 2018/6/28 9:33
 * description:
 * version:
 */

public interface ChatRecordContract {

    interface View extends ITSListView<ChatRecord,Presenter>{
        String getConversationId();
        String getSearchText();
    }

    interface Presenter extends ITSListPresenter<ChatRecord>{

    }

}
