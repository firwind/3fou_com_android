package com.zhiyicx.thinksnsplus.modules.currency.accountbook;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AccountBookListBean;
import com.zhiyicx.thinksnsplus.i.IntentKey;
import com.zhiyicx.thinksnsplus.utils.StringUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

/**
 * author: huwenyong
 * date: 2018/7/23 9:30
 * description:
 * version:
 */

public class AccountBookChildFragment extends TSListFragment<AccountBookChildContract.Presenter,AccountBookListBean>
        implements AccountBookChildContract.View{

    public static AccountBookChildFragment newInstance(String tag){
        AccountBookChildFragment fragment = new AccountBookChildFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IntentKey.SEARCH_TAG,tag);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Inject
    AccountBookChildPresenter mPresenter;

    @Override
    protected void initView(View rootView) {

        DaggerAccountBookChildComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .accountBookChildMoudle(new AccountBookChildMoudle(this))
                .build()
                .inject(this);


        super.initView(rootView);
    }

    @Override
    protected boolean isLayzLoad() {
        return true;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean setStatusbarGrey() {
        return false;
    }

    @Override
    protected int setLeftImg() {
        return 0;
    }

    //调整状态栏颜色
    @Override
    protected int setToolBarBackgroud() {
        return R.color.themeColor;
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return super.getItemDecoration();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日HH点mm分");
        int verifyColor = getResources().getColor(R.color.state_currency_verify);
        int successColor = getResources().getColor(R.color.state_currency_success);

        int rechargeColor = getResources().getColor(R.color.state_currency_record_recharge);
        int withdrawColor = getResources().getColor(R.color.state_currency_record_withdraw);
        int exchangeColor = getResources().getColor(R.color.state_currency_exchange);

        CommonAdapter<AccountBookListBean> mAdapter = new CommonAdapter<AccountBookListBean>(getContext(),
                R.layout.item_account_book_list,mListDatas) {
            @Override
            protected void convert(ViewHolder holder, AccountBookListBean accountBookListBean, int position) {

                holder.getTextView(R.id.tv_num).setText("数量："+accountBookListBean.getCurrency_num()+"ETH");
                holder.getTextView(R.id.tv_cost_num).setText("实际扣除："+accountBookListBean.getCurrency_service_num()+"ETH");
                holder.getTextView(R.id.tv_service_num).setText("手续费："+accountBookListBean.getCurrency_service_num()+"ETH");
                holder.getTextView(R.id.tv_record_time).setText(format.format(new Date(accountBookListBean.getTime())));

                String address = "地址："+accountBookListBean.getAddress().address;
                String tag = "地址标签："+accountBookListBean.getAddress().tag;
                holder.getTextView(R.id.tv_address).setText(StringUtils.getColorfulString(address,3,
                        address.length()-1, Color.BLACK));
                holder.getTextView(R.id.tv_address_tag).setText(StringUtils.getColorfulString(tag,5,tag.length(),Color.BLACK));

                if(accountBookListBean.getOp_type() == 0){
                    //提币
                    holder.getTextView(R.id.tv_record).setTextColor(withdrawColor);
                    holder.getTextView(R.id.tv_record_time).setTextColor(withdrawColor);
                    holder.getTextView(R.id.tv_record).setText("提币记录");
                }else if(accountBookListBean.getOp_type() == 1){
                    //充币
                    holder.getTextView(R.id.tv_record).setTextColor(rechargeColor);
                    holder.getTextView(R.id.tv_record_time).setTextColor(rechargeColor);
                    holder.getTextView(R.id.tv_record).setText("充币记录");
                }else {
                    //兑币
                    holder.getTextView(R.id.tv_record).setTextColor(exchangeColor);
                    holder.getTextView(R.id.tv_record_time).setTextColor(exchangeColor);
                    holder.getTextView(R.id.tv_record).setText("兑币记录");
                }

                //审核中
                if(accountBookListBean.getState() == 0){
                    holder.getTextView(R.id.tv_state).setText("审");
                    holder.getTextView(R.id.tv_state_desc).setText("等待审核");
                    holder.getTextView(R.id.tv_state).setTextColor(verifyColor);
                    holder.getTextView(R.id.tv_state_desc).setTextColor(verifyColor);
                }else {//操作成功


                    String desc = "未知状态";
                    switch (accountBookListBean.getOp_type()){
                        case 0:
                            desc = "提币成功";
                            break;
                        case 1:
                            desc = "充币成功";
                            break;
                        case 2:
                            desc = "兑币成功";
                            break;
                    }
                    holder.getTextView(R.id.tv_state).setText("成");
                    holder.getTextView(R.id.tv_state_desc).setText(desc);

                    holder.getTextView(R.id.tv_state).setTextColor(successColor);
                    holder.getTextView(R.id.tv_state_desc).setTextColor(successColor);
                    if(accountBookListBean.getOp_type() == 2){
                        holder.getTextView(R.id.tv_state).setText("兑");
                        holder.getTextView(R.id.tv_state).setTextColor(withdrawColor);
                        holder.getTextView(R.id.tv_state_desc).setTextColor(withdrawColor);
                    }
                }
            }
        };
        return mAdapter;
    }



}
