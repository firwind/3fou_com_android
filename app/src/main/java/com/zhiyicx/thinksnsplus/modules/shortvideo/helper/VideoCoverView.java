package com.zhiyicx.thinksnsplus.modules.shortvideo.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.FileDescriptorBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.VideoBitmapDecoder;
import com.bumptech.glide.signature.StringSignature;
import com.tym.shortvideo.utils.DeviceUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Jliuer
 * @Date 2018/04/26/11:49
 * @Email Jliuer@aliyun.com
 * @Description 1s 截图 2 张
 */
public class VideoCoverView extends RelativeLayout {

    private ImageView mCoverView;
    private RecyclerView mCoverListView;
    private ViewDragHelper mDragHelper;
    private CoverAdapter mAdapter;

    private OnScrollDistanceListener mOnScrollDistanceListener;

    private static final float TIME = 2.4f;
    private int mCoverViewWidth;
    private float mCurrentStartTime;

    private int oldLeft;

    public VideoCoverView(@NonNull Context context) {
        this(context, null);
    }

    public VideoCoverView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoCoverView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(ViewGroup root) {
        mDragHelper = ViewDragHelper.create(root, 10000.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == mCoverView;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return mCoverView.getTop();
            }

            /**
             *
             * @param child
             * @param left 将要移动到的位置的坐标
             * @param dx
             * @return
             */
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mCoverListView.getLayoutParams();
                final int leftBound = params.leftMargin;
                final int rightBound = getWidth() - mCoverView.getWidth() - leftBound;
                return Math.min(Math.max(left, leftBound), rightBound);
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return 0;
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return getMeasuredWidth() - child.getMeasuredWidth();
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                Log.d("onViewPositionChanged::", left + "");
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mCoverListView.getLayoutParams();
                int relativePosition = left + changedView.getWidth() - params.leftMargin;
                float time = TIME * relativePosition / mCoverViewWidth;
                int frame = (int) ((mCurrentStartTime + time) * 1000);
                Log.d("onViewPositionChanged::frame::", frame + "");
                if (mOnScrollDistanceListener != null && Math.abs(left - oldLeft) >= 20) {
                    oldLeft = left;
                    mOnScrollDistanceListener.changeTo(frame);
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mCoverView = ((ImageView) findViewById(R.id.iv_cover));
        LinearLayout root = ((LinearLayout) findViewById(R.id.drag));

        mCoverListView = (RecyclerView) findViewById(R.id.rl_cover_list);
        mAdapter = new CoverAdapter();
        mCoverListView.setAdapter(mAdapter);
        mCoverListView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        init(root);

        mCoverListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mOnScrollDistanceListener != null) {
                    // 这里有padding，并没有重 0 开始
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mCoverListView.getLayoutParams();
                    
                    mCoverViewWidth = mCoverListView.getWidth() - 2 * params.leftMargin;
                    int distance = getScollXDistance() + params.leftMargin;
                    mCurrentStartTime = TIME * distance / mCoverViewWidth;
                    Log.d("distance:", distance + "");
                    Log.d("time:", mCurrentStartTime + "");

                    int relativePosition = mCoverView.getRight() - params.leftMargin;
                    float time = TIME * relativePosition / mCoverViewWidth;
                    int frame = (int) ((mCurrentStartTime + time) * 1000);

                    mOnScrollDistanceListener.changeTo((int) (mCurrentStartTime * 1000));
                }

            }
        });
    }

    public void addImages(List<Video> coverList) {
        if (mAdapter != null) {
            mAdapter.addImages(coverList);
        }
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        if (mCoverView != null) {
            mCoverView.setImageBitmap(imageBitmap);
        }
    }

    class CoverAdapter extends RecyclerView.Adapter<Holder> {


        private List<Video> coverList = new ArrayList<>();

        public CoverAdapter() {
        }

        public CoverAdapter(List<Video> coverList) {
            this.coverList = coverList;
        }

        public void addImages(List<Video> coverList) {
            this.coverList.addAll(coverList);
            notifyDataSetChanged();
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_thumb_itme_layout, null);
            return new Holder(convertView);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {

            final int microSecond = (int) coverList.get(position).mecs;
            VideoBitmapDecoder videoBitmapDecoder = new VideoBitmapDecoder(microSecond) {
                @Override
                public Bitmap decode(ParcelFileDescriptor resource, BitmapPool bitmapPool, int outWidth, int outHeight, DecodeFormat decodeFormat) throws IOException {
                    MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                    mediaMetadataRetriever.setDataSource(resource.getFileDescriptor());
                    Bitmap result;
                    if (microSecond >= 0) {
                        result = mediaMetadataRetriever.getFrameAtTime(microSecond, MediaMetadataRetriever.OPTION_CLOSEST);
                    } else {
                        result = mediaMetadataRetriever.getFrameAtTime();
                    }
                    if (result == null) {
                        result = mediaMetadataRetriever.getFrameAtTime(microSecond);
                    }
                    mediaMetadataRetriever.release();
                    resource.close();
                    return result;
                }
            };
            FileDescriptorBitmapDecoder fileDescriptorBitmapDecoder = new FileDescriptorBitmapDecoder(videoBitmapDecoder, holder.mBitmapPool, DecodeFormat.PREFER_ARGB_8888);
            String path = coverList.get(position).path.getPath();
            Glide.with(getContext())
                    .load(path)
                    .asBitmap()
                    .override(DeviceUtils.dipToPX(60), DeviceUtils.dipToPX(60))
                    .signature(new StringSignature(path + microSecond))
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .placeholder(R.drawable.shape_default_image)
                    .error(R.drawable.shape_default_image)
                    .videoDecoder(fileDescriptorBitmapDecoder)
                    .into(holder.mImageView);
        }

        @Override
        public int getItemCount() {
            return coverList.size();
        }
    }

    class Holder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        BitmapPool mBitmapPool;

        public Holder(View itemView) {
            super(itemView);
            mBitmapPool = Glide.get(getContext()).getBitmapPool();
            mImageView = (ImageView) itemView.findViewById(R.id.thumb);
        }
    }

    /**
     * 要求 每个item View的高度一致，不然就判断 类型，自己加宽高
     *
     * @return
     */
    private int getScollXDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) mCoverListView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        if (firstVisiableChildView == null) {
            return 0;
        }
        int itemWidth = firstVisiableChildView.getWidth();
        return (position) * itemWidth - firstVisiableChildView.getLeft();
    }

    public void setOnScrollDistanceListener(OnScrollDistanceListener onScrollDistanceListener) {
        mOnScrollDistanceListener = onScrollDistanceListener;
    }

    public ImageView getCoverView() {
        return mCoverView;
    }

    public interface OnScrollDistanceListener {
        /**
         * 时间都去哪儿了
         *
         * @param millisecond
         */
        void changeTo(long millisecond);
    }

    public static class Video {
        Uri path;
        long mecs;

        public Video(Uri path, long mecs) {
            this.path = path;
            this.mecs = mecs;
        }
    }
}
