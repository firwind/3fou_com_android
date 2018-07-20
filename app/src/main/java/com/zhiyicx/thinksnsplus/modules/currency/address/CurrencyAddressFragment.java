package com.zhiyicx.thinksnsplus.modules.currency.address;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hyphenate.util.DensityUtil;
import com.lwy.righttopmenu.MenuItem;
import com.lwy.righttopmenu.RightTopMenu;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyAddress;
import com.zhiyicx.thinksnsplus.i.IntentKey;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * author: huwenyong
 * date: 2018/7/20 9:55
 * description:
 * version:
 */

public class CurrencyAddressFragment extends TSListFragment<CurrencyAddressContract.Presenter,CurrencyAddress>
        implements CurrencyAddressContract.View{


    private RightTopMenu mRightTopMenu;


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
        //super.setRightClick();
        showRightTopMenu();

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
        return new CommonAdapter<CurrencyAddress>(mActivity,R.layout.item_currency_address,mListDatas) {
            @Override
            protected void convert(ViewHolder holder, CurrencyAddress currencyAddress, int position) {

            }
        };
    }


    /**
     * 右上角弹窗
     */
    private void showRightTopMenu(){
        if(null == mRightTopMenu){
            List<MenuItem> list = new ArrayList<>();
            list.add(new MenuItem(R.mipmap.icon_scan_gray,"扫一扫"));
            list.add(new MenuItem(R.mipmap.icon_withdraw_gray,"转出记录"));
            mRightTopMenu = new RightTopMenu.Builder(mActivity)
                    .dimBackground(true) //背景变暗，默认为true
                    .needAnimationStyle(true) //显示动画，默认为true
                    .animationStyle(R.style.RTM_ANIM_STYLE)  //默认为R.style.RTM_ANIM_STYLE
                    .windowWidth(DensityUtil.dip2px(mActivity,160))
                    .menuItems(list)
                    .onMenuItemClickListener(position -> {

                    })
                    .build();
        }
        mRightTopMenu.showAsDropDown(mToolbarRight,DensityUtil.dip2px(mActivity,15),0);
    }

}
