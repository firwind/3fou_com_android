package com.zhiyicx.thinksnsplus.modules.home.mine.team.team;
/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/7/18
 * 描  述：
 * 版  权: 九曲互动
 */

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.TeamBean;

import java.util.List;

public class MyTeamListContract {
    interface View extends ITSListView<TeamBean.TeamListBean, Presenter> {

        void getTotal(String total,String unit);
        String getCurrencyType();
        int getLevel();
    }

    interface Presenter extends ITSListPresenter<TeamBean.TeamListBean> {

    }
}
