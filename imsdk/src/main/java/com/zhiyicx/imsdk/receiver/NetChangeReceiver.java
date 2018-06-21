package com.zhiyicx.imsdk.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

import com.zhiyicx.imsdk.service.SocketService;
import com.zhiyicx.imsdk.utils.common.DeviceUtils;

import org.simple.eventbus.EventBus;

/**
 * Created by jungle on 16/5/21.
 * com.zhiyicx.zhibo.app.receiver
 * zhibo_android
 * email:335891510@qq.com
 */
public class NetChangeReceiver extends BroadcastReceiver {
    public static final String EVENT_NETWORK_CONNECTED = "event_network_connected";
    private static final String NETWORK_CHANGE_SATE = "android.net.conn.CONNECTIVITY_CHANGE";
    public static final long TIME_SPACING = 1000;
    private long last_time = System.currentTimeMillis();
    private boolean hasWifi;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (System.currentTimeMillis() - last_time > TIME_SPACING) {
            if (NETWORK_CHANGE_SATE.equals(intent.getAction()) && DeviceUtils.hasInternet(context)) {
                // socket重连
                Intent socketretry = new Intent(SocketService.SOCKET_RETRY_CONNECT);
                context.sendBroadcast(socketretry);
                EventBus.getDefault().post(EVENT_NETWORK_CONNECTED);
            }
        }
        last_time = System.currentTimeMillis();

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

            StringBuilder sb = new StringBuilder();
            for (Network network : networks) {
                //获取ConnectivityManager对象对应的NetworkInfo对象
                NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
                if (networkInfo.getTypeName() != null) {
                    if ("WIFI".equals(networkInfo.getTypeName())) {
                        hasWifi = networkInfo.isConnected();
                    }
                    sb.append(networkInfo.getTypeName() + " connect is " + networkInfo.isConnected());
                }
            }
        }
    }
}
