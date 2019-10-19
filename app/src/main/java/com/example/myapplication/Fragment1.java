package com.example.myapplication;

import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DB.DatabaseAccess;
import com.example.myapplication.DB.DatabaseOpenHelper;
import com.example.myapplication.DB.Picture;
import com.example.myapplication.NewAlbum_Day.CardViewAdapter;
import com.example.myapplication.NewAlbum_Day.ChildDataModel;
import com.example.myapplication.NewAlbum_Day.MotherDataModel;

import java.util.ArrayList;
import java.util.List;

public class Fragment1 extends Fragment{
    private RecyclerView mRecyclerView;
    SQLiteDatabase database;
    private DatabaseAccess databaseAccess;
    private Picture picture;
    DatabaseOpenHelper openHelper;
    ImageButton NewAlbumDay,NewAlbumTotal,NewAlbumMap,NewAlbumFavorite;
    List<Address> mResultList;
    ArrayList<MotherDataModel> allSampleData;
    MotherDataModel MD = new MotherDataModel();
    ArrayList<ChildDataModel> childDataModels = new ArrayList<>();
    Long prevtime = null;

    public Fragment1()
    {
        // required
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,

                             @Nullable Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout)inflater.inflate(R.layout.fragment_fragment1,

                container, false);
        return layout;
    }

}
