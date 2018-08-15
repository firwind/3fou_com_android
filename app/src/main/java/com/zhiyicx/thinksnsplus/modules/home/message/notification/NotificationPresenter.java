package com.zhiyicx.thinksnsplus.modules.home.message.notification;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.NotificationBean;
import com.zhiyicx.thinksnsplus.data.beans.UnReadNotificaitonBean;
import com.zhiyicx.thinksnsplus.data.beans.UserFollowerCountBean;
import com.zhiyicx.thinksnsplus.data.source.repository.MessageRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.FuncN;

/**
 * author: huwenyong
 * date: 2018/8/15 10:45
 * description:
 * version:
 */

public class NotificationPresenter extends AppBasePresenter<NotificationContract.View> implements NotificationContract.Presenter{

    @Inject
    MessageRepository mMessageRepository;

    @Inject
    UserInfoRepository mUserInfoRepository;

    private Subscription mUnreadNotiSub;

    @Inject
    public NotificationPresenter(NotificationContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        if (mUnreadNotiSub != null && !mUnreadNotiSub.isUnsubscribed()) {
            mUnreadNotiSub.unsubscribe();
        }
        mUnreadNotiSub = Observable.zip(mMessageRepository.getUnreadNotificationData(),
                mUserInfoRepository.getUserAppendFollowerCount(), new Func2<UnReadNotificaitonBean, UserFollowerCountBean, List<NotificationBean>>() {
                    @Override
                    public List<NotificationBean> call(UnReadNotificaitonBean t1, UserFollowerCountBean t2) {

                        List<NotificationBean> list = new ArrayList<>();
                        for (int i = 0; i < 7; i++) {
                            NotificationBean bean = new NotificationBean();
                        }

                        return list;
                    }
                }).subscribe();
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<NotificationBean> data, boolean isLoadMore) {
        return false;
    }
}
