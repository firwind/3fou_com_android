package com.zhiyicx.thinksnsplus.modules.home.mine.team.earnings;
/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/7/21
 * 描  述：
 * 版  权: 九曲互动
 */

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.TeamBean;
import com.zhiyicx.thinksnsplus.modules.home.mine.team.MyTeamContract;

public class EarningsDetailContract {
    interface View extends ITSListView<TeamBean.TeamListBean,Presenter>{
        int getEarningId();
    }
    interface Presenter extends ITSListPresenter<TeamBean.TeamListBean> {

    }
}
