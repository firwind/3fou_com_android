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
import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.i.IntentKey;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingPrivacyFragment extends TSFragment<SettingPrivacyContract.Presenter> implements SettingPrivacyContract.View {

    @BindView(R.id.bt_allow_anyone)
    CombinationButton mAllowAnyone;
    @BindView(R.id.bt_need_verify)
    CombinationButton mNeedVerify;
    @BindView(R.id.bt_refuse_anyone)
    CombinationButton mRefuseAnyone;

    CombinationButton[] combinationButtons = new CombinationButton[3];
    private int mSetState;

    public static SettingPrivacyFragment getInstance(ChatGroupBean chatGroupBean) {
        SettingPrivacyFragment fragment = new SettingPrivacyFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(IntentKey.GROUP_INFO,chatGroupBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    //    pricacy_icon
    @Override
    protected void initView(View rootView) {
        combinationButtons[0] = mAllowAnyone;
        combinationButtons[1] = mNeedVerify;
        combinationButtons[2] = mRefuseAnyone;

        if(null == getChatGroupBean()){//单人加好友设置
            mSetState = mPresenter.getCurrentUser().getFriends_set();
            combinationButtons[mSetState].setRightImage(R.mipmap.pricacy_icon);
        }else {//群聊设置

            mAllowAnyone.setLeftText("允许任何人加群");
            mNeedVerify.setLeftText("需要身份验证");
            mRefuseAnyone.setLeftText("拒绝任何人加群");

            mSetState = getChatGroupBean().getPrivacy();
            combinationButtons[mSetState].setRightImage(R.mipmap.pricacy_icon);
        }

    }

    private void selectorItem(int p) {

        for (int i = 0; i < combinationButtons.length; i++) {
            if (i == p) {
                mPresenter.settingAddFriendOrGroupWay(p);
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
        return null == getChatGroupBean()?getString(R.string.add_friends_setting):"加群方式";
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

    @Override
    public void settingSuccess(int state) {

        combinationButtons[state].setRightImage(R.mipmap.pricacy_icon);
    }

    @Override
    public ChatGroupBean getChatGroupBean() {
        return null == getArguments()?null:getArguments().getParcelable(IntentKey.GROUP_INFO);
    }

}
