package com.zhiyicx.thinksnsplus.modules.information.smallvideo;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * author: huwenyong
 * date: 2018/8/27 18:58
 * description:
 * version:
 */
@FragmentScoped
public class SmallVideoPresenter extends AppBasePresenter<SmallVideoContract.View> implements SmallVideoContract.Presenter{
    @Inject
    public SmallVideoPresenter(SmallVideoContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

        mRootView.onCacheResponseSuccess(mRootView.getInitVideoList(),isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<DynamicDetailBeanV2> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public void handleLike(DynamicDetailBeanV2 bean) {

    }
}
