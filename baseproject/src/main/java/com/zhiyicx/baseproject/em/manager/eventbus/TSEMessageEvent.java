package com.zhiyicx.baseproject.em.manager.eventbus;

import com.hyphenate.chat.EMMessage;

/**
 * @author Jliuer
 * @Date 18/02/01 11:09
 * @Email Jliuer@aliyun.com
 * @Description 自定义EventBus post 的事件，传递消息变化信息
 */
public class TSEMessageEvent {

    /**
     * 变化的消息对象
     */
    private EMMessage message;

    /**
     * 消息状态
     */
    private EMMessage.Status status;

    /**
     * 消息进度
     */
    private int progress;

    /**
     * 消息出现错误时的错误码
     */
    private int errorCode;

    /**
     * 消息出现错误时的错误信息
     */
    private String errorMessage;

    public TSEMessageEvent() {

    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public EMMessage getMessage() {
        return message;
    }

    public void setMessage(EMMessage message) {
        this.message = message;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public EMMessage.Status getStatus() {
        return status;
    }

    public void setStatus(EMMessage.Status status) {
        this.status = status;
    }
}
