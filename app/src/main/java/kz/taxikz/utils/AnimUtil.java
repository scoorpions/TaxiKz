package kz.taxikz.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import kz.taxikz.taxi4.R;
import kz.taxikz.ui.widget.view.animation.Techniques;
import kz.taxikz.ui.widget.view.animation.YoYo;

/**
 *  Created by Andrey on 29.05.2016.
 */
public class AnimUtil {
    public static void fadeInRightAnimation(View view) {
        YoYo.with(Techniques.Flash).duration(1000).playOn(view);
    }

    public static void scaleUp(View view, int duration, boolean fillAfter) {
        Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.scale_up);
        animation.setDuration((long) duration);
        animation.setFillAfter(fillAfter);
        view.startAnimation(animation);
    }

    public static void scaleDown(View view, int duration, boolean fillAfter) {
        Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.scale_down);
        animation.setDuration((long) duration);
        animation.setFillAfter(fillAfter);
        view.startAnimation(animation);
    }
}
