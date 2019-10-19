package com.example.myapplication.Slider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.myapplication.R;

public class SliderAdapter extends PagerAdapter {

    private int[] images = {R.drawable.golfball1,R.drawable.golfcart,R.drawable.golfballwithdents};
    private LayoutInflater inflater;
    private Context context;
    private String[] texts = {"촬영 후 영상의 동작을 분석해보세요.","화면에 보이는 가이드에 몸을 맞춰주세요.","촬영 전, 측면과 정면을 선택해주세요."};
    public SliderAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.slider,container,false);
        ImageView imageView = v.findViewById(R.id.slider_imageView);
        TextView textView = (TextView)v.findViewById(R.id.slider_textView);
        imageView.setImageResource(images[position]);

        textView.setText(texts[position]);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.invalidate();
    }
}
