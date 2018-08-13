package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.CollectGroupDyanmciListBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/8
 * @contact email:450127106@qq.com
 */

public interface ChannelClient {

    /**
     * 获取频道的动态列表
     *
     * @param goup_id
     * @param limit
     * @param max_id
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_GROUP_DYNAMIC_LIST)
    Observable<List<GroupDynamicListBean>> getDynamicListFromGroup(@Path("group") long goup_id,
                                                                   @Query("limit") Integer limit, @Query("after") long max_id);

    /**
     * 获取所有社区列表的接口
     *
     * @param limit  限制条数
     * @param max_id max_id
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_ALL_GROUP)
    Observable<List<GroupInfoBean>> getAllGroupList(@Query("limit") Integer limit, @Query("after") long max_id);

    /**
     * 获取用户加入的社区列表
     *
     * @param limit  限制条数
     * @param max_id max_id
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_USER_JOINED_GROUP)
    Observable<List<GroupInfoBean>> getUserJoinedGroupList(@Query("limit") Integer limit, @Query("after") long max_id);

    /**
     * 加入社区
     *
     * @param groupId 社区id
     * @return
     */
    @POST(ApiConfig.APP_PATH_JOIN_GROUP)
    Observable<BaseJsonV2<Object>> joinGroup(@Path("group") long groupId);

    /**
     * 退出社区
     *
     * @param groupId 社区id
     */
    @DELETE(ApiConfig.APP_PATH_JOIN_GROUP)
    Observable<BaseJsonV2<Object>> quitGroup(@Path("group") long groupId);

    /**
     * 社区详情
     *
     * @param group_id 社区id
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_GROUP_DETAIL)
    Observable<GroupInfoBean> getGroupDetail(@Path("group") long group_id);

    /**
     * 获取社区动态详情
     *
     * @param group_id   社区id
     * @param dynamic_id 动态id
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_GROUP_DYNAMIC_DETAIL)
    Observable<GroupDynamicListBean> getGroupDynamicDetail(@Path("group") long group_id, @Path("post") long dynamic_id);

    /**
     * 获取社区动态的评论列表
     *
     *
     * @param group_id 社区id
     * @param dynamic_id 动态id
     * @param limit
     * @param max_id
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_GROUP_DYNAMIC_COMMENT_LIST)
    Observable<List<GroupDynamicCommentListBean>> getGroupDynamicCommentList(@Path("group") long group_id,
                                                                             @Path("post") long dynamic_id,
                                                                             @Query("limit") Integer limit,
                                                                             @Query("after") long max_id);

    /**
     * 获取我收藏的社区动态
     * @param limit
     * @param max_id
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_MYCOLLECT_GROUP_DYNAMIC_LIST)
    Observable<List<CollectGroupDyanmciListBean>> getMyCollectGroupDynamicList(@Query("limit") Integer limit,
                                                                               @Query("after") long max_id);


    /**
     * 点赞动态
     * @param group_id
     * @param dynamic_id
     * @return
     */
    @POST(ApiConfig.APP_PATH_DIGG_MYCOLLECT_GROUP_DYNAMIC)
    Observable<BaseJsonV2> diggGroupDynamic(@Path("group") long group_id, @Path("post") long dynamic_id);

    /**
     * 取消点赞
     * @param group_id
     * @param dynamic_id
     * @return
     */
    @DELETE(ApiConfig.APP_PATH_DIGG_MYCOLLECT_GROUP_DYNAMIC)
    Observable<BaseJsonV2> cancelDiggGroupDynamic(@Path("group") long group_id, @Path("post") long dynamic_id);

    /**
     * 收藏动态
     * @param group_id
     * @param dynamic_id
     * @return
     */
    @POST(ApiConfig.APP_PATH_COLLECT_GROUP_DYNAMIC)
    Observable<BaseJsonV2> collectGroupDynamic(@Path("group") long group_id, @Path("post") long dynamic_id);

    /**
     * 取消收藏
     * @param group_id
     * @param dynamic_id
     * @return
     */
    @DELETE(ApiConfig.APP_PATH_COLLECT_GROUP_DYNAMIC)
    Observable<BaseJsonV2> cancelCollectGroupDynamic(@Path("group") long group_id, @Path("post") long dynamic_id);

    /**
     *  获取动态点赞列表
     * @param group_id
     * @param dynamic_id
     * @param limit
     * @param max_id
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_MYCOLLECT_GROUP_DYNAMIC_DIGG_LIST)
    Observable<List<DynamicDigListBean>> getDigList(@Path("group") long group_id,
                                                    @Path("post") long dynamic_id,
                                                    @Query("limit") Integer limit,
                                                    @Query("after") long max_id);

    /**
     * 发布动态 v2 接口--社区
     * @param group
     * @param body
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST(ApiConfig.APP_PATH_SEND_GROUP_DYNAMIC)
    Observable<BaseJsonV2<Object>> sendGroupDynamic(@Path("group") int group, @Body RequestBody body);

    /**
     * 删除动态 v2 接口--社区
     *
     * @param groupId    社区id
     * @param dynamic_id 动态id
     */
    @DELETE(ApiConfig.APP_PATH_DELETE_GROUP_DYNAMIC)
    Observable<BaseJsonV2<Object>> deleteGroupDynamic(@Path("group") long groupId, @Path("post") long dynamic_id);
}
