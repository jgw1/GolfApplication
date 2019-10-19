package com.example.myapplication.NewAlbum_Total;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.myapplication.R;
import com.example.myapplication.Video;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class mediaAdapter extends ArrayAdapter<Video> {
    private Context context;
    private List<Video> videos;
    private boolean isSelectable = false;
    private boolean isAllChecked = false;
    private final String TAG = this.getClass().getSimpleName();
    private List<MediaHolder> holderList;

    public mediaAdapter(Context context, List<Video> objects){
        super(context, R.layout.grid_item_mp, objects);
        this.context = context;
        this.videos = objects;
        holderList = new ArrayList<>();
    }

    public void setSelectable(boolean isSelectable){
        this.isSelectable = isSelectable;
        notifyDataSetChanged();
    }
    public boolean getSelectable(){
        return isSelectable;
    }

    @Override
    public int getCount() {
        return videos.size();
    }

    /**
     * 현재 Videos 는 Preview 의 Path를 갖고 있어서
     * Album 의 Path 를 가진 List 를 생성해서 리턴한다.
     * @return
     */
    public List<Video> getAlbumVideoList(){
        List<Video> albumList = new ArrayList<>();
        for(Video v : videos){
            albumList.add(new Video(v.getPath()));
        }
        return albumList;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        MediaHolder holder;
        Video video = videos.get(position);
        String path = video.getPath();
        Log.d(TAG, "position = " + position + " : path = " + path);

        if (convertView == null){
            Log.d(TAG, "CREATEEEEEEEEEE");
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_mp, null);
            holder = new MediaHolder(convertView);
//			holder.binder = new SurfaceBinder(convertView, path);
            holder.setBinder(path);
            holderList.add(holder);
            convertView.setTag(holder);
        }else{
            Log.d(TAG, "GETTTT");
            holder = (MediaHolder)convertView.getTag();
            if( video.getFirst() || !path.equals(holder.videoPath)) {
                video.setFirst(false);
                holder.setBinder(path);
            }
        }

        holder.checkboxImg.setVisibility(isSelectable?View.VISIBLE:View.INVISIBLE);
        if( isSelectable ){
            holder.checkboxImg.setImageResource(video.getChecked()?R.drawable.check_32:R.drawable.uncheck_32);
        }

        return convertView;
    }

    public void setCheckedToggle(int i) {
        if( isSelectable ) {
            videos.get(i).setChecked(!videos.get(i).getChecked());
            notifyDataSetChanged();
        }
    }

    public void setCheckedToggleAll(){
        isAllChecked = !isAllChecked;
        for(Video v : videos){
            v.setChecked(isAllChecked);
        }
        notifyDataSetChanged();
    }

    public List<File> getCheckedFileList(){
//		videos.stream().filter(f->f.getChecked()).map(m->new File(m.getPath())).collect(Collectors.toList());
        List<File> removeList = new ArrayList<>();
        for(int i=videos.size()-1; i >=0; i--){
            if( videos.get(i).isChecked() ){
                removeList.add(new File(videos.get(i).getPath()));
                videos.remove(i);
            }
        }
        return removeList;
    }

    public void release(){
        try{
            for(MediaHolder h : holderList){
                if( h.exoPlayerView != null )
                    h.exoPlayerView.removeAllViews();
                if( h.player != null){
                    h.player.clearVideoSurface();
                    h.player.release();
                }
            }
        }catch (Exception ex){
            Log.e("#ERR", ex.toString());
        }
    }

    public class MediaHolder {
        ImageView checkboxImg;
        public PlayerView exoPlayerView;
        public SimpleExoPlayer player;
        private String videoPath;
        public MediaHolder(View view){
            checkboxImg = view.findViewById(R.id.checkboxImg);
            exoPlayerView = view.findViewById(R.id.exoPlayerView);

            if (player == null) {
                player = ExoPlayerFactory.newSimpleInstance(context);
                //플레이어 연결
                exoPlayerView.setUseController(false);
                exoPlayerView.setPlayer(player);
                exoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);	// 추가 8/22
//				exoPlayerView.hideController();
//				exoPlayerView.setControllerVisibilityListener(i -> {
//					if(i == 0) {
//						exoPlayerView.hideController();
//					}
//				});
                player.stop();
//                player.setRepeatMode(Player.REPEAT_MODE_ALL);
            }
        }
        public void setBinder(String videoPath){
            this.videoPath = videoPath;
//			player.stop(true);
            MediaSource mediaSource = buildMediaSource(Uri.fromFile(new File(videoPath)));
            //prepare
            player.prepare(mediaSource, true, false);
            player.setPlayWhenReady(true);
        }

        private MediaSource buildMediaSource(Uri uri) {
            return new ProgressiveMediaSource.Factory(new DefaultDataSourceFactory(context, "Exopalyer")).createMediaSource(uri);
//			return new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(context,"Exoplayer")).createMediaSource(uri);
        }
    }

}

