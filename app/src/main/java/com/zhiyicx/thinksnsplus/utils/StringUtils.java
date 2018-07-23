package com.zhiyicx.thinksnsplus.utils;
/*
 * 文件名: 字符串工具类
 * 创建者：zhangl
 * 时  间：2018/7/2
 * 描  述：
 * 版  权: 九曲互动
 */

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    /**
     * 判断是否含有特殊字符
     *
     * @param str
     * @return true为包含，false为不包含
     */
    public static boolean isSpecialChar(String str) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * 设置字符串字体大小
     * @param str
     * @return
     */
    public static Spannable setStringFontSize(String str,int start,int end,float fontSize){
        Spannable spanString = new SpannableString(str);
        AbsoluteSizeSpan span = new AbsoluteSizeSpan((int) fontSize);
        spanString.setSpan(span, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spanString;
    }

    /**
     * 设置字符串颜色
     * @param str
     * @param start
     * @param end
     * @param color
     * @return
     */
    public static SpannableString getColorfulString(String str,int start,int end,int color){
        SpannableString spanString = new SpannableString(str);
        ForegroundColorSpan span = new ForegroundColorSpan(color);
        spanString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }

}
