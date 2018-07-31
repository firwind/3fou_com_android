package com.zhiyicx.thinksnsplus.modules.settings.password.pay_password;

import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

/**
 * author: huwenyong
 * date: 2018/7/30 18:12
 * description:
 * version:
 */

public interface PayPasswordContract {

    interface View extends IBaseView<Presenter>{

        void setPayPasswordIsSetted(boolean isSetted);

    }

    interface Presenter extends IBasePresenter{

        void getPayPasswordIsSetted();

        UserInfoBean getCurrentUser();

    }

}
