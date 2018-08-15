package com.zhiyicx.thinksnsplus.data.beans;

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
    private List<CurrencyRankBean> jinsecaijing;
    private List<InfoListDataBean> news;
    private List<InfoListDataBean> flash;

    private List<RealAdvertListBean> banners;

    public List<InfoListDataBean> getFlash() {
        return flash;
    }

    public void setFlash(List<InfoListDataBean> flash) {
        this.flash = flash;
    }

    public String getYear_rate() {
        return year_rate;
    }

    public void setYear_rate(String year_rate) {
        this.year_rate = year_rate;
    }

    public List<CurrencyRankBean> getJinsecaijing() {
        return jinsecaijing;
    }

    public void setJinsecaijing(List<CurrencyRankBean> jinsecaijing) {
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
