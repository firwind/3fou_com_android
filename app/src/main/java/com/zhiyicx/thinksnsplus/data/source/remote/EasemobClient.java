package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupNewBean;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupServerBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageGroupAlbumBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupHankBean;
import com.zhiyicx.thinksnsplus.data.beans.NoticeItemBean;
import com.zhiyicx.thinksnsplus.data.beans.StickBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.List;
import java.util.Map;

import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
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
                                          @Query("group_face") String groupFace, @Query("new_owner_user") String newOwner, @Query("grouplevel") int groupLevel);

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
     * 获取推荐群
     *
     * @param
     */
    @GET(ApiConfig.APP_PATH_GET_GROUP_RECOMMEND)
    Observable<List<ChatGroupServerBean>> getSearchGroupInfoFace();

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
     * 举报群
     * @param user_id  用户ID
     * @param im_group_id 群ID
     * @param reason  内容
     * @param tel  手机
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
            ,@Query("grouplevel")int grouplevel);

    /**
     * 添加群组成员
     *
     * @param id     群Id
     * @param member 群成员 1,2,3 这种样式的
     */
    @DELETE(ApiConfig.APP_PATH_GET_GROUP_ADD_MEMBER)
    Observable<Object> removeGroupMember(@Query("im_group_id") String id,
                                         @Query("members") String member,@Query("grouplevel")int grouplevel);


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
     * @param image_id
     * @return
     */
    @DELETE(ApiConfig.APP_PATH_GET_GROUP_ALBUM)
    Observable<String> deleteGroupAlbum(@Query("image_id") String image_id,@Query("im_group_id")String im_group_id);

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
                                                                          @Query("per_page") int per_page, @Query("page") int page);

    /**
     * 删除群组
     * @param group_id
     * @return
     */
    @DELETE(ApiConfig.APP_PATH_GET_GROUP_DEL_NOTICE)
    Observable<String> deleteGroup(@Query("notice_id")String group_id);

    /**
     * 获取简单群信息
     * @param im_group_id
     * @param group_level
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_SIMPLE_GROUP_INFO)
    Observable<List<ChatGroupBean>> getSimpleGroupInfo(@Query("im_group_id")String im_group_id,@Query("grouplevel")int group_level);

    /**
     * 新的获取群组信息
     * @param ids
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_GROUP_INFO_NEW)
    Observable<ChatGroupNewBean> getNewGroupInfoV2(@Query("im_group_id") String ids);

}
