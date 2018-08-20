package com.zhiyicx.thinksnsplus.modules.home.mine.friends.verify;
/*
 * 文件名：
 * 创建者：zl
 * 时  间：2018/8/16 0016
 * 描  述：
 * 版  权：九曲互动
 * 
 */

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.trycatch.mysnackbar.Prompt;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.i.IntentKey;

import butterknife.BindView;

public class VerifyFriendOrGroupFragment extends TSFragment<VerifyFriendOrGroupContract.Presenter> implements VerifyFriendOrGroupContract.View{

    @BindView(R.id.et_verify_friends_info)
    EditText mEtVerifyFriendsInfo;

    public static VerifyFriendOrGroupFragment getInstance(String user_id,String groupId){
        VerifyFriendOrGroupFragment friendsFragment = new VerifyFriendOrGroupFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IntentKey.USER_ID,user_id);
        bundle.putString(IntentKey.GROUP_ID,groupId);
        friendsFragment.setArguments(bundle);
        return friendsFragment;
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setRightClick() {
        super.setRightClick();

        if(TextUtils.isEmpty(mEtVerifyFriendsInfo.getText().toString())){
            showSnackErrorMessage("请输入验证信息");
            return;
        }

        mPresenter.addFriendOrGroup(isGroupVerify()?((ChatGroupBean)getArguments().getParcelable(IntentKey.GROUP_INFO)).getId():
                getArguments().getString(IntentKey.USER_ID),mEtVerifyFriendsInfo.getText().toString());

    }

    @Override
    protected void snackViewDismissWhenTimeOut(Prompt prompt) {
        super.snackViewDismissWhenTimeOut(prompt);
        if(prompt == Prompt.SUCCESS){
            mActivity.finish();
        }
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_verify_friends;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.tv_add_friends);
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.chat_send_location);
    }

    @Override
    public boolean isGroupVerify() {
        return !TextUtils.isEmpty(getArguments().getString(IntentKey.GROUP_ID));
    }
}
