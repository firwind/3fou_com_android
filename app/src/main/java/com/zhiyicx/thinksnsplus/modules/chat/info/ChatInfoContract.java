package com.zhiyicx.thinksnsplus.modules.chat.info;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupNewBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.home.mine.friends.IBaseFriendsRepository;

import java.util.List;

import retrofit2.http.Query;
import rx.Observable;


/**
 * @author Catherine
 * @describe
 * @date 2018/1/22
 * @contact email:648129313@qq.com
 */

public interface ChatInfoContract {

    interface View extends IBaseView<Presenter> {
        String getChatId();

        void updateGroup(ChatGroupBean chatGroupBean);

        void getGroupInfoSuccess(ChatGroupNewBean chatGroupBean);

        ChatGroupBean getGroupBean();

        void isShowEmptyView(boolean isShow, boolean isSuccess);

        /**
         * 单聊的对方的id
         */
        String getToUserId();

        void createGroupSuccess(ChatGroupBean chatGroupBean);

        void closeCurrentActivity();

        void updateGroupOwner(ChatGroupBean chatGroupBean);
    }

    interface Presenter extends IBasePresenter {
        boolean isGroupOwner();

        void updateGroup(ChatGroupBean chatGroupBean, boolean isEditGroupFace);

        void getGroupChatInfo(String groupId);

        void createGroupFromSingleChat();

        /**
         * 接受，或者屏蔽群消息
         * @param isChecked
         * @param chatId
         */
        void openOrCloseGroupMessage(boolean isChecked, String chatId);

        /**
         * 禁言
         * @param im_group_id  群ID
         * @param user_id  用户id   以逗号隔开
         * @param times  禁言时长
         * @param members  用户昵称
         */
        void openBannedPost(String im_group_id ,String user_id,String times,String members);

        /**
         * 解除禁言
         * @param im_group_id
         * @param user_id
         * @param members
         */
        void removeBannedPost(String im_group_id ,String user_id,String members);
        /**
         * 退群 或者 解散群
         * 群主是 解散，一般成员是退群
         * @param chatId
         */
        void destoryOrLeaveGroup(String chatId);

        UserInfoBean getUserInfoFromLocal(String id);

        /**
         * 检查 userId 是否是小助手
         * @param chatId
         * @return
         */
        boolean checkImhelper(String chatId);

        void saveGroupInfo(ChatGroupBean chatGroupBean);
    }

    interface Repository extends IBaseFriendsRepository {
        Observable<List<ChatGroupBean>> getGroupChatInfo(String groupId);
        Observable<List<ChatGroupNewBean>> getGroupChatNewInfo(String groupId);
        Observable<String> openBannedPost(String im_group_id ,String user_id,String times,String members);
        Observable<String> removeBannedPost(String im_group_id ,String user_id,String members);
    }
}
