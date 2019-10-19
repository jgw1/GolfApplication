package com.example.myapplication.NewAlbum_Day;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class CardViewChildAdapter extends RecyclerView.Adapter<CardViewChildAdapter.ViewHolder> {
    private ArrayList<ChildDataModel> itemModelArrayList;
    Context mContext;


    public CardViewChildAdapter(Context context, ArrayList<ChildDataModel> itemModelArrayList){
        this.itemModelArrayList = itemModelArrayList;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_child,null);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChildDataModel childDataModel = itemModelArrayList.get(position);
        if(childDataModel.getThumbnail() == 0) {
            holder.thumbnail.setImageResource(R.drawable.golfcart);
        }
        Log.d("GWGWGWGWGW","itemModelArrayList.size " + itemModelArrayList.size());
    }
    @Override
    public int getItemCount() {
        return itemModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        protected ImageView thumbnail;

        public ViewHolder(View itemView) {
            super(itemView);
            this.thumbnail = itemView.findViewById(R.id.thumbnail);

        }
    }
}
