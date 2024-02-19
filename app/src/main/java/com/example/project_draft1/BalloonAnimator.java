package com.example.project_draft1;

import android.view.View;
import android.view.animation.ScaleAnimation;

public class BalloonAnimator {
    private final View balloonView;

    public BalloonAnimator(View balloonView) {
        this.balloonView = balloonView;
    }

    public void deflateBalloon() {
        // Implement balloon deflation animation
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.0f, 1.0f, 0.0f);
        scaleAnimation.setDuration(1000); // Set the duration as needed
        balloonView.startAnimation(scaleAnimation);
    }

    public void inflateBalloon(float newSize) {
        // Implement balloon inflation animation
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.0f, 1.0f, newSize);
        scaleAnimation.setDuration(500); // Set the duration as needed
        balloonView.startAnimation(scaleAnimation);
    }
}
