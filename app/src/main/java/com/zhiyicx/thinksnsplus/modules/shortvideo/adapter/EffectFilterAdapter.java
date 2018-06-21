package com.zhiyicx.thinksnsplus.modules.shortvideo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.tym.shortvideo.filter.helper.MagicFilterType;
import com.tym.shortvideo.filter.helper.type.GLFilterType;
import com.tym.shortvideo.utils.BitmapUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Jliuer
 * @Date 2018/03/28/11:13
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class EffectFilterAdapter extends CommonAdapter<MagicFilterType> {

    private List<WeakReference<Bitmap>> mWeakBitmaps = new ArrayList<>();

    public EffectFilterAdapter(Context context, int layoutId, List<MagicFilterType> datas) {
        super(context, layoutId, datas);
        for (int i = 0; i < datas.size(); i++) {
            mWeakBitmaps.add(i, null);
        }
    }

    @Override
    protected void convert(ViewHolder holder, MagicFilterType glFilterType, int position) {
        if (mWeakBitmaps.size() <= position
                || mWeakBitmaps.get(position) == null
                || mWeakBitmaps.get(position).get() == null
                || mWeakBitmaps.get(position).get().isRecycled()) {
            String path = "thumbs/" + glFilterType.name().toLowerCase() + ".jpg";
            Bitmap bitmap = BitmapUtils.getImageFromAssetsFile(mContext, path);
            if (bitmap != null) {
                mWeakBitmaps.add(position, new WeakReference<>(bitmap));
                holder.getImageViwe(R.id.effect_image).setImageBitmap(bitmap);
            }
        } else {
            holder.getImageViwe(R.id.effect_image).setImageBitmap(mWeakBitmaps.get(position).get());
        }

        holder.setText(R.id.effect_name,glFilterType.getValue());
    }
}
