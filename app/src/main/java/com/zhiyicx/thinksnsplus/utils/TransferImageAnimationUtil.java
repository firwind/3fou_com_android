package com.zhiyicx.thinksnsplus.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.DrawableProvider;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/21
 * @contact email:450127106@qq.com
 */

public class TransferImageAnimationUtil {
    // 动画持续时间
    public static final int ANIMATION_DURATION = 250;

    /**
     * 退出时的控件缩放处理
     *
     * @param backgroundAnimator 背景动画，alpha值的渐变
     * @param rect               转场动画初始时，由上一个界面传递过来的图片控件属性
     * @param imageView          当前界面要进行缩放的图片控件
     */
    public static void animateClose(android.animation.ObjectAnimator backgroundAnimator, AnimationRectBean rect, final ImageView imageView) {

        // 没有大图退出动画，直接关闭activity
        if (rect == null) {
            imageView.animate().alpha(0);
            backgroundAnimator.start();
            return;
        }
        // 小图rect属性
        final Rect startBounds = rect.scaledBitmapRect;
        // 大图rect属性
        final Rect finalBounds = DrawableProvider.getBitmapRectCloseImageView(imageView);
        // 没有大图退出动画，直接关闭activity
        if (finalBounds == null || startBounds == null) {
            imageView.animate().alpha(0);
            backgroundAnimator.start();
            return;
        }

        float startWScale = (float) startBounds.width() / finalBounds.width();
        float startHScale = (float) startBounds.height() / finalBounds.height();

        int deltaTop = startBounds.top - finalBounds.top;
        int deltaLeft = startBounds.left - finalBounds.left;
        // 设置XY轴心
        imageView.setPivotY((imageView.getHeight() - finalBounds.height()) / 2);
        imageView.setPivotX((imageView.getWidth() - finalBounds.width()) / 2);
        // 位移+缩小
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            imageView.animate()
                    .translationX(deltaLeft)
                    .translationY(deltaTop)
                    .scaleY(startHScale)
                    .scaleX(startWScale)
                    .setDuration(ANIMATION_DURATION)
                    .setInterpolator(new AccelerateDecelerateInterpolator());
        }
        AnimatorSet animationSet = new AnimatorSet();
        animationSet.setDuration(ANIMATION_DURATION);
        animationSet.setInterpolator(new AccelerateDecelerateInterpolator());

        animationSet.playTogether(backgroundAnimator);

        animationSet.start();
    }


    /**
     * 控件进入时的缩放处理
     *
     * @param rect      转场动画初始时，由上一个界面传递过来的图片控件属性
     * @param imageView 当前界面要进行缩放的图片控件
     * @param endAction 在监听ViewTree的同时，需要处理一些其他操作，在新的线程中进行
     */
    public static void startInAnim(final AnimationRectBean rect, final ImageView imageView, final Runnable endAction) {
        if (imageView == null) {
            return;
        }
        imageView.getViewTreeObserver()
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {

                        if (rect == null) {
                            imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                            endAction.run();
                            return true;
                        }

                        final Rect startBounds = new Rect(rect.scaledBitmapRect);
                        final Rect finalBounds =
                                DrawableProvider.getBitmapRectCloseImageView(imageView);

                        if (finalBounds == null) {
                            imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                            endAction.run();
                            return true;
                        }

                        float startScale = (float) finalBounds.width() / startBounds.width();

                        int deltaTop = startBounds.top - finalBounds.top;
                        int deltaLeft = startBounds.left - finalBounds.left;


                        int privotX = (imageView.getWidth() - finalBounds.width()) / 2;
                        int privotY = (imageView.getHeight() - finalBounds.height()) / 2;

                        imageView.setPivotX(privotX);
                        imageView.setPivotY(privotY);
                        int translationY = 0;
                        if (imageView.getHeight() > DeviceUtils.getScreenHeight(imageView.getContext())) {
                            translationY = (imageView.getHeight() - finalBounds.height()) / 2;
                        }
                        System.out.println("translationY = " + translationY);
                        System.out.println("deltaTop = " + deltaTop);
                        imageView.setScaleX(1 / startScale);
                        imageView.setScaleY(1 / startScale);
                        imageView.setTranslationX(deltaLeft);
                        imageView.setTranslationY(deltaTop);

                        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(imageView,
                                PropertyValuesHolder.ofFloat("setPivotX", privotX, privotX),
                                PropertyValuesHolder.ofFloat("setPivotY", privotY, privotY),

                                PropertyValuesHolder.ofFloat("scaleX", 1 / startScale, 1),
                                PropertyValuesHolder.ofFloat("scaleY", 1 / startScale, 1),

                                PropertyValuesHolder.ofFloat("translationX", deltaLeft, 0),
                                PropertyValuesHolder.ofFloat("translationY", deltaTop, 0)
                        ).setDuration(ANIMATION_DURATION);
                        animator.addListener(new com.nineoldandroids.animation.AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                                super.onAnimationEnd(animation);
                                if (endAction != null) {
                                    endAction.run();
                                }
                            }
                        });
                        animator.start();

                        imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                        return true;
                    }
                });
    }

    /**
     * 控件进入时的缩放处理
     *
     * @param rect      转场动画初始时，由上一个界面传递过来的图片控件属性
     * @param imageView 当前界面要进行缩放的图片控件
     */
    public static void startInAnim(final AnimationRectBean rect, final ImageView imageView, final FrameLayout viewGroup, final Runnable endAction) {
        if (imageView == null) {
            return;
        }
        imageView.getViewTreeObserver()
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {

                        if (rect == null) {
                            imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                            return true;
                        }

                        final Rect startBounds = new Rect(rect.scaledBitmapRect);
                        final Rect finalBounds =
                                DrawableProvider.getBitmapRectCloseImageView(imageView);

                        if (finalBounds == null) {
                            imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                            return true;
                        }

                        float startScale = (float) finalBounds.width() / startBounds.width();

                        int deltaTop = startBounds.top - finalBounds.top;
                        int deltaLeft = startBounds.left - finalBounds.left;

                        int pivotX = (imageView.getWidth() - finalBounds.width()) / 2;
                        int pivotY = (imageView.getHeight() - finalBounds.height()) / 2;

                        imageView.setPivotX(pivotX);
                        imageView.setPivotY(pivotY);
                        imageView.setScaleX(1 / startScale);
                        imageView.setScaleY(1 / startScale);
                        imageView.setTranslationX(deltaLeft);
                        imageView.setTranslationY(deltaTop);

                        // 位移+缩小
                        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(imageView,
                                PropertyValuesHolder.ofInt("setPivotX", pivotX, pivotX),
                                PropertyValuesHolder.ofInt("setPivotY", pivotY, pivotY),

                                PropertyValuesHolder.ofFloat("scaleX", 1 / startScale, 1),
                                PropertyValuesHolder.ofFloat("scaleY", 1 / startScale, 1),

                                PropertyValuesHolder.ofFloat("translationX", deltaLeft, 0),
                                PropertyValuesHolder.ofFloat("translationY", deltaTop, 0)
                        ).setDuration(ANIMATION_DURATION);
                        animator.setInterpolator(new AccelerateDecelerateInterpolator());
                        animator.addListener(new com.nineoldandroids.animation.AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                                super.onAnimationEnd(animation);
                                if (endAction != null) {
                                    endAction.run();
                                }
                            }
                        });

                        animator.addUpdateListener(animation -> {
                            float progress = animation.getCurrentPlayTime() / animation.getDuration();
                            progress = Math.round(progress * 100) / 100.0f;
                            progress = progress * 2;
                            int alpha = Math.round(progress * 255);
                            if (alpha > 255) {
                                alpha = 255;
                            }
                            String hex = Integer.toHexString(alpha).toUpperCase();
                            if (hex.length() == 1) {
                                hex = "0" + hex;
                            }
                            String color = "#" + hex + "000000";
                            viewGroup.setBackgroundColor(Color.parseColor(color));

                        });
                        animator.start();

                        imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                        return true;
                    }
                });
    }


    /**
     * 退出时的控件缩放处理
     *
     * @param rect      转场动画初始时，由上一个界面传递过来的图片控件属性
     * @param imageView 当前界面要进行缩放的图片控件
     */
    public static void animateClose(android.animation.ObjectAnimator backgroundAnimator, final FrameLayout viewGroup, AnimationRectBean rect, final ImageView imageView) {

        // 没有大图退出动画，直接关闭activity
        if (rect == null) {
            imageView.animate().alpha(0);
            backgroundAnimator.start();
            return;
        }
        // 小图rect属性
        final Rect startBounds = rect.scaledBitmapRect;
        // 大图rect属性
        final Rect finalBounds = DrawableProvider.getBitmapRectCloseImageView(imageView);
        // 没有大图退出动画，直接关闭activity
        if (finalBounds == null || startBounds == null) {
            imageView.animate().alpha(0);
            backgroundAnimator.start();
            return;
        }

        float startWScale = (float) startBounds.width() / finalBounds.width();
        float startHScale = (float) startBounds.height() / finalBounds.height();

        int deltaTop = startBounds.top - finalBounds.top;
        int deltaLeft = startBounds.left - finalBounds.left;

        int pivotX = (imageView.getWidth() - finalBounds.width()) / 2;
        int pivotY = (imageView.getHeight() - finalBounds.height()) / 2;

        // 设置XY轴心
        imageView.setPivotX(pivotX);
        imageView.setPivotY(pivotY);


        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(imageView,
                PropertyValuesHolder.ofInt("setPivotX", pivotX, pivotX),
                PropertyValuesHolder.ofInt("setPivotY", pivotY, pivotY),

                PropertyValuesHolder.ofFloat("scaleX", 1, startWScale),
                PropertyValuesHolder.ofFloat("scaleY", 1, startHScale),

                PropertyValuesHolder.ofFloat("translationX", 0, deltaLeft),
                PropertyValuesHolder.ofFloat("translationY", 0, deltaTop)
        ).setDuration(ANIMATION_DURATION);

        animator.setInterpolator(new AccelerateDecelerateInterpolator());

//        animator.addUpdateListener(animation -> {
//            float progress = 1 - animation.getCurrentPlayTime() / animation.getDuration();
//            progress = Math.round(progress * 100) / 100.0f;
//            progress =  progress / 2;
//
//            int alpha = Math.round(progress * 255);
//            String hex = Integer.toHexString(alpha).toUpperCase();
//            if (hex.length() == 1) {
//                hex = "0" + hex;
//            }
//
//            String color = "#" + hex + "000000";
//            viewGroup.setBackgroundColor(Color.parseColor(color));
//
//        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {
                super.onAnimationStart(animation);
                String color = "#00000000";
                viewGroup.setBackgroundColor(Color.parseColor(color));
            }
        });
        animator.start();
        backgroundAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        backgroundAnimator.setDuration(ANIMATION_DURATION);
        backgroundAnimator.start();


    }
}
