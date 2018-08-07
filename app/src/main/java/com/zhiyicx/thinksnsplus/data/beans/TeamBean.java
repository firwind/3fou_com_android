package com.zhiyicx.thinksnsplus.data.beans;
/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/7/19
 * 描  述：
 * 版  权: 九曲互动
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;

import java.util.ArrayList;
import java.util.List;

public class TeamBean extends BaseListBean{

    /**
     * total : 100
     * unit : BCB
     * teamList : [{"userName":"张小哇","earnings":"100","time":"2018/7/20","id":1},{"userName":"文能安邦","earnings":"100","time":"2018/7/20","id":2},{"userName":"huwenyong","earnings":"100","time":"2018/7/20","id":3}]
     */

    private String total;
    private String unit;
    private List<TeamListBean> teamList;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<TeamListBean> getTeamList() {
        return teamList;
    }

    public void setTeamList(List<TeamListBean> teamList) {
        this.teamList = teamList;
    }

    public static class TeamListBean extends BaseListBean implements Parcelable{

        /**
         * id : 82
         * name : 指导
         * avatar : http://test.faceke.com/api/v2/users/82/avatar?v=1532533158
         * bg : null
         * verified : null
         * extra : {"user_id":82,"likes_count":0,"comments_count":0,"followers_count":1,"followings_count":1,"updated_at":"2018-07-12 16:06:32","feeds_count":0,"questions_count":0,"answers_count":0,"checkin_count":0,"last_checkin_count":0}
         * totals : [{"total":"1000.0000000000"}]
         * time : 1532940203
         */

        private int id;
        private String name;
        private String avatar;
        private String bg;
        private String verified;

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        private String balance;
        private ExtraBean extra;
        private long time;
        private String totals;

        public String getTotals() {
            return totals;
        }

        public void setTotals(String totals) {
            this.totals = totals;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public Object getBg() {
            return bg;
        }

        public void setBg(String bg) {
            this.bg = bg;
        }

        public Object getVerified() {
            return verified;
        }

        public void setVerified(String verified) {
            this.verified = verified;
        }

        public ExtraBean getExtra() {
            return extra;
        }

        public void setExtra(ExtraBean extra) {
            this.extra = extra;
        }

        public long getTime() {
            return time*1000;
        }

        public void setTime(int time) {
            this.time = time;
        }



        public static class ExtraBean implements Parcelable{
            /**
             * user_id : 82
             * likes_count : 0
             * comments_count : 0
             * followers_count : 1
             * followings_count : 1
             * updated_at : 2018-07-12 16:06:32
             * feeds_count : 0
             * questions_count : 0
             * answers_count : 0
             * checkin_count : 0
             * last_checkin_count : 0
             */

            private int user_id;
            private int likes_count;
            private int comments_count;
            private int followers_count;
            private int followings_count;
            private String updated_at;
            private int feeds_count;
            private int questions_count;
            private int answers_count;
            private int checkin_count;
            private int last_checkin_count;

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public int getLikes_count() {
                return likes_count;
            }

            public void setLikes_count(int likes_count) {
                this.likes_count = likes_count;
            }

            public int getComments_count() {
                return comments_count;
            }

            public void setComments_count(int comments_count) {
                this.comments_count = comments_count;
            }

            public int getFollowers_count() {
                return followers_count;
            }

            public void setFollowers_count(int followers_count) {
                this.followers_count = followers_count;
            }

            public int getFollowings_count() {
                return followings_count;
            }

            public void setFollowings_count(int followings_count) {
                this.followings_count = followings_count;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public void setUpdated_at(String updated_at) {
                this.updated_at = updated_at;
            }

            public int getFeeds_count() {
                return feeds_count;
            }

            public void setFeeds_count(int feeds_count) {
                this.feeds_count = feeds_count;
            }

            public int getQuestions_count() {
                return questions_count;
            }

            public void setQuestions_count(int questions_count) {
                this.questions_count = questions_count;
            }

            public int getAnswers_count() {
                return answers_count;
            }

            public void setAnswers_count(int answers_count) {
                this.answers_count = answers_count;
            }

            public int getCheckin_count() {
                return checkin_count;
            }

            public void setCheckin_count(int checkin_count) {
                this.checkin_count = checkin_count;
            }

            public int getLast_checkin_count() {
                return last_checkin_count;
            }

            public void setLast_checkin_count(int last_checkin_count) {
                this.last_checkin_count = last_checkin_count;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.user_id);
                dest.writeInt(this.likes_count);
                dest.writeInt(this.comments_count);
                dest.writeInt(this.followers_count);
                dest.writeInt(this.followings_count);
                dest.writeString(this.updated_at);
                dest.writeInt(this.feeds_count);
                dest.writeInt(this.questions_count);
                dest.writeInt(this.answers_count);
                dest.writeInt(this.checkin_count);
                dest.writeInt(this.last_checkin_count);
            }

            public ExtraBean() {
            }

            protected ExtraBean(Parcel in) {
                this.user_id = in.readInt();
                this.likes_count = in.readInt();
                this.comments_count = in.readInt();
                this.followers_count = in.readInt();
                this.followings_count = in.readInt();
                this.updated_at = in.readString();
                this.feeds_count = in.readInt();
                this.questions_count = in.readInt();
                this.answers_count = in.readInt();
                this.checkin_count = in.readInt();
                this.last_checkin_count = in.readInt();
            }

            public static final Creator<ExtraBean> CREATOR = new Creator<ExtraBean>() {
                @Override
                public ExtraBean createFromParcel(Parcel source) {
                    return new ExtraBean(source);
                }

                @Override
                public ExtraBean[] newArray(int size) {
                    return new ExtraBean[size];
                }
            };
        }

        public static class TotalsBean {
            /**
             * total : 1000.0000000000
             */

            private String total;

            public String getTotal() {
                return total;
            }

            public void setTotal(String total) {
                this.total = total;
            }
        }

        public TeamListBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.id);
            dest.writeString(this.name);
            dest.writeString(this.avatar);
            dest.writeString(this.bg);
            dest.writeString(this.verified);
            dest.writeString(this.balance);
            dest.writeParcelable(this.extra, flags);
            dest.writeLong(this.time);
            dest.writeString(this.totals);
        }

        protected TeamListBean(Parcel in) {
            super(in);
            this.id = in.readInt();
            this.name = in.readString();
            this.avatar = in.readString();
            this.bg = in.readString();
            this.verified = in.readString();
            this.balance = in.readString();
            this.extra = in.readParcelable(ExtraBean.class.getClassLoader());
            this.time = in.readInt();
            this.totals = in.readString();
        }

        public static final Creator<TeamListBean> CREATOR = new Creator<TeamListBean>() {
            @Override
            public TeamListBean createFromParcel(Parcel source) {
                return new TeamListBean(source);
            }

            @Override
            public TeamListBean[] newArray(int size) {
                return new TeamListBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.total);
        dest.writeString(this.unit);
        dest.writeList(this.teamList);
    }

    public TeamBean() {
    }

    protected TeamBean(Parcel in) {
        super(in);
        this.total = in.readString();
        this.unit = in.readString();
        this.teamList = new ArrayList<TeamListBean>();
        in.readList(this.teamList, TeamListBean.class.getClassLoader());
    }

    public static final Creator<TeamBean> CREATOR = new Creator<TeamBean>() {
        @Override
        public TeamBean createFromParcel(Parcel source) {
            return new TeamBean(source);
        }

        @Override
        public TeamBean[] newArray(int size) {
            return new TeamBean[size];
        }
    };
}
