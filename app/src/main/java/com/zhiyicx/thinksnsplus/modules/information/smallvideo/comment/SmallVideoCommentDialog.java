package com.zhiyicx.thinksnsplus.modules.information.smallvideo.comment;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.EmptyView;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.i.OnCommentTextClickListener;
import com.zhiyicx.thinksnsplus.i.OnUserInfoClickListener;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.adapter.DynamicDetailCommentItem;
import com.zhiyicx.thinksnsplus.widget.DynamicCommentEmptyItem;
import com.zhiyicx.thinksnsplus.widget.dialog.EditTextDialog;
import com.zhiyicx.thinksnsplus.widget.dialog.HBaseBottomSheetDialog;
import com.zhiyicx.thinksnsplus.widget.dialog.HBaseDialog;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.iwf.photopicker.utils.AndroidLifecycleUtils;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * author: huwenyong
 * date: 2018/8/30 10:00
 * description:
 * version:
 */

public class SmallVideoCommentDialog extends HBaseDialog implements OnLoadmoreListener, OnUserInfoClickListener,
        OnCommentTextClickListener, DynamicDetailCommentItem.OnCommentResendListener,
        MultiItemTypeAdapter.OnItemClickListener,SmallVideoCommentView {

    private SmartRefreshLayout mRefreshlayout;
    private RecyclerView mRvList;
    private TextView mTvComment;

    /**
     * 列表脚视图
     */
    private View mFooterView;

    /**
     * 因为添加了 header 和 footer 故取消了 adater 的 emptyview，改为手动判断
     */
    protected EmptyView mEmptyView;
    /**
     * 没有更多数据
     */
    private TextView mTvNoMoredataText;

    /**
     * 数据源
     */
    private List<DynamicCommentBean> mListDatas = new ArrayList<>();

    /**
     * 列表头和脚适配器
     */
    protected HeaderAndFooterWrapper mHeaderAndFooterWrapper;

    /**
     * 避免 Glide.resume.重复设置增加开销
     */
    private static boolean sIsScrolling;

    /**
     * 输入框监听
     */
    private OnInputDialogShowListener mOnInputDialogShowListener;
    public void setOnInputDialogShowListener(OnInputDialogShowListener mOnInputDialogShowListener) {
        this.mOnInputDialogShowListener = mOnInputDialogShowListener;
    }

    /**
     * 动态实体类
     */
    private DynamicDetailBeanV2 mCurrentDynamicDetailBean;
    public void setCurrentDynamicDetailData(DynamicDetailBeanV2 data) {

        //重新设置数据源时，清空评论列表
        if(mCurrentDynamicDetailBean != data){
            this.mCurrentDynamicDetailBean = data;
            mListDatas.clear();
            refreshData();
        }
    }

    private SmallVideoCommentPresenter mPresenter;

    public void setPresenter(SmallVideoCommentPresenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    /**
     * 构造方法
     *
     * @param context              上下文
     * @param cancelOnTouchOutside
     */
    public SmallVideoCommentDialog(Activity context, boolean cancelOnTouchOutside) {
        super(context, R.layout.dialog_small_video_comment, cancelOnTouchOutside);
        //设置进场动画
        getDialog().getWindow().setWindowAnimations(R.style.style_actionPopupAnimation);
        //处理键盘
        //getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setGravity(Gravity.BOTTOM);
        initView();
    }

    @Override
    public void showDialog() {
        super.showDialog();

        if(null == mCurrentDynamicDetailBean)
            return;

        getTextView(R.id.tv_comment_count).setText(mCurrentDynamicDetailBean.getFeed_comment_count()+"条评论");

        //只有当评论列表为空时，才去请求数据
        if(mListDatas.size() == 0)
            requestNetData(0L,false);

    }

    /**
     * 初始化view
     */
    private void initView(){

        mTvComment = getView(R.id.tv_comment);
        mTvComment.setOnClickListener(v -> {
            if(null != mOnInputDialogShowListener)
                mOnInputDialogShowListener.onInputDilaogShow("精彩评论...", str -> mPresenter.sendCommentV2(0,str));
        });
        //关闭弹窗
        getView(R.id.iv_close).setOnClickListener(v -> dismissDialog());
        //阴影
        getView(R.id.view_shadow).setOnClickListener(v -> dismissDialog());

        mRefreshlayout = getView(com.zhiyicx.baseproject.R.id.refreshlayout);
        mRvList = getView(com.zhiyicx.baseproject.R.id.swipe_target);

        mRefreshlayout.setOnLoadmoreListener(this);

        mRvList.setLayoutManager(new LinearLayoutManager(mContext));
        //设置动画
        mRvList.setItemAnimator(new DefaultItemAnimator());
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(getAdapter());
        mHeaderAndFooterWrapper.addFootView(getFooterView());
        mRvList.setAdapter(mHeaderAndFooterWrapper);
        mRefreshlayout.setEnableAutoLoadmore(false);
        mRefreshlayout.setEnableRefresh(false);
        mRvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                // SCROLL_STATE_FLING; //屏幕处于甩动状态
                // SCROLL_STATE_IDLE; //停止滑动状态
                // SCROLL_STATE_TOUCH_SCROLL;// 手指接触状态
                if (mContext != null) {
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_SETTLING) {
                        sIsScrolling = true;
                        Glide.with(mContext).pauseRequests();
                    } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (sIsScrolling) {
                            if (AndroidLifecycleUtils.canLoadImage(mContext)) {
                                Glide.with(mContext).resumeRequests();
                            }
                        }
                        sIsScrolling = false;
                    }
                }
            }
        });

    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        requestNetData(getMaxId(mListDatas),true);
    }

    protected View getFooterView() {
        // 添加加载更多没有了的提示
        mFooterView = LayoutInflater.from(mContext).inflate(com.zhiyicx.baseproject.R.layout.view_refresh_footer, null);
        mTvNoMoredataText = (TextView) mFooterView.findViewById(com.zhiyicx.baseproject.R.id.tv_no_moredata_text);
        mFooterView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return mFooterView;
    }


    @Override
    public DynamicDetailBeanV2 getCurrentDynamic() {
        return mCurrentDynamicDetailBean;
    }

    @Override
    public void onNetResponseSuccess(List<DynamicCommentBean> list, boolean isLoadMore) {
        mRefreshlayout.finishRefresh();
        mRefreshlayout.finishLoadmore();
        handleReceiveData(list, isLoadMore, false);
    }

    @Override
    public void onNetResponseError() {
        setEmptyViewVisiable(true);
    }

    /**
     * 处理服务器或者缓存中拿到的数据
     *
     * @param data       返回的数据
     * @param isLoadMore 是否是加载更多
     */
    private void handleReceiveData(List<DynamicCommentBean> data, boolean isLoadMore, boolean isFromCache) {
        // 刷新
        if (!isLoadMore) {

            mTvNoMoredataText.setVisibility(View.GONE);
            mRefreshlayout.setEnableLoadmore(true);
            mListDatas.clear();
            if (data != null && data.size() != 0) {

                // 内存处理数据
                mListDatas.addAll(data);
                refreshData();

            } else {
                layzLoadEmptyView();
                mEmptyView.setErrorImag(com.zhiyicx.baseproject.R.mipmap.img_default_nothing);
                refreshData();
            }
        } else { // 加载更多
            if (data != null && data.size() != 0) {

                // 内存处理数据
                mListDatas.addAll(data);
                refreshData();
            }
        }
        // 数据加载后，所有的数据数量小于一页，说明没有更多数据了，就不要上拉加载了(除开缓存)
        boolean isNetNoData = data == null || data.isEmpty() || (getPagesize() != null && data.size() < getPagesize());
        if (!isFromCache && isNetNoData) {
            mRefreshlayout.setEnableLoadmore(false);
            // mListDatas.size() >= DEFAULT_ONE_PAGE_SHOW_MAX_SIZE 当前数量大于一页显示数量时，显示加载更多
            if (mListDatas.size() >= TSListFragment.DEFAULT_ONE_PAGE_SHOW_MAX_SIZE) {
                mTvNoMoredataText.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 默认加载条数，具体数据又后端确定
     *
     * @return
     */
    protected Integer getPagesize() {
        return TSListFragment.DEFAULT_PAGE_SIZE;
    }

    @Override
    public void refreshData() {
        if (mHeaderAndFooterWrapper != null) {
            setEmptyViewVisiable(mListDatas.isEmpty() && mHeaderAndFooterWrapper.getHeadersCount() <= 0);
            try {
                mHeaderAndFooterWrapper.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        getTextView(R.id.tv_comment_count).setText(mCurrentDynamicDetailBean.getFeed_comment_count()+"条评论");

    }

    @Override
    public List<DynamicCommentBean> getListDatas() {
        return mListDatas;
    }

    /**
     * 设置 emptyview 可见性
     *
     * @param visiable true 可见
     */
    public void setEmptyViewVisiable(boolean visiable) {
        layzLoadEmptyView();
        if (mEmptyView != null) {
            mEmptyView.setVisibility(visiable ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 懒加载 emptyView
     */
    protected void layzLoadEmptyView() {
        if (mEmptyView == null) {
            try {
                ViewStub viewStub = (ViewStub) getView(com.zhiyicx.baseproject.R.id.stub_empty_view);
                mEmptyView = (EmptyView) viewStub.inflate();
                mEmptyView.setErrorImag(com.zhiyicx.baseproject.R.mipmap.img_default_nothing);
                mEmptyView.setNeedTextTip(false);
                mEmptyView.setNeedClickLoadState(false);
                RxView.clicks(mEmptyView)
                        .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                        .subscribe(aVoid -> requestNetData(0L,false));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 获取网络数据
     *
     * @param maxId
     * @param isLoadMore
     */
    protected void requestNetData(Long maxId, boolean isLoadMore) {
        if (mPresenter != null) {
            mPresenter.requestCommentList(null == maxId ? 0L : maxId, isLoadMore);
        }
    }

    /**
     * @param data
     * @return maxId 用户分页使用
     */
    protected Long getMaxId(@NotNull List<DynamicCommentBean> data) {
        if (mListDatas.size() > 0) {
            return mListDatas.get(mListDatas.size() - 1).getMaxId();
        }
        return 0L;
    }

    /**
     * 适配器
     * @return
     */
    protected MultiItemTypeAdapter<DynamicCommentBean> getAdapter() {
        MultiItemTypeAdapter<DynamicCommentBean> adapter = new MultiItemTypeAdapter<>(mContext, mListDatas);
        DynamicDetailCommentItem dynamicDetailCommentItem = new DynamicDetailCommentItem();
        dynamicDetailCommentItem.setOnUserInfoClickListener(this);
        dynamicDetailCommentItem.setOnCommentTextClickListener(this);
        dynamicDetailCommentItem.setOnCommentResendListener(this);
        adapter.addItemViewDelegate(dynamicDetailCommentItem);
        DynamicCommentEmptyItem dynamicCommentEmptyItem = new DynamicCommentEmptyItem();
        adapter.addItemViewDelegate(dynamicCommentEmptyItem);
        adapter.setOnItemClickListener(this);
        return adapter;
    }

    @Override
    public void onUserInfoClick(UserInfoBean userInfoBean) {

    }

    @Override
    public void onCommentTextClick(int position) {
        if(null != mOnInputDialogShowListener)
            mOnInputDialogShowListener.onInputDilaogShow("精彩评论...", str ->
                    mPresenter.sendCommentV2(mListDatas.get(position).getUser_id(),str));
    }

    @Override
    public void onCommentTextLongClick(int position) {

    }

    @Override
    public void reSendComment(DynamicCommentBean dynamicCommentBean) {

    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        if(null != mOnInputDialogShowListener)
            mOnInputDialogShowListener.onInputDilaogShow("精彩评论...", str ->
                    mPresenter.sendCommentV2(mListDatas.get(position).getUser_id(),str));
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }


    public interface OnInputDialogShowListener{
        void onInputDilaogShow(String hint, EditTextDialog.OnInputOkListener listener);
    }

}
