package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.jurisdiction;
/*
 * 文件名：
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/22 18:02
 * 修改者：
 * 修改备注：
 * 修改时间：
 * 版  权：互动科技
 */

import android.text.TextUtils;

import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.ChatInfoRepository;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

public class JurisdictionPresenter extends AppBasePresenter<JurisdictionContract.View> implements JurisdictionContract.Presenter {
    @Inject
    ChatInfoRepository mRepository;

    @Inject
    public JurisdictionPresenter(JurisdictionContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        String keyWord = mRootView.getSearchKeyWord();

        mRepository.getGroupMemberInfo(mRootView.getGroupData().getId(),keyWord,maxId)
                .map(userInfoBeans -> {

                    //移除管理员和自己
                    int position = -1;
                    for (int i = 0; i < userInfoBeans.size(); i++) {
                        userInfoBeans.get(i).setIsSelected(0);
                        if (userInfoBeans.get(i).getUser_id().equals(AppApplication.getMyUserIdWithdefault())) {
                            position = i;
                        }else if (userInfoBeans.get(i).getAdmin_type() != 0){
                            userInfoBeans.get(i).setIsSelected(-1);
                        }
                    }
                    if (position != -1) {
                        userInfoBeans.remove(position);
                    }

                    // 有key表示是搜素，没有就是全部 直接获取就好了
                    if (TextUtils.isEmpty(keyWord)) {
                        return userInfoBeans;
                    } else {
                        List<UserInfoBean> searchResult = new ArrayList<>();
                        for (UserInfoBean userInfoBean : userInfoBeans) {
                            if (!TextUtils.isEmpty(userInfoBean.getName()) && userInfoBean.getName().contains(keyWord)) {
                                searchResult.add(userInfoBean);
                            }
                        }
                        return searchResult;
                    }

                })
                .subscribe(new BaseSubscribeForV2<List<UserInfoBean>>() {
                    @Override
                    protected void onSuccess(List<UserInfoBean> data) {
                        mRootView.onNetResponseSuccess(data,isLoadMore);
                    }
                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(throwable.getMessage());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.showSnackErrorMessage(e.getMessage());
                    }
                });
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }


    @Override
    public boolean insertOrUpdateData(@NotNull List<UserInfoBean> data, boolean isLoadMore) {
        return true;
    }

    @Override
    public void dealGroupMember(List<UserInfoBean> list) {

    }

    @Override
    public void openBannedPost(String im_group_id, String user_id, String times, String members) {
        Subscription subscription = mRepository.openBannedPost(im_group_id, user_id, times, members)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.circle_dealing)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<String>() {

                    @Override
                    protected void onSuccess(String data) {
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.chat_info_setting_banned_post_success));
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(throwable.getMessage());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.showSnackErrorMessage(e.getMessage());
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void addGroupRole(List<UserInfoBean> mSelectedList) {
        StringBuilder id = new StringBuilder();
        String type = null;
        for (UserInfoBean userInfoBean : mSelectedList) {
            id.append(userInfoBean.getUser_id()).append(",");
        }
        id = new StringBuilder(id.substring(0, id.length() - 1));
        if (mRootView.setAddRuloName().equals(mContext.getString(R.string.administrator))) {//添加管理员
            type = "1";
        } else if (mRootView.setAddRuloName().equals(mContext.getString(R.string.lecturer))) {//添加讲师
            type = "3";
        } else if (mRootView.setAddRuloName().equals(mContext.getString(R.string.compere))) {
            type = "2";
        }
        Subscription subscription = mRepository.addGroupRole(mRootView.setGroupId(), id.toString(), type)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.circle_dealing)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<String>() {

                    @Override
                    protected void onSuccess(String data) {
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.chat_edit_group_add_success));
                        mRootView.addGroupRuloSuccess();
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(throwable.getMessage());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.showSnackErrorMessage(e.getMessage());
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void removeBannedPost(String im_group_id, String user_id, String members) {
        Subscription subscription = mRepository.removeBannedPost(im_group_id, user_id, members)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.circle_dealing)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<String>() {

                    @Override
                    protected void onSuccess(String data) {
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.chat_info_relieve_banned_post_success));
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(throwable.getMessage());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.showSnackErrorMessage(e.getMessage());
                    }
                });
        addSubscrebe(subscription);
    }
}
