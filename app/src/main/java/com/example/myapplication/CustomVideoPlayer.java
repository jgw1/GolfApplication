package com.example.myapplication;

import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.CompareShot.TwoVideoPlayer;

import java.io.IOException;
import java.util.ArrayList;

public class CustomVideoPlayer extends AppCompatActivity implements TextureView.SurfaceTextureListener,View.OnClickListener {
    TextureView textureView;
    TextView Durationtime, PlayTime;
    MediaPlayer mediaPlayer;
    SeekBar VideoControlBar;
    ImageView PauseButton,Rewind,FastForward;
    ImageButton Analyze;
    ToggleButton Favorite;


    Button PlayRate;
    public ArrayList<String> RateList;
    ArrayList<Video> videoArrayList;

    String Speed, Videopath;
    int a;
    Surface surface;

    private Handler updateHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_video_player);
        videoArrayList = (ArrayList<Video>) getIntent().getSerializableExtra("List");
        Videopath = getIntent().getStringExtra("data");
        initComponents();
    }
    /**
     * init
     */
    private void initComponents() {
        textureView = findViewById(R.id.firstVideo);
        textureView.setSurfaceTextureListener(this);

        Durationtime = findViewById(R.id.DurationTime);
        PlayTime = findViewById(R.id.PlayTime);
        VideoControlBar = findViewById(R.id.VideoControlBar);
        Analyze =findViewById(R.id.Analyze);


        PauseButton = findViewById(R.id.PlayButton);
        Favorite = findViewById(R.id.Favorite);
        Rewind = findViewById(R.id.Rewind);
        FastForward = findViewById(R.id.FastForward);


        Analyze.setOnClickListener(this);
        Rewind.setOnClickListener(this);
        PauseButton.setOnClickListener(this);
        FastForward.setOnClickListener(this);

        mediaPlayer = new MediaPlayer();

        Rewind.setImageResource(R.drawable.rewind);
        PauseButton.setImageResource(R.drawable.playbutton);
        FastForward.setImageResource(R.drawable.fastforward);


        PlayRate = findViewById(R.id.PlayRate);
        playrateSetting();
        Speed = (String) PlayRate.getText();

        textureView.setOnTouchListener(new OnSwipeTouchListener(CustomVideoPlayer.this) {
            public void onSwipeRight() {
                TrackChange("RIGHT");
            }

            //왼쪽방향 스와이프 - 트랙이동
            public void onSwipeLeft() {
                TrackChange("LEFT");
            }

            //더블터치 - 재생 & 정지
            public void onDoubleTouch() {
                pauseplay();
            }
            public void onSingleTap(){
                Log.d("GWGWGWGWGW","latitude");
            }

        });

        VideoControlBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean Input) {
                if (Input) {
                    mediaPlayer.seekTo(progress, MediaPlayer.SEEK_CLOSEST);

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

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        surface = new Surface(surfaceTexture);
        playerinitial(Videopath);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Rewind:
                TrackChange("LEFT");
                break;
            case R.id.PlayButton:
                pauseplay();
                break;
            case R.id.FastForward:
                TrackChange("RIGHT");
                break;
            case R.id.Analyze:
               Intent intent = new Intent(getApplicationContext(), TwoVideoPlayer.class);
               intent.putExtra("data",Videopath);
               startActivity(intent);
               break;
//            case R.id.Favorite:
//                Favorite.setImageResource(R.mipmap.yellowstar);
        }
    }

    private void pauseplay() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            PauseButton.setImageResource(R.drawable.playbutton);
        } else {
            PauseButton.setImageResource(R.drawable.pausebutton);
            Speed = (String) PlayRate.getText();
            Speed = Speed.replaceAll("배속", "f");
            mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(Float.valueOf(Speed)));
            mediaPlayer.start();
        }
    }
    private void playrateSetting() {
        RateList = new ArrayList<>();
        RateList.add("0.25배속");
        RateList.add("0.5배속");
        RateList.add("1배속");
        PlayRate.setText(RateList.get(2));
        PlayRate.setOnClickListener(view -> {
            String playSpeedText = (String) PlayRate.getText();
            int size = RateList.size();
            for (int i = 0; i < size; i++) {
                if (RateList.get(i).contains(playSpeedText)) {
                    a = i - 1;
                    if (a < 0) {
                        a = size - 1;
                    }
                }
            }
            PlayRate.setText(RateList.get(a));
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                Speed = (String) PlayRate.getText();
                Speed = Speed.replaceAll("배속", "f");
                mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(Float.valueOf(Speed)));
                mediaPlayer.start();
            }

        });
    }

    public static String playtime(long duration) {
        long seconds = (duration / 1000);
        long second = seconds % 60;
        long minute = (seconds / 60) % 60;
        long hour = (seconds / (60 * 60)) % 24;
        if (hour > 0) {
            return String.format("%02d:%02d:%02d", hour, minute, second);
        } else {
            return String.format("%02d:%02d", minute, second);
        }
    }

    private Runnable updateVideoTime = new Runnable() {
        public void run() {
            long durationtime = mediaPlayer.getDuration();
            long currentPosition = mediaPlayer.getCurrentPosition();
            String DurationTime = playtime(durationtime);
            String playtime = playtime(currentPosition);
            VideoControlBar.setProgress((int) currentPosition);
            PlayTime.setText(String.valueOf(playtime));
            Durationtime.setText(String.valueOf(DurationTime));
            updateHandler.postDelayed(this, 10);
        }
    };

    private void videoplay(String videopath) {
        mediaPlayer.stop();
        mediaPlayer.reset();
        playerinitial(videopath);
    }

    private void TrackChange(String Direction) {
        int size = videoArrayList.size();
        for (int i = 0; i < size; i++) {
            if (videoArrayList.get(i).getPath().contains(Videopath)) {
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
        Video video = videoArrayList.get(a);
        Videopath = video.getPath();
        videoplay(Videopath);

    }

    private void playerinitial(String VideoPath) {
        try {
            PauseButton.setImageResource(mediaPlayer.isPlaying() ? R.drawable.playbutton : R.drawable.pausebutton);
            mediaPlayer.setDataSource(VideoPath);
            mediaPlayer.setSurface(surface);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mediaPlayer -> {
                mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(1f));
                PlayRate.setText(RateList.get(2));
                mediaPlayer.start();

                long finalTime = mediaPlayer.getDuration();
                VideoControlBar.setMax((int) finalTime);
                updateHandler.postDelayed(updateVideoTime, 100);

            });
            mediaPlayer.setLooping(true);
        } catch (Exception e) {
        }
    }
}
