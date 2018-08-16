package com.zhiyicx.thinksnsplus.modules.home.mine.friends.verify;
/*
 * 文件名：
 * 创建者：zl
 * 时  间：2018/8/16 0016
 * 描  述：
 * 版  权：九曲互动
 * 
 */

import com.zhiyicx.common.mvp.BasePresenter;

import javax.inject.Inject;

public class VerifyFriendsPresenter extends BasePresenter<VerifyFriendsContract.View> implements VerifyFriendsContract.Presenter{
    @Inject
    public VerifyFriendsPresenter(VerifyFriendsContract.View rootView) {
        super(rootView);
    }
}
