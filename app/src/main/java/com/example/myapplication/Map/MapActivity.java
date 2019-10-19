package com.example.myapplication.Map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DB.DatabaseAccess;
import com.example.myapplication.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {
    Marker selectedMarker;
    View marker_root_view;
    TextView tv_marker;
    LinearLayout linearLayout;
    RecyclerView mapRecyclerView;
    ArrayList<MapRecyclerViewModel> mapRecyclerViewModels;
    private GoogleMap mMap;
    TextView position;
    Context mContext;
    private DatabaseAccess databaseAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        initComponents();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    public void initComponents(){
        linearLayout = findViewById(R.id.menu);
        mapRecyclerView = findViewById(R.id.mapRecyclerView);
        position = findViewById(R.id.address);
        linearLayout.setVisibility(View.INVISIBLE);
        mapRecyclerViewModels = new ArrayList<>();
        this.databaseAccess = DatabaseAccess.getInstance(this);
    }






    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ClusterManager<MapMarkerItem> mClusterManager = new ClusterManager<>(this,mMap);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.537523, 126.96558), 14));
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setOnMapClickListener(this);

        //위치값 받아 오는데 필요한 허가 확인
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



        // Custom 마커를 위해 Render setting
        ClusterRenderer renderer = new ClusterRenderer(this,mMap,mClusterManager);
        mClusterManager.setRenderer(renderer);

        //선택 클러스터를 화면 중앙에 배치
        mMap.setOnCameraChangeListener(mClusterManager);


        //결과확인을 위한 테스트용 세팅
        for(int i = 0; i<50;i++){
            double lat = 37.537523 + (i/200d);
            double lng = 126.96558 + (i/200d);
            mClusterManager.addItem(new MapMarkerItem((new LatLng(lat, lng)),"1"));
        }

        //전체 다 하나의 마커를 하나의 클러스터로 묶을 경우 사용
        //mClusterManager.cluster();

        //클러스터 선택시 해당 주소와 그 이하의 섬네일 표시
        mClusterManager.setOnClusterClickListener(cluster -> {
            Geocoder mGeoCoder = new Geocoder(getApplicationContext());
            //섬네일 리스트 상단에 선택 클러스터 주소 표시
            try {
                List<Address> mResultList = mGeoCoder.getFromLocation(cluster.getPosition().latitude,cluster.getPosition().longitude,1);
                position.setText(String.valueOf(mResultList.get(0).getAddressLine(0)));
                position.setTextColor(Color.WHITE);
            }
            catch (IOException e) {
                e.printStackTrace();
            }


            databaseAccess.open();
            //선택 클러스터에 포함된 마커들 섬네일 표시
            for(ClusterItem clusterItem : cluster.getItems()){
                double a = clusterItem.getPosition().latitude;
                double b = clusterItem.getPosition().longitude;

                linearLayout.setVisibility(View.VISIBLE);

                //DB제작 이후 해당부분 업데이트 필요 - 현재는 테스트용으로 코딩함
                // 위도,경도 받은 이후 DB 서치 -> 해당 섬네일 또는 경로ArrayList에 저장
//                if((a!=0)&(b!=0)){
//                    mapRecyclerViewModels.add(new MapRecyclerViewModel("SUCCESS"));
//                }
                String File_name = databaseAccess.FileName(String.valueOf(a),String.valueOf(b));
                mapRecyclerViewModels.add(new MapRecyclerViewModel(File_name));
                Log.d("HAHAHA","File_name : " + File_name);
                }

            databaseAccess.close();
            MapRecyclerViewAdapter itemListDataAdapter = new MapRecyclerViewAdapter(mContext, mapRecyclerViewModels);
            mapRecyclerView.setHasFixedSize(true);
            mapRecyclerView.setAdapter(itemListDataAdapter);
            mapRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            mapRecyclerViewModels = new ArrayList<>();
            return false;
        });
    }

    // View를 Bitmap으로 변환 - 지도 표현 부분
    private Bitmap createDrawableFromView(Context context, View view) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    //마커 선택시 화면 중앙으로 배치
    @Override
    public boolean onMarkerClick(Marker marker) {
        CameraUpdate center = CameraUpdateFactory.newLatLng(marker.getPosition());
        mMap.animateCamera(center);
        changeSelectedMarker(marker);
        return true;
    }


    private void changeSelectedMarker(Marker marker) {
        // 선택했던 마커 되돌리기
        if (selectedMarker != null) {
            addMarker(selectedMarker, false);
            selectedMarker.remove();
        }
        // 선택한 마커 표시
        if (marker != null) {
            selectedMarker = addMarker(marker, true);
            marker.remove();
        }
    }

    private Marker addMarker(Marker marker, boolean isSelectedMarker) {
        double lat = marker.getPosition().latitude;
        double lon = marker.getPosition().longitude;
        LatLng a = new LatLng(lat,lon);
        String price = "a";
        MapMarkerItem temp = new MapMarkerItem(
                a, price);
        return addMarker(temp, isSelectedMarker);

    }

    private Marker addMarker(MapMarkerItem markerItem, boolean isSelectedMarker) {
        LatLng position = new LatLng(markerItem.getLat(), markerItem.getLon());
        String address = markerItem.getAddress();
        tv_marker.setText(address);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(address);
        markerOptions.position(position);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker_root_view)));
        return mMap.addMarker(markerOptions);
    }

    @Override
    public void onMapClick(LatLng latLng) {
//        changeSelectedMarker(null);
    }

    // 마커 및 클러스터 Custom하게 설정
    public class ClusterRenderer extends DefaultClusterRenderer<MapMarkerItem> {
        private final Context mContext;
        public ClusterRenderer(Context context, GoogleMap map, ClusterManager<MapMarkerItem> clusterManager) {
            super(context, map, clusterManager);
            mContext  = context;
        }
        @Override
        //뭉치 마커 커스텀 해주는 부분
        protected void onBeforeClusterRendered(Cluster<MapMarkerItem> cluster, MarkerOptions markerOptions){
            final IconGenerator mClusterIconGenerator;
            mClusterIconGenerator = new IconGenerator(mContext.getApplicationContext());

            //클러스터 디자인 설정
            LayoutInflater myInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View activityview = myInflater.inflate(R.layout.view,null,false);
            TextView textview = activityview.findViewById(R.id.textview);
            ImageView imageView = activityview.findViewById(R.id.flag);
//
//            Bitmap bitmap = Thumbnail.MakeThumbnail("FullscreenActionBarStyle");

            //해당 클러스터에 포함된 마커 개수들 표현
            textview.setText(String.valueOf(cluster.getSize()));
//            for(ClusterItem clusterItem : cluster.getItems()){
//                Log.d("HAHAHA","GETITEMS"+clusterItem.getPosition());
//            }

//            textview.setBackground(ContextCompat.getDrawable(mContext,R.drawable.background_circle));
           //해당 클러스터에 포함된 마커 개수들 표현 디자인 설정
            textview.setTextColor(Color.BLACK);

            textview.setTextSize(15f);
            textview.setGravity(View.TEXT_ALIGNMENT_CENTER);

            mClusterIconGenerator.setContentView(activityview);
            // mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext,R.drawable.background_circle));
            mClusterIconGenerator.setTextAppearance(R.style.AppTheme_WhiteTextAppearance);
            final Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        //낱개 마커 커스텀 해주는 부분
        @Override
        protected void  onBeforeClusterItemRendered(MapMarkerItem markerItem, MarkerOptions markerOptions){
            final IconGenerator mClusterIconGenerator;
            mClusterIconGenerator = new IconGenerator(mContext.getApplicationContext());

            //클러스터 디자인 설정
            LayoutInflater myInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View activityview = myInflater.inflate(R.layout.view,null,false);
            TextView textview = activityview.findViewById(R.id.textview);
            ImageView imageView = activityview.findViewById(R.id.flag);
//
//            Bitmap bitmap = Thumbnail.MakeThumbnail("FullscreenActionBarStyle");

            //해당 클러스터에 포함된 마커 개수들 표현

//            for(ClusterItem clusterItem : cluster.getItems()){
//                Log.d("HAHAHA","GETITEMS"+clusterItem.getPosition());
//            }

//            textview.setBackground(ContextCompat.getDrawable(mContext,R.drawable.background_circle));
            //해당 클러스터에 포함된 마커 개수들 표현 디자인 설정
            textview.setText("1");
            textview.setTextColor(Color.BLACK);
            Log.d("HAHAHA","TEXTSIZE" + textview.getTextSize());
            textview.setTextSize(15f);
            textview.setGravity(View.TEXT_ALIGNMENT_CENTER);

            mClusterIconGenerator.setContentView(activityview);
            // mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext,R.drawable.background_circle));
            mClusterIconGenerator.setTextAppearance(R.style.AppTheme_WhiteTextAppearance);
            final Bitmap icon = mClusterIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
//            markerOptions.title("HAHAHA");
        }


    }
}
