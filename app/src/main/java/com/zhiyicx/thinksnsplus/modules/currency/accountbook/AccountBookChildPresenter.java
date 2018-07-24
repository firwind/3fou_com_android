package com.zhiyicx.thinksnsplus.modules.currency.accountbook;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.AccountBookListBean;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyAddress;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * author: huwenyong
 * date: 2018/7/23 9:32
 * description:
 * version:
 */
@FragmentScoped
public class AccountBookChildPresenter extends AppBasePresenter<AccountBookChildContract.View>
        implements AccountBookChildContract.Presenter{

    @Inject
    public AccountBookChildPresenter(AccountBookChildContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

        mRootView.onNetResponseSuccess(falseData(),isLoadMore);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<AccountBookListBean> data, boolean isLoadMore) {
        return false;
    }


    private List<AccountBookListBean>  falseData(){

        List<AccountBookListBean> list = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            AccountBookListBean bean = new AccountBookListBean();
            //审核中 ，成功
            bean.setState(i%2);
            //充币，提币，兑币
            bean.setOp_type(i%3);
            //充币/提币 数量
            bean.setCurrency_num(i*1000);
            //手续费
            bean.setCurrency_service_num(i);
            //时间
            bean.setTime(System.currentTimeMillis()-i*1000*60);
            //钱包地址
            CurrencyAddress address = new CurrencyAddress("11","tag"+i,(i*1000)+"dssssdddddssssssssseeeeeee");
            bean.setAddress(address);

            list.add(bean);
        }

        AccountBookListBean bean = new AccountBookListBean();
        //审核中 ，成功
        bean.setState(1);
        //充币，提币，兑币
        bean.setOp_type(2);
        //充币/提币 数量
        bean.setCurrency_num(1000);
        //手续费
        bean.setCurrency_service_num(200);
        //时间
        bean.setTime(System.currentTimeMillis());
        //钱包地址
        CurrencyAddress address = new CurrencyAddress("22","tag","dssssdddddssssssssseeeeeee");
        bean.setAddress(address);

        list.add(bean);

        return list;
    }

}
