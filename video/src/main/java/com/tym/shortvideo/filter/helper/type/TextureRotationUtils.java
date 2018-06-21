package com.tym.shortvideo.filter.helper.type;

import android.hardware.Camera;

import com.tym.shortvideo.recodrender.RecordManager;
import com.tym.shortvideo.utils.CameraUtils;
import com.tym.shortvideo.utils.DeviceUtils;
import com.tym.shortvideo.utils.Size;

import java.nio.FloatBuffer;


/**
 * @author Jliuer
 * @Date 18/04/28 9:56
 * @Email Jliuer@aliyun.com
 * @Description 纹理工具
 */
public class TextureRotationUtils {

    // 摄像头是否倒置，主要是应对Nexus 5X (bullhead) 的后置摄像头图像倒置的问题
    private static boolean mBackReverse = false;

    public static final int CoordsPerVertex = 2;

    public static final float CubeVertices[] = {
            -1.0f, -1.0f,  // 0 bottom left
            1.0f, -1.0f,  // 1 bottom right
            -1.0f, 1.0f,  // 2 top left
            1.0f, 1.0f,  // 3 top right
    };

    public static void setRatio_1_1(CameraUtils.Ratio ratio, Size size) {
        boolean is1_1 = ratio.getRatio() == 0.75f;

        /**
         *  16:9 转 1:1
         *  不知为何， 16:9 转 1:1 计算得到的值并不好用，相差了 0.02f
         *
         TextureVertices[0] = 0.0f;
         TextureVertices[1] = 0.880625f;

         TextureVertices[2] = 1.0f;
         TextureVertices[3] = 0.880625f;

         TextureVertices[4] = 0.0f;
         TextureVertices[5] = 0.119375f;

         TextureVertices[6] = 1.0f;
         TextureVertices[7] = 0.119375f;

         4:3 转 1:1

         TextureVertices[0] = 0.0f;
         TextureVertices[1] = 0.0625f;

         TextureVertices[2] = 1.0f;
         TextureVertices[3] = 0.0625f;

         TextureVertices[4] = 0.0f;
         TextureVertices[5] = 0.9375f;

         TextureVertices[6] = 1.0f;
         TextureVertices[7] = 0.9375f;
         */

        if (is1_1) {
            if (ratio == CameraUtils.Ratio.RATIO_4_3_2_1_1) {
                TextureVertices[0] = 0.0f;
                TextureVertices[1] = 0.0625f;

                TextureVertices[2] = 1.0f;
                TextureVertices[3] = 0.0625f;

                TextureVertices[4] = 0.0f;
                TextureVertices[5] = 0.9375f;

                TextureVertices[6] = 1.0f;
                TextureVertices[7] = 0.9375f;
            } else if (ratio == CameraUtils.Ratio.RATIO_16_9_2_1_1) {
                TextureVertices[0] = 0.0f;
                TextureVertices[1] = 0.870625f;

                TextureVertices[2] = 1.0f;
                TextureVertices[3] = 0.870625f;

                TextureVertices[4] = 0.0f;
                TextureVertices[5] = 0.109375f;

                TextureVertices[6] = 1.0f;
                TextureVertices[7] = 0.109375f;
            }
        } else {
            TextureVertices[0] = 0.0f;
            TextureVertices[1] = 0.0f;

            TextureVertices[2] = 1.0f;
            TextureVertices[3] = 0.0f;

            TextureVertices[4] = 0.0f;
            TextureVertices[5] = 1.0f;

            TextureVertices[6] = 1.0f;
            TextureVertices[7] = 1.0f;

            if (ratio == CameraUtils.Ratio.RATIO_16_9) {
                RecordManager.RECORD_WIDTH = DeviceUtils.getScreenWidth();
                RecordManager.RECORD_HEIGHT = DeviceUtils.getScreenHeight();
            } else if (ratio == CameraUtils.Ratio.RATIO_4_3) {
                RecordManager.RECORD_HEIGHT = DeviceUtils.getScreenHeight();
                RecordManager.RECORD_WIDTH = (int)(DeviceUtils.getScreenHeight() * ratio.getRatio());
            }


        }
    }

    public static final float TextureVertices[] = {
            0.0f, 0.0f,     // 0 bottom left
            1.0f, 0.0f,     // 1 bottom right
            0.0f, 0.875f,     // 2 top left
            1.0f, 0.875f      // 3 top right
    };

    public static final float TextureVertices_90[] = {
            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            0.0f, 1.0f,
    };

    public static final float TextureVertices_180[] = {
            1.0f, 1.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f,
    };

    public static final float TextureVertices_270[] = {
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
    };

    public static float[] getTextureVertices() {
        float[] result;
        switch (CameraUtils.getPreviewOrientation()) {
            case 0:
                result = TextureVertices_90;
                break;

            case 90:
                result = TextureVertices;
                break;

            case 180:
                result = TextureVertices_270;
                break;

            case 270:
                result = TextureVertices_180;
                break;

            default:
                result = TextureVertices;
        }
        return result;
    }

    public static FloatBuffer getTextureBuffer() {
        FloatBuffer result;
        switch (CameraUtils.getPreviewOrientation()) {
            case 0:
                if (CameraUtils.getCameraID() == Camera.CameraInfo.CAMERA_FACING_BACK
                        && mBackReverse) {
                    result = GlUtil.createFloatBuffer(TextureRotationUtils.TextureVertices_270);
                } else {
                    result = GlUtil.createFloatBuffer(TextureRotationUtils.TextureVertices_90);
                }
                break;

            case 90:
                if (CameraUtils.getCameraID() == Camera.CameraInfo.CAMERA_FACING_BACK
                        && mBackReverse) {
                    result = GlUtil.createFloatBuffer(TextureRotationUtils.TextureVertices_180);
                } else {
                    result = GlUtil.createFloatBuffer(TextureRotationUtils.TextureVertices);
                }
                break;

            case 180:
                if (CameraUtils.getCameraID() == Camera.CameraInfo.CAMERA_FACING_BACK
                        && mBackReverse) {
                    result = GlUtil.createFloatBuffer(TextureRotationUtils.TextureVertices_90);
                } else {
                    result = GlUtil.createFloatBuffer(TextureRotationUtils.TextureVertices_270);
                }
                break;

            case 270:
                if (CameraUtils.getCameraID() == Camera.CameraInfo.CAMERA_FACING_BACK
                        && mBackReverse) {
                    result = GlUtil.createFloatBuffer(TextureRotationUtils.TextureVertices);
                } else {
                    result = GlUtil.createFloatBuffer(TextureRotationUtils.TextureVertices_180);
                }
                break;

            default:
                if (CameraUtils.getCameraID() == Camera.CameraInfo.CAMERA_FACING_BACK
                        && mBackReverse) {
                    result = GlUtil.createFloatBuffer(TextureRotationUtils.TextureVertices_180);
                } else {
                    result = GlUtil.createFloatBuffer(TextureRotationUtils.TextureVertices);
                }

        }
        return result;
    }

    public static void setBackReverse(boolean reverse) {
        mBackReverse = reverse;
    }
}
