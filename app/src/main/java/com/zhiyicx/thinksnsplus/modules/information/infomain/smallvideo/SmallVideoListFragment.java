package com.zhiyicx.thinksnsplus.modules.information.infomain.smallvideo;

import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.FrameLayout;

import com.hyphenate.util.DensityUtil;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.impl.share.ShareModule;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.SmallVideoDataBean;
import com.zhiyicx.thinksnsplus.modules.information.infomain.InfoMainContract;
import com.zhiyicx.thinksnsplus.modules.information.smallvideo.SmallVideoActivity;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.simple.eventbus.Subscriber;

import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author: huwenyong
 * date: 2018/8/27 10:02
 * description:
 * version:
 */
public class SmallVideoListFragment extends TSListFragment<InfoMainContract.SmallVideoListPresenter,
        DynamicDetailBeanV2> implements InfoMainContract.SmallVideoListView {

    @Inject
    SmallVideoListPresenter mSmallVideoListPresenter;

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);

        rootView.setBackgroundColor(Color.WHITE);
        Observable.create(subscriber -> {
            DaggerSmallVideoListComponent.builder()
                    .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                    .smallVideoListPresenterModule(new SmallVideoListPresenterModule(SmallVideoListFragment.this))
                    .shareModule(new ShareModule(mActivity))
                    .build()
                    .inject(SmallVideoListFragment.this);
            subscriber.onCompleted();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new rx.Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        initData();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });
    }

    @Override
    protected void initData() {
        if (mPresenter != null) {
            super.initData();
        }
    }

    @Override
    public void showMessage(String message) {
        showMessageNotSticky(message);
    }

    @Override
    protected boolean setUseSatusbar() {
        return false;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }


    /**
     * 小视频详情中 - 更新数据源
     * @param dataBean
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_UPDATE_SMALL_VIDEO_LIST)
    public void updateSmallVideoList(SmallVideoDataBean dataBean){


        if(null != dataBean.list){
            try {
                mListDatas.clear();
                mListDatas.addAll(dataBean.list);
                refreshData();

                mRvList.smoothScrollToPosition(dataBean.currentPosition);
            }catch (Exception e){
                //
            }
        }

    }



    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        int width = mActivity.getResources().getDimensionPixelSize(R.dimen.dp4);
        return new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                StaggeredGridLayoutManager.LayoutParams params =
                        (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
                /**
                 * 根据params.getSpanIndex()来判断左右边确定分割线
                 * 第一列设置左边距为space，右边距为space/2  （第二列反之）
                 */
                if (params.getSpanIndex() % 2 == 0) {
                    outRect.left = -width;
                    outRect.right = width / 2;
                } else {
                    outRect.right = -width;
                    outRect.left = width / 2;
                }
                outRect.bottom = width;
            }
        };
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        return layoutManager;
    }

    @Override
    protected CommonAdapter getAdapter() {
        int itemWidth = (DeviceUtils.getScreenWidth(mActivity)-mActivity.getResources().getDimensionPixelSize(R.dimen.dp4))/2;
        int minHeight = DensityUtil.dip2px(mActivity,200);
        CommonAdapter<DynamicDetailBeanV2> mAdapter = new CommonAdapter<DynamicDetailBeanV2>(mActivity,
                R.layout.item_small_video,mListDatas) {
            @Override
            protected void convert(ViewHolder holder, DynamicDetailBeanV2 smallVideoListBean, int position) {

                //重设高度
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder.getView(R.id.video_container)
                        .getLayoutParams();
                params.height = (null != smallVideoListBean.getVideo() && 0 != smallVideoListBean.getVideo().getWidth() &&
                        0 != smallVideoListBean.getVideo().getHeight())?
                        smallVideoListBean.getVideo().getHeight()*itemWidth/smallVideoListBean.getVideo().getWidth():
                        minHeight;
                //params.setMargins(isLeft?subWidth:0,0,isLeft?0:subWidth,0);
                holder.getView(R.id.video_container).setLayoutParams(params);

                holder.getTextView(R.id.tv_title).setText(smallVideoListBean.getFeed_content());
                holder.getTextView(R.id.tv_play_count).setText(String.valueOf(smallVideoListBean.getFeed_view_count()) );

                ImageUtils.loadVerticalVideoThumbDefault(holder.getImageViwe(R.id.iv_cover),
                        ImageUtils.getVideoUrl( (null != smallVideoListBean.getVideo()?smallVideoListBean.getVideo().getCover_id():0)));

                //分享
                holder.getView(R.id.iv_share).setOnClickListener(v -> mPresenter.shareVideo(smallVideoListBean));
            }
        };

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                //startActivity(new Intent(mActivity, SmallVideoActivity.class));
                SmallVideoActivity.startSmallVideoActivity(mActivity,mListDatas,position);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        return mAdapter;
    }


}
