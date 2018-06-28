package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.maintain.upgradegroup;
/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/6/28
 * 描  述：
 * 版  权: 九曲互动
 */

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.UpgradeTypeBean;

import java.util.List;

public interface UpgradeGroupContract {
    interface View extends IBaseView<Presenter> {
        void getUpgradeTypes(List<UpgradeTypeBean> data);
    }

    interface Presenter extends IBasePresenter {
        void getUpgradeType();
        void upgradegroup(String groupId,int type);
    }
}
