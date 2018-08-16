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
import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;

public class VerifyFriendsFragment extends TSFragment<VerifyFriendsContract.Presenter> implements VerifyFriendsContract.View{

    public static VerifyFriendsFragment getInstance(Bundle bundle){
        VerifyFriendsFragment friendsFragment = new VerifyFriendsFragment();
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

}
