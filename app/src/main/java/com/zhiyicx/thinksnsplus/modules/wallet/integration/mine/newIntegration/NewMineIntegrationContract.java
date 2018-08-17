package com.zhiyicx.thinksnsplus.modules.wallet.integration.mine.newIntegration;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.IntegrationRuleBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.integration.IntegrationBean;
import com.zhiyicx.thinksnsplus.data.beans.integration.IntegrationConfigBean;

import java.util.List;

/**
 * author: huwenyong
 * date: 2018/8/3 18:17
 * description:
 * version:
 */

public interface NewMineIntegrationContract {

    interface View extends ITSListView<IntegrationRuleBean,Presenter>{
        /**
         * update balance
         *
         * @param balance current user's balance
         */
        void updateBalance(IntegrationBean balance);

        /**
         * handle request loading
         *
         * @param isShow true ,show loading
         */
        void handleLoading(boolean isShow);

        /**
         * wallet callback
         *
         * @param configBean integration config info
         * @param tag              action tag, 1 recharge 2 withdraw
         */
        void integrationConfigCallBack(IntegrationConfigBean configBean, int tag);

        void conversionSuccess(long candiesNum);
        void conversionFailure();
    }

    interface Presenter extends ITSListPresenter<IntegrationRuleBean>{
        String getTipPopRule();

        /**
         *
         * @return advert  for  integration
         */
        List<RealAdvertListBean> getIntegrationAdvert();

        /**
         * update user info
         */
        void updateUserInfo();

        /**
         * @return true when first looking wallet page
         */
        boolean checkIsNeedTipPop();

        /**
         * check wallet config info, if walletconfig has cach used it or get it from server
         *
         * @param tag action tag
         */
        void checkIntegrationConfig(int tag,boolean isNeedTip);

        /**
         * 兑换FCC
         * @param candiesNum  糖果数量
         */
        void  conversionFcc(String candiesNum);
    }

}
