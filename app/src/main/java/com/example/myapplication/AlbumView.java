package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.myapplication.CompareShot.TwoVideoPlayer;
import com.example.myapplication.DB.DatabaseAccess;
import com.example.myapplication.NewAlbum_Total.MediaListActivity;

import java.util.ArrayList;

public class AlbumView extends AppCompatActivity implements View.OnClickListener{
    ImageView imageView;
    String PicturePath;
    ArrayList<Video> videoArrayList;
    LinearLayout top_navigation,bottom_navigation;
    int a;
    ImageButton TrashButton,ImageCompare;
    ToggleButton PictureFavorite;
    private DatabaseAccess databaseAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_view);
        PicturePath = getIntent().getStringExtra("PicturePath");
        videoArrayList = (ArrayList<Video>) getIntent().getSerializableExtra("List");
        ArrayList<Video> videoArrayList;


        this.databaseAccess = DatabaseAccess.getInstance(this);
        imageView = findViewById(R.id.ImagePicture);
        top_navigation = findViewById(R.id.top_navigation);
        bottom_navigation = findViewById(R.id.bottom_navigation);

        TrashButton = findViewById(R.id.TrashButton);
        PictureFavorite = findViewById(R.id.PictureFavorite);
        ImageCompare = findViewById(R.id.ImageCompare);

        TrashButton.setOnClickListener(this);
        ImageCompare.setOnClickListener(this);

        PictureFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    //favorite - Yes로 변환
                    databaseAccess.open();
                    databaseAccess.FavoriteChange(PicturePath,1);
                    databaseAccess.close();
                }else{
                    //favorite - No로 변환
                    databaseAccess.open();
                    databaseAccess.FavoriteChange(PicturePath,1);
                    databaseAccess.close();

                }
            }
        });


        imageView.setOnTouchListener(new OnSwipeTouchListener(AlbumView.this) {

            //오른쪽 방향 스와이프 - 사진 변경
            public void onSwipeRight() {
                TrackChange("RIGHT");
            }

            //왼쪽방향 스와이프 - 사진 변경
            public void onSwipeLeft() {
                TrackChange("LEFT");
            }

            // 한번 터치시 위 아래의 Layout_invisible, 다시 터치시 Visible
            public void onSingleTap(){
                if(top_navigation.getVisibility() == View.INVISIBLE){
                    top_navigation.setVisibility(View.VISIBLE);
                    top_navigation.bringToFront();
                    bottom_navigation.setVisibility(View.VISIBLE);
                    top_navigation.bringToFront();
                }else{
                    top_navigation.setVisibility(View.INVISIBLE);
                    bottom_navigation.setVisibility(View.INVISIBLE);
                }
                Log.d("GWGWGWGWGW","latitude");
            }
            //더블터치 - 재생 & 정지
        });
    }
    private void TrackChange(String Direction) {
        int size = videoArrayList.size();
        for (int i = 0; i < size; i++) {
            if (videoArrayList.get(i).getPath().contains(PicturePath)) {
                if (Direction == "RIGHT") {
                    a = i + 1;
                    if (a == size) {
                        a = 0;
                    }
                } else if (Direction == "LEFT") {
                    a = i - 1;
                    if (a < 0) {
                        a = size - 1;
                    }
                }
            }
        }
        Uri uri = Uri.parse("file:///" + Environment.getExternalStorageDirectory() + "/572/내그림/image_sample.jpg");
        imageView.setImageURI(uri);
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.TrashButton:
                //File Delete -- MemoAPP 참고;
                Log.d("GQGQGQG","TrashButton");
               break;

            case R.id.ImageCompare:
                Intent intent = new Intent(this, TwoVideoPlayer.class);
                startActivity(intent);
                finish();
                Log.d("GQGQGQG","NewAlbumFavorite");
                break;
        }
    }

    @Override
    public void onBackPressed(){
        setContentView(R.layout.activity_main);
        Intent in=new Intent(this, MainActivity.class);
        startActivity(in);
        finish();
    }
}
