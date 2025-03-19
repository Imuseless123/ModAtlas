package com.example.modatlas.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;

public class OrbitMenuView extends View {
    private static final int ITEM_COUNT = 6;
    private float angleOffset = 90;
    private GestureDetectorCompat gestureDetector;
    private RectF[] buttonBounds;
    private boolean isDragging = false;


    public OrbitMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gestureDetector = new GestureDetectorCompat(context, new GestureListener());
        buttonBounds = new RectF[ITEM_COUNT];

        setClickable(true);  // Ensures it gets touch events
        setFocusable(true);
        setFocusableInTouchMode(true);
        gestureDetector.setIsLongpressEnabled(true);
//        snapToClosestItem();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        int radiusX = getWidth() / 3; // Horizontal stretch (oval)
        int radiusY = getHeight() / 4; // Vertical stretch (oval)

        for (int i = 0; i < ITEM_COUNT; i++) {
            double angle = Math.toRadians((360 / ITEM_COUNT) * i + angleOffset);
            float x = centerX + (float) (radiusX * Math.cos(angle));
            float y = centerY + (float) (radiusY * Math.sin(angle));

            // Button size depends on Y position (biggest at bottom, smallest at top)
            float size = 60 + 120 * (1 + (float) Math.sin(angle)); // Range: 60 -> 120

            // Load and scale image
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(
                    "menu_button_" + (i + 1), "drawable", getContext().getPackageName()));

            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) size, (int) size, false);

            // Draw image
            canvas.drawBitmap(scaledBitmap, x - size / 2, y - size / 2, null);

            // Store button bounds for touch detection
            buttonBounds[i] = new RectF(x - size / 2, y - size / 2, x + size / 2, y + size / 2);
        }
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.v("Touch", "Action: " + event.getAction());
        boolean result = gestureDetector.onTouchEvent(event);

        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (!isDragging) {
                for (int i = 0; i < ITEM_COUNT; i++) {
                    if (buttonBounds[i] != null && buttonBounds[i].contains(event.getX(), event.getY())) {
                        Log.v("Button Clicked", "Item " + (i + 1));
                    }
                }
            }

            snapToClosestItem();
        }

        return result || super.onTouchEvent(event);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//            Log.v("Scroll", "distanceX: " + distanceX + ", distanceY: " + distanceY);
            isDragging = true;
            angleOffset += distanceX / 5; // Adjust sensitivity
            invalidate(); // Redraw the view
            return true;
        }

        @Override
        public boolean onDown(@NonNull MotionEvent e) {
            isDragging = false;
            return true;
        }
    }

    private void snapToClosestItem() {
        float closestAngle = 0;
        float minDifference = Float.MAX_VALUE;

        // Find the closest item to 270°
        for (int i = 0; i < ITEM_COUNT; i++) {
            float itemAngle = (360 / ITEM_COUNT) * i + angleOffset;
            itemAngle = (itemAngle + 360) % 360; // Keep within 0-360

            float diff = Math.abs(itemAngle - 270);
            if (diff < minDifference) {
                minDifference = diff;
                closestAngle = itemAngle;
            }
        }

        // Calculate the required rotation adjustment
        float adjustment = 270 - closestAngle;
        animateRotation(adjustment);
    }
    private void animateRotation(float adjustment) {
        ValueAnimator animator = ValueAnimator.ofFloat(angleOffset, angleOffset + adjustment);
        animator.setDuration(300); // Animation duration in milliseconds
        animator.setInterpolator(new DecelerateInterpolator()); // Smooth slowdown effect

        animator.addUpdateListener(animation -> {
            angleOffset = (float) animation.getAnimatedValue();
            invalidate(); // Redraw view
        });

        animator.start();
    }
}
