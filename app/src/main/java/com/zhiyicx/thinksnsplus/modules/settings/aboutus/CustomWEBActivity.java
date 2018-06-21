package com.zhiyicx.thinksnsplus.modules.settings.aboutus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.guide.GuideActivity;
import com.zhiyicx.thinksnsplus.modules.guide.GuideFragment_v2;
import com.zhiyicx.thinksnsplus.modules.register.RegisterPresenter;

import java.util.HashMap;

/**
 * @Describe 关于我们等网页
 * @Author zl
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */

public class CustomWEBActivity extends TSActivity<RegisterPresenter, CustomWEBFragment> {

    private static String flag = "";

    /**
     * 广告跳转
     *
     * @param context
     * @param args
     */
    public static void startToWEBActivity(Context context, String... args) {
        startToWEBActivity(context, null, args);
    }

    /**
     * 网页跳转
     *
     * @param context
     * @param headers
     * @param args    args[0] url ; args[1] title
     */
    @SuppressLint("LogNotUsed")
    public static void startToWEBActivity(Context context, HashMap<String, String> headers, String... args) {
        flag = "";
        Intent intent = new Intent(context, CustomWEBActivity.class);
        Bundle bundle = new Bundle();
        Log.e("zl","args--"+ args[0]);
        if (args.length > 0) {
            try {
                String url = args[0];
                // 广告需要 token
                url = url.replace("__token__", AppApplication.getTOKEN().replace(" ", "|"));
                bundle.putString(CustomWEBFragment.BUNDLE_PARAMS_WEB_URL, url);
                bundle.putString(CustomWEBFragment.BUNDLE_PARAMS_WEB_TITLE, args[1]);
                if (headers != null) {
                    bundle.putSerializable(CustomWEBFragment.BUNDLE_PARAMS_WEB_HEADERS, headers);
                }
                flag = args[2];
            } catch (Exception e) {
                e.printStackTrace();
            }
            intent.putExtras(bundle);
        }
        context.startActivity(intent);

    }

    /**
     * 跳转浏览器网页
     *
     * @param context
     * @param url
     */
    public static void startToOutWEBActivity(Context context, String url) {
        try {
            // 广告需要 token
            url = url.replace("__token__", AppApplication.getTOKEN());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        String urlRege = "^http://[\\s\\S]+";
        if (!url.matches(MarkdownConfig.NETSITE_FORMAT) && !url.matches(urlRege)) {
            url = url.replace(MarkdownConfig.SCHEME_ZHIYI, "");
            url = MarkdownConfig.SCHEME_HTTP + url;
        }
        intent.setData(Uri.parse(url));
        if (!DeviceUtils.hasPreferredApplication(context, intent)) {
            intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
        }
        context.startActivity(intent);
    }

    @Override
    protected void componentInject() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (GuideFragment_v2.ADVERT.equals(flag)) {
            finish();
            startActivity(new Intent(this, GuideActivity.class));
        }
    }

    @Override
    protected CustomWEBFragment getFragment() {
        return CustomWEBFragment.newInstance(getIntent().getExtras());
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }

    /**
     * 覆盖系统的回退键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mContanierFragment.onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
