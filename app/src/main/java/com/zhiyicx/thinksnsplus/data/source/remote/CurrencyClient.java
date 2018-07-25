package com.zhiyicx.thinksnsplus.data.source.remote;
/*
 * 文件名:货币相关接口  我的团队
 * 创建者：zhangl
 * 时  间：2018/7/19
 * 描  述：
 * 版  权: 九曲互动
 */

import com.zhiyicx.baseproject.config.ApiConfig;

import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

import com.zhiyicx.thinksnsplus.data.beans.SystemConversationBean;
import com.zhiyicx.thinksnsplus.data.beans.TeamBean;

import java.util.List;


public interface CurrencyClient {

    @GET(ApiConfig.APP_PATH_GET_MY_CURRENCY_LIST)
    Observable<String> getMyCurrencyList();

    @GET(ApiConfig.APP_PATH_CURRENCY_ADDRESS_LIST)
    Observable<String> getCurrencyAddressList(@Query("currency") String currency);

    @POST(ApiConfig.APP_PATH_CURRENCY_ADDRESS_MANAGE)
    Observable<String> addCurrencyAddress(@Query("address")String address,@Query("currency")String currency,@Query("mark")String mark);

    @GET(ApiConfig.APP_PATH_CURRENCY_ADDRESS_MANAGE)
    Observable<String> editCurrencyAddress(@Query("address_id")String address_id,@Query("address")String address,@Query("mark")String mark);

    @DELETE(ApiConfig.APP_PATH_CURRENCY_ADDRESS_MANAGE)
    Observable<String> deleteCurrencyAddress(@Query("address_id")String address_id);



    /**
     * 获取团队列表
     * @param type 获取币种
     * @param grade 推荐人等级
     * @return
     */
    @GET(ApiConfig.APP_PATH_GET_MY_TEAM_LIST)
    Observable<List<TeamBean.TeamListBean>> getTeamList(@Path ("type") String type, @Path("grade") int grade);
}
