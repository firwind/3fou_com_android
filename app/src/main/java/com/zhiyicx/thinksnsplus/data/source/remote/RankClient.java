package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;


/**
 * @Describe
 * @Author zl
 * @Date 2018/4/24
 * @Contact master.jungle68@gmail.com
 */
public interface RankClient {
    /**
     * 全站粉丝排行
     *
     * @param limit 分页数量
     * @param offset  偏移量
     * @return
     */
    @GET(ApiConfig.APP_PATH_RANK_ALL_FOLLOWER)
    Observable<List<UserInfoBean>> getRankFollower(@Query("limit") Integer limit,
                                                   @Query("offset") int offset);

    /**
     * 财富排行榜
     * @param limit
     * @param offset
     * @return
     */
    @GET(ApiConfig.APP_PATH_RANK_ALL_RICHES)
    Observable<List<UserInfoBean>> getRankRiches(@Query("limit") Integer limit,
                                                 @Query("offset") int offset);

    /**
     * 收入排行榜
     * @param limit
     * @param offset
     * @return
     */
    @GET(ApiConfig.APP_PATH_RANK_INCOME)
    Observable<List<UserInfoBean>> getRankIncome(@Query("limit") Integer limit,
                                                 @Query("offset") int offset);

    /**
     * 连续签到排行榜
     * @param limit
     * @param offset
     * @return
     */
    @GET(ApiConfig.APP_PATH_RANK_CHECK_IN)
    Observable<List<UserInfoBean>> getRankCheckIn(@Query("limit") Integer limit,
                                                  @Query("offset") int offset);

    /**
     * 社区专家排行榜
     * @param limit
     * @param offset
     * @return
     */
    @GET(ApiConfig.APP_PATH_RANK_QUESTION_EXPERTS)
    Observable<List<UserInfoBean>> getRankQuestionExpert(@Query("limit") Integer limit,
                                                         @Query("offset") int offset);

    /**
     * 问答达人排行榜
     */
    @GET(ApiConfig.APP_PATH_RANK_QUESTION_LIKES)
    Observable<List<UserInfoBean>> getRankQuestionLikes(@Query("limit") Integer limit,
                                                        @Query("offset") int offset);

    /**
     * 问答解答排行榜
     *
     * @param type 筛选类型 day - 日排行 week - 周排行 month - 月排行
     */
    @GET(ApiConfig.APP_PATH_RANK_QUESTION_ANSWER)
    Observable<List<UserInfoBean>> getRankAnswer(@Query("type") String type,
                                                 @Query("limit") Integer limit,
                                                 @Query("offset") int offset);

    /**
     * 动态排行榜
     *
     * @param type 筛选类型 day - 日排行 week - 周排行 month - 月排行
     */
    @GET(ApiConfig.APP_PATH_RANK_FEEDS)
    Observable<List<UserInfoBean>> getRankDynamic(@Query("type") String type,
                                                  @Query("limit") Integer limit,
                                                  @Query("offset") int offset);

    /**
     * 资讯排行榜
     *
     * @param type 筛选类型 day - 日排行 week - 周排行 month - 月排行
     */
    @GET(ApiConfig.APP_PATH_RANK_NEWS)
    Observable<List<UserInfoBean>> getRankInfo(@Query("type") String type,
                                               @Query("limit") Integer limit,
                                               @Query("offset") int offset);
}
