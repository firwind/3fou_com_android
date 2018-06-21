package com.zhiyicx.thinksnsplus.modules.wallet.sticktop;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.StickTopAverageBean;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/05/22/16:10
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface StickTopContract {

    interface View extends IBaseView<Presenter> {
        boolean insufficientBalance();

        void gotoRecharge();

        void initStickTopInstructionsPop(String dec);

        int getTopDyas();

        double getInputMoney();

        void topSuccess();

        String getType();

        void updateBalance(long balance);

        boolean useInputMoney();

        void onFailure(String message, int code);
    }

    interface Presenter extends IBaseTouristPresenter {
        /**
         * 平均置顶金额
         *
         * @return
         */
        StickTopAverageBean getStickTopAverageBean();

        /**
         * 当前积分余额
         *
         * @return
         */
        long getBalance();

        /**
         * 置顶内容
         *
         * @param parent_id
         */
        void stickTop(long parent_id);

        /**
         * 置顶内容中的评论
         *
         * @param parent_id
         * @param child_id
         */
        void stickTop(long parent_id, long child_id);
    }

}
