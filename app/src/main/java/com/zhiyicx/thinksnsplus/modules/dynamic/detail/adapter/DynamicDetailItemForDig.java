package com.zhiyicx.thinksnsplus.modules.dynamic.detail.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicToolBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.dig_list.DigListActivity;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.dig_list.DigListFragment;
import com.zhiyicx.thinksnsplus.widget.DynamicHorizontalStackIconView;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/28
 * @contact email:450127106@qq.com
 */

public class DynamicDetailItemForDig implements ItemViewDelegate<DynamicBean> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_detail_dig;
    }

    @Override
    public boolean isForViewType(DynamicBean item, int position) {
        return position == 1;
    }

    @Override
    public void convert(final ViewHolder holder, DynamicBean dynamicBean, DynamicBean lastT, int position,int itemCounts) {

        DynamicHorizontalStackIconView dynamicHorizontalStackIconView = holder.getView(R.id.detail_dig_view);
        final DynamicDetailBean dynamicDetailBean = dynamicBean.getFeed();
        DynamicToolBean dynamicToolBean = dynamicBean.getTool();
        if (dynamicToolBean == null) {
            return;
        }
        dynamicHorizontalStackIconView.setDigCount(dynamicToolBean.getFeed_digg_count());
        dynamicHorizontalStackIconView.setPublishTime(dynamicDetailBean.getCreated_at());
        dynamicHorizontalStackIconView.setViewerCount(dynamicToolBean.getFeed_view_count());
        // 设置点赞头像
      /*  List<UserInfoBean> userInfoList = dynamicBean.getDigUserInfoList();
        List<ImageBean> imageBeanList = null;
        if (userInfoList != null && !imageBeanList.isEmpty()) {
            imageBeanList = new ArrayList<>();
            for (UserInfoBean userInfoBean : userInfoList) {
                ImageBean imageBean = new ImageBean();
                imageBean.setStorage_id(userInfoBean.getAvatar());
                imageBeanList.add(imageBean);
            }
        }
        dynamicHorizontalStackIconView.setDigUserHeadIcon(imageBeanList);*/

        // 设置跳转到点赞列表
        dynamicHorizontalStackIconView.setDigContainerClickListener(new DynamicHorizontalStackIconView.DigContainerClickListener() {
            @Override
            public void digContainerClick(View digContainer) {
                Context context = holder.getConvertView().getContext();
                Bundle bundle = new Bundle();
                bundle.putLong(DigListFragment.DIG_LIST_DATA, dynamicDetailBean.getFeed_id());
                Intent intent = new Intent(context, DigListActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }


}
