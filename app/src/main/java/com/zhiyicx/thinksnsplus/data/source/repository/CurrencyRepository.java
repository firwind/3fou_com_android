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
import com.zhiyicx.thinksnsplus.data.beans.CurrencyTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.TeamBean;
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
    public Observable<List<CurrencyTypeBean>> getCurrencyType(Context context) {
        String currencyJson = JsonUtils.getJson("currencyType", context);

        return Observable.just(currencyJson).map((Func1<String, List<CurrencyTypeBean>>) s -> new Gson().fromJson(s, new TypeToken<List<CurrencyTypeBean>>() {
        }.getType()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    @Override
    public Observable<TeamBean> getTeamList(Context context) {
        String teamBeanJson = JsonUtils.getJson("MyTeam", context);
        TeamBean teamBean = new Gson().fromJson(teamBeanJson, TeamBean.class);
        return Observable.just(teamBean)
                .map(new Func1<TeamBean, TeamBean>() {
                    @Override
                    public TeamBean call(TeamBean s) {
                        return s;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<TeamBean.TeamListBean>> getEarningList(Context context, int id) {
        String earnings = JsonUtils.getJson("Earnings", context);
        return Observable.just(earnings).map((Func1<String, List<TeamBean.TeamListBean>>) s -> new Gson().fromJson(s, new TypeToken<List<TeamBean.TeamListBean>>() {
        }.getType()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> getMyCurrencyList() {
        return mCurrencyClient.getMyCurrencyList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> getCurrencyAddressList(String currency) {
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
}
