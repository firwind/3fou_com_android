package com.zhiyicx.thinksnsplus.modules.personal_center.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/10
 * @contact email:450127106@qq.com
 */

public class PersonalCenterDynamicListItemForNineImage extends PersonalCenterDynamicListBaseItem {

    private static final int IMAGE_COUNTS = 9;// 动态列表图片数量
    private static final int CURREN_CLOUMS = 3;// 当前列数

    public PersonalCenterDynamicListItemForNineImage(Context context) {
        super(context);
    }

    @Override
    public boolean isForViewType(DynamicDetailBeanV2 item, int position) {
        return item.getId() != null && (item.getImages() != null && item.getImages().size
                () >= getImageCounts());
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_personal_center_dynamic_list_nine_image;
    }

    @Override
    public int getImageCounts() {
        return IMAGE_COUNTS;
    }

    @Override
    public  int getCurrenCloums() {
        return CURREN_CLOUMS;
    }


    @Override
    public void convert(ViewHolder holder, final DynamicDetailBeanV2 dynamicBean, DynamicDetailBeanV2 lastT, int position, int itemCounts) {
        super.convert(holder, dynamicBean, lastT, position,itemCounts);
        // 数字 0 代表 image 当前的位置， 1 代表他相对与 CURREN_CLOUMS 的份数
        initImageView(holder, holder.getView(R.id.siv_0), dynamicBean, 0,1);
        initImageView(holder, holder.getView(R.id.siv_1), dynamicBean, 1,1);
        initImageView(holder, holder.getView(R.id.siv_2), dynamicBean, 2,1);
        initImageView(holder, holder.getView(R.id.siv_3), dynamicBean, 3,1);
        initImageView(holder, holder.getView(R.id.siv_4), dynamicBean, 4,1);
        initImageView(holder, holder.getView(R.id.siv_5), dynamicBean, 5,1);
        initImageView(holder, holder.getView(R.id.siv_6), dynamicBean, 6,1);
        initImageView(holder, holder.getView(R.id.siv_7), dynamicBean, 7,1);
        initImageView(holder, holder.getView(R.id.siv_8), dynamicBean, 8,1);
    }

}
