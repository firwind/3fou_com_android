package com.zhiyicx.thinksnsplus.modules.home.message.homepage;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.HomeMessageIndexBean;

/**
 * author: huwenyong
 * date: 2018/8/9 9:36
 * description:
 * version:
 */

public interface MessageHomePageContract {

    interface View extends ITSListView<InfoListDataBean,Presenter>{
        void setHeaderData(HomeMessageIndexBean data);
    }

    interface Presenter extends ITSListPresenter<InfoListDataBean>{

    }

}
