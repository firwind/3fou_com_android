package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.maintain.upgradepay;
/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/7/11
 * 描  述：
 * 版  权: 九曲互动
 */

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;

public class PaySuccessActivity extends TSActivity{
    @Override
    protected Fragment getFragment() {
        return PaySuccessFragment.newInstance() ;
    }

    @Override
    protected void componentInject() {

    }
    public static void startPaySuccessActivity(Context context){
        Intent intent = new Intent(context,PaySuccessActivity.class);
        context.startActivity(intent);
    }
}
