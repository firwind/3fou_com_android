package com.zhiyicx.thinksnsplus.modules.shortvideo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.tym.shortvideo.media.VideoInfo;
import com.tym.shortvideo.utils.DateUtil;
import com.zhiyicx.thinksnsplus.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * @Author Jliuer
 * @Date 2018/03/28/14:08
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class VideoGridViewAdapter extends CommonAdapter<VideoInfo> {

    BitmapFactory.Options options = new BitmapFactory.Options();


    public VideoGridViewAdapter(Context context, int layoutId, List<VideoInfo> datas) {
        super(context, layoutId, datas);
        options.inPreferredConfig = Bitmap.Config.RGB_565;
    }

    /**
     * @param holder
     * @param video
     * @param position
     * @link https://blog.csdn.net/u012947056/article/details/78508986
     * DiskCacheStrategy.RESULT && DiskCacheStrategy.SOURCE
     */
    @Override
    protected void convert(ViewHolder holder, VideoInfo video, int position) {
        if (TextUtils.isEmpty(video.getPath())) {
            holder.setVisible(R.id.tv_duration, View.GONE);
            holder.setImageResource(R.id.iv_cover, R.mipmap.pic_shootvideo);
        } else {
            holder.setVisible(R.id.tv_duration, View.VISIBLE);
            holder.setText(R.id.tv_duration, DateUtil.convertSecondsToTime(video.getDuration() / 1000));

            Glide.with(mContext)
                    .load(video.getPath())
                    .signature(new StringSignature(video.getPath() + video.getCreateTime()))
                    .placeholder(R.drawable.shape_default_image)
                    .error(R.drawable.shape_default_image)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(holder.getImageViwe(R.id.iv_cover));
        }


    }
}
