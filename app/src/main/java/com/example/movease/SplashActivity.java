package com.example.movease;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView truck = findViewById(R.id.ivTruck);
        ImageView houseLeft = findViewById(R.id.ivHouseLeft);
        ImageView houseRight = findViewById(R.id.ivHouseRight);

        // Wait for layout to finish, then calculate the distance
        ViewTreeObserver observer = houseRight.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Remove listener to avoid multiple calls
                houseRight.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                // Get the X coordinates of the right edge of the left house and left edge of the right house
                float leftHouseRightEdge = houseLeft.getX() + houseLeft.getWidth();
                float rightHouseLeftEdge = houseRight.getX();

                // Distance the truck must travel = from its current X to align with right house
                float distance = rightHouseLeftEdge - truck.getX() - truck.getWidth() + 30; // small gap adjustment

                // Animate truck translation
                ObjectAnimator animator = ObjectAnimator.ofFloat(truck, "translationX", 0f, distance);
                animator.setDuration(2000);
                animator.setInterpolator(new LinearInterpolator());
                animator.setRepeatCount(ObjectAnimator.INFINITE);
                animator.setRepeatMode(ObjectAnimator.RESTART);
                animator.start();
            }
        });

        // After 5 seconds, go to login
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }, 5000);
    }
}