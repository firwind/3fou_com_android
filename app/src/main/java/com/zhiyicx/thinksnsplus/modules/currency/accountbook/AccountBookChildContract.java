package com.zhiyicx.thinksnsplus.modules.currency.accountbook;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.AccountBookListBean;

/**
 * author: huwenyong
 * date: 2018/7/23 9:30
 * description:
 * version:
 */

public interface AccountBookChildContract {

    interface View extends ITSListView<AccountBookListBean,Presenter>{

    }

    interface Presenter extends ITSListPresenter<AccountBookListBean>{

    }

}
