package com.zhiyicx.thinksnsplus.modules.dynamic.send;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupSendDynamicDataBean;
import com.zhiyicx.thinksnsplus.data.beans.SendDynamicDataBean;
import com.zhiyicx.thinksnsplus.data.beans.SendDynamicDataBeanV2;
import com.zhiyicx.thinksnsplus.modules.dynamic.IDynamicReppsitory;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/20
 * @contact email:450127106@qq.com
 */

public interface SendDynamicContract {
    //对于经常使用的关于UI的方法可以定义到BaseView中,如显示隐藏进度条,和显示文字消息
    interface View extends IBaseView<SendDynamicContract.Presenter> {
        void sendDynamicComplete();

        /**
         * 获取动态上一个页面的数据，用来判断发送动态的某些逻辑
         *
         * @return
         */
        SendDynamicDataBean getDynamicSendData();

        /**
         * 是否付费认证
         *
         * @return
         */
        boolean hasTollVerify();

        /**
         * 打包动态图片数据信息v2
         *
         * @param sendDynamicDataBeanV2
         */
        void packageDynamicStorageDataV2(SendDynamicDataBeanV2 sendDynamicDataBeanV2);

        /**
         * 打包动态图片数据信息
         *
         * @param sendDynamicDataBeanV2
         */
        void packageGroupDynamicStorageData(GroupSendDynamicDataBean sendDynamicDataBeanV2);

        /**
         * @return 付费金额
         */
        double getTollMoney();

        /**
         * @return true 文字金额长度过了收费
         */
        boolean wordsNumLimit();

        /**
         * @return true  需要处理适配
         */
        boolean needCompressVideo();

        /**
         * 需要拿首帧做封面
         * @return
         */
        boolean needGetCoverFromVideo();

        /**
         * 提示弹框
         *
         * @param des
         */
        void initInstructionsPop(String des);
    }

    interface Presenter extends IBaseTouristPresenter {
        void sendGroupDynamic(GroupDynamicListBean dynamicBean);

        void sendDynamicV2(DynamicDetailBeanV2 dynamicBean);
    }
}
