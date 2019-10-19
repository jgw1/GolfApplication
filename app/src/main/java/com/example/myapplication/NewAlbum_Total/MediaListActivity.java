package com.example.myapplication.NewAlbum_Total;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.CustomVideoPlayer;
import com.example.myapplication.MainActivity;
import com.example.myapplication.NewAlbum_Total.mediaAdapter;
import com.example.myapplication.R;
import com.example.myapplication.Video;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MediaListActivity extends AppCompatActivity {
    private GridView gridView;
    private com.example.myapplication.NewAlbum_Total.mediaAdapter mediaAdapter;
    private LinearLayout bottom_album_navigation;
    private List<File> removeFileList;
    private boolean Left,Right;
    private boolean isFullScreenRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        removeFileList = new ArrayList<>();
        gridView = findViewById(R.id.gridview);
        bottom_album_navigation = findViewById(R.id.bottom_album_navigation);
        String path = "/sdcard/DCIM/SnapShot";

        Left =getIntent().getBooleanExtra("leftPath",false);
        Right =getIntent().getBooleanExtra("rightPath",false);

        mediaAdapter = new mediaAdapter(this, getVideosFromPath(path));
        gridView.setAdapter(mediaAdapter);
        gridView.setOnItemClickListener((adapterView, view, i, l) -> {
            if(Left){
                Intent intent = new Intent();
                String prevPath = mediaAdapter.getItem(i).getPath();
                String onlyName = prevPath.substring(prevPath.lastIndexOf('/') + 1);
                String videoPath = MainActivity.ALBUM_PATH + "/" + onlyName;
                intent.putExtra("LEFT_PATH", videoPath);
                setResult(RESULT_OK,intent);
                finish();
            }
            else if(Right){ Intent intent = new Intent();
                String prevPath = mediaAdapter.getItem(i).getPath();
                String onlyName = prevPath.substring(prevPath.lastIndexOf('/') + 1);
                String videoPath = MainActivity.ALBUM_PATH + "/" + onlyName;
                intent.putExtra("RIGHT_PATH", videoPath);
                setResult(RESULT_OK,intent);
                finish();}
            else {
                if (mediaAdapter.getSelectable())
                    mediaAdapter.setCheckedToggle(i);
                else {
                    if (!isFullScreenRunning) {
                        isFullScreenRunning = true;
//					Intent intent = new Intent(this, VideoFullScreen.class);
//					String prevPath = mediaAdapter.getItem(i).getPath();
//					String onlyName = prevPath.substring(prevPath.lastIndexOf('/') + 1);
//					String videoPath = MainActivity.ALBUM_PATH + "/" + onlyName;
//					intent.putExtra("data", videoPath);
//					startActivity(intent);

                        Intent intent = new Intent(this, CustomVideoPlayer.class);
                        String prevPath = mediaAdapter.getItem(i).getPath();
                        String onlyName = prevPath.substring(prevPath.lastIndexOf('/') + 1);
                        String videoPath = MainActivity.ALBUM_PATH + "/" + onlyName;
                        intent.putExtra("data", videoPath);
                        intent.putExtra("List", (ArrayList<Video>) mediaAdapter.getAlbumVideoList());
                        startActivity(intent);

                        // default player app
//					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoPath));
//					intent.setDataAndType(Uri.parse(videoPath), "video/mp4");
//					startActivity(intent);
                    }
                }
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        isFullScreenRunning = false;
    }

    private ArrayList<Video> getVideosFromPath(String rootPath){
        ArrayList<Video> videos = new ArrayList<>();

        File files = new File(rootPath);
//		File files = new File(MainActivity.ALBUM_PATH);
        File[] fl = files.listFiles();
        if( fl != null && fl.length > 0 ) {
            Arrays.sort(fl,Comparator.reverseOrder());
            for(int i=0; i< fl.length;i++){
                videos.add(new Video(fl[i].getPath()));
            }
//			for (Object file : Arrays.stream(fl).sorted(Comparator.reverseOrder()).toArray()) {
//				String path = ((File) file).getPath();
//				videos.add(new Video(path));
//			}
        }
        return videos;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaAdapter.release();

        removeFileListIfExists();
    }

    private void removeFileListIfExists(){
        // remove
        for(int i=0; i < removeFileList.size(); i++){
            String prevPath = removeFileList.get(i).getAbsolutePath();
            File orgFile = new File(MainActivity.ALBUM_PATH, prevPath.substring(prevPath.lastIndexOf('/')+1));
            removeFileList.get(i).delete();
            orgFile.delete();
        }
        removeFileList.clear();
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.nav_back_button:
                finish();
                break;
            case R.id.nav_btn_remove :
                removeFileList.addAll(mediaAdapter.getCheckedFileList());
                mediaAdapter.notifyDataSetChanged();
                removeFileListIfExists();
                break;
            case R.id.nav_textview_select:
                TextView tv = (TextView)view;
                if( "선택".equals(tv.getText().toString()) ) {
                    mediaAdapter.setSelectable(true);
                    bottom_album_navigation.setVisibility(View.VISIBLE);
                    tv.setText("취소");
                }else{
                    mediaAdapter.setSelectable(false);
                    bottom_album_navigation.setVisibility(View.INVISIBLE);
                    tv.setText("선택");
                }
                break;
            case R.id.textview_selectall:
                mediaAdapter.setCheckedToggleAll();
                break;
        }
    }
}
