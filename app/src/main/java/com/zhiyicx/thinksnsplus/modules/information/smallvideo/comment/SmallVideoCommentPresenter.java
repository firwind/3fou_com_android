package com.zhiyicx.thinksnsplus.modules.information.smallvideo.comment;

/**
 * author: huwenyong
 * date: 2018/8/30 10:57
 * description:
 * version:
 */

public interface SmallVideoCommentPresenter {

    void requestCommentList(Long maxId,boolean isLoadMore);

    void sendCommentV2(long replyToUserId, String commentContent);
}
