package com.zhiyicx.thinksnsplus.modules.home.mine.friends;

import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.hyphenate.easeui.bean.ChatVerifiedBean;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.base.BaseSubscriberV3;
import com.zhiyicx.thinksnsplus.data.beans.UserFollowerCountBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseFriendsRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * @author Catherine
 * @describe
 * @date 2017/12/22
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class MyFriendsListPresenter extends AppBasePresenter<MyFriendsListContract.View>
        implements MyFriendsListContract.Presenter {

    @Inject
    BaseFriendsRepository mBaseFriendsRepository;

    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    public MyFriendsListPresenter(MyFriendsListContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Subscription subscription = mBaseFriendsRepository.getUserFriendsList(maxId, "")
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

        if (!isLoadMore&&!mRootView.isHasParentFragment()) {//只有当刷新和当前界面仅仅是获取好友列表的时候才清空
            mUserInfoRepository.clearUserMessageCount(UserFollowerCountBean.UserBean.MESSAGE_TYPE_MUTUAL)
                    .subscribe(new BaseSubscribeForV2<Object>() {
                        @Override
                        protected void onSuccess(Object data) {
                        }
                    });
        }
    }


    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        //获取本地相互关注的列表
        //List<UserInfoBean> followFansBeanList = mUserInfoBeanGreenDao.getUserFriendsList(maxId);
        mRootView.onCacheResponseSuccess(null, isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<UserInfoBean> data, boolean isLoadMore) {
        mUserInfoBeanGreenDao.saveMultiData(data);
        return true;
    }

    @Override
    public List<ChatUserInfoBean> getChatUserList(UserInfoBean userInfoBean) {
        List<ChatUserInfoBean> list = new ArrayList<>();
        list.add(getChatUser(mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault())));
        list.add(getChatUser(userInfoBean));
        return list;
    }

    @Override
    public void deleteFriend(int index, UserInfoBean userInfoBean) {
        mBaseFriendsRepository.deleteFriend(String.valueOf(userInfoBean.getUser_id()))
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage("请稍后..."))
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriberV3<String>(mRootView) {
                    @Override
                    protected void onSuccess(String data) {
                        super.onSuccess(data);
                        mRootView.deleteFriendOk(index,userInfoBean);
                    }
                });
    }

    private ChatUserInfoBean getChatUser(UserInfoBean userInfoBean) {
        ChatUserInfoBean chatUserInfoBean = new ChatUserInfoBean();
        chatUserInfoBean.setUser_id(userInfoBean.getUser_id());
        chatUserInfoBean.setAvatar(userInfoBean.getAvatar());
        chatUserInfoBean.setName(userInfoBean.getName());
        chatUserInfoBean.setSex(userInfoBean.getSex());
        if (userInfoBean.getVerified() != null) {
            ChatVerifiedBean verifiedBean = new ChatVerifiedBean();
            verifiedBean.setDescription(userInfoBean.getVerified().getDescription());
            verifiedBean.setIcon(userInfoBean.getVerified().getIcon());
            verifiedBean.setStatus(userInfoBean.getVerified().getStatus());
            verifiedBean.setType(userInfoBean.getVerified().getType());
            chatUserInfoBean.setVerified(verifiedBean);
        }
        return chatUserInfoBean;
    }
}
