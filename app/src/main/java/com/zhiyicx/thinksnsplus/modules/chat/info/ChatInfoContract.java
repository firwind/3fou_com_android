package com.zhiyicx.thinksnsplus.modules.chat.info;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupNewBean;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.beans.GroupHankBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupOrFriendReviewBean;
import com.zhiyicx.thinksnsplus.data.beans.StickBean;
import com.zhiyicx.thinksnsplus.data.beans.UpgradeTypeBean;
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

        void setIsInGroup(boolean isInGroup);

        void updateGroup(ChatGroupBean chatGroupBean);

        void getGroupInfoSuccess(ChatGroupNewBean chatGroupBean);

        ChatGroupBean getGroupBean();

        void isShowEmptyView(boolean isShow, boolean isSuccess);

        String getToUserId();

        void createGroupSuccess(ChatGroupBean chatGroupBean);

        void closeCurrentActivity();

        //void updateGroupOwner(ChatGroupBean chatGroupBean);

        //void setSticksSuccess();

        void setBannedPostSuccess();

        boolean getIsInGroup();

        void goChatActivity();

        void setStickState(boolean isStick);
    }

    interface Presenter extends IBasePresenter {

        boolean isGroupAdminer();

        boolean isGroupOwner();

        void getIsInGroup();

        void updateGroup(ChatGroupBean chatGroupBean, boolean isEditGroupFace);

        void getGroupChatInfo(String groupId);

        void createGroupFromSingleChat();

        /**
         * 接受，或者屏蔽群消息
         *
         * @param isChecked
         * @param chatId
         */
        void openOrCloseGroupMessage(boolean isChecked, String chatId);

        /**
         * 禁言
         *
         * @param im_group_id 群ID
         * @param user_id     用户id   以逗号隔开
         * @param times       禁言时长
         * @param members     用户昵称
         */
        void openBannedPost(String im_group_id, String user_id, String times, String members);

        /**
         * 解除禁言
         *
         * @param im_group_id
         * @param user_id
         * @param members
         */
        void removeBannedPost(String im_group_id, String user_id, String members);

        /**
         * 上传置顶群/单聊ID
         *
         * @param stick_id
         * @param author
         * @param isStick  是否置顶
         */
        void setSticks(String stick_id, String author, int isStick);

        /**
         * 退群 或者 解散群
         * 群主是 解散，一般成员是退群
         *
         * @param chatId
         */
        void destoryOrLeaveGroup(String chatId);

        UserInfoBean getUserInfoFromLocal(String id);

        /**
         * 检查 userId 是否是小助手
         *
         * @param chatId
         * @return
         */
        boolean checkImhelper(String chatId);

        void saveGroupInfo(ChatGroupBean chatGroupBean);

        void getConversationStickList(String chatId);

    }

    interface Repository extends IBaseFriendsRepository {
        Observable<List<ChatGroupBean>> getGroupChatInfo(String groupId);
        @Deprecated

        Observable<List<ChatGroupNewBean>> getGroupChatNewInfo(String groupId);

        Observable<String> openBannedPost(String im_group_id, String user_id, String times, String members);

        Observable<String> addGroupRole(String im_group_id, String admin_id, String admin_type);

        Observable<String> removeBannedPost(String im_group_id, String user_id, String members);

        //获取群管理以及讲师或者主持人
        Observable<List<UserInfoBean>> getGroupHankInfo(String im_group_id);

        //删除管理员
        Observable<String> removeRole(String im_group_id, String removeadmin, String admin_type);

        //群/单聊置顶
        Observable<String> setStick(String stick_id, String author, int isStick);

        //群/单聊置顶
        Observable<List<StickBean>> refreshSticks(String author);

        //获取本地升级群类型
        Observable<List<UpgradeTypeBean>> getUpgradeGroups();

        //提交升级群
        Observable<String> upgradeGroup(String groupId,int type);
        //新的获取群信息的接口
        Observable<ChatGroupNewBean> getNewGroupInfoV2(String group_id);
        //获取群成员接口
        Observable<List<UserInfoBean>> getGroupMemberInfo(String group_id,String searchKey,Long maxId);
        //提交举报群
        Observable<String> reportGroup(String userId, String groupId, String reason, String tel);
        //获取群说话权限
        Observable<BaseJsonV2<Boolean>> getTalkingState(String groupId);

        Observable<CircleInfo> getCommunityInfo(String groupId);
        //设置群隐私权限
        Observable<String> setGroupPrivacy(String groupId,int state);
        //提交验证信息
        Observable<String> verifyEnterGroup(String groupId,String information,boolean isVerify);
        //加群审核列表
        Observable<List<GroupOrFriendReviewBean>> getGroupReviewList();
        //加群申请审核
        Observable<String> reviewGroupApply(String id,boolean isAgree);
        //清空群申请列表
        Observable<String> clearGroupApply();
    }
}
