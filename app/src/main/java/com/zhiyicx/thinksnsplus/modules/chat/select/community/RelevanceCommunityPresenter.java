package com.zhiyicx.thinksnsplus.modules.chat.select.community;
/*
 * 文件名：
 * 创建者：zl
 * 时  间：2018/8/21 0021
 * 描  述：
 * 版  权：九曲互动
 * 
 */

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.base.BaseSubscriberV3;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.source.repository.SelectFriendsRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

public class RelevanceCommunityPresenter extends AppBasePresenter<RelevanceCommunityContract.View> implements RelevanceCommunityContract.Presenter{
    @Inject
    SelectFriendsRepository mRepository;

    @Inject
    public RelevanceCommunityPresenter(RelevanceCommunityContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Subscription subscribe = mRepository.communityList(maxId.intValue(),mRootView.getSearchKeyWord())
                .subscribe(new BaseSubscriberV3<List<CircleInfo>>(mRootView){
                    @Override
                    protected void onSuccess(List<CircleInfo> data) {
                        super.onSuccess(data);
                        mRootView.onNetResponseSuccess(data,isLoadMore);
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<CircleInfo> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public void relevanceCommunity(long communityId) {
        Subscription subscription = mRepository.relevanceCommunity(mRootView.getGroupId(),communityId)
                .subscribe(new BaseSubscriberV3<String>(mRootView){
                    @Override
                    protected void onSuccess(String data) {
                        super.onSuccess(data);
                        mRootView.showSnackSuccessMessage("关联成功");
                    }

                });
        addSubscrebe(subscription);
    }
}
