package com.zhiyicx.thinksnsplus.modules.wallet.bill;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.source.local.RechargeSuccessBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BillRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Author Jliuer
 * @Date 2017/06/05/10:06
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class BillPresenter extends AppBasePresenter<BillContract.View> implements BillContract.Presenter {

    @Inject
    RechargeSuccessBeanGreenDaoImpl mRechargeSuccessBeanGreenDao;
    @Inject
    BillRepository mBillRepository;

    @Inject
    public BillPresenter(BillContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore) {
        Subscription subscribe = mBillRepository.getBillList(maxId.intValue(), mRootView.getBillType())
                .subscribe(new BaseSubscribeForV2<List<RechargeSuccessBean>>() {
                    @Override
                    protected void onSuccess(List<RechargeSuccessBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.onResponseError(throwable, isLoadMore);
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
///        List<RechargeSuccessBean> data = mRechargeSuccessBeanGreenDao.getMultiDataFromCache();
//        Collections.sort(data, new TimeStringSortClass());
        mRootView.onCacheResponseSuccess(new ArrayList<>(), isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<RechargeSuccessBean> data, boolean isLoadMore) {
        mRechargeSuccessBeanGreenDao.saveMultiData(data);
        return true;
    }
}
