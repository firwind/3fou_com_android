package com.zhiyicx.thinksnsplus.modules.home.mine.friends;

import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupOrFriendReviewBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.List;

import retrofit2.http.Query;
import rx.Observable;

/**
 * @author Catherine
 * @describe 好友的公用Repository
 * @date 2017/12/25
 * @contact email:648129313@qq.com
 */

public interface IBaseFriendsRepository {
    /**
     * 获取用户好友列表
     *
     * @param maxId   offset
     * @param keyword 搜索用的关键字
     * @return Observable
     */
    Observable<List<UserInfoBean>> getUserFriendsList(long maxId, String keyword);

    /**
     * 获取用户好友列表(带有是否在群里的信息)
     *
     * @param maxId   offset
     * @param keyword 搜索用的关键字
     * @return Observable
     */
    Observable<List<UserInfoBean>> getUserFriendsListInGroup(long maxId, String keyword,String groupId);

    /**
     * 创建群组会话
     * groupName 	// 群组名称（必填）
     * groupIntro 		// 群组简介（必填）
     * isPublic 		// 是否是公开群，此属性为必须的（必填）
     * maxUser	// 群组成员最大数
     * isMemberOnly	// 加入群是否需要群主或者群管理员审批，默认是false
     * isAllowInvites	// 是否允许群成员邀请别人加入此群
     * owner		// 群组的管理员（必填）
     * members		// 群组的成员
     */
    Observable<ChatGroupBean> createGroup(String groupName, String groupIntro, boolean isPublic,
                                          int maxUser, boolean isMemberOnly, boolean isAllowInvites,
                                          long owner, String members);

    Observable<ChatGroupBean> updateGroup(String im_group_id, String groupName, String groupIntro, int isPublic,
                                          int maxUser, boolean isMemberOnly,  int isAllowInvites, String groupFace,
                                          boolean isEditGroupFace, String newOwner,int groupLevel);


    /**
     * 添加成员
     * @param id
     * @param member
     * @return
     */
    Observable<Object> addGroupMember(String id, String member,int grouplevel);

    /**
     * 移除成员
     * @param id
     * @param member
     * @return
     */
    Observable<Object> removeGroupMember(String id, String member,int grouplevel);

    /**
     * 删除群组
     * @param group_id
     * @return
     */
    Observable<String> deleteGroup(String group_id);

    /**
     * 设置加好友方式
     * @param state  设置，0为允许，1验证，2不允许
     * @return
     */
    Observable<String> settingAddFriends(int state);


    /**
     * 添加好友
     * @param user_id
     * @return
     */
    Observable<String> addFriend(String user_id,String information);

    /**
     * 删除好友
     * @param user_id
     * @return
     */
    Observable<String> deleteFriend(String user_id);

    /**
     * 获取好友审核列表
     * @param maxId
     * @return
     */
    Observable<List<GroupOrFriendReviewBean>> getFriendReviewList(Integer maxId);

    /**
     * 通过或拒绝好友申请
     * @param id
     * @param status 1-同意；2-拒绝
     * @return
     */
    Observable<String> reviewFriendApply(String id,int status);

    /**
     * 让服务器同步环信信息
     * @param group_id
     * @param group_level
     * @return
     */
    Observable<String> synHuanxinGroupInfo(String group_id,int group_level);

}
