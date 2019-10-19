package com.example.myapplication.CompareShot;

import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.NewAlbum_Total.MediaListActivity;
import com.example.myapplication.R;
import com.example.myapplication.Video;

import java.io.IOException;
import java.util.ArrayList;

public class TwoVideoPlayer extends AppCompatActivity implements View.OnClickListener{
    TextureView leftVideo, rightVideo;
    ImageView leftVideoPath,rightVideoPath;
    SeekBar videoControlBar;
    ImageView pauseButton;
    Button playRate;
    Button playerAB;
    ImageButton first_rewind,first_forward,second_rewind,second_forward;
    ArrayList<Video> videoArrayList;
    String leftPath, rightPath, Speed;
    public ArrayList<String> rateList;

    MediaPlayer leftPlayer, rightPlayer;
    int a;
    int leftPlayerAPoint, rightPlayerAPoint, leftplayerBPoint, rightPlayerBPoint = 0;

    long currentPosition;
    private Handler updateHandler = new Handler();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_two_video_player);
        videoArrayList = (ArrayList<Video>) getIntent().getSerializableExtra("List");
        leftPath = getIntent().getStringExtra("data");
        rightPath = getIntent().getStringExtra("RIGHT_DATA");

        initComponents();
        playerSetting();
    }

    private void initComponents() {
        leftVideo = findViewById(R.id.firstVideo);
        rightVideo = findViewById(R.id.secondVideo);



        videoControlBar = findViewById(R.id.Player_VideoControlBar);

        leftVideoPath = findViewById(R.id.addFirstVideo);
        rightVideoPath = findViewById(R.id.addSecondVideo);

        playRate = findViewById(R.id.Player_PlayRate);

        rateList = new ArrayList<>();
        rateList.add("0.25배속");
        rateList.add("0.5배속");
        rateList.add("1배속");
        playRate.setText(rateList.get(2));
        playRate.setOnClickListener(this);

        playerAB = findViewById(R.id.ABsetting);
        playerAB.setText("A");
        playerAB.setOnClickListener(this);

        first_rewind = findViewById(R.id.firstRewind);
        first_forward = findViewById(R.id.firstForward);
        second_rewind = findViewById(R.id.secondRewind);
        second_forward = findViewById(R.id.secondForward);

        first_rewind.setOnClickListener(this);
        first_forward.setOnClickListener(this);
        second_rewind.setOnClickListener(this);
        second_forward.setOnClickListener(this);

        leftVideoPath.setOnClickListener(this);
        rightVideoPath.setOnClickListener(this);


        pauseButton = findViewById(R.id.pauseButton);
        pauseButton.setImageResource(R.drawable.playbutton);
        pauseButton.setOnClickListener(this);




        if(leftPath == null){
            leftVideoPath.setVisibility(View.VISIBLE);
        }else{
            leftVideoPath.setVisibility(View.GONE);
        }

        if(rightPath == null){
            rightVideoPath.setVisibility(View.VISIBLE);
        }else{
            rightVideoPath.setVisibility(View.GONE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void playerSetting(){
        leftPlayer = new MediaPlayer();
        rightPlayer = new MediaPlayer();

        leftPlayer.setOnCompletionListener(mediaPlayer -> {
            leftPlayer.seekTo(leftPlayerAPoint, MediaPlayer.SEEK_CLOSEST);
            pauseButton.setImageResource(R.drawable.playbutton);
        });

        leftVideo.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture1, int i, int i1) {
                try {
                    leftPlayer.setDataSource(leftPath);
                    leftPlayer.setSurface(new Surface(surfaceTexture1));
                    leftPlayer.prepareAsync();
                    leftPlayer.setOnPreparedListener(mediaPlayer -> {
                        leftPlayer.setPlaybackParams(leftPlayer.getPlaybackParams().setSpeed(Float.valueOf(1f)));
                        leftPlayer.seekTo(0);
                        leftPlayer.pause();
                        updateHandler.postDelayed(updateVideoTime, 100);
                    });

                } catch (Exception e) {
                }
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
                try {
                    leftPlayer.setDataSource(leftPath);
                    leftPlayer.setSurface(new Surface(surfaceTexture));
                    leftPlayer.prepareAsync();
                    leftPlayer.setOnPreparedListener(mediaPlayer -> {
                        leftPlayer.setPlaybackParams(leftPlayer.getPlaybackParams().setSpeed(Float.valueOf(1f)));
                        leftPlayer.seekTo(0);
                        leftPlayer.pause();
                        updateHandler.postDelayed(updateVideoTime, 100);
                    });

                } catch (Exception e) {
                }
            }
        });


        rightVideo.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
                try {
                    rightPlayer.setDataSource(rightPath);
                    rightPlayer.setSurface(new Surface(surfaceTexture));
                    rightPlayer.prepareAsync();
                    rightPlayer.setOnPreparedListener(mediaPlayer -> {
                        rightPlayer.setPlaybackParams(rightPlayer.getPlaybackParams().setSpeed(1f));

                        rightPlayer.seekTo(0);
                        rightPlayer.pause();
                        updateHandler.postDelayed(updateVideoTime, 100);
                    });

                } catch (Exception e) {
                }
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                return false;
            }
            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
                try {
                    rightPlayer.setDataSource(rightPath);
                    rightPlayer.setSurface(new Surface(surfaceTexture));
                    rightPlayer.prepareAsync();
                    rightPlayer.setOnPreparedListener(mediaPlayer -> {
                        rightPlayer.setPlaybackParams(rightPlayer.getPlaybackParams().setSpeed(1f));

                        rightPlayer.seekTo(0);
                        rightPlayer.pause();
                        updateHandler.postDelayed(updateVideoTime, 100);
                    });

                } catch (Exception e) {
                }
            }
        });

        rightPlayer.setOnCompletionListener(mediaPlayer -> {
            rightPlayer.seekTo(rightPlayerBPoint,MediaPlayer.SEEK_CLOSEST);

        });

        videoControlBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean Input) {
                if (Input) {
                    leftPlayer.seekTo(progress,MediaPlayer.SEEK_CLOSEST);
                    rightPlayer.seekTo(progress,MediaPlayer.SEEK_CLOSEST);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.pauseButton:
                if (leftPlayer.isPlaying()) {
                    pauseButton.setImageResource(R.drawable.playbutton);
                    leftPlayer.pause();
                    rightPlayer.pause();
                } else{

                    pauseButton.setImageResource(R.drawable.pausebutton);
                    Speed = (String) playRate.getText();
                    Speed = Speed.replaceAll("배속", "f");
                    leftPlayer.setPlaybackParams(new PlaybackParams().setSpeed(1f));
                    rightPlayer.setPlaybackParams(rightPlayer.getPlaybackParams().setSpeed(Float.valueOf(Speed)));
                    leftPlayer.start();
                    rightPlayer.start();
                }
                break;
            case R.id.Player_PlayRate:
                String playSpeedText = (String) playRate.getText();
                int size = rateList.size();
                for (int i = 0; i < size; i++) {
                    if (rateList.get(i).contains(playSpeedText)) {
                        a = i - 1;
                        if (a < 0) {
                            a = size - 1;
                        }
                    }
                }
                playRate.setText(rateList.get(a));
                if (leftPlayer.isPlaying()) {
                    leftPlayer.pause();
                    rightPlayer.pause();

                    Speed = (String) playRate.getText();
                    Speed = Speed.replaceAll("배속", "f");

                    leftPlayer.setPlaybackParams(leftPlayer.getPlaybackParams().setSpeed(Float.valueOf(Speed)));
                    leftPlayer.start();

                    rightPlayer.setPlaybackParams(rightPlayer.getPlaybackParams().setSpeed(Float.valueOf(Speed)));
                    rightPlayer.start();
                }
                break;
            case R.id.ABsetting:
                if(playerAB.getText() == "A"){
                    leftPlayerAPoint = leftPlayer.getCurrentPosition();
                    rightPlayerAPoint = rightPlayer.getCurrentPosition();
                    playerAB.setText("B");
                }
                else if(playerAB.getText() == "B"){
                    leftplayerBPoint = leftPlayer.getCurrentPosition();
                    rightPlayerBPoint = rightPlayer.getCurrentPosition();
                    playerAB.setText("AB 해제");
                }
                else if(playerAB.getText()=="AB 해제"){
                    leftPlayerAPoint = rightPlayerAPoint = leftplayerBPoint = rightPlayerBPoint = 0;
                    playerAB.setText("A");
                }
                break;

            case R.id.firstRewind:
                leftPlayer.seekTo(leftPlayer.getCurrentPosition()-33, MediaPlayer.SEEK_CLOSEST);
                break;

            case R.id.firstForward:
                leftPlayer.seekTo(leftPlayer.getCurrentPosition()+33, MediaPlayer.SEEK_CLOSEST);
                break;

            case R.id.secondRewind:
                rightPlayer.seekTo(rightPlayer.getCurrentPosition()-33, MediaPlayer.SEEK_CLOSEST);
                break;

            case R.id.secondForward:
                rightPlayer.seekTo(rightPlayer.getCurrentPosition()+33, MediaPlayer.SEEK_CLOSEST);
                break;

            case R.id.addFirstVideo:
                Intent LEFT = new Intent(this, MediaListActivity.class);
                LEFT.putExtra("leftPath",true);
                startActivityForResult(LEFT,1000);
                leftVideoPath.setVisibility(View.GONE);
                break;

            case R.id.addSecondVideo:
                Intent RIGHT = new Intent(this, MediaListActivity.class);
                RIGHT.putExtra("rightPath",true);
                startActivityForResult(RIGHT,2000);
                rightVideoPath.setVisibility(View.GONE);
                break;
        }
    }

    private Runnable updateVideoTime = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void run() {
            int  leftDurationTime = leftPlayer.getDuration();
            int  rightDurationTime = rightPlayer.getDuration();
            int  DurationTime = Math.max(leftDurationTime,rightDurationTime);
            videoControlBar.setMax(DurationTime);
            if (DurationTime == leftDurationTime){
                currentPosition = rightPlayer.getCurrentPosition();
            }else{
                currentPosition = rightPlayer.getCurrentPosition();
            }
            if(leftplayerBPoint !=0 & rightPlayerBPoint !=0){
                if((leftPlayer.getCurrentPosition() > leftplayerBPoint) & (rightPlayer.getCurrentPosition()> rightPlayerBPoint)) {
                    leftPlayer.seekTo(leftPlayerAPoint, MediaPlayer.SEEK_CLOSEST);
                    rightPlayer.seekTo(rightPlayerAPoint, MediaPlayer.SEEK_CLOSEST);

//                    leftPlayer.start();
//                    rightPlayer.start();
                }
            }
            if((leftPath == null) | (rightPath == null)){
                leftPlayer.pause();
                rightPlayer.pause();
            }
            videoControlBar.setProgress((int) currentPosition);
            updateHandler.postDelayed(this, 10);
        }
    };

    @Override
    public void onBackPressed(){
        Intent in=new Intent(this, MediaListActivity.class);
        startActivity(in);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent Data){
        super.onActivityResult(requestCode, resultCode, Data);
        if(resultCode == RESULT_OK){
            switch(requestCode){
                case 1000:
                    leftPath = Data.getStringExtra("LEFT_PATH");
                    break;
                case 2000:
                    rightPath = Data.getStringExtra("RIGHT_PATH");
                    break;

            }
        }
    }

}
