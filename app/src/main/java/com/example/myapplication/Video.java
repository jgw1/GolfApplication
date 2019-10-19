package com.example.myapplication;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Video implements Serializable {
    private String path;
    private boolean checked = false;
    private boolean removeRequest = false;
    private boolean isFirst = true;
    private double longitude;
    private double latitude;
    private int Favorite, Estimation = 0;

    private Date date;
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");

    public Video(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean getChecked() {
        return this.checked;
    }

    public void setRemoveRequest(boolean removeRequest){
        this.removeRequest = removeRequest;
    }
    public boolean getRemoveRequest(){
        return removeRequest;
    }

    public void setFirst(boolean isFirst){
        this.isFirst = isFirst;
    }
    public boolean getFirst(){
        return isFirst;
    }

    public double getLongitude(){return longitude;}
    public void setLongitude(double longitude){this.longitude = longitude;}

    public double getLatitude(){return latitude;}
    public void setLatitude(double latitude){this.latitude = latitude;}

    public int getFavorite(){
        return Favorite;
    }
    public void setFavorite(int Favorite){
        this.Favorite =Favorite;
    }

    public int getEstimation(){ return Estimation; }
    public void setEstimation(int estimation){this.Estimation = estimation;}

};