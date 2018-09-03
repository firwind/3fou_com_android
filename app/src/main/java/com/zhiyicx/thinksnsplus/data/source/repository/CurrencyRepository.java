package com.zhiyicx.thinksnsplus.data.source.repository;
/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/7/19
 * 描  述：
 * 版  权: 九曲互动
 */

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.AccountBookListBean;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyAddress;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyBalanceBean;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.ExchangeCurrencyRate;
import com.zhiyicx.thinksnsplus.data.beans.TeamBean;
import com.zhiyicx.thinksnsplus.data.beans.WithdrawCurrencyBean;
import com.zhiyicx.thinksnsplus.data.source.remote.CurrencyClient;
import com.zhiyicx.thinksnsplus.data.source.remote.EasemobClient;
import com.zhiyicx.thinksnsplus.data.source.remote.FollowFansClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IBaseChannelRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.i.ICurrencyRepository;
import com.zhiyicx.thinksnsplus.utils.JsonUtils;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class CurrencyRepository implements ICurrencyRepository {

    CurrencyClient mCurrencyClient;

    @Inject
    public CurrencyRepository(ServiceManager manager) {
        mCurrencyClient = manager.getmCurrencyClient();
    }

    @Override
    public Observable<List<CurrencyTypeBean>> getCurrencyType() {
        return mCurrencyClient.getTeamCurrencyList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    @Override
    public Observable<List<TeamBean.TeamListBean>> getTeamList(String type, int grade) {
        return mCurrencyClient.getTeamList(type, grade)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<TeamBean.TeamListBean>> getEarningList(int id,String currency) {
        return mCurrencyClient.getDetailLog(id,currency)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<CurrencyBalanceBean>> getMyCurrencyList() {
        return mCurrencyClient.getMyCurrencyList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<CurrencyAddress>> getCurrencyAddressList(String currency) {
        return mCurrencyClient.getCurrencyAddressList(currency)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> addCurrencyAddress(String currency, String address, String mark) {
        return mCurrencyClient.addCurrencyAddress(currency,address,mark)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> editCurrencyAddress(String address_id, String address, String mark) {
        return mCurrencyClient.editCurrencyAddress(address_id,address,mark)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> deleteCurrencyAddress(String address_id) {
        return mCurrencyClient.deleteCurrencyAddress(address_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<AccountBookListBean>> getAccountBookList(Integer page, Integer target_type,String currency) {
        return mCurrencyClient.getAccountBookList(page, TSListFragment.DEFAULT_PAGE_SIZE,target_type,currency)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJson<String>> rechargeCurrencyAddress(String currency) {
        return mCurrencyClient.rechargeCurrency(currency)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> withdrawCurrency(String currency, String address, String mark,
                                               boolean isSave, String money, String remark,String pay_password) {
        return mCurrencyClient.withdrawCurrency(currency,address,mark,isSave?"1":"0",money,remark,pay_password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<WithdrawCurrencyBean> getWithdrawRate(String currency) {
        return mCurrencyClient.getWithdrawRate(currency)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ExchangeCurrencyRate> getExchangeRate(String currency, String currency2) {
        return mCurrencyClient.getExchangeRate(currency,currency2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> exchangeCurrency(String currency, String currency2, String num,String phone, String verifyCode, String password) {
        return mCurrencyClient.exchangeCurrency(currency,currency2,num,phone,verifyCode,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
