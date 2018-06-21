package com.zhiyicx.thinksnsplus.utils;

import com.zhiyicx.common.utils.ConvertUtils;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_DOMAIN;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_SHARE_URL_FORMAT;

/**
 * @author zl
 * @describe
 * @date 2018/5/9
 * @contact master.jungle68@gmail.com
 */
public class TSShareUtils {

    /**
     * 用户组合分享数据地址
     *
     * @param shareTypeUrl
     * @return
     */
    public static String convert2ShareUrl(String shareTypeUrl) {
        return APP_DOMAIN + APP_SHARE_URL_FORMAT + ConvertUtils.urlencode(shareTypeUrl);
    }

}
