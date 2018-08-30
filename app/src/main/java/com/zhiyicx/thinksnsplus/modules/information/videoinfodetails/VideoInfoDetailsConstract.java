package com.zhiyicx.thinksnsplus.modules.information.videoinfodetails;

import android.graphics.Bitmap;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsCountBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;

import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/03/15
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface VideoInfoDetailsConstract {

    interface View extends ITSListView<InfoCommentListBean, Presenter> {
        Long getNewsId();

        InfoListDataBean getCurrentInfo();

        void loadAllError();

        void updateInfoHeader(InfoListDataBean infoDetailBean);

        void setDigg(boolean isDigged,int count);


    }

    interface Presenter extends ITSListPresenter<InfoCommentListBean> {

        void sendComment(int reply_id, String content);

        void shareInfo(Bitmap bitmap);

        void deleteComment(InfoCommentListBean data);

        void handleLike(InfoListDataBean data);

        void shareVideo(InfoListDataBean infoListDataBean);

        void shareVideo(InfoListDataBean infoListDataBean,SHARE_MEDIA type);
    }

}
