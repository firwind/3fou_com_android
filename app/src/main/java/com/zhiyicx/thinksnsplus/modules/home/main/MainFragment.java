package com.zhiyicx.thinksnsplus.modules.home.main;

import android.Manifest;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;

import com.lwy.righttopmenu.MenuItem;
import com.lwy.righttopmenu.RightTopMenu;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.config.TouristConfig;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.StatusBarUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.SendDynamicDataBean;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.modules.chat.select.SelectFriendsActivity;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.DynamicContract;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.DynamicFragment;
import com.zhiyicx.thinksnsplus.modules.dynamic.send.SendDynamicActivity;
import com.zhiyicx.thinksnsplus.modules.findsomeone.contianer.FindSomeOneContainerActivity;
import com.zhiyicx.thinksnsplus.modules.home.HomeActivity;
import com.zhiyicx.thinksnsplus.modules.home.mine.scan.ScanCodeActivity;
import com.zhiyicx.thinksnsplus.modules.photopicker.PhotoAlbumDetailsActivity;
import com.zhiyicx.thinksnsplus.modules.shortvideo.helper.ZhiyiVideoView;
import com.zhiyicx.thinksnsplus.modules.shortvideo.videostore.VideoSelectActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import cn.jzvd.JZUtils;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerManager;

/**
 * @Describe 主页 MainFragment
 * @Author zl
 * @Date 2017/1/5
 * @Contact master.jungle68@gmail.com
 */
public class MainFragment extends TSViewPagerFragment implements DynamicFragment.OnCommentClickListener {
    // 关注动态列表位置，如果更新了，记得修改这儿
    public static final int PAGER_FOLLOW_DYNAMIC_LIST_POSITION = 0;
    @BindView(R.id.v_status_bar_placeholder)
    View mStatusBarPlaceholder;
    @BindView(R.id.v_shadow)
    View mVShadow;
    private RightTopMenu mRightTopMenu;
    @Inject
    AuthRepository mIAuthRepository;
    @Inject
    DynamicBeanGreenDaoImpl mDynamicBeanGreenDao;

    public void setOnImageClickListener(DynamicFragment.OnCommentClickListener onCommentClickListener) {
        mOnCommentClickListener = onCommentClickListener;
    }

    DynamicFragment.OnCommentClickListener mOnCommentClickListener;

    public static MainFragment newInstance(DynamicFragment.OnCommentClickListener l) {
        MainFragment fragment = new MainFragment();
        fragment.setOnImageClickListener(l);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_main_viewpager;
    }

    @Override
    protected boolean setUseSatusbar() {
        return false;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected int getOffsetPage() {
        return 2;
    }
    @Override
    protected void setLeftClick() {
        onBackPressed();
    }

    @Override
    protected void initView(View rootView) {
        // 需要在 initview 之前，应为在 initview 中使用了 dagger 注入的数据
        AppApplication.AppComponentHolder.getAppComponent().inject(this);
        super.initView(rootView);
        initToolBar();
    }

    private void initToolBar() {
        // toolBar设置状态栏高度的marginTop
        /*RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, DeviceUtils
                .getStatuBarHeight(getContext()));
        mStatusBarPlaceholder.setLayoutParams(layoutParams);
        // 适配非6.0以上、非魅族系统、非小米系统状态栏
        if (StatusBarUtils.intgetType(getActivity().getWindow()) == 0) {
            mStatusBarPlaceholder.setBackgroundResource(R.color.themeColor);
        }*/
        //不需要返回键
//        mTsvToolbar.setLeftImg(0);
        mTsvToolbar.setRightImg(R.mipmap.ico_camera,R.color.white);
        mTsvToolbar.getRightTextView().setVisibility(View.VISIBLE);
        mTsvToolbar.setRightClickListener(this, () -> {//点击“+”号
//            Intent intent = new Intent(getContext(), SelectFriendsActivity.class);
//            startActivity(intent);
            List<MenuItem> menuItems = new ArrayList<>();
            menuItems.add(new MenuItem(R.mipmap.ico_text,getString(R.string.send_words_dynamic)));
            menuItems.add(new MenuItem(R.mipmap.ico_image, getString(R.string.send_image_dynamic)));
            menuItems.add(new MenuItem(R.mipmap.ico_videos, getString(R.string.send_vidoe)));
            if (mRightTopMenu == null) {
                mRightTopMenu = new RightTopMenu.Builder(getActivity())
                        .dimBackground(true)           //背景变暗，默认为true
                        .needAnimationStyle(true)   //显示动画，默认为true
                        .animationStyle(R.style.RTM_ANIM_STYLE)  //默认为R.style.RTM_ANIM_STYLE
                        .menuItems(menuItems)
                        .onMenuItemClickListener(new RightTopMenu.OnMenuItemClickListener() {
                            @Override
                            public void onMenuItemClick(int position) {
                                switch (position){
                                    case 0://文字
                                        SendDynamicDataBean sendDynamicDataBean = new SendDynamicDataBean();
                                        sendDynamicDataBean.setDynamicBelong(SendDynamicDataBean.NORMAL_DYNAMIC);
                                        sendDynamicDataBean.setDynamicType(SendDynamicDataBean.TEXT_ONLY_DYNAMIC);
                                        SendDynamicActivity.startToSendDynamicActivity(getContext(), sendDynamicDataBean);
                                        break;
                                    case 1://图片
                                        SendDynamicDataBean sendDynamicDataBeanP = new SendDynamicDataBean();
                                        sendDynamicDataBeanP.setDynamicBelong(SendDynamicDataBean.NORMAL_DYNAMIC);
                                        sendDynamicDataBeanP.setDynamicType(SendDynamicDataBean.PHOTO_TEXT_DYNAMIC);
                                        SendDynamicActivity.startToSendDynamicActivity(getContext(), sendDynamicDataBeanP);
                                        break;
                                    case 2://视频
                                        SendDynamicDataBean sendDynamicDataBeanV = new SendDynamicDataBean();
                                        sendDynamicDataBeanV.setDynamicBelong(SendDynamicDataBean.NORMAL_DYNAMIC);
                                        sendDynamicDataBeanV.setDynamicType(SendDynamicDataBean.VIDEO_TEXT_DYNAMIC);
                                        SendDynamicActivity.startToSendDynamicActivity(getContext(), sendDynamicDataBeanV);
                                        break;
                                }
                            }
                        }).build();
            }

            mRightTopMenu.showAsDropDown(mTsvToolbar.getRightTextView(), -20, 0);

        });
    }

    @Override
    protected void initData() {

        mVpFragment.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 停掉当前播放
                if (JZVideoPlayerManager.getCurrentJzvd() != null) {
                    if (JZVideoPlayerManager.getCurrentJzvd().currentState == ZhiyiVideoView.CURRENT_STATE_PREPARING
                            || JZVideoPlayerManager.getCurrentJzvd().currentState == ZhiyiVideoView.CURRENT_STATE_PREPARING_CHANGING_URL) {
                        ZhiyiVideoView.releaseAllVideos();
                    }
                }
                ZhiyiVideoView.goOnPlayOnPause();


                // 游客处理
                if (!TouristConfig.FOLLOW_CAN_LOOK && position == mVpFragment.getChildCount() - 1 && !mIAuthRepository.isLogin()) {
                    showLoginPop();
                    // 转回热门
                    mVpFragment.setCurrentItem(1);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    ((DynamicContract.View) mFragmentList.get(mVpFragment.getCurrentItem())).closeInputView();
                }
            }
        });

        // 启动 app，如果本地没有最新数据，应跳到“热门”页面 关联 github  #113  #366
        try {
            if (mDynamicBeanGreenDao.getNewestDynamicList(System.currentTimeMillis()).size() == 0) {
                mVpFragment.setCurrentItem(1);
            }
        } catch (SQLiteException ignored) {
        }

    }

    @Override
    protected List<String> initTitles() {
        return Arrays.asList(getString(R.string.the_last)
                , getString(R.string.hot)
                , getString(R.string.follow));
    }

    @Override
    protected List<Fragment> initFragments() {
        if (mFragmentList == null) {
            mFragmentList = new ArrayList();
            mFragmentList.add(DynamicFragment.newInstance(ApiConfig.DYNAMIC_TYPE_NEW, this));
            mFragmentList.add(DynamicFragment.newInstance(ApiConfig.DYNAMIC_TYPE_HOTS, this));
            // 游客处理
            if (TouristConfig.FOLLOW_CAN_LOOK || mIAuthRepository.isLogin()) {
                mFragmentList.add(DynamicFragment.newInstance(ApiConfig.DYNAMIC_TYPE_FOLLOWS, this));
            } else {
                // 用于viewpager 占位
                mFragmentList.add(DynamicFragment.newInstance(ApiConfig.DYNAMIC_TYPE_EMPTY, this));
            }
        }
        return mFragmentList;
    }


    @Override
    public void onButtonMenuShow(boolean isShow) {
        mVShadow.setVisibility(isShow ? View.GONE : View.VISIBLE);
//        if (mOnCommentClickListener != null) {
//            mOnCommentClickListener.onButtonMenuShow(isShow);
//        }
    }

    /**
     * viewpager页面切换公开方法
     */
    public void setPagerSelection(int position) {
        mVpFragment.setCurrentItem(position, true);
    }

    /**
     * 刷新当前页
     */
    public void refreshCurrentPage() {
        ((ITSListView) mFragmentList.get(mVpFragment.getCurrentItem())).startRefrsh();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getActivity(), HomeActivity.class));
    }
}
