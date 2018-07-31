package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.baseproject.config.ApiConfig;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_CHANGE_PASSWORD_V2;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_FIND_PASSWORD_V2;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_SET_OR_UPDATE_PAY_PASSWORD;


/**
 * @Describe 密码相关
 * @Author zl
 * @Date 2017/1/
 * @Contact master.jungle68@gmail.com
 */

public interface PasswordClient {

    public static final String REGITER_TYPE_EMAIL = "mail";
    public static final String REGITER_TYPE_SMS = "sms";

    @PUT(APP_PATH_CHANGE_PASSWORD_V2)
    Observable<Object> changePasswordV2(@Body Map<String,String> data);


    /**
     * 找回密码
     */
    @FormUrlEncoded
    @PUT(APP_PATH_FIND_PASSWORD_V2)
    Observable<CacheBean> findPasswordV2( @Field("phone") String phone
            , @Field("email") String email
            , @Field("verifiable_code") String vertifyCode
            , @Field("verifiable_type") String verifiable_type
            , @Field("password") String newPassword);

    /**
     * 设置或更新支付密码
     * @param type
     * @param password
     * @param old_pay_pwd
     * @return
     */
    @POST(APP_PATH_SET_OR_UPDATE_PAY_PASSWORD)
    Observable<String> setOrUpdatePayPwd(@Query("type")String type,@Query("password")String password,
                                         @Query("old_pay_password")String old_pay_pwd);

    /**
     * 找回支付密码
     * @param phone
     * @param code
     * @param pay_password
     * @return
     */
    @POST(ApiConfig.APP_PATH_FIND_PAY_PASSWORD)
    Observable<String> findPayPassword(@Query("phone")String phone,@Query("code")String code,
                                       @Query("pay_password")String pay_password);
}
