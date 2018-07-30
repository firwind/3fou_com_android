package com.zhiyicx.thinksnsplus.modules.currency.accountbook;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
    private int mTag;
    public static AccountBookChildFragment newInstance(int tag){
        AccountBookChildFragment fragment = new AccountBookChildFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(IntentKey.SEARCH_TAG,tag);
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
    protected void initData() {
        super.initData();
        if (getArguments()!=null){
            mTag = getArguments().getInt(IntentKey.SEARCH_TAG);
        }
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
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
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
    public int getBookTag() {
        return mTag;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日HH点mm分");
        int verifyColor = getResources().getColor(R.color.state_currency_verify);
        int successColor = getResources().getColor(R.color.state_currency_success);
        int failColor = getResources().getColor(R.color.state_currency_fail);

        int rechargeColor = getResources().getColor(R.color.state_currency_record_recharge);
        int withdrawColor = getResources().getColor(R.color.state_currency_record_withdraw);
        int exchangeColor = getResources().getColor(R.color.state_currency_exchange);

        CommonAdapter<AccountBookListBean> mAdapter = new CommonAdapter<AccountBookListBean>(getContext(),
                R.layout.item_account_book_list,mListDatas) {
            @Override
            protected void convert(ViewHolder holder, AccountBookListBean accountBookListBean, int position) {

                holder.getTextView(R.id.tv_num).setText("数量："+accountBookListBean.number+accountBookListBean.currency);
                holder.getTextView(R.id.tv_cost_num).setText("实际扣除："+accountBookListBean.service_charge+accountBookListBean.currency);
                holder.getTextView(R.id.tv_service_num).setText("手续费："+accountBookListBean.service_charge+accountBookListBean.currency);
                holder.getTextView(R.id.tv_record_time).setText(format.format(new Date(accountBookListBean.updated_time*1000)));

                String address = "地址："+ (TextUtils.isEmpty(accountBookListBean.toaddress)?"":accountBookListBean.toaddress);
                String tag = "地址标签："+(TextUtils.isEmpty(accountBookListBean.mark)?"":accountBookListBean.mark );
                holder.getTextView(R.id.tv_address).setText(StringUtils.getColorfulString(address,3,
                        address.length(), Color.BLACK));
                holder.getTextView(R.id.tv_address_tag).setText(StringUtils.getColorfulString(tag,5,tag.length(),Color.BLACK));

                //1：入账、-1：支出、0：兑换',
                if(accountBookListBean.type == -1){
                    //提币
                    holder.getTextView(R.id.tv_record).setTextColor(withdrawColor);
                    holder.getTextView(R.id.tv_record_time).setTextColor(withdrawColor);
                    holder.getTextView(R.id.tv_record).setText("提币记录");
                }else if(accountBookListBean.type == 1){
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

                //0: 等待，1：审核，2：成功，-1: 失败',
                if(accountBookListBean.state == -1){

                    holder.getTextView(R.id.tv_state).setText("败");
                    holder.getTextView(R.id.tv_state_desc).setText("失败");

                    holder.getTextView(R.id.tv_state).setTextColor(failColor);
                    holder.getTextView(R.id.tv_state_desc).setTextColor(failColor);

                    holder.getTextView(R.id.tv_num).setTextColor(failColor);
                    holder.getTextView(R.id.tv_cost_num).setTextColor(failColor);
                    holder.getTextView(R.id.tv_service_num).setTextColor(failColor);
                    holder.getTextView(R.id.tv_record_time).setTextColor(failColor);
                    holder.getTextView(R.id.tv_address).setTextColor(failColor);
                    holder.getTextView(R.id.tv_address_tag).setTextColor(failColor);
                    holder.getTextView(R.id.tv_record).setTextColor(failColor);
                    holder.getTextView(R.id.tv_record_time).setTextColor(failColor);


                } else if(accountBookListBean.state == 0){

                    holder.getTextView(R.id.tv_state).setText("等");
                    holder.getTextView(R.id.tv_state_desc).setText("正在等待");
                    holder.getTextView(R.id.tv_state).setTextColor(verifyColor);
                    holder.getTextView(R.id.tv_state_desc).setTextColor(verifyColor);

                } else if(accountBookListBean.state == 1){
                    holder.getTextView(R.id.tv_state).setText("审");
                    holder.getTextView(R.id.tv_state_desc).setText("等待审核");
                    holder.getTextView(R.id.tv_state).setTextColor(verifyColor);
                    holder.getTextView(R.id.tv_state_desc).setTextColor(verifyColor);
                }else if(accountBookListBean.state == 2){//操作成功
                    String desc = "未知状态";
                    switch (accountBookListBean.type){
                        case -1:
                            desc = "提币成功";
                            break;
                        case 1:
                            desc = "充币成功";
                            break;
                        case 0:
                            desc = "兑币成功";
                            break;
                    }
                    holder.getTextView(R.id.tv_state).setText("成");
                    holder.getTextView(R.id.tv_state_desc).setText(desc);

                    holder.getTextView(R.id.tv_state).setTextColor(successColor);
                    holder.getTextView(R.id.tv_state_desc).setTextColor(successColor);
                    if(accountBookListBean.type == 0){
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
