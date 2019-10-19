package com.example.myapplication.Slider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.Slider.PreferenceManager;
import com.example.myapplication.Slider.SliderAdapter;
import com.example.myapplication.R;

public class CustomDialog extends Dialog {

    private LinearLayout dotsLayout;
    private Context context;
    SliderAdapter adapter;
    ViewPager viewPager;
    Button btnskip;
    private TextView[] dots;
    PreferenceManager preferenceManager;

    public CustomDialog(Context context) {
        super(context,android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        getWindow().setFlags(layoutParams.FLAG_FULLSCREEN,layoutParams.FLAG_FULLSCREEN);
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_slider);
        setLayout();

    }

    private void setLayout(){
        btnskip = findViewById(R.id.btnskip);
        dotsLayout = findViewById(R.id.layoutDots);
        viewPager = findViewById(R.id.view);
        adapter = new SliderAdapter(getContext());
        addBottomDots(0);
        viewPager.setAdapter(adapter);
        btnskip.setOnClickListener(view -> launchHomeScreen());
        preferenceManager = new PreferenceManager(getContext());
        ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        };
        viewPager.setOnPageChangeListener(viewPagerPageChangeListener);
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[3];

        int[] colorsActive = getContext().getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getContext().getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getContext());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[0]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[0]);
    }

    private void launchHomeScreen(){
        preferenceManager.setFirstTimeLaunch(false);
        dismiss();
    }

}