package com.tym.shortvideo.recodrender;

/**
 * @author Jliuer
 * @Date 18/04/28 9:32
 * @Email Jliuer@aliyun.com
 * @Description 录制实时FPS计算类
 */
public class FrameRateMeter {

    // 获取消息
    public static final int MSG_GAIN_FPS = 0x100;

    private static final long TIMETRAVEL = 1;
    private static final long TIMETRAVEL_MS = TIMETRAVEL * 1000;
    private static final long TIMETRAVEL_MAX_DIVIDE = 2 * TIMETRAVEL_MS;

    private int mTimes;
    private float mCurrentFps;
    private long mUpdateTime;

    public FrameRateMeter() {
        mTimes = 0;
        mCurrentFps = 0;
        mUpdateTime = 0;
    }

    /**
     * 计算绘制帧数据
     */
    public void drawFrameCount() {
        long currentTime = System.currentTimeMillis();
        if (mUpdateTime == 0) {
            mUpdateTime = currentTime;
        }
        if ((currentTime - mUpdateTime) > TIMETRAVEL_MS) {
            mCurrentFps = ((float) mTimes / (currentTime - mUpdateTime)) * 1000.0f;
            mUpdateTime = currentTime;
            mTimes = 0;
        }
        mTimes++;
    }

    /**
     * 获取FPS
     * @return
     */
    public float getFPS() {
        if ((System.currentTimeMillis() - mUpdateTime) > TIMETRAVEL_MAX_DIVIDE) {
            return 0;
        } else {
            return mCurrentFps;
        }
    }
}
