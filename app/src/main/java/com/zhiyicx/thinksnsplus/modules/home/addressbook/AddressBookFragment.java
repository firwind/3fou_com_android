package com.zhiyicx.thinksnsplus.modules.home.addressbook;
/*
 * 文件名：通讯录
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/12 15:49
 * 修改者：
 * 修改备注：
 * 修改时间：
 * 版  权：互动科技
 */

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RelativeLayout;

import com.lwy.righttopmenu.MenuItem;
import com.lwy.righttopmenu.RightTopMenu;
import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.StatusBarUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;

import com.zhiyicx.thinksnsplus.modules.chat.select.SelectFriendsActivity;
import com.zhiyicx.thinksnsplus.modules.findsomeone.contacts.ContactsFragment;
import com.zhiyicx.thinksnsplus.modules.findsomeone.contianer.FindSomeOneContainerActivity;
import com.zhiyicx.thinksnsplus.modules.home.common.invite.InviteShareActivity;
import com.zhiyicx.thinksnsplus.modules.home.message.messagegroup.MessageGroupListFragment;
import com.zhiyicx.thinksnsplus.modules.home.mine.friends.MyFriendsListFragment;
import com.zhiyicx.thinksnsplus.modules.home.mine.scan.ScanCodeActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class AddressBookFragment extends TSViewPagerFragment {
    @BindView(R.id.v_status_bar_placeholder)
    View mStatusBarPlaceholder;
    @BindView(R.id.v_shadow)
    View mVShadow;
    private RightTopMenu mRightTopMenu;

    public static AddressBookFragment newInstance() {
        AddressBookFragment fragment = new AddressBookFragment();
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
        return true;
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
    protected void initView(View rootView) {
        // 需要在 initview 之前，应为在 initview 中使用了 dagger 注入的数据
        AppApplication.AppComponentHolder.getAppComponent().inject(this);
        super.initView(rootView);
        initToolBar();
    }

    @Override
    protected boolean usePermisson() {
        return true;
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
        //不需要返回键
        mTsvToolbar.setLeftImg(0);
        mTsvToolbar.setRightImg(R.mipmap.channel_ico_add_blue, R.color.white);
        mTsvToolbar.setRightClickListener(this, () -> {//点击“+”号
//            Intent intent = new Intent(getContext(), SelectFriendsActivity.class);
//            startActivity(intent);
            List<MenuItem> menuItems = new ArrayList<>();
            menuItems.add(new MenuItem(R.mipmap.icon_add_friends, getString(R.string.tv_add_friends)));
            menuItems.add(new MenuItem(R.mipmap.icon_add_group_chat, getString(R.string.tv_add_group_chat)));
            menuItems.add(new MenuItem(R.mipmap.icon_create_group_chat, getString(R.string.tv_create_group_chat)));
            menuItems.add(new MenuItem(R.mipmap.icon_scan, getString(R.string.my_qr_code_title)));
            menuItems.add(new MenuItem(R.mipmap.icon_share_to, getString(R.string.tv_invite_Share)));
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
                                    case 0://添加好友
                                        Intent itFollow = new Intent(getActivity(), FindSomeOneContainerActivity.class);
                                        Bundle bundleFollow = new Bundle();
                                        itFollow.putExtras(bundleFollow);
                                        startActivity(itFollow);
                                        break;
                                    case 1://加入群聊
                                        ToastUtils.showLongToast("该功能正在开发");
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
            mRightTopMenu.showAsDropDown(mTsvToolbar.getRightTextView(), 20, 0);
        });
    }

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    protected List<String> initTitles() {
        return Arrays.asList(getString(R.string.friends)
                , getString(R.string.group_title)
                , getString(R.string.mobile_contact_title));
    }

    @Override
    protected List<Fragment> initFragments() {
        if (mFragmentList == null) {
            mFragmentList = new ArrayList();
            mFragmentList.add(MyFriendsListFragment.newInstance());
            mFragmentList.add(MessageGroupListFragment.newInstance());
            mFragmentList.add(ContactsFragment.newInstance());
        }
        return mFragmentList;
    }

    /**
     * viewpager页面切换公开方法
     */
    public void setPagerSelection(int position) {
        mVpFragment.setCurrentItem(position, true);
    }

    @Override
    protected void initData() {

    }
}
