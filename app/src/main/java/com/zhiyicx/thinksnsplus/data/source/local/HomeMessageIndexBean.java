package com.zhiyicx.thinksnsplus.data.source.local;

import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.MarketCurrencyBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;

import java.util.List;

/**
 * author: huwenyong
 * date: 2018/8/9 14:38
 * description:
 * version:
 */

public class HomeMessageIndexBean {

    private String year_rate;
    private List<MarketCurrencyBean> jinsecaijing;
    private List<InfoListDataBean> news;

    private List<RealAdvertListBean> banners;

    public String getYear_rate() {
        return year_rate;
    }

    public void setYear_rate(String year_rate) {
        this.year_rate = year_rate;
    }

    public List<MarketCurrencyBean> getJinsecaijing() {
        return jinsecaijing;
    }

    public void setJinsecaijing(List<MarketCurrencyBean> jinsecaijing) {
        this.jinsecaijing = jinsecaijing;
    }

    public List<InfoListDataBean> getNews() {
        return news;
    }

    public void setNews(List<InfoListDataBean> news) {
        this.news = news;
    }

    public List<RealAdvertListBean> getBanners() {
        return banners;
    }

    public void setBanners(List<RealAdvertListBean> banners) {
        this.banners = banners;
    }
}
