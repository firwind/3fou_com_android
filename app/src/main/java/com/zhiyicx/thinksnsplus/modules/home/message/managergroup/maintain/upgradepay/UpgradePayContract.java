package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.maintain.upgradepay;
/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/7/9
 * 描  述：
 * 版  权: 九曲互动
 */

import android.app.Activity;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.tspay.TSPayClient;

public class UpgradePayContract {
    public interface View extends IBaseView<Presenter>{
        double getMoney();
        Activity getActivity();
    }
    public interface Presenter extends IBasePresenter{
        void getPayStr(String groupId,String upGradeType,String channel,double amount ,int fewmouths);
    }

}
