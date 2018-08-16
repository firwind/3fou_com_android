package com.zhiyicx.thinksnsplus.modules.home.message.notification.review;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.GroupOrFriendReviewBean;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseFriendsRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * author: huwenyong
 * date: 2018/8/16 13:50
 * description:
 * version:
 */

public class NotificationReviewPresenter extends AppBasePresenter<NotificationReviewContract.View>
        implements NotificationReviewContract.Presenter{

    @Inject
    BaseFriendsRepository mBaseFriendsRepository;

    @Inject
    public NotificationReviewPresenter(NotificationReviewContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        mBaseFriendsRepository.getFriendReviewList(maxId)
                .subscribe(new BaseSubscribeForV2<List<GroupOrFriendReviewBean>>() {
                    @Override
                    protected void onSuccess(List<GroupOrFriendReviewBean> data) {
                        mRootView.onNetResponseSuccess(data,isLoadMore);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.onResponseError(e,isLoadMore);
                    }
                });
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<GroupOrFriendReviewBean> data, boolean isLoadMore) {
        return false;
    }
}
