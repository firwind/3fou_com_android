package com.zhiyicx.thinksnsplus.modules.wallet.integration.mine.newIntegration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import rx.functions.Action1;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.baseproject.widget.popwindow.CenterInfoPopWindow;
import com.zhiyicx.common.BuildConfig;
import com.zhiyicx.common.config.ConstantConfig;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.IntegrationRuleBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.integration.IntegrationBean;
import com.zhiyicx.thinksnsplus.data.beans.integration.IntegrationConfigBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailAdvertHeader;
import com.zhiyicx.thinksnsplus.modules.home.common.invite.InviteShareActivity;
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.CustomWEBActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.integration.detail.IntegrationDetailActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.integration.detail.IntegrationDetailListFragment;
import com.zhiyicx.thinksnsplus.modules.wallet.integration.recharge.IntegrationRechargeActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.integration.recharge.IntegrationRechargeFragment;
import com.zhiyicx.thinksnsplus.modules.wallet.integration.withdrawal.IntegrationWithdrawalsActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.integration.withdrawal.IntegrationWithdrawalsFragment;
import com.zhiyicx.thinksnsplus.modules.wallet.rule.WalletRuleActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.rule.WalletRuleFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_INTEGRATION_RECHARGE;

/**
 * author: huwenyong
 * date: 2018/8/3 16:23
 * description:
 * version:
 */

public class NewMineIntegrationFragment extends TSListFragment<NewMineIntegrationContract.Presenter, IntegrationRuleBean>
        implements NewMineIntegrationContract.View {

    TextView mTvUnit;

    TextView mTvUnitDay;

    TextView mTvMineMoney;

    TextView mTvMineMondyDay;

    TextView mTvDigDay;

    CombinationButton mBtReCharge;

    CombinationButton mBtWithdraw;

    CombinationButton mBtIntegrationShop;

    TextView mTvDigHead;

    @BindView(R.id.tv_recharge_and_withdraw_rule)
    TextView mTvReChargeAndWithdrawRule;

    @BindView(R.id.tv_toolbar_center)
    TextView mTvToolbarCenter;

    @BindView(R.id.tv_toolbar_left)
    TextView mTvToolbarLeft;

    @BindView(R.id.tv_toolbar_right)
    TextView mTvToolbarRight;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    /**
     * 充值提示规则选择弹框
     */
    private CenterInfoPopWindow mRulePop = null;
    private DynamicDetailAdvertHeader mDynamicDetailAdvertHeader;

    private String mGoldName = "";


    public static NewMineIntegrationFragment newInstance() {

        NewMineIntegrationFragment fragment = new NewMineIntegrationFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    protected boolean isRefreshEnable() {
        return false;
    }

    @Override
    protected boolean isLoadingMoreEnable() {
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
    protected boolean setStatusbarGrey() {
        return false;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_mine_integration_new;
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.themeColor;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);

        View view = LayoutInflater.from(mActivity).inflate(R.layout.view_mine_integration_header,null);
        mHeaderAndFooterWrapper.addHeaderView(view);
        mTvUnit = (TextView) view.findViewById(R.id.tv_account_unit);
        mTvMineMoney = (TextView) view.findViewById(R.id.tv_mine_money);
        mBtReCharge = (CombinationButton) view.findViewById(R.id.bt_recharge);
        mBtWithdraw = (CombinationButton) view.findViewById(R.id.bt_withdraw);
        mBtIntegrationShop = (CombinationButton) view.findViewById(R.id.bt_integration_shop);
        //mTvReChargeAndWithdrawRule = (TextView) view.findViewById(R.id.tv_recharge_and_withdraw_rule);
        mTvDigHead = (TextView) view.findViewById(R.id.tv_dig_head);
        mTvUnitDay = (TextView) view.findViewById(R.id.tv_account_unit_day);
        mTvMineMondyDay = (TextView) view.findViewById(R.id.tv_mine_money_day);
        mTvDigDay = (TextView) view.findViewById(R.id.tv_dig_day);

        mIvRefresh = (ImageView) mRootView.findViewById(R.id.iv_refresh);
        mToolbar.setBackgroundResource(R.color.themeColor);
        mTvToolbarCenter.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
        mGoldName = mPresenter.getGoldName();
        mTvToolbarCenter.setText(getString(R.string.my_integration_name, mGoldName));
        mTvToolbarRight.setText(getString(R.string.detail));
        mTvUnit.setText(getString(R.string.current_integraiton_format, mGoldName));
        mTvUnitDay.setText(getString(R.string.current_day_integration_format, mGoldName));
        mTvToolbarLeft.setCompoundDrawables(UIUtils.getCompoundDrawables(mActivity, R.mipmap.topbar_back_white),
                null, null, null);
        initListener();
        initAdvert(mActivity,view, mPresenter.getIntegrationAdvert());
        mSystemConfigBean = mPresenter.getSystemConfigBean();
        if (mSystemConfigBean == null || mSystemConfigBean.getCurrencyRecharge() == null || !mSystemConfigBean.getCurrencyRecharge().isOpen()) {
            mBtReCharge.setVisibility(View.GONE);
        } else {
            mBtReCharge.setVisibility(View.VISIBLE);
        }
        if (mSystemConfigBean == null || mSystemConfigBean.getCurrencyCash() == null || !mSystemConfigBean.getCurrencyCash().isOpen()) {
            mBtWithdraw.setVisibility(View.GONE);
        } else {
            mBtWithdraw.setVisibility(View.VISIBLE);
        }

        mBtReCharge.setLeftText(getString(R.string.recharge_integration_foramt, mGoldName));
        mBtWithdraw.setLeftText(getString(R.string.withdraw_integration_foramt, mGoldName));
        mBtIntegrationShop.setLeftText(getString(R.string.integration_shop_foramt, mGoldName));
        mTvReChargeAndWithdrawRule.setText(getString(R.string.integration_rule_format, mGoldName));
    }

    @Override
    protected void initData() {
        mPresenter.requestNetData(0L,false);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.updateUserInfo();
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<IntegrationRuleBean> data, boolean isLoadMore) {
        if(null != data && data.size() > 0)
            mTvDigHead.setVisibility(View.VISIBLE);
        super.onNetResponseSuccess(data, isLoadMore);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        String goldName = mPresenter.getGoldName();
        CommonAdapter<IntegrationRuleBean> mAdapter = new CommonAdapter<IntegrationRuleBean>(mActivity,
                R.layout.item_new_integration_rule, mListDatas) {
            @Override
            protected void convert(ViewHolder holder, IntegrationRuleBean integrationRuleBean, int position) {
                ImageUtils.loadImageDefault(holder.getImageViwe(R.id.iv_icon),integrationRuleBean.getIcon());
                holder.getTextView(R.id.tv_rule).setText(integrationRuleBean.getTitle()+"+送"+integrationRuleBean.getValue()+goldName);
                holder.getTextView(R.id.tv_rule_desc).setText(integrationRuleBean.getDec());
                holder.getTextView(R.id.tv_rule_desc).setVisibility(integrationRuleBean.isExpand()? View.VISIBLE:View.GONE);
                holder.getView(R.id.iv_expand).setRotation(integrationRuleBean.isExpand()?180:0);
                holder.getTextView(R.id.tv_invite).setText(!TextUtils.isEmpty(integrationRuleBean.getStr()) &&
                        integrationRuleBean.getStr().contains("register")?"立即邀请":"暂未开放");
                holder.getView(R.id.iv_expand).setOnClickListener(v -> {
                    integrationRuleBean.setExpand(!integrationRuleBean.isExpand());
                    NewMineIntegrationFragment.this.refreshData();
                });
                holder.getTextView(R.id.tv_invite).setOnClickListener(v -> {
                    if(holder.getTextView(R.id.tv_invite).getText().equals("立即邀请"))
                        startActivity(InviteShareActivity.newIntent(mActivity));
                });

            }
        };

        return mAdapter;
    }

    private void initAdvert(Context context,View rootView, List<RealAdvertListBean> adverts) {
        rootView.findViewById(R.id.ll_advert_tag).setVisibility(View.GONE);

        if (!BuildConfig.USE_ADVERT || adverts == null || adverts.isEmpty()) {
            rootView.findViewById(R.id.ll_advert).setVisibility(View.GONE);
            return;
        }
        mDynamicDetailAdvertHeader = new DynamicDetailAdvertHeader(context, rootView.findViewById(R.id.ll_advert));
        mDynamicDetailAdvertHeader.setAdverts(adverts);
        mDynamicDetailAdvertHeader.setOnItemClickListener((v, position, url) -> {
            toAdvert(context, adverts.get(position).getAdvertFormat().getImage().getLink(), adverts.get(position).getTitle());
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mPresenter.checkIsNeedTipPop()) {
            mRootView.post(() -> mPresenter.checkIntegrationConfig(NewMineIntegrationPresenter.TAG_SHOWRULE_POP, false));
        }
    }


    private void initListener() {
        // 明细
        RxView.clicks(mTvToolbarRight)
                .throttleFirst(ConstantConfig.JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    mPresenter.checkIntegrationConfig(NewMineIntegrationPresenter.TAG_DETAIL, true);
                });
        // 充值积分
        RxView.clicks(mBtReCharge)
                .throttleFirst(ConstantConfig.JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> mPresenter.checkIntegrationConfig(NewMineIntegrationPresenter.TAG_RECHARGE, true));

        // 提取积分
        RxView.clicks(mBtWithdraw)
                .throttleFirst(ConstantConfig.JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> mPresenter.checkIntegrationConfig(NewMineIntegrationPresenter.TAG_WITHDRAW, true));

        // 积分商城
        RxView.clicks(mBtIntegrationShop)
                .throttleFirst(ConstantConfig.JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    HashMap<String, String> headers = new HashMap();
                    AuthBean authBean = AppApplication.getmCurrentLoginAuth();
                    if (authBean != null) {
                        headers.put("Authorization", authBean.getToken());
                    }
                    CustomWEBActivity.startToWEBActivity(mActivity, headers, ApiConfig.APP_DOMAIN + ApiConfig.URL_INTEGRATION_SHOP,
                            getString(R.string.integration_shop_foramt, mGoldName));

                });
        // 积分规则
        RxView.clicks(mTvReChargeAndWithdrawRule)
                .throttleFirst(ConstantConfig.JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mPresenter.checkIntegrationConfig(NewMineIntegrationPresenter.TAG_SHOWRULE_JUMP, true);
                    }
                });

        RxView.clicks(mTvToolbarLeft)
                .throttleFirst(ConstantConfig.JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> mActivity.finish());
    }

    private void jumpWalletRuleActivity() {
        Intent intent = new Intent(mActivity, WalletRuleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(WalletRuleFragment.BUNDLE_RULE, mPresenter.getTipPopRule());
        bundle.putString(WalletRuleFragment.BUNDLE_TITLE, getString(R.string.integration_rule_format, mGoldName));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 初始化登录选择弹框
     */
    private void showRulePopupWindow() {
        if (mRulePop != null) {
            mRulePop.show();
            return;
        }
        mRulePop = CenterInfoPopWindow.builder()
                .titleStr(getString(R.string.integration_rule_format, mGoldName))
                .desStr(mPresenter.getTipPopRule())
                .item1Str(getString(R.string.get_it))
                .item1Color(R.color.themeColor)
                .isOutsideTouch(true)
                .isFocus(true)
                .animationStyle(R.style.style_actionPopupAnimation)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(mActivity)
                .buildCenterPopWindowItem1ClickListener(() -> mRulePop.hide())
                .parentView(getView())
                .build();
        mRulePop.show();
    }

    @Override
    public void updateBalance(IntegrationBean balance) {
        mTvMineMoney.setText(String.valueOf(balance == null ? 0 : balance.getSum()));
        mTvMineMondyDay.setText(String.valueOf(balance == null ? 0 : balance.getToday_total()));
        int day = 0;
        if(null != balance){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                long create = format.parse(balance.getCreated_at()).getTime();
                day = (int)((System.currentTimeMillis()-create)/1000/60/60/24);
            } catch (ParseException e) {
                //
            }
        }
        mTvDigDay.setText(String.format("已挖矿%s天，继续努力",day));
    }

    @Override
    public void handleLoading(boolean isShow) {
        if (isShow) {
            showLeftTopLoading();
        } else {
            hideLeftTopLoading();
        }
    }

    @Override
    public void integrationConfigCallBack(@NotNull IntegrationConfigBean configBean, int tag) {

        Bundle bundle = new Bundle();
        switch (tag) {

            case NewMineIntegrationPresenter.TAG_DETAIL:
                bundle.putSerializable(IntegrationDetailListFragment.BUNDLE_INTEGRATION_CONFIG, configBean);
                jumpActivity(bundle, IntegrationDetailActivity.class);
                break;

            case NewMineIntegrationPresenter.TAG_RECHARGE:
                bundle.putSerializable(IntegrationRechargeFragment.BUNDLE_DATA, configBean);
                jumpActivity(bundle, IntegrationRechargeActivity.class);

                break;

            case NewMineIntegrationPresenter.TAG_WITHDRAW:
                bundle.putSerializable("data", configBean);
                jumpActivity(bundle, IntegrationWithdrawalsActivity.class);
                break;

            case NewMineIntegrationPresenter.TAG_SHOWRULE_POP:
                showRulePopupWindow();
                break;

            case NewMineIntegrationPresenter.TAG_SHOWRULE_JUMP:
                jumpWalletRuleActivity();
                break;

        }

    }


    private void jumpActivity(Bundle bundle, Class cls) {
        try {
            Intent to = new Intent(mActivity, cls);
            to.putExtras(bundle);
            startActivity(to);
        } catch (Exception e) {
            showSnackErrorMessage(getString(R.string.data_too_large));
        }

    }

    /**
     * jump  to  advert
     *
     * @param context
     * @param link
     * @param title
     */
    private void toAdvert(Context context, String link, String title) {
        CustomWEBActivity.startToWEBActivity(context, link, title);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dismissPop(mRulePop);
    }


    @Subscriber(tag = EVENT_INTEGRATION_RECHARGE, mode = ThreadMode.MAIN)
    void onRechargeSuccessUpdate(String result) {
        initData();
    }

}
