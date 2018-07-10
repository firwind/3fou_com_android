package com.zhiyicx.thinksnsplus.utils.badge;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.zhiyicx.thinksnsplus.modules.guide.GuideActivity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * author: huwenyong
 * date: 2018/7/10 9:19
 * description: 腾讯QQ对各大厂商的适配方案，待测试
 * version:
 */

public class CommonBadgeUtil {

    private static String mLauncherClassName;

    static {

        mLauncherClassName = GuideActivity.class.getName();

    }

    public static void setBadge(Context paramContext, int paramInt) {
        if (Build.MANUFACTURER.equalsIgnoreCase("ZUK")) {
            changeZUKBadge(paramContext, paramInt);
        } else if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
            changeMIBadge(paramContext, paramInt);
        } else if (Build.MANUFACTURER.equalsIgnoreCase("samsung")) {
            setSamsungBadge(paramContext, paramInt);
        } else if (Build.MANUFACTURER.equalsIgnoreCase("huawei")) {
            setHuaweiBadge(paramContext, paramInt);
        } else if (Build.MANUFACTURER.equalsIgnoreCase("OPPO")) {
            changeOPPOBadge(paramContext, paramInt);
        } else if (Build.MANUFACTURER.equalsIgnoreCase("vivo")) {
            changeVivoBadge(paramContext, paramInt);
        }
    }

    /**
     * 适配小米的角标
     * 小米 2018.7.2更新
     * MIUI 6 至 MIUI 10
     * @param count
     * 小米的设置应用角标方式比较有个性，跟其他厂商的不太一样，
     * 是跟Notification绑定在一起的。而且小米系统还有个比较特殊的地方，
     * 如果在应用内直接调用设置角标的方法，设置角标会不生效，
     * 所以只能在应用在后台并且收到推送的情况下进行角标的设置。
     * 另外，即使你设置了角标的显示，只要用户点击应用图标进入到应用内，应用的角标就会自动消失掉，即使应用内还存在新的未读消息。
     * 所以，针对小米机型，建议在收到推送后并且进行notification的时机更新应用角标。
     */
    public static void changeMIUIBadge(Context context, Notification notification, int count,String chatId){

        if(!Build.MANUFACTURER.equalsIgnoreCase("xiaomi"))
            return;

        try {
            NotificationManager mNotificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            Field field = notification.getClass().getDeclaredField("extraNotification");
            Object extraNotification = field.get(notification);
            Method method = extraNotification.getClass().getDeclaredMethod("setMessageCount", int.class);
            method.invoke(extraNotification, count);
            mNotificationManager.notify(chatId,0,notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void changeMIBadge(Context paramContext, int paramInt) {

        //小米的角标跟通知相关联，在通知里边儿做


    }

    private static void changeOPPOBadge(Context paramContext, int paramInt) {
        try {
            Bundle localBundle = new Bundle();
            localBundle.putInt("app_badge_count", paramInt);
            paramContext.getContentResolver().call(Uri.parse("content://com.android.badge/badge"),
                    "setAppBadgeCount", null, localBundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void changeVivoBadge(Context paramContext, int paramInt) {

        try {
            Intent localIntent = new Intent("launcher.action.CHANGE_APPLICATION_NOTIFICATION_NUM");
            localIntent.putExtra("packageName", paramContext.getPackageName());
            localIntent.putExtra("className", mLauncherClassName);
            localIntent.putExtra("notificationNum", paramInt);
            paramContext.sendBroadcast(localIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void changeZUKBadge(Context paramContext, int paramInt) {
        try {
            Bundle localBundle = new Bundle();
            localBundle.putStringArrayList("app_shortcut_custom_id", null);
            localBundle.putInt("app_badge_count", paramInt);
            paramContext.getContentResolver().call(Uri.parse("content://com.android.badge/badge"),
                    "setAppBadgeCount", null, localBundle);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static boolean isSupportBadge() {

        return Build.MANUFACTURER.equalsIgnoreCase("Xiaomi") ||
                Build.MANUFACTURER.equalsIgnoreCase("samsung") ||
                Build.MANUFACTURER.equalsIgnoreCase("huawei") ||
                Build.MANUFACTURER.equalsIgnoreCase("OPPO") ||
                Build.MANUFACTURER.equalsIgnoreCase("vivo") ||
                Build.MANUFACTURER.equalsIgnoreCase("zuk");
    }


    private static void setHuaweiBadge(Context paramContext, int paramInt) {
        try {
            Bundle localBundle = new Bundle();
            localBundle.putString("package", paramContext.getPackageName());
            localBundle.putString("class", mLauncherClassName);
            localBundle.putInt("badgenumber", paramInt);
            paramContext.getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"),
                    "change_badge", null, localBundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setLenovoBadge(Context paramContext, int paramInt) {
        // TODO: 2018/7/10  后续再适配联想的
    }


    private static void setSamsungBadge(Context paramContext, int paramInt) {
        // TODO: 2018/7/10  后续再适配三星的
    }

    private static void setSonyBadge(Context paramContext, int paramInt) {
        // TODO: 2018/7/10  后续再适配索尼的
    }
}

