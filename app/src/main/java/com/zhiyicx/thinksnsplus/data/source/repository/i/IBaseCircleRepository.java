package com.zhiyicx.thinksnsplus.data.source.repository.i;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.CircleEarningListBean;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.beans.CircleMemberCountBean;
import com.zhiyicx.thinksnsplus.data.beans.CircleMembers;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.data.beans.CircleReportListBean;
import com.zhiyicx.thinksnsplus.data.beans.CircleTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.PostDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.PostPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.circle.CircleCommentZip;
import com.zhiyicx.thinksnsplus.data.beans.circle.CreateCircleBean;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_ALL_POSTLIST;

/**
 * @author Jliuer
 * @Date 2017/11/21/15:34
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface IBaseCircleRepository {

    Observable<List<CircleTypeBean>> getCategroiesList(Integer limit, int offet);

    /**
     * 创建社区
     *
     * @param createCircleBean
     * @return
     */
    Observable<BaseJsonV2<CircleInfo>> createCircle(CreateCircleBean createCircleBean);

    /**
     * 获取社区协议
     *
     * @return
     */
    Observable<BaseJsonV2<String>> getCircleRule();

    /**
     * 修改社区信息
     *
     * @param createCircleBean
     * @return
     */
    Observable<BaseJsonV2<CircleInfo>> updateCircle(CreateCircleBean createCircleBean);

    Observable<BaseJsonV2<CirclePostListBean>> sendCirclePost(PostPublishBean publishBean);

    Observable<List<CirclePostListBean>> getPostListFromCircle(long circleId, long maxId, String type);

    /**
     * 获取我的帖子列表
     *
     * @param limit  默认 15 ，数据返回条数 默认为15
     * @param offset 默认 0 ，数据偏移量，传递之前通过接口获取的总数。
     * @param type   参数 type 默认 1，   1-发布的 2- 已置顶 3-置顶待审
     * @return
     */
    Observable<List<CirclePostListBean>> getMinePostList(Integer limit, Integer offset, Integer type);

    /**
     * 全部帖子列表包含搜索
     *
     * @param limit    默认 15 ，数据返回条数 默认为15
     * @param offset   默认 0 ，数据偏移量，传递之前通过接口获取的总数。
     * @param keyword  搜索关键词，模糊匹配社区名称
     * @param group_id 获取某个社区下面的全部帖子
     * @return
     */
    @GET(APP_PATH_GET_ALL_POSTLIST)
    Observable<List<CirclePostListBean>> getAllePostList(Integer limit, Integer offset, String keyword, Long group_id);

    /**
     * 用户帖子收藏列表
     *
     * @param limit  默认 15 ，数据返回条数 默认为15
     * @param offset 默认 0 ，数据偏移量，传递之前通过接口获取的总数。
     * @return
     */
    Observable<List<CirclePostListBean>> getUserCollectPostList(Integer limit, Integer offset);

    /**
     * 获取我加入的社区
     *
     * @param limit
     * @param offet
     * @param type  默认: join, join 我加入 audit 待审核 allow_post 可以发帖的
     * @return
     */
    Observable<List<CircleInfo>> getMyJoinedCircle(Integer limit, int offet, String type);

    /**
     * 获取全部社区
     *
     * @param limit       默认 15 ，数据返回条数 默认为15
     * @param offet       默认 0 ，数据偏移量，传递之前通过接口获取的总数。
     * @param keyword     用于搜索社区，按圈名搜索
     * @param category_id 社区分类id
     * @return
     */
    Observable<List<CircleInfo>> getAllCircle(Integer limit, Integer offet, String keyword
            , Integer category_id);

    /**
     * 设置社区权限
     *
     * @return
     */
    Observable<BaseJsonV2<Object>> setCirclePermissions(long circleId, List<String> permissions);

    Observable<BaseJsonV2<Integer>> getCircleCount();

    void sendPostComment(String commentContent, Long postId, Long replyToUserId, Long commentMark);

    void deletePostComment(long postId, long commentId);

    void deletePost(long circleId, long postId);

    void dealLike(boolean isLike, long postId);

    void dealCollect(boolean isCollected, long postId);

    Observable<BaseJsonV2<Object>> dealCircleJoinOrExit(CircleInfo circleInfo);

    Observable<List<RewardsListBean>> getPostRewardList(long post_id, Integer limit,
                                                        Integer offset, String order,
                                                        String order_type);

    Observable<List<PostDigListBean>> getPostDigList(long postId, Integer limit, long offet);

    /**
     * 将某个成员踢出社区
     *
     * @return
     */
    Observable<BaseJsonV2<Object>> cancleCircleMember(long circleId, long memberId);

    /**
     * 指定/撤销社区管理员职位
     *
     * @param circleId
     * @param memberId
     * @return
     */
    Observable<BaseJsonV2<Object>> appointCircleManager(long circleId, long memberId);

    Observable<BaseJsonV2<Object>> cancleCircleManager(long circleId, long memberId);

    /**
     * 加入/移除社区黑名单
     *
     * @param circleId
     * @param memberId
     * @return
     */
    Observable<BaseJsonV2<Object>> appointCircleBlackList(long circleId, long memberId);

    Observable<BaseJsonV2<Object>> cancleCircleBlackList(long circleId, long memberId);

    Observable<CircleInfo> getCircleInfo(long circleId);

    /**
     * 社区收入记录
     *
     * @param circleId 社区id
     * @param start    秒级时间戳，起始筛选时间
     * @param end      秒级时间戳，结束筛选时间
     * @param after    默认 0 ，翻页标识。
     * @param limit    默认 15 ，数据返回条数 默认为15
     * @param type     默认 all, all-全部 join-成员加入 pinned-帖子置顶
     * @return
     */
    Observable<List<CircleEarningListBean>> getCircleEarningList(Long circleId, Long start, Long end, Long after, Integer limit, String type);

    /**
     * 社区举报列表
     *
     * @param groupId 社区id
     * @param after
     * @param limit
     * @param start   秒级时间戳，起始筛选时间
     * @param end     秒级时间戳，结束筛选时间
     * @param status  状态 默认全部，0-未处理 1-已处理 2-已驳回
     * @return
     */
    Observable<List<CircleReportListBean>> getCircleReportList(Long groupId, Integer status, Integer after,
                                                               Integer limit, Long start, Long end);

    /**
     * 同意举报
     *
     * @param reportId 舉報的id
     * @return
     */
    Observable<BaseJsonV2> approvedCircleReport(Long reportId);

    /**
     * 拒绝举报
     *
     * @param reportId 舉報的id
     * @return
     */
    Observable<BaseJsonV2> refuseCircleReport(Long reportId);

    /**
     * @param circleId
     * @param after
     * @param limit
     * @param type     默认 all, all-所有, manager-管理员, member-成员, blacklist-黑名单, audit - 带审核
     * @param name     仅仅用于搜索
     * @return
     */
    Observable<List<CircleMembers>> getCircleMemberList(long circleId, int after, Integer limit, String type, String name);

    Observable<CircleMembers> attornCircle(long circleId, long userId);

    /**
     * 社区成员角色统计
     * @param circleId
     * @return
     */
    Observable<CircleMemberCountBean> getGroupMemberCount(long circleId);



    Observable<CirclePostListBean> getPostDetail(long circleId, long postId);

    Observable<List<CirclePostCommentBean>> getPostComments(long postId, Integer limit, int after);

    Observable<CircleCommentZip> getPostCommentList(long postId, Long maxId);

    /**
     * 获取推荐的社区
     *
     * @param limit 默认 20 ，数据返回条数 默认为20
     * @param offet 默认 0 ，数据偏移量，传递之前通过接口获取的总数。
     * @param type  random 随机
     * @return
     */
    Observable<List<CircleInfo>> getRecommendCircle(Integer limit, int offet, String type);

    Observable<List<CircleInfo>> getCircleList(long categoryId, long maxId);

    /**
     * 圈主和管理员置顶帖子
     *
     * @param postId
     * @param day    天数
     * @return
     */
    Observable<BaseJsonV2<Object>> stickTopPost(Long postId, int day);

    /**
     * 圈主和管理员撤销置顶帖子
     *
     * @param postId
     * @return
     */
    Observable<BaseJsonV2<Object>> undoTopPost(Long postId);

    void handleFollow(UserInfoBean userInfoBean);
}
