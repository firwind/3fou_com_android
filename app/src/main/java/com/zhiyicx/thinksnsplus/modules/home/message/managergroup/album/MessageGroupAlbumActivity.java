package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.album;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hyphenate.util.DensityUtil;
import com.orhanobut.logger.Logger;
import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.i.IntentKey;

/**
 * <pre>
 *     @author : huwenyong
 *     @date : 2018/6/22 15:41
 *     desc : 群相册
 *     version : 1.0
 * <pre>
 */

public class MessageGroupAlbumActivity extends TSActivity<MessageGroupAlbumPresenter,MessageGroupAlbumFragment>{

    public static Intent newIntent(Context mContext,String group_id){
        Intent intent = new Intent(mContext,MessageGroupAlbumActivity.class);
        intent.putExtra(IntentKey.GROUP_ID,group_id);
        return intent;
    }

    @Override
    protected MessageGroupAlbumFragment getFragment() {
        return MessageGroupAlbumFragment.newInstance(getIntent().getStringExtra(IntentKey.GROUP_ID));
    }

    @Override
    protected void componentInject() {
        DaggerMessageGroupAlbumComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .messageGroupAlbumPresenterModule(new MessageGroupAlbumPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);


    }

}
