package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class RecordingActivity extends AppCompatActivity {
    private TextureView RecordingTextureView;
    private Preview mpreview;
    private ImageButton RecordingStopButton;
    Activity RecordingActivity = this;

    private static final String TAG = "RECORDINGACTIVITY";
    static final int REQUEST_CAMERA = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);

        RecordingTextureView = (TextureView) findViewById(R.id.RecordingTextureView);
        mpreview = new Preview(RecordingActivity, RecordingTextureView);

        RecordingStopButton = (ImageButton) findViewById(R.id.RecordingStopButton);
        RecordingStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_main);
                Intent in = new Intent(RecordingActivity,MainActivity.class);
                startActivity(in);
            }
        });
    }
    @Override
    public void onBackPressed(){
        setContentView(R.layout.activity_main);
        Intent in = new Intent(RecordingActivity,MainActivity.class);
        startActivity(in);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mpreview.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mpreview.onPause();
    }


}
