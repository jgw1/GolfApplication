package com.example.myapplication.NewAlbum_Day;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DB.DatabaseAccess;
import com.example.myapplication.DB.DatabaseOpenHelper;
import com.example.myapplication.DB.Picture;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CardViewAlbum extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_card_view_album);

        NewAlbumDay = findViewById(R.id.NewAlbumDay);
        NewAlbumTotal = findViewById(R.id.NewAlbumTotal);
        NewAlbumMap = findViewById(R.id.NewAlbumMap);
        NewAlbumFavorite = findViewById(R.id.NewAlbumFavorite);
        this.databaseAccess = DatabaseAccess.getInstance(this);


        allSampleData = new ArrayList<>();
        databaseAccess.open();
        allSampleData = databaseAccess.getAllMemos();
        Log.d("ALLSAMPLEDATA","ALLSAMPLEDATA " + allSampleData.size());
        databaseAccess.close();
        mRecyclerView = (RecyclerView) findViewById(R.id.cardviewalbum);
        mRecyclerView.setHasFixedSize(true);
        CardViewAdapter adapter = new CardViewAdapter(this,allSampleData);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

    }

    @Override
    public void onBackPressed(){
        Intent in=new Intent(this, MainActivity.class);
        allSampleData = new ArrayList<>();
        startActivity(in);
        finish();
    }
}
