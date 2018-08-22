package com.zhiyicx.thinksnsplus.modules.chat;

import com.hyphenate.chat.EMMessage;
import com.zhiyicx.baseproject.em.manager.eventbus.TSEMRefreshEvent;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.List;

/**
 * @Describe
 * @Author zl
 * @Date 2017/1/6
 * @Contact master.jungle68@gmail.com
 */

public interface ChatContract {

    interface View extends IBaseView<Presenter> {

        String getChatId();

        void onMessageReceivedWithUserInfo(List<EMMessage> messages);

        void handleNotRoamingMessageWithUserInfo();

        void setTalkingState(boolean isTalking,String content);

        void updateUserInfo(UserInfoBean userInfoBean);

        void updateChatGroupInfo(ChatGroupBean chatGroupBean);
    }

    interface Presenter extends IBasePresenter {

        //聊天对象或者本人是否是前链小助手
        boolean isImHelper();

        String getChatGroupName();

        //处理收到的消息
        void dealMessages(List<EMMessage> messages);

        //从本地拿群组信息
        ChatGroupBean getChatGroupInfoFromLocal();

        //从本地拿用户信息
        UserInfoBean getUserInfoFromLocal();

        UserInfoBean getUserInfoFromLocal(String user_id);

        void getUserInfoFromServer();

        //从服务器拿群组信息
        void getChatGroupInfoFromServer();

        //更改群名称
        void updateGroupName(ChatGroupBean chatGroupBean);

        //更改群人数
        boolean updateChatGroupMemberCount(String id,int count,boolean add);

        //找出未获取到用户信息的消息
        void handleNotRoamingMessageList(List<EMMessage> messages);

        //获取当前用户信息的
        void getCurrentTalkingState(String groupId);

    }
}
