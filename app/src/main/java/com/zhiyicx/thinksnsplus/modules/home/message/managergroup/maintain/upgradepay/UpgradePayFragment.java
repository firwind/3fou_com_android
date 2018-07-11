package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.maintain.upgradepay;
/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/7/9
 * 描  述：
 * 版  权: 九曲互动
 */

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSFragment;

import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.common.utils.recycleviewdecoration.GridDecoration;
import com.zhiyicx.common.widget.NoPullRecycleView;
import com.zhiyicx.imsdk.utils.common.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UpgradeTypeBean;
import com.zhiyicx.thinksnsplus.utils.StringUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.zhiyicx.thinksnsplus.modules.home.message.managergroup.maintain.upgradepay.UpgradePayActivity.GRADE_ID;
import static com.zhiyicx.thinksnsplus.modules.home.message.managergroup.maintain.upgradepay.UpgradePayActivity.UPGRADE_TYPE;
import static com.zhiyicx.tspay.TSPayClient.CHANNEL_ALIPAY_V2;
import static com.zhiyicx.tspay.TSPayClient.CHANNEL_WXPAY_V2;

public class UpgradePayFragment extends TSFragment<UpgradePayContract.Presenter> implements UpgradePayContract.View {
    private static final int DEFAULT_COLUMN = 3;
    @BindView(R.id.rv_money_list)
    NoPullRecycleView mMoneyList;
    @BindView(R.id.tv_pay_money)
    TextView mPayMoney;
    @BindView(R.id.cb_wechat_pay)
    CheckBox mWechatPay;
    @BindView(R.id.ll_wechat_pay)
    LinearLayout llWechatPay;
    @BindView(R.id.cb_ali_pay)
    CheckBox mAliPay;
    @BindView(R.id.ll_ali_pay)
    LinearLayout llAliPay;
    @BindView(R.id.cb_balance_pay)
    CheckBox mBalancePay;
    @BindView(R.id.ll_balance_pay)
    LinearLayout llBalancePay;
    @BindView(R.id.cb_agree_agreement)
    CheckBox mAgreeAgreement;
    Unbinder unbinder;
    CheckBox[] checkBoxes = new CheckBox[3];
    private CommonAdapter mCommonAdapter;
    private UpgradeTypeBean mUpgradeTypeBean;
    private String mGroupId;
    private String upgradeType;
    private String payType;
    private double mPayPrice;
    private int mPayMonth;
    public static final UpgradePayFragment newInstance(Bundle bundle) {
        UpgradePayFragment fragment = new UpgradePayFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        mBalancePay.setChecked(true);
        checkBoxes[0] = mWechatPay;
        checkBoxes[1] = mAliPay;
        checkBoxes[2] = mBalancePay;

    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.selector_pay_way);
    }

    @Override
    protected int setToolBarBackgroud() {
        return super.setToolBarBackgroud();
    }

    private void initRv() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), DEFAULT_COLUMN);
        mMoneyList.setLayoutManager(layoutManager);
        mMoneyList.setHasFixedSize(true);
        mMoneyList.addItemDecoration(new GridDecoration(getResources().getDimensionPixelOffset(R.dimen.spacing_mid), getResources()
                .getDimensionPixelOffset(R.dimen.spacing_mid)));
        mCommonAdapter = new CommonAdapter<UpgradeTypeBean.ZhekouDataBean>(getContext(), R.layout.item_upgrade_combo, mUpgradeTypeBean.getZhekou_data()) {

            @Override
            protected void convert(ViewHolder holder, UpgradeTypeBean.ZhekouDataBean comboBean, int position) {
                StringBuffer priceBf = new StringBuffer();
                StringBuffer durationBf = new StringBuffer();
                int money = (int) (mUpgradeTypeBean.getPrice()*comboBean.getDiscount());
                priceBf.append("￥").append(money).append("/月");
                durationBf.append(comboBean.getFewmouths()).append("个月");
                TextView textView =  holder.getView(R.id.tv_price);
                textView.setText(StringUtils.setStringFontSize(priceBf.toString(), 1, priceBf.toString().indexOf("/"), DeviceUtils.dpToPixel(getContext(), 28f)));
                holder.setText(R.id.tv_combo_duration,durationBf.toString());
                if (comboBean.isSelector()){
                    holder.setTextColor(R.id.tv_price,mContext.getResources().getColor(R.color.themeColor));
                    holder.setTextColor(R.id.tv_combo_duration,mContext.getResources().getColor(R.color.themeColor));
                    mPayPrice = mUpgradeTypeBean.getPrice()*comboBean.getDiscount()*comboBean.getFewmouths();
                    mPayMonth = comboBean.getFewmouths();
                    mPayMoney.setText("￥ "+(mUpgradeTypeBean.getPrice()*comboBean.getDiscount()*comboBean.getFewmouths()));
                }else {
                    holder.setTextColor(R.id.tv_price,mContext.getResources().getColor(R.color.black_deep));
                    holder.setTextColor(R.id.tv_combo_duration,mContext.getResources().getColor(R.color.black_deep));
                }
            }
        };
        mCommonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                List<UpgradeTypeBean.ZhekouDataBean> comboBeans = (List<UpgradeTypeBean.ZhekouDataBean>) mCommonAdapter.getDatas();
                for (UpgradeTypeBean.ZhekouDataBean comboBean : comboBeans){
                    if (comboBeans.indexOf(comboBean) == position){
                        comboBean.setSelector(true);
//                        mPayMoney.setText("￥ "+(mUpgradeTypeBean.getPrice()*comboBean.getDiscount()*comboBean.getFewmouths()));

                    }else {
                        comboBean.setSelector(false);
                    }
                }
                mCommonAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mMoneyList.setAdapter(mCommonAdapter);
    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            mUpgradeTypeBean = getArguments().getParcelable(UPGRADE_TYPE);
            mGroupId = getArguments().getString(GRADE_ID);
        }
        mUpgradeTypeBean.getZhekou_data().get(0).setSelector(true);//默认选择第一个为选中目标
        initRv();
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_upgrade_pay;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ll_wechat_pay, R.id.ll_ali_pay, R.id.ll_balance_pay, R.id.tv_agree_agreement, R.id.bt_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_wechat_pay:
                selectorPayType(0);
                payType = CHANNEL_WXPAY_V2;
                break;
            case R.id.ll_ali_pay:
                selectorPayType(1);
                payType = CHANNEL_ALIPAY_V2;
                break;
            case R.id.ll_balance_pay:
                selectorPayType(2);
                break;
            case R.id.tv_agree_agreement:
                break;
            case R.id.bt_pay:
//                mPresenter.getPayStr(mGroupId,String.valueOf(mUpgradeTypeBean.getClause_id()),payType, PayConfig.realCurrencyYuan2Fen(mPayPrice),mPayMonth);
                mPresenter.getPayStr(mGroupId,String.valueOf(mUpgradeTypeBean.getClause_id()),payType, 1,mPayMonth);
                break;
        }
    }

    public void selectorPayType(int p) {
        for (int i = 0; i < checkBoxes.length; i++) {
            if (i == p) {
                checkBoxes[i].setChecked(true);
            } else {
                checkBoxes[i].setChecked(false);
            }
        }
    }

    @Override
    public double getMoney() {
        return 0;
    }
}
