package com.zhiyicx.thinksnsplus.modules.information.flashdetails;
/*
 * 文件名：
 * 创建者：zl
 * 时  间：2018/8/9 0009
 * 描  述：
 * 版  权：九曲互动
 * 
 */

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.InviteAndQrcode;

public class FlashDetailsContract {

    interface View extends IBaseView<Presenter> {
        void setInviteAndQrCode(InviteAndQrcode inviteAndQrCode);
    }

    interface Presenter extends IBasePresenter {

        void inviteShare(android.view.View v, SHARE_MEDIA shareMedia);
        void getInviteCode();
    }
}
