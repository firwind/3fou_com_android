package com.zhiyicx.thinksnsplus.data.beans.request;

/**
 * @Describe
 * @Author zl
 * @Date 2017/8/23
 * @Contact master.jungle68@gmail.com
 */
public class ThirdAccountBindRequestBean {
    private String access_token;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public ThirdAccountBindRequestBean(String access_token) {
        this.access_token = access_token;
    }
}
