package com.zhiyicx.thinksnsplus.modules.home.message.container;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.util.DensityUtil;
import com.lwy.righttopmenu.MenuItem;
import com.lwy.righttopmenu.RightTopMenu;
import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.baseproject.base.TSViewPagerFragmentV2;
import com.zhiyicx.baseproject.widget.TabSelectView;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.StatusBarUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.chat.select.SelectFriendsActivity;
import com.zhiyicx.thinksnsplus.modules.chat.select.addgroup.AddGroupActivity;
import com.zhiyicx.thinksnsplus.modules.findsomeone.contianer.FindSomeOneContainerActivity;
import com.zhiyicx.thinksnsplus.modules.home.common.invite.InviteShareActivity;
import com.zhiyicx.thinksnsplus.modules.home.message.MessageFragment;
import com.zhiyicx.thinksnsplus.modules.home.message.homepage.MessageHomePageFragment;
import com.zhiyicx.thinksnsplus.modules.home.message.messagegroup.MessageGroupActivity;
import com.zhiyicx.thinksnsplus.modules.home.message.messagegroup.MessageGroupListFragment;
import com.zhiyicx.thinksnsplus.modules.home.message.messagelist.MessageConversationFragment;
import com.zhiyicx.thinksnsplus.modules.home.message.notification.NotificationFragment;
import com.zhiyicx.thinksnsplus.modules.home.mine.scan.ScanCodeActivity;

import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgeAnchor;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgeRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * @author Catherine
 * @describe 消息页面
 * @date 2017/8/31
 * @contact email:648129313@qq.com
 */

public class MessageContainerFragment extends TSViewPagerFragmentV2/* implements EaseConversationListFragment.EaseConversationListItemClickListener*/ {

    private static final int DEFAULT_TAB_UNSELECTED_TEXTCOLOR = com.zhiyicx.baseproject.R.color.normal_for_assist_text;// 缺省的tab未选择文字
    private static final int DEFAULT_TAB_SELECTED_TEXTCOLOR = com.zhiyicx.baseproject.R.color.important_for_content;// 缺省的tab被选择文字
    private static final int DEFAULT_TAB_TEXTSIZE = com.zhiyicx.baseproject.R.integer.tab_text_size;// 缺省的tab文字大小
    private static final int DEFAULT_TAB_MARGIN = com.zhiyicx.baseproject.R.integer.tab_margin;// 缺省的tab左右padding
    private static final int DEFAULT_TAB_PADDING = com.zhiyicx.baseproject.R.integer.tab_padding;// 缺省的tab的线和文字的边缘距离
    private static final int DEFAULT_TAB_LINE_COLOR = com.zhiyicx.baseproject.R.color.themeColor;// 缺省的tab的线的颜色
    private static final int DEFAULT_TAB_LINE_HEGIHT = com.zhiyicx.baseproject.R.integer.line_height;// 缺省的tab的线的高度

    @BindView(R.id.v_status_bar_placeholder)
    View mStatusBarPlaceholder;
    private RightTopMenu mRightTopMenu;
    private List<BadgePagerTitleView> mBadgePagerTitleViews;
    /**
     * 0-消息 1=通知
     */
    private boolean mIsMessageTipShow;
    private boolean mIsNotificationTipShow;

    public static MessageContainerFragment instance() {
        return new MessageContainerFragment();
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_main_viewpager;
    }

    @Override
    protected int getOffsetPage() {
        return 3;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        //initToolBar();
    }

    @Override
    protected List<String> initTitles() {
        return Arrays.asList("首页",getString(R.string.official_group),"消息",getString(R.string.notification));
    }

    @Override
    protected List<Fragment> initFragments() {
        if (mFragmentList == null) {
            mFragmentList = new ArrayList<>();
            /*EaseConversationListFragment conversationListFragment = new EaseConversationListFragment();
            conversationListFragment.setConversationListItemClickListener(this);*/
            mFragmentList.add(new MessageHomePageFragment());
            mFragmentList.add(MessageGroupListFragment.newInstance());
            mFragmentList.add(new MessageConversationFragment());
            mFragmentList.add(/*MessageFragment.newInstance()*/NotificationFragment.newInstance());
        }

        return mFragmentList;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected boolean usePermisson() {
        return true;
    }

    @Override
    protected void initViewPager(View rootView) {
        super.initViewPager(rootView);

        mTsvToolbar.setLeftImg(0);
        mTsvToolbar.setRightImg(R.mipmap.channel_ico_add_blue, R.color.white);
        mTsvToolbar.setLeftClickListener(this, () -> {
            Intent intent = new Intent(getContext(), MessageGroupActivity.class);
            startActivity(intent);
        });
        mTsvToolbar.setRightClickListener(this, () -> {//点击“+”号
//            Intent intent = new Intent(getContext(), SelectFriendsActivity.class);
//            startActivity(intent);
            showRightTopMenu();
        });
        mBadgePagerTitleViews = new ArrayList<>();
        CommonNavigatorAdapter commonNavigatorAdapter = getCommonNavigatorAdapter(initTitles());
        mTsvToolbar.initTabView(mVpFragment, initTitles(), commonNavigatorAdapter);
        mTsvToolbar.setLeftImg(0);

    }

    public int getCurrentItem() {
        return mVpFragment.getCurrentItem();
    }

    public void setCurrentItem(int position){
        if(position < mFragmentList.size())
            mVpFragment.setCurrentItem(position);
    }

    @Override
    protected View getRightViewOfMusicWindow() {
        return mTsvToolbar.getRightTextView();
    }

    @Override
    protected boolean setUseSatusbar() {
        return false;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    /**
     * 设置提示的红点的显示和隐藏
     *
     * @param isShow   状态
     * @param position 位置 0-消息 1=通知
     */
    public void setNewMessageNoticeState(boolean isShow, int position) {
        if (position != 2 && position != 3) {
            return;
        }
        switch (position) {
            case 2:
                if (isShow == mIsMessageTipShow) {
//                    return;
                } else {
                    mIsMessageTipShow = isShow;
                }
                break;
            case 3:
                if (isShow == mIsNotificationTipShow) {
//                    return;
                } else {
                    mIsNotificationTipShow = isShow;
                }
                break;
            default:
        }
        BadgePagerTitleView badgePagerTitleView;
        try {
            badgePagerTitleView = mBadgePagerTitleViews.get(position);
            if (isShow) {
                ImageView badgeImageView = (ImageView) LayoutInflater.from(getContext()).inflate(R.layout.simple_count_badge_layout, null);
                badgePagerTitleView.setBadgeView(badgeImageView);
                badgePagerTitleView.setXBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_RIGHT, UIUtil.dip2px(getContext(), 10)));
                badgePagerTitleView.setYBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_TOP, -UIUtil.dip2px(getContext(), 3)));
            }
            if (!isShow) {
                badgePagerTitleView.setBadgeView(null);
            }
        } catch (Exception ignore) {
            LogUtils.d("can not find badgeView");
        }
    }

    private void initToolBar() {
        // toolBar设置状态栏高度的marginTop
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, DeviceUtils
                .getStatuBarHeight(getContext()));
        mStatusBarPlaceholder.setLayoutParams(layoutParams);
        // 适配非6.0以上、非魅族系统、非小米系统状态栏
        if (StatusBarUtils.intgetType(getActivity().getWindow()) == 0) {
            mStatusBarPlaceholder.setBackgroundResource(R.color.themeColor);
        }
    }

    @NonNull
    private CommonNavigatorAdapter getCommonNavigatorAdapter(final List<String> mStringList) {
        return new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mStringList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                final BadgePagerTitleView badgePagerTitleView = new BadgePagerTitleView(context);
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(ContextCompat.getColor(context, DEFAULT_TAB_UNSELECTED_TEXTCOLOR));
                int leftRightPadding = UIUtil.dip2px(context, getContext().getResources().getInteger(DEFAULT_TAB_MARGIN));
                simplePagerTitleView.setPadding(leftRightPadding, 0, leftRightPadding, 0);
                simplePagerTitleView.setSelectedColor(ContextCompat.getColor(context, DEFAULT_TAB_SELECTED_TEXTCOLOR));
                simplePagerTitleView.setText(mStringList.get(index));
                simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, context.getResources().getInteger(DEFAULT_TAB_TEXTSIZE));
                simplePagerTitleView.setOnClickListener(v -> {
                    mVpFragment.setCurrentItem(index);
                });
                badgePagerTitleView.setInnerPagerTitleView(simplePagerTitleView);
                // don't cancel badge when tab selected
                badgePagerTitleView.setAutoCancelBadge(false);
                mBadgePagerTitleViews.add(badgePagerTitleView);
                return badgePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_MATCH_EDGE);// 占满
                linePagerIndicator.setXOffset(UIUtil.dip2px(context, context.getResources()
                        .getInteger(DEFAULT_TAB_PADDING)));// 每个item边缘到指示器的边缘距离
                linePagerIndicator.setLineHeight(UIUtil.dip2px(context, context.getResources()
                        .getInteger(DEFAULT_TAB_LINE_HEGIHT)));
                linePagerIndicator.setColors(ContextCompat.getColor(context,
                        DEFAULT_TAB_LINE_COLOR));
                return linePagerIndicator;
            }
        };
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mFragmentList != null && !mFragmentList.isEmpty()) {
            for (Object o : mFragmentList) {
                if (o instanceof Fragment) {
                    ((Fragment) o).setUserVisibleHint(isVisibleToUser);
                }
            }
        }
    }

    /**
     * 右边弹窗
     */
    private void showRightTopMenu(){

        if (mRightTopMenu == null) {
            List<MenuItem> menuItems = new ArrayList<>();
            menuItems.add(new MenuItem(R.mipmap.icon_add_friends, getString(R.string.tv_add_friends)));
            menuItems.add(new MenuItem(R.mipmap.icon_add_group_chat, getString(R.string.tv_add_group_chat)));
            menuItems.add(new MenuItem(R.mipmap.icon_create_group_chat, getString(R.string.tv_create_group_chat)));
            menuItems.add(new MenuItem(R.mipmap.icon_scan, getString(R.string.my_qr_code_title)));
            menuItems.add(new MenuItem(R.mipmap.icon_share_to, getString(R.string.tv_invite_Share)));
            mRightTopMenu = new RightTopMenu.Builder(getActivity())
                    .dimBackground(true)           //背景变暗，默认为true
                    .needAnimationStyle(true)   //显示动画，默认为true
                    .animationStyle(R.style.RTM_ANIM_STYLE)  //默认为R.style.RTM_ANIM_STYLE
                    .menuItems(menuItems)
                    .windowWidth(DensityUtil.dip2px(mActivity,160))
                    .onMenuItemClickListener(new RightTopMenu.OnMenuItemClickListener() {
                        @Override
                        public void onMenuItemClick(int position) {
                            switch (position){
                                case 0://添加好友
                                    Intent itFollow = new Intent(getActivity(), FindSomeOneContainerActivity.class);
                                    Bundle bundleFollow = new Bundle();
                                    itFollow.putExtras(bundleFollow);
                                    startActivity(itFollow);
                                    break;
                                case 1://加入群聊
                                    AddGroupActivity.startAddGroupActivity(getContext());
                                    break;
                                case 2:
                                    Intent intent = new Intent(getContext(), SelectFriendsActivity.class);
                                    Bundle bundle = new Bundle();
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    break;
                                case 3:
                                    mRxPermissions
                                            .requestEach(Manifest.permission.CAMERA)
                                            .subscribe(permission -> {
                                                if (permission.granted) {
                                                    // 权限被允许
                                                    startActivity(new Intent(getContext(), ScanCodeActivity.class));
                                                } else if (permission.shouldShowRequestPermissionRationale) {
                                                    // 权限没有被彻底禁止
                                                } else {
                                                    // 权限被彻底禁止
                                                    showSnackWarningMessage(getString(R.string.camera_permission_tip));
                                                }
                                            });
                                    break;
                                case 4:
                                    //ToastUtils.showLongToast("该功能正在开发");
                                    startActivity(InviteShareActivity.newIntent(mActivity));
                                    break;
                            }
                        }
                    }).build();
        }

        mRightTopMenu.showAsDropDown(mTsvToolbar.getRightTextView(),DensityUtil.dip2px(mActivity,15),0);
    }

}
