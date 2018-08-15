package com.zhiyicx.thinksnsplus.modules.settings.privacy;
/*
 * 文件名：
 * 创建者：zl
 * 时  间：2018/8/14 0014
 * 描  述：
 * 版  权：九曲互动
 * 
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.thinksnsplus.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SettingPricacyFragment extends TSFragment<SettingPricacyContract.Presenter> implements SettingPricacyContract.View {

    @BindView(R.id.bt_allow_anyone)
    CombinationButton mAllowAnyone;
    @BindView(R.id.bt_need_verify)
    CombinationButton mNeedVerify;
    @BindView(R.id.bt_refuse_anyone)
    CombinationButton mRefuseAnyone;

    CombinationButton[] combinationButtons = new CombinationButton[3];
    Unbinder unbinder;

    public static SettingPricacyFragment getInstance(Bundle bundle) {
        SettingPricacyFragment fragment = new SettingPricacyFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    //    pricacy_icon
    @Override
    protected void initView(View rootView) {
        combinationButtons[0] = mAllowAnyone;
        combinationButtons[1] = mNeedVerify;
        combinationButtons[2] = mRefuseAnyone;
        selectorItem(0);
    }

    private void selectorItem(int p) {
        for (int i = 0; i < combinationButtons.length; i++) {
            if (i == p) {
                combinationButtons[p].setRightImage(R.mipmap.pricacy_icon);
            } else {
                combinationButtons[i].setRightImage(0);
            }
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_setting_pricacy;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.add_friends_setting);
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }


    @OnClick({R.id.bt_allow_anyone, R.id.bt_need_verify, R.id.bt_refuse_anyone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_allow_anyone:
                selectorItem(0);
                break;
            case R.id.bt_need_verify:
                selectorItem(1);
                break;
            case R.id.bt_refuse_anyone:
                selectorItem(2);
                break;
        }
    }
}
