package com.zhiyicx.thinksnsplus.modules.chat.select.addgroup;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupServerBean;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseMessageRepository;
import com.zhiyicx.thinksnsplus.modules.home.message.messagegroup.MessageGroupContract;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/*
 * 文件名：
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/25 18:04
 * 修改者：
 * 修改备注：
 * 修改时间：
 * 版  权：互动科技
 */
@FragmentScoped
public class AddGroupPresenter extends AppBasePresenter<AddGroupContract.View>
        implements AddGroupContract.Presenter {

    @Inject
    BaseMessageRepository mBaseMessageRepository;
    private Subscription mGroupExistSubscription;

    @Inject
    public AddGroupPresenter(AddGroupContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Subscription subscribe = mBaseMessageRepository.getSearchGroupInfoFace(mRootView.getsearchKeyWord(),mRootView.getPage())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<List<ChatGroupServerBean>>() {
                    @Override
                    protected void onSuccess(List<ChatGroupServerBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
//                        mRootView.showStickyMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.onResponseError(throwable, false);
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<ChatGroupServerBean> data, boolean isLoadMore) {
        return false;
    }

    EMGroup group = null;

    /**
     * 检测是否加入该群
     *
     * @param id
     * @param data
     */
    @Override
    public void checkIsAddGroup(String id, EMGroup data) {
        Observable.create(new Observable.OnSubscribe<List<EMGroup>>() {
            @Override
            public void call(Subscriber<? super List<EMGroup>> subscriber) {
                try {
                    List<EMGroup> grouplist = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
                    subscriber.onNext(grouplist);
                    subscriber.onCompleted();

                } catch (HyphenateException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).map(new Func1<List<EMGroup>, Boolean>() {
            @Override
            public Boolean call(List<EMGroup> emGroups) {
                boolean mResult = false;
                for (EMGroup emGroup : emGroups) {
                    if (emGroup.getGroupId().equals(id)) {
                        mResult = true;
                        break;
                    }
                }
                return mResult;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<Boolean>() {
                    @Override
                    protected void onSuccess(Boolean result) {
                        mRootView.checkIsAddGroup(id, data, result);
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }
                });
    }

    @Override
    public void checkGroupExist(String id) {
        if (mGroupExistSubscription != null && !mGroupExistSubscription.isUnsubscribed()) {
            mGroupExistSubscription.unsubscribe();
        }
        mGroupExistSubscription = Observable.just(id)
                .subscribeOn(Schedulers.io())
                .map(s -> {
                    /*EMGroup group = null;
                    try {
                        group = EMClient.getInstance().groupManager().getGroupFromServer(s);
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                    return group;*/
                    return EMClient.getInstance().groupManager().getGroup(id);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<EMGroup>() {
                    @Override
                    protected void onSuccess(EMGroup data) {
                        mRootView.checkGroupExist(id, data);
                    }
                });

        addSubscrebe(mGroupExistSubscription);

    }
}
