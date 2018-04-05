package cn.supertruth.autoscrolllayout;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/***************************************************************************************************
 *                                  Copyright (C), Nexgo Inc.                                      *
 *                                    http://www.nexgo.cn                                          *
 ***************************************************************************************************
 * usage           : 
 * Version         : 1
 * Author          : Truth
 * Date            : 2018/4/1
 * Modify          : create file
 **************************************************************************************************/
public class AutoScrollLayout extends FrameLayout{
    public AutoScrollLayout(Context context) {
        super(context);
        init();
    }

    public AutoScrollLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoScrollLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private View childView;
    private int during = 5000;
    private void init(){
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        System.out.println("onVisibilityChanged->"+visibility);
    }

    @Override
    public void onViewAdded(View child) {
        childView = child;
        LayoutParams orgLayoutParams = (LayoutParams) child.getLayoutParams();
        LayoutParams layoutParams = new LayoutParams(orgLayoutParams.width, orgLayoutParams.height);
        childView.setLayoutParams(layoutParams);
        super.onViewAdded(child);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        post(startAnimationRunnable);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(anim != null){
            anim.cancel();
        }
    }

    private ValueAnimator anim;
    private Runnable startAnimationRunnable = new Runnable() {
        @Override
        public void run() {
            if(getChildCount() != 1){
                return;
            }

            if(anim != null){
                anim.cancel();
            }

            int childWidth = childView.getMeasuredWidth();
            int parentWidth = getMeasuredWidth();

            anim = ValueAnimator.ofInt(-childWidth, parentWidth);
            anim.setDuration(during);
            anim.setRepeatCount(ValueAnimator.INFINITE);

            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    childView.setX((int)animation.getAnimatedValue());
                }
            });

            anim.start();
        }
    };

    public void setDuring(int during){
        this.during = during;
    }
}
