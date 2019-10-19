package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.VideoView;

import java.util.List;

public class VideoAdapter extends ArrayAdapter<Video> {
    private Context mContext;
    private List<Video> mVideos;

    private boolean isSelectable, ischecked = false;


    public VideoAdapter(Context context, List<Video> objects) {
        super(context,R.layout.adult_row_item,objects);
        mContext = context;
        mVideos = objects;
    }

    public void setSelectable(boolean isSelectable){
        this.isSelectable = isSelectable;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent){
        final ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adult_row_item,null);
            holder = new ViewHolder();
            holder.videoView = convertView.findViewById(R.id.videoView);
            holder.checkBox = convertView.findViewById(R.id.checkBox);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        final Video video = mVideos.get(position);

        final String path = video.getPath();
        holder.videoView.setVideoPath(path);
        holder.videoView.start();

        holder.checkBox.setChecked(video.isChecked());

        holder.checkBox.setVisibility(isSelectable ? convertView.VISIBLE : convertView.INVISIBLE);
//        holder.videoView.set(isSelectable ? )
//        holder.videoView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
        holder.videoView.setOnCompletionListener(mediaPlayer -> holder.videoView.start());

        holder.videoView.setOnClickListener(view -> {
            if (isSelectable) {
                if (video.isChecked()) {
                    holder.checkBox.setChecked(false);
                    video.setChecked(false);
                } else {
                    holder.checkBox.setChecked(true);
                    video.setChecked(true);
                }
            } else {
                Intent intent = new Intent(mContext, Main2Activity.class);
                intent.putExtra("data", path);
                mContext.startActivity(intent);
            }

        });
        return convertView;
    }

    public static class ViewHolder{
        VideoView videoView;
        CheckBox checkBox;
    }

    public void setIschecked(boolean ischecked){
        this.ischecked = ischecked;
    }
}
