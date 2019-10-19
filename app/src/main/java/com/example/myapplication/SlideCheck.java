package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SlideCheck extends AppCompatActivity {

    Animation slideUpAnimation, slideDownAnimation;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_check);
        TextView mTextview = (TextView) findViewById(R.id.slidetext);

        final Animation in = new AlphaAnimation(0.0f,1.0f);
        in.setDuration(3000);
        final Animation  out = new AlphaAnimation(1.0f,0.0f);
        out.setDuration(2000);

        Button fadein = (Button) findViewById(R.id.fadebutton);
        fadein.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextview.startAnimation(out);
                mTextview.setVisibility(View.INVISIBLE);
            }
        });
        imageView = findViewById(R.id.slideImage);

        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up_animation);

        slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down_animation);

    }

    public void startSlideUpAnimation(View view) {
        imageView.startAnimation(slideUpAnimation);
    }

    public void startSlideDownAnimation(View view) {
        imageView.startAnimation(slideDownAnimation);
    }
}
