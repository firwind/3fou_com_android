package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.List;

import rx.Observable;

/**
 * @Describe
 * @Author zl
 * @Date 2018/4/24
 * @Contact master.jungle68@gmail.com
 */
public interface IBaseRankRepository {
    /**
     * 全站粉丝排行
     *
     * @param offset 偏移量
     * @return
     */
    Observable<List<UserInfoBean>> getRankFollower(int offset);

    /**
     * 财富排行榜
     *
     * @param offset
     * @return
     */
    Observable<List<UserInfoBean>> getRankRiches(int offset);

    /**
     * 收入排行榜
     *
     * @param offset
     * @return
     */
    Observable<List<UserInfoBean>> getRankIncome(int offset);

    /**
     * 连续签到排行榜
     *
     * @param offset
     * @return
     */
    Observable<List<UserInfoBean>> getRankCheckIn(int offset);

    /**
     * 社区专家排行榜
     *
     * @param offset
     * @return
     */
    Observable<List<UserInfoBean>> getRankQuestionExpert(int offset);

    /**
     * 问答达人排行榜
     */
    Observable<List<UserInfoBean>> getRankQuestionLikes(int offset);

    /**
     * 问答解答排行榜
     *
     * @param type 筛选类型 day - 日排行 week - 周排行 month - 月排行
     */
    Observable<List<UserInfoBean>> getRankAnswer(String type, int offset);

    /**
     * 动态排行榜
     *
     * @param type 筛选类型 day - 日排行 week - 周排行 month - 月排行
     */
    Observable<List<UserInfoBean>> getRankDynamic(String type, int offset);

    /**
     * 资讯排行榜
     *
     * @param type 筛选类型 day - 日排行 week - 周排行 month - 月排行
     */
    Observable<List<UserInfoBean>> getRankInfo(String type, int offset);
}
