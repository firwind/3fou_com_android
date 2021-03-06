package com.zhiyicx.thinksnsplus.modules.home.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.baseproject.widget.BadgeView;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.BCWalletBean;
import com.zhiyicx.thinksnsplus.data.beans.SendCertificationBean;
import com.zhiyicx.thinksnsplus.data.beans.UserCertificationInfo;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.certification.detail.CertificationDetailActivity;
import com.zhiyicx.thinksnsplus.modules.certification.input.CertificationInputActivity;
import com.zhiyicx.thinksnsplus.modules.circle.mine.container.MyCircleContainerActivity;
import com.zhiyicx.thinksnsplus.modules.collect.CollectListActivity;
import com.zhiyicx.thinksnsplus.modules.currency.MyCurrencyActivity;
import com.zhiyicx.thinksnsplus.modules.draftbox.DraftBoxActivity;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoActivity;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListActivity;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListFragment;
import com.zhiyicx.thinksnsplus.modules.home.common.invite.InviteShareActivity;
import com.zhiyicx.thinksnsplus.modules.home.mine.friends.MyFriendsListActivity;
import com.zhiyicx.thinksnsplus.modules.home.mine.mycode.MyCodeActivity;
import com.zhiyicx.thinksnsplus.modules.home.mine.team.MyTeamActivity;
import com.zhiyicx.thinksnsplus.modules.information.my_info.ManuscriptsActivity;
import com.zhiyicx.thinksnsplus.modules.music_fm.paided_music.MyMusicActivity;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
//import com.zhiyicx.thinksnsplus.modules.personal_center.person_home.PersonalCenterFragmentV2;
import com.zhiyicx.thinksnsplus.modules.q_a.mine.container.MyQuestionActivity;
import com.zhiyicx.thinksnsplus.modules.settings.SettingsActivity;
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.CustomWEBActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.WalletActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.integration.mine.newIntegration.NewMineIntegrationActivity;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.widget.CertificationTypePopupWindow;
import com.zhiyicx.thinksnsplus.modules.wallet.red_packet.IntegralRedPacketDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.schedulers.Schedulers;

import static com.zhiyicx.thinksnsplus.modules.certification.detail.CertificationDetailActivity.BUNDLE_DETAIL_DATA;
import static com.zhiyicx.thinksnsplus.modules.certification.detail.CertificationDetailActivity.BUNDLE_DETAIL_TYPE;
import static com.zhiyicx.thinksnsplus.modules.certification.input.CertificationInputActivity.BUNDLE_CERTIFICATION_TYPE;
import static com.zhiyicx.thinksnsplus.modules.certification.input.CertificationInputActivity.BUNDLE_TYPE;

/**
 * @Describe 我的页面
 * @Author zl
 * @Date 2017/1/5
 * @Contact master.jungle68@gmail.com
 */
public class MineFragment extends TSFragment<MineContract.Presenter> implements MineContract.View,
        CertificationTypePopupWindow.OnTypeSelectListener {

    @BindView(R.id.iv_head_icon)
    UserAvatarView mIvHeadIcon;
    @BindView(R.id.tv_user_name)
    TextView mTvUserName;
    @BindView(R.id.tv_user_signature)
    TextView mTvUserSignature;
    @BindView(R.id.tv_fans_count)
    TextView mTvFansCount;
    @BindView(R.id.tv_follow_count)
    TextView mTvFollowCount;
    @BindView(R.id.tv_friends_count)
    TextView mTvFriendsCount;
    @BindView(R.id.bt_certification)
    CombinationButton mBtCertification;
    @BindView(R.id.bv_fans_new_count)
    BadgeView mVvFansNewCount;
    @BindView(R.id.bv_friends_new_count)
    BadgeView mBvFriendsNewCount;

    @BindView(R.id.tv_integration)
    TextView mTvIntegration;
    @BindView(R.id.tv_wallet_count)
    TextView mTvWalletCount;
    @BindView(R.id.tv_integration_count)
    TextView mTvIntegrationCount;
    @BindView(R.id.tv_digital_wallet_count)
    TextView mTvDigitalWalletCount;

    /**
     * 选择认证人类型的弹窗
     */
    private CertificationTypePopupWindow mCertificationWindow;

    @Inject
    public MinePresenter mMinePresenter;

    private UserInfoBean mUserInfoBean;
    private UserCertificationInfo mUserCertificationInfo;

    private IntegralRedPacketDialog mIntegralRedPacketDialog;

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Observable.create(subscriber -> {
            DaggerMinePresenterComponent.builder()
                    .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                    .minePresenterModule(new MinePresenterModule(MineFragment.this))
                    .build().inject(MineFragment.this);
            subscriber.onCompleted();
        }).subscribeOn(Schedulers.io())
                .subscribe(o -> {
                }, Throwable::printStackTrace);
    }

    @Override
    protected void initView(View rootView) {
        mBtCertification.setEnabled(false);
    }

    @Override
    protected void initData() {
        reLoadUserInfo(true);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        reLoadUserInfo(isVisibleToUser);
    }

    private void reLoadUserInfo(boolean isVisibleToUser) {
        if (isVisibleToUser && mPresenter != null) {
            //mPresenter.getUserInfoFromDB();
            mPresenter.updateUserNewMessage();
            mPresenter.updateUserInfo();
            mPresenter.getCertificationInfo();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        reLoadUserInfo(getUserVisibleHint());
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.mine);
    }

    @Override
    protected int setRightImg() {
        return R.mipmap.ico_code;
    }

    @Override
    protected String setRightTitle() {
        return "";
    }

    @Override
    protected int setLeftImg() {
        return 0;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
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
    protected int getBodyLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.white;
    }

    @Override
    protected void setRightClick() {
        super.setRightClick();
        startActivity(new Intent(mActivity, MyCodeActivity.class));
    }

    @OnClick({R.id.rl_userinfo_container, R.id.ll_fans_container, R.id.ll_follow_container, R.id.bt_my_info,
            R.id.bt_personal_page, R.id.bt_collect, R.id.ll_wallet_container/*bt_cash*/, R.id./*bt_mine_integration*/ll_integration_container, R.id.bt_music,
            R.id.bt_draft_box, R.id.bt_setting, R.id.bt_certification, R.id.bt_my_qa, R.id.bt_my_group,
            R.id.ll_friends_container, R.id.ll_digital_wallet_container/*bt_wallet*/, R.id.bt_team, R.id.bt_my_invite, R.id.bt_download, R.id.bt_my_activity})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_userinfo_container:
                startActivity(new Intent(mActivity, UserInfoActivity.class));
                break;
                /*
                  粉丝列表
                 */
            case R.id.ll_fans_container:
                long fansUserId = AppApplication.getmCurrentLoginAuth().getUser_id();
                Bundle bundleFans = new Bundle();
                bundleFans.putInt(FollowFansListFragment.PAGE_TYPE, FollowFansListFragment.FANS_FRAGMENT_PAGE);
                bundleFans.putLong(FollowFansListFragment.PAGE_DATA, fansUserId);
                Intent itFans = new Intent(mActivity, FollowFansListActivity.class);
                itFans.putExtras(bundleFans);
                startActivity(itFans);
                break;
                /*
                 关注列表
                 */
            case R.id.ll_follow_container:
                long followUserId = AppApplication.getmCurrentLoginAuth().getUser_id();
                Bundle bundleFollow = new Bundle();
                bundleFollow.putInt(FollowFansListFragment.PAGE_TYPE, FollowFansListFragment.FOLLOW_FRAGMENT_PAGE);
                bundleFollow.putLong(FollowFansListFragment.PAGE_DATA, followUserId);
                Intent itFollow = new Intent(mActivity, FollowFansListActivity.class);
                itFollow.putExtras(bundleFollow);
                startActivity(itFollow);
                break;
            case R.id.bt_personal_page:
                PersonalCenterFragment.startToPersonalCenter(mActivity, mUserInfoBean);
//                PersonalCenterFragmentV2.startToPersonalCenter(mActivity, mUserInfoBean);
                break;
            case R.id.bt_team://我的团队
                MyTeamActivity.startMyTeamActivity(getContext());
                break;
            /*
             * 我的投稿
             */
            case R.id.bt_my_info:
                startActivity(new Intent(mActivity, ManuscriptsActivity.class));
                break;
            /*
              我的收藏
             */
            case R.id.bt_collect:
                startActivity(new Intent(mActivity, CollectListActivity.class));
                break;
            /*
              我的现金
             */
            case R.id./*bt_cash*/ll_wallet_container:
                startActivity(new Intent(mActivity, WalletActivity.class));
                break;
            /*
              我的糖果
             */
            case R.id./*bt_mine_integration*/ll_integration_container:
                startActivity(new Intent(mActivity, NewMineIntegrationActivity/*MineIntegrationActivity*/.class));
                break;
            /*
              我的音乐
             */
            case R.id.bt_music:
                startActivity(new Intent(mActivity, MyMusicActivity.class));
                break;
//            case R.id.bt_suggestion:
//                // 意见反馈跳转 ts+ 小助手 2018-3-12 11:47:12 by tym
//                List<SystemConfigBean.ImHelperBean> tsHlepers = mPresenter.getImHelper();
//                if (tsHlepers == null || tsHlepers.isEmpty() && EMClient.getInstance().isConnected()) {
//                    startActivity(new Intent(mActivity, FeedBackActivity.class));
//                } else {
//                    ChatActivity.startChatActivity(mActivity, String.valueOf(tsHlepers.get(0).getUid()),
//                            EaseConstant.CHATTYPE_SINGLE);
//                }
//                break;
             /*
              草稿箱
              */
            case R.id.bt_draft_box:
                startActivity(new Intent(mActivity, DraftBoxActivity.class));
                break;
            case R.id.bt_setting:
                startActivity(new Intent(mActivity, SettingsActivity.class));
                break;
            /**
             * 认证
             */
            case R.id.bt_certification:
                // 弹窗选择个人或者机构，被驳回也只能重新申请哦 (*^__^*)
                if (mUserCertificationInfo != null
                        && mUserCertificationInfo.getId() != 0
                        && mUserCertificationInfo.getStatus() != UserCertificationInfo.CertifyStatusEnum.REJECTED.value) {
                    Intent intentToDetail = new Intent(mActivity, CertificationDetailActivity.class);
                    Bundle bundleData = new Bundle();
                    if (mUserCertificationInfo.getCertification_name().equals(SendCertificationBean.USER)) {
                        // 跳转个人认证
                        bundleData.putInt(BUNDLE_DETAIL_TYPE, 0);
                    } else {
                        // 跳转企业认证
                        bundleData.putInt(BUNDLE_DETAIL_TYPE, 1);
                    }
                    bundleData.putParcelable(BUNDLE_DETAIL_DATA, mUserCertificationInfo);
                    intentToDetail.putExtra(BUNDLE_DETAIL_TYPE, bundleData);
                    startActivity(intentToDetail);
                } else {
                    initCertificationTypePop();
                }
                break;
            case R.id.bt_my_qa:
                // 我的问答
                startActivity(new Intent(mActivity, MyQuestionActivity.class));
                break;
            case R.id.bt_my_group:
                // 我的社区
                startActivity(new Intent(mActivity, MyCircleContainerActivity.class));
                break;
            case R.id.ll_friends_container:
                // 我的朋友
//                startActivity(new Intent(mActivity, MyFriendsListActivity.class));
                MyFriendsListActivity.startMyFriendsListActivity(getContext(), true);
                break;
            case R.id./*bt_wallet*/ll_digital_wallet_container:
                //数字资产
                MyCurrencyActivity.startMyCurrencyActivity(getContext());
                //ToastUtils.showToast(mActivity,"该功能暂未开放~");
                break;
            case R.id.bt_my_invite:
                startActivity(InviteShareActivity.newIntent(mActivity));
                break;
            case R.id.bt_download:
                //应用推荐
                CustomWEBActivity.startToWEBActivity(getContext(), ApiConfig.URL_USE_RECOMMEND);
                break;
            case R.id.bt_my_activity://我的活动

                break;
            default:
        }
    }

    @Override
    public void setUserInfo(UserInfoBean userInfoBean) {
        //btMineIntegration.setLeftText(getString(R.string.my_integration_name, mPresenter.getGoldName()));
        mTvIntegration.setText(mPresenter.getGoldName());
        if (userInfoBean == null) {
            return;
        }
        if (mUserInfoBean == null) {
            // 设置用户头像
            ImageUtils.loadCircleUserHeadPic(userInfoBean, mIvHeadIcon);
        } else {
            boolean imageAvatarIsChanged = userInfoBean.getAvatar() != null && (mUserInfoBean.getAvatar() == null || !userInfoBean.getAvatar()
                    .equals(mUserInfoBean.getAvatar()));
            boolean verifiedIsChanged = userInfoBean.getVerified() != null && userInfoBean.getVerified().getType() != null && (mUserInfoBean
                    .getVerified() == null ||
                    !userInfoBean.getVerified().getType().equals(mUserInfoBean
                            .getVerified()
                            .getType()));
            if (imageAvatarIsChanged || verifiedIsChanged) {
                // 设置用户头像
                ImageUtils.loadCircleUserHeadPic(userInfoBean, mIvHeadIcon);
            }
        }
        // 设置用户名
        mTvUserName.setText(userInfoBean.getName());
        // 设置简介
        mTvUserSignature.setText(TextUtils.isEmpty(userInfoBean.getIntro()) ? getString(R.string.intro_default) : userInfoBean.getIntro());
        // 设置粉丝数
        String followedCount = String.valueOf(userInfoBean.getExtra().getFollowers_count());
        mTvFansCount.setText(ConvertUtils.numberConvert(Integer.parseInt(followedCount)));
        // 设置关注数
        String followingCount = String.valueOf(userInfoBean.getExtra().getFollowings_count());
        mTvFollowCount.setText(ConvertUtils.numberConvert(Integer.parseInt(followingCount)));
        double myMoney = 0;
        if (userInfoBean.getWallet() != null) {
            myMoney = userInfoBean.getWallet().getBalance();
        }
        /*mBtCash.setRightText(getString(R.string.money_format_with_unit, PayConfig.realCurrencyFen2Yuan(myMoney)
                , ""));
        btMineIntegration.setRightText(String.valueOf(userInfoBean.getFormatCurrencyNum()));*/
        mTvWalletCount.setText(getString(R.string.money_format_with_unit, PayConfig.realCurrencyFen2Yuan(myMoney), ""));
        mTvIntegrationCount.setText(String.valueOf(userInfoBean.getFormatCurrencyNum()));

        this.mUserInfoBean = userInfoBean;
        // 设置好友数
        String friendsCount = String.valueOf(userInfoBean.getFriends_count());
        mTvFriendsCount.setText(friendsCount);

        String balance = "0.00";
        if (null != mUserInfoBean.getBcwallet() && !TextUtils.isEmpty(mUserInfoBean.getBcwallet().getBalance()))
            balance = mUserInfoBean.getBcwallet().getBalance();
        mTvDigitalWalletCount.setText("¥ " + balance);

        if (null != this.mUserInfoBean.getCurrency() &&
                !this.mUserInfoBean.getCurrency().isIs_sweet() &&
                null == mIntegralRedPacketDialog) {
            mPresenter.requestIntegralRedPacketNum();
        }
    }

    @Override
    public void setIntegralNum(String str) {
        mIntegralRedPacketDialog = new IntegralRedPacketDialog(mActivity, true, str);
        mIntegralRedPacketDialog.setReceiveIntegralRedPacketListener(() -> mPresenter.requestReceiveIntegralRedPacket());
        mIntegralRedPacketDialog.showDialog();
    }

    @Override
    public void receivedRedPacket(boolean isSuccess) {
        if (mIntegralRedPacketDialog.isShowing())
            mIntegralRedPacketDialog.dismissDialog();
        if (isSuccess)
            mActivity.startActivity(new Intent(mActivity, NewMineIntegrationActivity.class));
        else
            ToastUtils.showToast(mActivity, "领取失败！");
    }


    @Override
    public void setNewFollowTip(int count) {
        mVvFansNewCount.setBadgeCount(Integer.parseInt(ConvertUtils.messageNumberConvert(count)));
    }


    @Override
    public void setNewFriendsTip(int count) {
        mBvFriendsNewCount.setBadgeCount(Integer.parseInt(ConvertUtils.messageNumberConvert(count)));
    }


    @Override
    public void setNewSystemInfo(boolean isShow) {
//        setToolBarRightImage(isShow ? ico_me_message_remind : ico_me_message_normal);
    }

    @Override
    public void updateCertification(UserCertificationInfo data) {
        mBtCertification.setEnabled(true);
        if (data != null && data.getId() != 0) {
            mUserCertificationInfo = data;
            if (data.getStatus() == UserCertificationInfo.CertifyStatusEnum.PASS.value) {
                mBtCertification.setRightText(getString(R.string.certification_state_success));
            } else if (data.getStatus() == UserCertificationInfo.CertifyStatusEnum.REVIEWING.value) {
                mBtCertification.setRightText(getString(R.string.certification_state_ing));
            } else if (data.getStatus() == UserCertificationInfo.CertifyStatusEnum.REJECTED.value) {
                mBtCertification.setRightText(getString(R.string.certification_state_failed));
            }
        } else {
            mBtCertification.setRightText("");
        }
        if (mCertificationWindow != null) {
            mCertificationWindow.dismiss();
        }
    }


    private void initCertificationTypePop() {
        if (mCertificationWindow == null) {
            mCertificationWindow = CertificationTypePopupWindow.Builder()
                    .with(mActivity)
                    .alpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                    .setListener(this)
                    .build();
        }
        mCertificationWindow.show();
    }

    @Override
    public void onTypeSelected(int position) {
        mCertificationWindow.dismiss();
        Intent intent = new Intent(mActivity, CertificationInputActivity.class);
        Bundle bundle = new Bundle();
        if (position == 0) {
            // 跳转个人认证
            bundle.putInt(BUNDLE_TYPE, 0);
        } else {
            // 跳转企业认证
            bundle.putInt(BUNDLE_TYPE, 1);
        }
        intent.putExtra(BUNDLE_CERTIFICATION_TYPE, bundle);
        startActivity(intent);
    }

}
