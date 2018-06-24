package com.zhiyicx.baseproject.base;

import com.zhiyicx.common.mvp.i.IBasePresenter;

/**
 * @Describe 用于游客模式的公用判断
 * @Author zl
 * @Date 2017/5/15
 * @Contact master.jungle68@gmail.com
 */

public interface IBaseTouristPresenter extends IBasePresenter {
    /**
     * 是否是游客
     * @return
     */
    boolean isTourist();

    /**
     * 判断是否登录
     *
     * @return true  is login
     */
    boolean isLogin();

    /**
     * 处理游客点击事件
     *
     * @return
     */
    boolean handleTouristControl();

    SystemConfigBean getSystemConfigBean();

    String getGoldName();

    String getGoldUnit();

    int getRatio();
}