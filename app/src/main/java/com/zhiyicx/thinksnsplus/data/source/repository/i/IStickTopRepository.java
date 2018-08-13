package com.zhiyicx.thinksnsplus.data.source.repository.i;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.data.beans.StickTopAverageBean;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_DYNAMIC_TOP_AVERAGE_NUM;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_TOP_AVERAGE_NUM;

/**
 * @Describe
 * @Author zl
 * @Date 2017/12/26
 * @Contact master.jungle68@gmail.com
 */
public interface IStickTopRepository {
    /**
     * 置顶内容
     * @param type
     * @param parent_id
     * @param amount
     * @param day
     * @return
     */
    Observable<BaseJsonV2<Integer>> stickTop(String type, long parent_id, double amount, int day);

    /**
     * 置顶内容中的评论
     * @param type
     * @param parent_id
     * @param child_id
     * @param amount
     * @param day
     * @return
     */
    Observable<BaseJsonV2<Integer>> stickTop(String type,long parent_id,long child_id, double amount, int day);

    /**
     * @return 动态平均置顶金额
     */
    Observable<StickTopAverageBean> getDynamicAndCommentTopAverageNum();
    /**
     * @return 资讯平均置顶金额
     */
    Observable<StickTopAverageBean> getInfoAndCommentTopAverageNum();

    /**
     * 社区、帖子、评论置顶平均金额
     * @return
     */
    Observable<StickTopAverageBean> getCircleAndCommentTopAverageNum();

    /**
     * 根据 type 获取平均置顶金额
     * @param type @link{#StickTopFragment.TYPE_DYNAMIC}
     * @return
     */
    Observable<StickTopAverageBean> getCircleAndCommentTopAverageNumByType(String type );
}
