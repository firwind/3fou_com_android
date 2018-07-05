package com.zhiyicx.thinksnsplus.utils.kline;

import com.github.tifezh.kchartlib.chart.BaseKChartAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * author: huwenyong
 * date: 2018/7/5 16:08
 * description:
 * version:
 */

public class KLineAdapter extends BaseKChartAdapter{

    private List<KLineEntity> mData = new ArrayList<>();

    public void setData(List<KLineEntity> data){
        this.mData = data;
        notifyDataSetChanged();
    }

    public void addData(List<KLineEntity> data){
        this.mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public Date getDate(int position) {
        return new Date(mData.get(position).dateTime);
    }
}
