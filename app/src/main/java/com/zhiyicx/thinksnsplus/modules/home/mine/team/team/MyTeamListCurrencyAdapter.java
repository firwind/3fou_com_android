package com.zhiyicx.thinksnsplus.modules.home.mine.team.team;
/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/7/19
 * 描  述：
 * 版  权: 九曲互动
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.BaseAdapter;
import com.zhiyicx.thinksnsplus.base.ViewHolder;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyTypeBean;

import java.util.List;

public class MyTeamListCurrencyAdapter extends BaseAdapter<CurrencyTypeBean> {
    public MyTeamListCurrencyAdapter(Context con, List<CurrencyTypeBean> data, int layouid) {
        super(con, data, layouid);
    }

    @Override
    public void convert(ViewHolder viewHolder, CurrencyTypeBean bean, int position, View convertView) {
        viewHolder.setText(R.id.tv_currency_name, bean.getCurrencyName());
    }
}
