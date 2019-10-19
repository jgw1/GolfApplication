package com.example.myapplication;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;

public class Directory {
    private Activity activity;
    private String path = null;
    public Directory(Activity activity){
        this.activity = activity;
    }
    //동영상 저장 폴더 경로 추출
    public String getRoute(String filename) {
        try {
            String[] projection = new String[]{
                    MediaStore.Video.VideoColumns._ID,
                    MediaStore.Video.VideoColumns.DATA,
                    MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME,
                    MediaStore.Video.VideoColumns.DATE_TAKEN,
                    MediaStore.Video.VideoColumns.MIME_TYPE};

            Cursor cursor = activity.getContentResolver().query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    null,
                    null,
                    MediaStore.Video.VideoColumns.DATE_TAKEN + " DESC");
            if (cursor != null) {
                cursor.moveToFirst();
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                do {
                    if (path.contains(filename)) {
                        File file = new File(path);
                        path = file.getParent();
                        cursor.close();
                        return path;
                    }
                } while (cursor.moveToNext());

                File file = new File((Environment.getExternalStorageDirectory())+"/DCIM");
                path = file.getParentFile()+filename;



                File Album_directory = new File(path,filename);
                Album_directory.mkdirs();
                cursor.close();
                return path;
            }
        }
        catch (Exception e) {
        }
        return "";
    }

    // 동영상 저장
    public void Add_Video(String File_path, String File_name,ArrayList<Video> videos) throws Exception {

        String path = File_path + "/" + File_name +".mp4";

        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        else{
            file.createNewFile();
            videos.add(new Video(path));
        }
    }

    //체크된 동영상 삭제 & 리스트 업데이트
    public void Delete_Video(ArrayList<Video> videos){
        int length = videos.size();
        for (int i=0;i<length;i++){
            Video video = videos.get(i);
            if(video.isChecked())
            {
                File file = new File(video.getPath());
                file.delete();
                videos.remove(i);
                i--;
                length=videos.size();
            }
           }
        }
}
