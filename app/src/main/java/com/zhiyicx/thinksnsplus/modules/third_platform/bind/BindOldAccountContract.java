package com.zhiyicx.thinksnsplus.modules.third_platform.bind;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.ThridInfoBean;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/31
 * @contact email:648129313@qq.com
 */

public interface BindOldAccountContract {

    interface View extends IBaseView<Presenter> {
        void showErrorTips(String message);

        void setLoginState(boolean b);

        void setLogining();

        void setVerifyCodeBtEnabled(boolean isEnable);

        void setVerifyCodeLoading(boolean isEnable);

        void setVerifyCodeBtText(String text);

        void goHome();
    }

    interface Presenter extends IBaseTouristPresenter {
        void checkName(ThridInfoBean thridInfoBean,String name);
        void bindAccount(ThridInfoBean thridInfoBean, String string, String string1,String phone,String verifiable_code,String user_code);
        void getVertifyCode(String trim);
    }

}
