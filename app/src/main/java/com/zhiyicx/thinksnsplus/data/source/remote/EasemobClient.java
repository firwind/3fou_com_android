package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupNewBean;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupServerBean;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.beans.ExpandOfficialChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupOrFriendReviewBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageGroupAlbumBean;
import com.zhiyicx.thinksnsplus.data.beans.NoticeItemBean;
import com.zhiyicx.thinksnsplus.data.beans.OrganizationBean;
import com.zhiyicx.thinksnsplus.data.beans.StickBean;
import com.zhiyicx.thinksnsplus.data.beans.UpgradeTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.ExpandChatGroupBean;

import java.util.List;

import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/15
 * @contact email:648129313@qq.com
 */

public interface EasemobClient {

    /**
     * 创建群组会话
     * groupname 	// 群组名称（必填）
     * desc 		// 群组简介（必填）
     * public 		// 是否是公开群，此属性为必须的（必填）
     * maxusers	// 群组成员最大数
     * members_only	// 加入群是否需要群主或者群管理员审批，默认是false
     * allowinvites	// 是否允许群成员邀请别人加入此群
     * owner		// 群组的管理员（必填）
     * members		// 群组的成员
     */
    @POST(ApiConfig.APP_PATH_CREATE_CHAT_GROUP)
    Observable<ChatGroupBean> createGroup(@Query("groupname") String groupName, @Query("desc") String groupIntro, @Query("public") int isPublic,
                                          @Query("maxusers") int maxUser, @Query("members_only") int isMemberOnly, @Query("allowinvites") int isAllowInvites,
                                          @Query("owner") long owner, @Query("members") String members);

    /**
     * 创建群组会话
     * groupname 	// 群组名称（必填）
     * desc 		// 群组简介（必填）
     * public 		// 是否是公开群，此属性为必须的（必填）
     * maxusers	// 群组成员最大数
     * members_only	// 加入群是否需要群主或者群管理员审批，默认是false
     * allowinvites	// 是否允许群成员邀请别人加入此群
     * owner		// 群组的管理员（必填）
     * members		// 群组的成员
     */
    @POST(ApiConfig.APP_PATH_CREATE_CHAT_GROUP)
    Observable<ChatGroupBean> createGroup(@Query("groupname") String groupName, @Query("desc") String groupIntro, @Query("public") int isPublic,
                                          @Query("maxusers") int maxUser, @Query("members_only") int isMemberOnly, @Query("allowinvites") int isAllowInvites,
                                          @Query("owner") long owner, @Query("members") String members, @Query("organize_id") int organize_id);

    /**
     * 更新群信息
     *
     * @param groupName      groupName
     * @param groupIntro     groupIntro
     * @param isPublic       isPublic
     * @param maxUser        maxUser
     * @param isMemberOnly   isMemberOnly
     * @param isAllowInvites isAllowInvites
     * @param groupFace      groupFace
     */
    @PATCH(ApiConfig.APP_PATH_CREATE_CHAT_GROUP)
    Observable<ChatGroupBean> updateGroup(@Query("im_group_id") String im_group_id, @Query("groupname") String groupName, @Query("desc") String groupIntro, @Query("public") int isPublic,
                                          @Query("maxusers") int maxUser, @Query("members_only") int isMemberOnly, @Query("allowinvites") int isAllowInvites,
                                          @Query("group_face") String groupFace, @Query("new_owner_user") String newOwner, @Query("group_level") int groupLevel);

    /**
     * 批量获取群信息
     *
     * @param ids im_group_id 以逗号隔开
     */
    @GET(ApiConfig.APP_PATH_GET_GROUP_INFO_S)
    Observable<List<ChatGroupBean>> getGroupInfo(@Query("im_group_id") String ids);

    /**
     * 批量获取群信息
     *
     * @param ids im_group_id 以逗号隔开
     */
    @Deprecated
    @GET(ApiConfig.APP_PATH_GET_GROUP_INFO_S)
    Observable<List<ChatGroupNewBean>> getNewGroupInfo(@Query("im_group_id") String ids);

    /**
     * 批量获取群信息，只返回群头像
     *
     * @param ids im_group_id 以逗号隔开
     */
    @GET(ApiConfig.APP_PATH_GET_GROUP_INFO_S_FACE)
    Observable<List<ChatGroupBean>> getGroupInfoOnlyGroupFace(@Query("im_group_id") String ids);

    /**
     * 获取所有官方群
     *
     * @param
     */
    @GET(ApiConfig.APP_PATH_GET_GROUP_INFO_OFFICIAL)
    Observable<List<ChatGroupBean>> getOfficialGroupInfo();

    /**
     * 获取带组织的官方群列表
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_GROUP_INFO_OFFICIAL_V2)
    Observable<List<ExpandOfficialChatGroupBean>> getOfficialGroupInfoV2();

    /**
     * 获取用户加入的群组列表
     *
     * @param
     */
    @GET(ApiConfig.APP_PATH_GET_GROUP_INFO_S_FACE_V2)
    Observable<ExpandChatGroupBean> getGroupInfoOnlyGroupFaceV2();

    /**
     * 获取用户加入的群组列表（按组织分类）
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_MY_GROUP_LIST_WITH_ORGANIZE)
    Observable<List<ExpandOfficialChatGroupBean>> getMyGroupListWithOrganize();

    /**
     * 获取推荐群
     *
     * @param
     */
    @GET(ApiConfig.APP_PATH_GET_GROUP_RECOMMEND)
    Observable<List<ChatGroupServerBean>> getSearchGroupInfoFace(@Query("name") String name, @Query("per_page") Integer per_page, @Query("page") long page);

    /**
     * 搜索群
     *
     * @param
     */
    @GET(ApiConfig.APP_PATH_GET_GROUP_SEARCE)
    Observable<List<ChatGroupServerBean>> getSearchGroupInfo(@Query("name") String name);

    /**
     * 获取群公告列表
     *
     * @param ids im_group_id
     */
    @GET(ApiConfig.APP_PATH_GET_GROUP_NOTICE_LIST)
    Observable<List<NoticeItemBean>> getGruopNoticeList(@Query("im_group_id") String ids);

    /**
     * 添加禁言
     *
     * @param im_group_id im_group_id
     */
    @GET(ApiConfig.APP_PATH_GET_GROUP_ADD_MUTE)
    Observable<String> openBannedPost(@Query("im_group_id") String im_group_id,
                                      @Query("user_id") String user_id,
                                      @Query("times") String times,
                                      @Query("members") String members);

    /**
     * 移除禁言
     *
     * @param im_group_id im_group_id
     */
    @GET(ApiConfig.APP_PATH_GET_GROUP_REMOVE_MUTE)
    Observable<String> removeBannedPost(@Query("im_group_id") String im_group_id,
                                        @Query("user_id") String user_id,
                                        @Query("members") String members);

    /**
     * 移除禁言
     *
     * @param im_group_id im_group_id
     */
    @GET(ApiConfig.APP_PATH_GET_GROUP_ADD_ROLU)
    Observable<String> addGroupRole(@Query("im_group_id") String im_group_id,
                                    @Query("admin_id") String admin_id,
                                    @Query("admin_type") String admin_type);

    /**
     * 获取管理员及主持人/讲师
     *
     * @param im_group_id im_group_id
     */
    @GET(ApiConfig.APP_PATH_GET_GROUP_ADMIN_LIST)
    Observable<List<UserInfoBean>> getGroupHankInfo(@Query("im_group_id") String im_group_id);

    /**
     * 移除管理员
     *
     * @param im_group_id im_group_id
     */
    @GET(ApiConfig.APP_PATH_GET_GROUP_REMOVE_ADMIN)
    Observable<String> removeRole(@Query("im_group_id") String im_group_id,
                                  @Query("admin_id") String removeadmin,
                                  @Query("admin_type") String admin_type);

    /**
     * 设置置顶
     *
     * @param stick_id im_group_id
     */
    @GET(ApiConfig.APP_PATH_GET_GROUP_SET_STICK)
    Observable<String> setStick(@Query("stick_id") String stick_id,
                                @Query("author") String author);

    /**
     * 取消置顶
     *
     * @param stick_id im_group_id
     */
    @GET(ApiConfig.APP_PATH_GET_GROUP_CANCEL_STICK)
    Observable<String> cancelStick(@Query("stick_id") String stick_id,
                                   @Query("author") String author);

    /**
     * 获取置顶ID列表
     *
     * @param author 用户ID
     */
    @GET(ApiConfig.APP_PATH_GET_GROUP_GET_STICKS)
    Observable<List<StickBean>> getSticks(@Query("author") String author);

    /**
     * 升级群
     *
     * @param type 升级类型
     */
    @POST(ApiConfig.APP_PATH_GET_GROUP_UPGRADE_GROUP)
    Observable<String> upgradeGroup(@Query("im_group_id") String im_group_id,
                                    @Query("type") int type);

    /**
     * 升级群
     *
     * @param
     */
    @GET(ApiConfig.APP_PATH_GET_GROUP_UPGRADE_GROUP_CLAUSE)
    Observable<List<UpgradeTypeBean>> upgradeGroupClause();

    /**
     * 举报群
     *
     * @param user_id     用户ID
     * @param im_group_id 群ID
     * @param reason      内容
     * @param tel         手机
     * @return
     */
    @POST(ApiConfig.APP_PATH_GET_GROUP_REPORT_GROUP)
    Observable<String> reportGroup(@Query("user_id") String user_id,
                                   @Query("im_group_id") String im_group_id,
                                   @Query("reason") String reason,
                                   @Query("tel") String tel);

    /**
     * 发布公告
     *
     * @param ids im_group_id
     */
    @POST(ApiConfig.APP_PATH_GET_GROUP_ADD_NOTICE)
    Observable<String> releaseNotice(@Query("id") String ids,
                                     @Query("title") String title,
                                     @Query("content") String content,
                                     @Query("author") String author);

    /**
     * 修改群公告
     *
     * @param ids im_group_id
     */
    @PATCH(ApiConfig.APP_PATH_GET_GROUP_UPDATE_NOTICE)
    Observable<String> updateNotice(@Query("notice_id") String ids,
                                    @Query("title") String title,
                                    @Query("content") String content,
                                    @Query("author") String author);

    /**
     * 添加群组成员
     *
     * @param id     群Id
     * @param member 群成员 1,2,3 这种样式的
     */
    @POST(ApiConfig.APP_PATH_GET_GROUP_ADD_MEMBER)
    Observable<Object> addGroupMember(@Query("im_group_id") String id, @Query("members") String member
            , @Query("group_level") int grouplevel);

    /**
     * 移除群组成员
     *
     * @param id     群Id
     * @param member 群成员 1,2,3 这种样式的
     */
    @DELETE(ApiConfig.APP_PATH_GET_GROUP_ADD_MEMBER)
    Observable<Object> removeGroupMember(@Query("im_group_id") String id,
                                         @Query("members") String member, @Query("group_level") int grouplevel);


    /**
     * 添加群相册图片
     *
     * @param images
     * @param group_id
     * @return
     */
    @POST(ApiConfig.APP_PATH_GET_GROUP_ALBUM)
    Observable<String> addGroupAlbum(@Query("images") String images,
                                     @Query("group_id") String group_id);

    /**
     * 删除群相册图片
     *
     * @param image_id
     * @return
     */
    @DELETE(ApiConfig.APP_PATH_GET_GROUP_ALBUM)
    Observable<String> deleteGroupAlbum(@Query("image_id") String image_id, @Query("im_group_id") String im_group_id);

    /**
     * 获取群相册图片
     *
     * @param group_id
     * @param per_page
     * @param page
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_GROUP_ALBUM_LIST)
    Observable<BaseJsonV2<List<MessageGroupAlbumBean>>> getGroupAlbumList(@Query("group_id") String group_id,
                                                                          @Query("per_page") Integer per_page, @Query("page") int page);

    /**
     * 删除群组
     *
     * @param group_id
     * @return
     */
    @DELETE(ApiConfig.APP_PATH_GET_GROUP_DEL_NOTICE)
    Observable<String> deleteNotice(@Query("notice_id") String group_id);

    /**
     * 删除群组
     *
     * @param group_id
     * @return
     */
    @DELETE(ApiConfig.APP_PATH_GET_GROUP_INFO_S)
    Observable<String> deleteGroup(@Query("im_group_id") String group_id);

    /**
     * 获取简单群信息
     *
     * @param im_group_id
     * @param group_level
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_SIMPLE_GROUP_INFO)
    Observable<List<ChatGroupBean>> getSimpleGroupInfo(@Query("im_group_id") String im_group_id, @Query("group_level") int group_level);

    /**
     * 新的获取群组信息
     *
     * @param ids
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_GROUP_INFO_NEW)
    Observable<ChatGroupNewBean> getNewGroupInfoV2(@Query("im_group_id") String ids);

    /**
     * 获取群成员
     *
     * @param id
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_GROUP_MEMBER_INFO_NEW)
    Observable<List<UserInfoBean>> getGroupMemberInfo(@Query("im_group_id") String id, @Query("username") String username,
                                                      @Query("last_user_id") Long maxId, @Query("limit") Integer limit);

    /**
     * 是否加入群
     *
     * @param ids
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_GROUP_MEMBER_IS_ADD_GROUP)
    Observable<ChatGroupBean> getChickIsAddGroup(@Query("im_group_id") String ids);

    /**
     * 获取群说话权限
     *
     * @param groupId
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_TALKING_STATE)
    Observable<BaseJsonV2<Boolean>> getTalkingState(@Query("im_group_id") String groupId);

    /**
     * 设置加好友方式
     *
     * @param state 设置，0为允许，1验证，2不允许
     * @return
     */
    @POST(ApiConfig.APP_PATH_SET_ADD_FRIEND)
    Observable<String> setAddFriendState(@Query("friends_set") int state);


    /**
     * 添加好友
     *
     * @param user_id
     * @return
     */
    @PUT(ApiConfig.APP_PATH_ADD_FRIEND)
    Observable<String> addFriend(@Query("friend_user_id")String user_id,
                                 @Query("information")String information,@Query("type")String type);

    /**
     * 删除好友
     *
     * @param user_id
     * @return
     */
    @DELETE(ApiConfig.APP_PATH_DELETE_FRIEND)
    Observable<String> deleteFriend(@Query("friend_user_id") String user_id);

    /**
     * 获取好友审核列表
     *
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_FRIEND_REVIEW_LIST)
    Observable<List<GroupOrFriendReviewBean>> getFriendReviewList(@Query("limit")int limit, @Query("offset")Long offset);

    /**
     * 通过或拒绝好友申请
     *
     * @param id
     * @param status 1-同意，2-拒绝
     * @return
     */
    @POST(ApiConfig.APP_PATH_REVIEW_FRIEND_APPLY)
    Observable<String> reviewFriendApply(@Query("id") String id, @Query("status") int status);

    /**
     * 退出群聊
     * 让服务器同步环信信息
     *
     * @param im_group_id
     * @param group_level
     * @return
     */
    @GET(ApiConfig.APP_PATH_SYN_EXIT_GROUP)
    Observable<String> synExitGroup(@Query("im_group_id") String im_group_id, @Query("group_level") int group_level);


    /**
     * 设置群隐私
     * @param im_group_id
     * @return
     */
    @POST(ApiConfig.APP_PATH_SET_GROUP_PRIVACY)
    Observable<String> setGroupPrivacy(@Query("im_group_id")String im_group_id,@Query("privacy")int state);

    /**
     * 提交验证信息进入群聊
     * @param im_group_id
     * @return
     */
    @POST(ApiConfig.APP_PATH_VERIFY_ENTER_GROUP)
    Observable<String> verifyGroupPrivacy(@Query("im_group_id")String im_group_id,@Query("information")String information,@Query("type")String type);

    /**
     * 清空好友审核列表
     * @return
     */
    @POST(ApiConfig.APP_PATH_CLEAR_FRIEND_APPLY_LIST)
    Observable<String> clearFriendApplyList();

    /**
     * 加群审核列表
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_GROUP_REVIEW_LIST)
    Observable<List<GroupOrFriendReviewBean>> getGroupReviewList();


    /**
     * 加群申请审核
     * @param id
     * @param status
     * @return
     */
    @POST(ApiConfig.APP_PATH_REVIEW_GROUP_APPLY)
    Observable<String> reviewGroupApply(@Query("id")String id,@Query("status")int status);

    /**
     * 清空群申请
     * @return
     */
    @POST(ApiConfig.APP_PATH_CLEAR_GROUP_APPLY_LIST)
    Observable<String> clearGroupApplyList();

    /**
     * 建群选择组织
     *
     * @param
     * @param
     * @return
     */
    @GET(ApiConfig.APP_PATH_SELECT_ORGANIZATION)
    Observable<List<OrganizationBean>> getOrganizationList(@Query("limit") Integer limit, @Query("offset") int offset, @Query("keyword") String keyword);

    /**
     * 更换组织
     *
     * @param
     * @param
     * @return
     */
    @POST(ApiConfig.APP_PATH_CHANGE_ORGANIZATION)
    Observable<String> changOrganization(@Query("im_group_id") String groupId, @Query("organize_id") int organizationId);

    /**
     * 获取社区列表
     *
     * @param
     * @param
     * @return
     */
    @GET(ApiConfig.APP_PATH_RELEVANCE_COMMUNITY_LIST)
    Observable<List<CircleInfo>> getCommunityList(@Query("limit") int limit, @Query("offset") int offset, @Query("keyword") String keyword);

    /**
     * 关联社区
     *
     * @param
     * @param
     * @return
     */
    @POST(ApiConfig.APP_PATH_RELEVANCE_COMMUNITY)
    Observable<String> relevanceCommunity(@Query("im_group_id") String groupId, @Query("community_id") long communityId);
    /**
     * 关联社区
     *
     * @param
     * @param
     * @return
     */
    @GET(ApiConfig.APP_PATH_RELEVANCE_COMMUNITY)
    Observable<CircleInfo> getGroupCommunity(@Query("im_group_id") String groupId);
}
