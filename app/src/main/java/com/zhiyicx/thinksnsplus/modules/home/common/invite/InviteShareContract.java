package com.zhiyicx.thinksnsplus.modules.home.common.invite;

import android.view.View;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.InviteAndQrcode;

/**
 * <pre>
 *     @author : huwenyong
 *     @date : 2018/6/24 9:33
 *     desc :
 *     version : 1.0
 * <pre>
 */

public interface InviteShareContract {

    interface View extends IBaseView<Presenter>{
        void setInviteAndQrCode(InviteAndQrcode inviteAndQrCode);
    }

    interface Presenter extends IBasePresenter{
        String getUserAvatar();
        void getInviteCode();
        void inviteShare(android.view.View v,SHARE_MEDIA shareMedia);
    }

}
