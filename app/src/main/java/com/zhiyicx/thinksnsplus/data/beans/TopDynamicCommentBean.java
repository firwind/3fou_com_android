package com.zhiyicx.thinksnsplus.data.beans;

import com.zhiyicx.baseproject.base.BaseListBean;

/**
 * @Author Jliuer
 * @Date 2017/07/05
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class TopDynamicCommentBean extends BaseListBean {

    public static final int TOP_REFUSE = 0;
    public static final int TOP_REVIEWING = 1;
    public static final int TOP_SUCCESS = 2;


    /**
     * id : 4
     * channel : comment
     * target : 1
     * user_id : 1
     * amount : 1
     * day : 3
     * expires_at : null
     * created_at : 2017-07-21 03:47:09
     * updated_at : 2017-07-21 03:47:09
     * target_user : 1
     * raw : 1
     * feed : {"id":1,"user_id":1,"feed_content":"动态内容","feed_from":1,"like_count":1,"feed_view_count":0,"feed_comment_count":6,"feed_latitude":null,"feed_longtitude":null,"feed_geohash":null,"audit_status":1,"feed_mark":1,"pinned":0,"created_at":"2017-06-27 07:04:32","updated_at":"2017-07-20 08:53:24","deleted_at":null,"pinned_amount":0,"images":[],"paid_node":null}
     * comment : {"id":1,"user_id":1,"target_user":1,"reply_user":0,"body":"我是第一条评论","commentable_id":1,"commentable_type":"feeds","created_at":"2017-07-20 08:34:41","updated_at":"2017-07-20 08:34:41"}
     */

    private Long id;
    private String channel;
    private int target;
    private Long user_id;
    private int amount;
    private int day;
    private String expires_at;
    private String created_at;
    private String updated_at;
    private int target_user;
    private int raw;
    private DynamicDetailBeanV2 feed;
    private CommentedBean comment;
    private UserInfoBean userInfoBean;

    @Override
    public Long getMaxId() {
        return id;
    }

    public UserInfoBean getUserInfoBean() {
        return userInfoBean;
    }

    public void setUserInfoBean(UserInfoBean userInfoBean) {
        this.userInfoBean = userInfoBean;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getExpires_at() {
        return expires_at;
    }

    public void setExpires_at(String expires_at) {
        this.expires_at = expires_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getTarget_user() {
        return target_user;
    }

    public void setTarget_user(int target_user) {
        this.target_user = target_user;
    }

    public int getRaw() {
        return raw;
    }

    public void setRaw(int raw) {
        this.raw = raw;
    }

    public DynamicDetailBeanV2 getFeed() {
        return feed;
    }

    public void setFeed(DynamicDetailBeanV2 feed) {
        this.feed = feed;
    }

    public CommentedBean getComment() {
        return comment;
    }

    public void setComment(CommentedBean comment) {
        this.comment = comment;
    }

}
