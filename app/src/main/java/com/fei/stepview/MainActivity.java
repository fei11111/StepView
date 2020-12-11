package com.fei.stepview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final StepView stepView = findViewById(R.id.step);

        stepView.setMaxProgress(3000);
        stepView.setCurrentProgress(0);
        ValueAnimator valueAnimator = ObjectAnimator.ofFloat(0, 3000);
        valueAnimator.setDuration(3000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                stepView.setCurrentProgress((int) value);
            }
        });
        valueAnimator.start();

    }
}