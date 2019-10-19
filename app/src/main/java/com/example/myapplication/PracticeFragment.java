package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.ArrayList;

public class PracticeFragment extends AppCompatActivity {
    ViewPager pager;
    Spinner spinner;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;
    ImageButton NewAlbumDay,NewAlbumTotal,NewAlbumMap,NewAlbumFavorite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_fragment);
        pager = findViewById(R.id.pager);
        spinner = findViewById(R.id.PracticeSpinner);
        NewAlbumDay = findViewById(R.id.NewAlbumDay);
        NewAlbumTotal = findViewById(R.id.NewAlbumTotal);
        NewAlbumMap = findViewById(R.id.NewAlbumMap);
        NewAlbumFavorite = findViewById(R.id.NewAlbumFavorite);

        pager.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        pager.setCurrentItem(0);

        arrayList = new ArrayList<>();
        arrayList.add("전체");
        arrayList.add("일별");
        arrayList.add("좋아요");

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                arrayList);


        spinner.setAdapter(arrayAdapter);
        spinner.bringToFront();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                pager.setCurrentItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        View.OnClickListener movePageListener = view -> {
            int tag = (int)view.getTag();
            pager.setCurrentItem(tag);
        };

        NewAlbumDay.setOnClickListener(movePageListener);
        NewAlbumDay.setTag(0);
        NewAlbumTotal.setOnClickListener(movePageListener);
        NewAlbumTotal.setTag(1);
        NewAlbumMap.setOnClickListener(movePageListener);
        NewAlbumMap.setTag(2);
        NewAlbumFavorite.setOnClickListener(movePageListener);
        NewAlbumFavorite.setTag(3);
    }

    private class pagerAdapter extends FragmentStatePagerAdapter{
        public pagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position)
            {
                case 0:
                    return new Fragment1();
                case 1:
                    return new Fragment2();
                case 2:
                    return new Fragment3();
                case 3:
                    return new Fragment4();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }


}
