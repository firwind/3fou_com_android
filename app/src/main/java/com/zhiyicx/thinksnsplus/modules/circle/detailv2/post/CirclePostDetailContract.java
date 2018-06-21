package com.zhiyicx.thinksnsplus.modules.circle.detailv2.post;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsCountBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IBaseCircleRepository;

import java.util.List;

import rx.Observable;

/**
 * @author Jliuer
 * @Date 2017/12/01/16:18
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface CirclePostDetailContract {
    interface View extends ITSListView<CirclePostCommentBean, Presenter> {
        void setCollect(boolean isCollected);

        void setDigg(boolean isDigged);

        long getPostId();

        long getCircleId();

        void allDataReady(CirclePostListBean data);

        CirclePostListBean getCurrentePost();

        void updateReWardsView(RewardsCountBean rewardsCountBean, List<RewardsListBean> postRewardList);

        Bundle getArgumentsBundle();

        void updateCommentView(CirclePostListBean currentePost);

        void postHasBeDeleted();

        void loadAllError();

        void upDateFollowFansState(UserInfoBean userInfoBean);
    }

    interface Presenter extends ITSListPresenter<CirclePostCommentBean> {
        void deleteComment(CirclePostCommentBean data);

        List<RealAdvertListBean> getAdvert();

        void handleLike(boolean isLiked, long id);

        void shareInfo(Bitmap bitmap);

        void handleCollect(boolean isUnCollected, long id);

        void sendComment(long replyUserId, String text);

        void updateRewardData();

        /**
         * 圈主和管理员置顶帖子
         *
         * @param postId
         * @return
         */
        void stickTopPost(Long postId,int day);

        /**
         * 圈主和管理员撤销置顶帖子
         *
         * @param postId
         * @return
         */
        void undoTopPost(Long postId);

        void handleFollowUser(UserInfoBean userInfoBean);

        void setNeedDynamicListRefresh(boolean needDynamicListRefresh);
    }

}
