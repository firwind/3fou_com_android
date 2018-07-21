package com.zhiyicx.thinksnsplus.modules.currency.address;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hyphenate.util.DensityUtil;
import com.lwy.righttopmenu.MenuItem;
import com.lwy.righttopmenu.RightTopMenu;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyAddress;
import com.zhiyicx.thinksnsplus.i.IntentKey;
import com.zhiyicx.thinksnsplus.widget.dialog.AddCurrencyAddressDialog;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * author: huwenyong
 * date: 2018/7/20 9:55
 * description: 钱包管理
 * version:
 */

public class CurrencyAddressFragment extends TSListFragment<CurrencyAddressContract.Presenter,CurrencyAddress>
        implements CurrencyAddressContract.View{

    private AddCurrencyAddressDialog mAddAddressDialog;

    public static CurrencyAddressFragment newInstance(boolean isSelect){

        CurrencyAddressFragment fragment = new CurrencyAddressFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(IntentKey.IS_SELECT,isSelect);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        setCenterTextColor(R.color.white);

    }

    @Override
    protected boolean isLayzLoad() {
        return true;
    }

    @Override
    protected boolean setStatusbarGrey() {
        return false;
    }

    @Override
    protected int setLeftImg() {
        return R.mipmap.topbar_back_white;
    }

    @Override
    protected int setRightImg() {
        return R.mipmap.icon_add_white;
    }

    @Override
    protected void setRightClick() {
        showAddCurrencyAddressDialog();
    }

    @Override
    protected String setCenterTitle() {
        return "常用地址";
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.themeColor;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        CommonAdapter<CurrencyAddress> adapter = new CommonAdapter<CurrencyAddress>(mActivity, R.layout.item_currency_address, mListDatas) {
            @Override
            protected void convert(ViewHolder holder, CurrencyAddress currencyAddress, int position) {
                holder.getTextView(R.id.tv_tag).setText(currencyAddress.tag);
                holder.getTextView(R.id.tv_address).setText(currencyAddress.address);
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if(getArguments().getBoolean(IntentKey.IS_SELECT,false)){
                    Intent intent = mActivity.getIntent();
                    intent.putExtra(IntentKey.RESULT_CURRENCY_ADDRESS,mListDatas.get(position));
                    mActivity.setResult(Activity.RESULT_OK,intent);
                    mActivity.finish();
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        return adapter;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == mActivity.RESULT_OK && requestCode == IntentKey.REQ_CODE_GET_SCAN_RESULT
                && null != data){
            if(null != mAddAddressDialog && mAddAddressDialog.isShowing()){
                ((EditText)mAddAddressDialog.getView(R.id.et_address)).setText(data.getStringExtra(IntentKey.RESULT_SCAN));
            }
        }
    }

    /**
     * 添加钱包地址弹窗
     */
    private void showAddCurrencyAddressDialog(){

        if(null == mAddAddressDialog){
            mAddAddressDialog = new AddCurrencyAddressDialog(mActivity,false);
            mAddAddressDialog.setOnAddressConfirmedListener((address, tag) -> {
                mListDatas.add(new CurrencyAddress(tag,address));
                refreshData();
            });
        }
        if(!mAddAddressDialog.isShowing())
            mAddAddressDialog.showDialog();
    }

}
