package com.abner.ming.base.viewpage.transformer;

import android.support.v4.view.ViewCompat;
import android.view.View;

public class DepthPageTransformer extends BGAPageTransformer {
    private float mMinScale = 0.8f;

    public DepthPageTransformer() {
    }

    public DepthPageTransformer(float minScale) {
        setMinScale(minScale);
    }

    @Override
    public void handleInvisiblePage(View view, float position) {
        ViewCompat.setAlpha(view, 0);
    }

    @Override
    public void handleLeftPage(View view, float position) {
        ViewCompat.setAlpha(view, 1);
        ViewCompat.setTranslationX(view, 0);
        ViewCompat.setScaleX(view, 1);
        ViewCompat.setScaleY(view, 1);
    }

    @Override
    public void handleRightPage(View view, float position) {
        ViewCompat.setAlpha(view, 1 - position);
        ViewCompat.setTranslationX(view, -view.getWidth() * position);
        float scale = mMinScale + (1 - mMinScale) * (1 - position);
        ViewCompat.setScaleX(view, scale);
        ViewCompat.setScaleY(view, scale);
    }

    public void setMinScale(float minScale) {
        if (minScale >= 0.6f && minScale <= 1.0f) {
            mMinScale = minScale;
        }
    }

}