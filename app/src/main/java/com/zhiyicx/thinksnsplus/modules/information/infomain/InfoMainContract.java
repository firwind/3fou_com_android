package com.zhiyicx.thinksnsplus.modules.information.infomain;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserCertificationInfo;

import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/03/14
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface InfoMainContract {

    /**
     * 外层类型列表
     */
    interface InfoContainerView extends IBaseView<InfoContainerPresenter> {
        void setInfoType(InfoTypeBean infoType);
        void setUserCertificationInfo(UserCertificationInfo userCertificationInfo);
    }

    interface InfoContainerPresenter extends IBaseTouristPresenter {
        void getInfoType();
        void checkCertification();
        boolean isNeedPayTip();
        void savePayTip(boolean isNeed);
    }

    /**
     * 内层内容列表
     */
    interface InfoListView extends ITSListView<BaseListBean,InfoListPresenter> {
        String getInfoType();
        int isRecommend();
        boolean isVideoInfo();
    }

    interface InfoListPresenter extends ITSListPresenter<BaseListBean> {
        void getInfoList(String cate_id, long max_id,
                         long limit, long page);
        List<RealAdvertListBean> getBannerAdvert();
        List<RealAdvertListBean> getListAdvert();
        void handleLike(InfoListDataBean dataBean);
    }
    /**
     * 内层内容列表
     */
    interface FlashListView extends ITSListView<BaseListBean,FlashListPresenter> {
        String getInfoType();
        int isRecommend();
        void commitResult(InfoListDataBean bean);
    }

    interface FlashListPresenter extends ITSListPresenter<BaseListBean> {

        void commitBull(InfoListDataBean bean);

        void commitBearNews(InfoListDataBean bean);
    }

    //小视频
    interface SmallVideoListPresenter extends ITSListPresenter<DynamicDetailBeanV2>{

    }
    //小视频
    interface SmallVideoListView extends ITSListView<DynamicDetailBeanV2,SmallVideoListPresenter>{

    }

}
