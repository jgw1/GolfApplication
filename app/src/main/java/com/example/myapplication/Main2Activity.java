package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.NewAlbum_Total.MediaListActivity;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_custom_video_player);
        String path = getIntent().getStringExtra("data");
        VideoView videoview = (VideoView)findViewById(R.id.fullscreenVideo);
        videoview.setVideoPath(path);
        MediaController mediaController = new MediaController(this);
        videoview.setMediaController(mediaController);
        videoview.start();



        videoview.setOnCompletionListener(mediaPlayer -> onBackPressed());
    }

    @Override
    public void onBackPressed(){
        setContentView(R.layout.activity_main);
        Intent in=new Intent(this, MediaListActivity.class);
        startActivity(in);
        finish();
    }
}
