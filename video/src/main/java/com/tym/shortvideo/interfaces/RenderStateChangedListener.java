package com.tym.shortvideo.interfaces;


/**
 * @author Jliuer
 * @Date 18/04/28 9:31
 * @Email Jliuer@aliyun.com
 * @Description 渲染状态变更监听器
 */
public interface RenderStateChangedListener {
    // 是否处于预览状态
    void onPreviewing(boolean previewing);
}
