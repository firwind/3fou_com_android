package com.zhiyicx.thinksnsplus.modules.shortvideo.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

import org.simple.eventbus.EventBus;

import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_NETSTATE_CHANGE;

/**
 * Created by jungle on 16/5/21.
 * com.zhiyicx.zhibo.app.receiver
 * zhibo_android
 * email:335891510@qq.com
 */
public class NetChangeReceiver extends BroadcastReceiver {

    private boolean hasWifi;

    @Override
    public void onReceive(Context context, Intent intent) {
        //检测API是不是小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            hasWifi = wifiNetworkInfo.isConnected();
        } else {
            //API大于23时使用下面的方式进行网络监听
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取所有网络连接的信息
            Network[] networks = connMgr.getAllNetworks();
            for (Network network : networks) {
                //获取ConnectivityManager对象对应的NetworkInfo对象
                NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
                if ("WIFI".equals(networkInfo.getTypeName())) {
                    hasWifi = networkInfo.isConnected();
                } else {
                    hasWifi = false;
                }
            }
        }
        EventBus.getDefault().post(hasWifi, EVENT_NETSTATE_CHANGE);
    }
}
