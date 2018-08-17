package com.zhiyicx.thinksnsplus.data.beans;

import java.util.List;

/**
 * @Describe 为读书统计总模型  {@see https://slimkit.github.io/plus-docs/v2/core/users/unread}
 * @Author zl
 * @Date 2017/10/23
 * @Contact master.jungle68@gmail.com
 */
public class UnReadNotificaitonBean {
    private CountBean counts;
    private List<UnreadCountBean> comments;
    private List<UnreadCountBean> likes;
    private List<UnreadCountBean> groupChecks;
    private List<UnreadCountBean> friendChecks;
    private UnhandlePinnedBean pinneds;
    private TSPNotificationBean system;

    public List<UnreadCountBean> getGroupChecks() {
        return groupChecks;
    }

    public void setGroupChecks(List<UnreadCountBean> groupChecks) {
        this.groupChecks = groupChecks;
    }

    public List<UnreadCountBean> getFriendChecks() {
        return friendChecks;
    }

    public void setFriendChecks(List<UnreadCountBean> friendChecks) {
        this.friendChecks = friendChecks;
    }

    public CountBean getCounts() {
        return counts;
    }

    public void setCounts(CountBean counts) {
        this.counts = counts;
    }

    public List<UnreadCountBean> getComments() {
        return comments;
    }

    public void setComments(List<UnreadCountBean> comments) {
        this.comments = comments;
    }

    public List<UnreadCountBean> getLikes() {
        return likes;
    }

    public void setLikes(List<UnreadCountBean> likes) {
        this.likes = likes;
    }

    public UnhandlePinnedBean getPinneds() {
        return pinneds;
    }

    public void setPinneds(UnhandlePinnedBean pinneds) {
        this.pinneds = pinneds;
    }

    public TSPNotificationBean getSystem() {
        return system;
    }

    public void setSystem(TSPNotificationBean system) {
        this.system = system;
    }

    public static class CountBean {
        private Long user_id;
        private int unread_comments_count;
        private int unread_likes_count;
        private int unread_group_join_count;
        private String created_at;
        private String updated_at;
        private int unread_friend;
        /**
         * 系统消息未读数
         */
        private int system;

        public int getUnread_friend() {
            return unread_friend;
        }

        public void setUnread_friend(int unread_friend) {
            this.unread_friend = unread_friend;
        }

        public Long getUser_id() {
            return user_id;
        }

        public void setUser_id(Long user_id) {
            this.user_id = user_id;
        }

        public int getUnread_comments_count() {
            return unread_comments_count;
        }

        public int getUnread_group_join_count() {
            return unread_group_join_count;
        }

        public void setUnread_group_join_count(int unread_group_join_count) {
            this.unread_group_join_count = unread_group_join_count;
        }

        public void setUnread_comments_count(int unread_comments_count) {
            this.unread_comments_count = unread_comments_count;
        }

        public int getUnread_likes_count() {
            return unread_likes_count;
        }

        public void setUnread_likes_count(int unread_likes_count) {
            this.unread_likes_count = unread_likes_count;
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

        public int getSystem() {
            return system;
        }

        public void setSystem(int system) {
            this.system = system;
        }
    }
}
