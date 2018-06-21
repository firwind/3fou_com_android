package com.zhiyicx.thinksnsplus.data.beans;

import com.google.gson.annotations.SerializedName;

/**
 * @author zl
 * @describe
 * @date 2018/5/8
 * @contact master.jungle68@gmail.com
 */
public class StickTopAverageBean {
    /**
     * /api/v2/feeds/average
     * {feed: 100, feed_comment: 100 }
     * /api/v2/news/average
     * {news: 100, news_comment: 100}
     * /api/v2/plus-group/average
     * { post: 100, post_comment: 100}
     */
    @SerializedName(value = "feed", alternate = {"news", "post"})
    private int sourceTopAverageNum;
    @SerializedName(value = "feed_comment", alternate = {"news_comment", "post_comment"})
    private int commentTopAverageNum;

    public int getSourceTopAverageNum() {
        return sourceTopAverageNum;
    }

    public void setSourceTopAverageNum(int sourceTopAverageNum) {
        this.sourceTopAverageNum = sourceTopAverageNum;
    }

    public int getCommentTopAverageNum() {
        return commentTopAverageNum;
    }

    public void setCommentTopAverageNum(int commentTopAverageNum) {
        this.commentTopAverageNum = commentTopAverageNum;
    }
}
