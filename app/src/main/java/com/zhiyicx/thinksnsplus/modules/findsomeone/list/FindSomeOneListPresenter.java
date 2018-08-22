package com.zhiyicx.thinksnsplus.modules.findsomeone.list;

import android.support.v4.app.Fragment;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.base.BaseSubscriberV3;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseFriendsRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.home.mine.friends.verify.VerifyFriendOrGroupActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;

import static com.zhiyicx.baseproject.base.TSListFragment.DEFAULT_PAGE_SIZE;
import static com.zhiyicx.thinksnsplus.modules.findsomeone.list.FindSomeOneListFragment.TYPE_HOT;
import static com.zhiyicx.thinksnsplus.modules.findsomeone.list.FindSomeOneListFragment.TYPE_NEARBY;
import static com.zhiyicx.thinksnsplus.modules.findsomeone.list.FindSomeOneListFragment.TYPE_NEW;
import static com.zhiyicx.thinksnsplus.modules.findsomeone.list.FindSomeOneListFragment.TYPE_RECOMMENT;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/13
 * @contact email:450127106@qq.com
 */
@FragmentScoped
public class FindSomeOneListPresenter extends AppBasePresenter<FindSomeOneListContract.View> implements FindSomeOneListContract.Presenter {

    UserInfoRepository mUserInfoRepository;
    @Inject
    BaseFriendsRepository mBaseFriendsRepository;

    @Inject
    public FindSomeOneListPresenter(FindSomeOneListContract.View rootView,UserInfoRepository userInfoRepository) {
        super(rootView);
        mUserInfoRepository = userInfoRepository;
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(null, isLoadMore);

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<UserInfoBean> data, boolean isLoadMore) {
        return true;
    }

    @Override
    public void requestNetData(final Long maxId, final boolean isLoadMore, final int pageType) {
        Observable<List<UserInfoBean>> observable = null;

        switch (pageType) {
            case TYPE_HOT:
                observable = mUserInfoRepository.getHotUsers(DEFAULT_PAGE_SIZE, maxId.intValue());
                break;

            case TYPE_NEW:
                observable = mUserInfoRepository.getNewUsers(DEFAULT_PAGE_SIZE, maxId.intValue());
                break;
            // 后台推荐 + 用户 tag 推荐 ，
            case TYPE_RECOMMENT:
                if (!isLoadMore) {
                    observable = Observable.zip(mUserInfoRepository.getRecommendUserInfo(), mUserInfoRepository.getUsersRecommentByTag
                            (DEFAULT_PAGE_SIZE, maxId.intValue()), (userInfoBeen, userInfoBeen2) -> {
                        mRootView.setRecommentUserSize(userInfoBeen.size());
                        userInfoBeen.addAll(userInfoBeen2);
                        return userInfoBeen;
                    });

                } else {
                    observable = mUserInfoRepository.getUsersRecommentByTag(DEFAULT_PAGE_SIZE, maxId.intValue());
                }

                break;
            case TYPE_NEARBY:
                observable = mUserInfoRepository.getHotUsers(DEFAULT_PAGE_SIZE, maxId.intValue());
                break;
            default:
        }

        Subscription subscription = observable
                .subscribe(new BaseSubscribeForV2<List<UserInfoBean>>() {
                    @Override
                    protected void onSuccess(List<UserInfoBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        Throwable throwable = new Throwable(message);
                        mRootView.onResponseError(throwable, isLoadMore);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        LogUtils.e(throwable, throwable.getMessage());
                        mRootView.onResponseError(throwable, isLoadMore);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void followUser(int index, UserInfoBean followFansBean) {
        mUserInfoRepository.handleFollow(followFansBean);
        mRootView.upDateFollowFansState(index);

    }

    @Override
    public void cancleFollowUser(int index, UserInfoBean followFansBean) {
        mUserInfoRepository.handleFollow(followFansBean);
        mRootView.upDateFollowFansState(index);
    }

    @Override
    public void addFriend(int index, UserInfoBean userInfoBean) {
        mBaseFriendsRepository.addFriend(String.valueOf(userInfoBean.getUser_id()))
                .subscribe(new BaseSubscriberV3<String>(mRootView) {
                    @Override
                    protected void onSuccess(String data) {
                        super.onSuccess(data);
                        userInfoBean.setIs_my_friend(true);
                        mRootView.upDateFollowFansState(index);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        //super.onFailure(message, code);
                        if(code == 504){//需要验证
                            VerifyFriendOrGroupActivity.startVerifyFriendsActivity( ((Fragment)mRootView).getContext(),
                                    String.valueOf(userInfoBean.getUser_id()) );
                        }else{
                            mRootView.showSnackErrorMessage(message);
                        }
                    }
                });
    }


}
