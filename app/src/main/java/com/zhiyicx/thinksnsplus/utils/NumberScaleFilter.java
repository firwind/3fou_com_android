package com.zhiyicx.thinksnsplus.utils;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * author: huwenyong
 * date: 2018/8/11 15:15
 * description:  保留scale位小数
 * version:
 */

public class NumberScaleFilter implements InputFilter{

    private int scale = 0;//保留scale位小数

    public NumberScaleFilter(int scale){
        this.scale = scale;
    }

    /**
     * @param source 新输入的字符串
     * @param start  新输入的字符串起始下标，一般为0
     * @param end    新输入的字符串终点下标，一般为source长度-1
     * @param dest   输入之前文本框内容
     * @param dstart 原内容起始坐标，一般为0
     * @param dend   原内容终点坐标，一般为dest长度-1
     * @return 输入内容
     */
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        if(source.length() >= 1){
            //首先如果第一位是0，那么第二位就不能是0或者其他数字,只能是.
            if(dest.length() == 1 && dest.toString().equals("0")){
                source = ".";
            }

            //原字符串中包含 .     1.2（保留1位），再输入
            if(dest.toString().contains(".")){
                int index = dest.toString().indexOf(".");
                if( dend-index > scale )
                    source = "";
            }
            return dest.subSequence(dstart,dend)+source.toString();
        }else
            return null;
    }
}
