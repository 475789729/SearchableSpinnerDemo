package com.testcity.liuyao.searchablespinner;

import android.animation.Animator;

public abstract class SimpleAnimatorListener implements Animator.AnimatorListener {


    @Override
    public abstract void onAnimationStart(Animator animation);

    @Override
    public abstract void onAnimationEnd(Animator animation);

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
