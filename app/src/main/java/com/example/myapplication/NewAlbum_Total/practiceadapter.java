package com.example.myapplication.NewAlbum_Total;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.CompareShot.TwoVideoPlayer;
import com.example.myapplication.Video;

import java.util.ArrayList;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class practiceadapter extends RecyclerView.Adapter<practiceadapter.ViewHolder> {

    private boolean isSelectable, ischecked = false;
    private Context mContext;
    private ArrayList<Video> videoArrayList;
    private BitmapDrawable bitmapDrawable;

    public practiceadapter(Context mContext, ArrayList<Video> videoArrayList){
        this.mContext=mContext;
        this.videoArrayList = videoArrayList;
    }

    public void setSelectable(boolean isSelectable){
        this.isSelectable = isSelectable;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = (View) LayoutInflater.from(mContext).inflate(R.layout.adult_row_item,parent,false);
        int height = parent.getMeasuredHeight();
        itemView.setMinimumHeight(height/3);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Video video = videoArrayList.get(position);
        if(position+1 ==  videoArrayList.size())
            position = 0;
        else{
            position +=1;
        }
        final Video video1 = videoArrayList.get(position);
        /* holder.videoView.setBackground(video.getBitmapDrawable());*/


        holder.videoView.setVideoPath(video.getPath());
        holder.videoView.seekTo(100);
        /*holder.videoView.start();*/
        holder.checkBox.setVisibility(isSelectable ? VISIBLE : INVISIBLE);
        holder.checkBox.setChecked(video.isChecked());

        holder.videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSelectable){
                    if(video.isChecked()){
                        holder.checkBox.setChecked(false);
                        video.setChecked(false);
                    }else
                    {
                        holder.checkBox.setChecked(true);
                        video.setChecked(true);
                    }}
                else{
                    Intent intent = new Intent(mContext, TwoVideoPlayer.class);

                    intent.putExtra("List",videoArrayList);
                    intent.putExtra("data", video.getPath());
                    intent.putExtra("data1", video1.getPath());
                    mContext.startActivity(intent);
                }
            }
        });


/*

        holder.videoView.start();
        holder.videoView.setOnCompletionListener(mediaPlayer -> holder.videoView.start());*/

        /*holder.videoView.seekTo(100);*/

        /* Video video = videoArrayList.get(position);


        holder.videoView.start();
        */
    }

    @Override
    public int getItemCount() {
        return videoArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private VideoView videoView;
        private CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
            checkBox = itemView.findViewById(R.id.checkBox);

        }

        public VideoView getVideo(){
            return videoView;
        }
        public CheckBox getCheckBox(){
            return checkBox;
        }
    }
}

