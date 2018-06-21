package com.tym.shortvideo.mediacodec.jni;

/**
 * @author Jliuer
 * @Date 18/04/28 9:26
 * @Email Jliuer@aliyun.com
 * @Description 混合音频
 */
public class AudioJniUtils {


    static {
        System.loadLibrary("native-lib");
    }
    public static native byte[] audioMix(byte[] sourceA,byte[] sourceB,byte[] dst,float firstVol , float secondVol);
}
