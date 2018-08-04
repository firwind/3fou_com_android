package com.zhiyicx.thinksnsplus.modules.wallet.red_packet;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.hyphenate.util.DensityUtil;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.utils.StringUtils;
import com.zhiyicx.thinksnsplus.widget.dialog.HBaseDialog;

/**
 * author: huwenyong
 * date: 2018/8/4 16:25
 * description:
 * version:
 */

public class IntegralRedPacketDialog extends HBaseDialog{

    private ObjectAnimator mRotationAnimator;
    private ReceiveIntegralRedPacketListener mReceiveIntegralRedPacketListener;

    public void setReceiveIntegralRedPacketListener(ReceiveIntegralRedPacketListener mReceiveIntegralRedPacketListener) {
        this.mReceiveIntegralRedPacketListener = mReceiveIntegralRedPacketListener;
    }

    /**
     * 构造方法
     *
     * @param context              上下文
     * @param cancelOnTouchOutside
     */
    public IntegralRedPacketDialog(Activity context, boolean cancelOnTouchOutside,String num) {
        super(context, R.layout.view_integral_red_packet, cancelOnTouchOutside);
        setGravity(Gravity.CENTER);
        getTextView(R.id.tv_red_packet_num)
                .setText(StringUtils.setStringFontSize(num+"新手糖果红包",0,num.length(), DensityUtil.dip2px(mContext,35)));
        getDialog().setOnDismissListener(dialog -> {
            cancleRotationAnim();
        });
    }

    @Override
    public void showDialog() {
        super.showDialog();

        getView(R.id.iv_open).setOnClickListener(v -> {
            ((ImageView)getView(R.id.iv_open)).setImageResource(R.mipmap.icon_money_coin);
            getView(R.id.iv_open).setEnabled(false);
            if(mRotationAnimator == null){
                mRotationAnimator = ObjectAnimator.ofFloat(getView(R.id.iv_open), "rotationY", 0,360);
                mRotationAnimator.setDuration(1000);
                mRotationAnimator.setRepeatCount(-1);
                mRotationAnimator.setInterpolator(new LinearInterpolator());
            }
            mRotationAnimator.start();

            if(null != mReceiveIntegralRedPacketListener)
                mReceiveIntegralRedPacketListener.startReceiveIntegralRedPacket();

        });
    }

    /**
     * 取消动画
     */
    public void cancleRotationAnim(){
        if(null != mRotationAnimator)
            mRotationAnimator.cancel();
    }

    public interface ReceiveIntegralRedPacketListener{
        void startReceiveIntegralRedPacket();
    }

}
