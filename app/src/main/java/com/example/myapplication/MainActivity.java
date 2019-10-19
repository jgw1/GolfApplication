package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.myapplication.DB.DatabaseAccess;
import com.example.myapplication.DB.Picture;
import com.example.myapplication.Map.MapActivity;
import com.example.myapplication.NewAlbum_Day.CardViewAlbum;
import com.example.myapplication.NewAlbum_Total.MediaListActivity;
import com.example.myapplication.PersonalInfo.PersonalInfo;
import com.example.myapplication.Setting.Setting;
import com.example.myapplication.Slider.CustomDialog;
import com.example.myapplication.Slider.PreferenceManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    PreferenceManager preferenceManager;
    private TextureView mCameraTextureView;
    public Preview mPreview;
    Activity mainActivity;
    DatabaseAccess databaseAccess;
    private ImageView AlbumButton;
    private ImageButton  RecordingButton, ChangeCameraButton;
    private CustomDialog customDialog;
    private Picture picture;
    private String TAG;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_CAMERA = 0;
    public static String ALBUM_PATH;
    public static String PREV_PATH;
    public static String mCurrentPhotoPath;

    public MainActivity() {
        mainActivity = this;
        TAG = this.getClass().getSimpleName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();   // component 초기화

//        AlbumButton.setOnClickListener(this);
//        RecordingButton.setOnClickListener(this);
//        ChangeCameraButton.setOnClickListener(this);



        this.databaseAccess = DatabaseAccess.getInstance(this);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            picture = (Picture) bundle.get("Picture");
        }
        //if(!f.exists()) {
        //    f.mkdir();
        //}
/*
        initAlbumButtonThumb();*/

    }

    /**
     * init
     */
    private void initComponents() {
        mCameraTextureView = findViewById(R.id.cameraTextureView);
        mPreview = new Preview(this, mCameraTextureView);

        preferenceManager = new PreferenceManager(this);
        AlbumButton = findViewById(R.id.AlbumButton);
        RecordingButton = findViewById(R.id.RecordingButton);
        ChangeCameraButton = findViewById(R.id.ChangeCameraButton);
        ALBUM_PATH = Environment.getExternalStorageDirectory() + "/DCIM/SnapShot";
        PREV_PATH = getExternalFilesDir(null) + "/SnapShotPreview";
        if(preferenceManager.isFirstTimeLaunch() == true){
            customDialog = new CustomDialog(MainActivity.this);
            customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            customDialog.show();
        }
    }

    /**
     * 앨범버튼의 섬네일을 만들어서 버튼에 표시한다.
     */
    private void initAlbumButtonThumb() {
        String PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM/VideoFolder";
        File f = new File(Environment.getExternalStorageDirectory(), "/DCIM/VideoFolder"); // Environment.getExternalStorageDirectory(), "/DCIM/VideoFolder");
        /*getExternalFilesDir(Environment.DIRECTORY_DCIM).getPath(), getResources().getString(R.string.base_dir)*/
        File a = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM).getPath());
        File b = new File("/sdcard/ddf");
        File C = getApplicationContext().getDataDir();
        String w = Environment.getRootDirectory() + "/VideoFolder";
        File q = new File(w);
        q.mkdirs();
        a.mkdirs();
        File file = Thumbnail.latestFileModified(PATH);
        String path = file.getPath();
        Bitmap bitmap = Thumbnail.MakeThumbnail(path);
        AlbumButton.setImageBitmap(bitmap);
    }

    /**
     * onClick...
     * @param v View...
     */
    public void onClick(View v){
        switch (v.getId()){
            case R.id.AlbumButton:
               startActivity(new Intent(this, MediaListActivity.class));
                break;
            case R.id.RecordingButton:
                final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                String locationProvider = LocationManager.NETWORK_PROVIDER;
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }

                // 현재 위치 정보 저장 - 차후에 DB시 필요 / 여기에선 필요 없음
                Location location = lm.getLastKnownLocation(locationProvider); //현재 위치 정보
                double latitude = location.getLatitude(); //현재 위치 - 위도 값
                double longitude = location.getLongitude(); //현재 위치 - 경도 값

                databaseAccess.open();
                Picture temp = new Picture();
                if(picture == null) {
                    temp.setLatitude(latitude);
                    temp.setLongitude(longitude);
                    Log.d("GWGWGWGWGW","latitude = " + latitude);
                    Log.d("GWGWGWGWGW","longitude = " + longitude);
                    databaseAccess.save(temp);
                } else {
                    // Update the memo
                    temp.setLatitude(latitude);
                    temp.setLongitude(longitude);
                    Log.d("GWGWGWGWGW","latitude = " + latitude);
                    Log.d("GWGWGWGWGW","longitude = " + longitude);
                   databaseAccess.update(picture);
                }
                databaseAccess.close();


                //                Intent intent1 = new Intent(MainActivity.this, MapActivity.class);
//                startActivity(intent1);
                break;
            case R.id.ChangeCameraButton:
                onPause();
                if (mPreview.isFrontCamera())
                    mPreview.setCameraToBack();
                else
                    mPreview.setCameraToFront();
                mPreview.openCamera();
                onResume();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA:
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];
                    if (permission.equals(Manifest.permission.CAMERA)) {
                        if(grantResult == PackageManager.PERMISSION_GRANTED) {
                            mCameraTextureView = findViewById(R.id.cameraTextureView);
                            mPreview = new Preview(mainActivity, mCameraTextureView);
                            Log.d(TAG,"mPreview set");

                        } else {
                            Toast.makeText(this,"Should have camera permission to run", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                }
                break;


        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPreview.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mPreview.onPause();
    }
    public static void saveBitmaptoJpeg(Context context, Bitmap bitmap,String folder, String name){
        String ex_storage =Environment.getExternalStorageDirectory().getAbsolutePath();
        String folder_name = "/"+folder+"/";
        String file_name = name+".jpg";
        String string_path = ex_storage+folder_name;
        File file_path;
        try{ file_path = new File(string_path);
            if(!file_path.isDirectory()){ file_path.mkdirs();
            } FileOutputStream out = new FileOutputStream(string_path+file_name);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.parse("file:"+string_path+file_name);
            intent.setData(uri);
            context.sendBroadcast(intent);
        }catch(FileNotFoundException exception){ Log.e("FileNotFoundException", exception.getMessage());

        }catch(IOException exception){ Log.e("IOException", exception.getMessage()); }

    }

}
