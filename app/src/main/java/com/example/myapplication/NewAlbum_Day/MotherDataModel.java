package com.example.myapplication.NewAlbum_Day;

import java.util.ArrayList;

public class MotherDataModel {
    private String headerTitle;
    private String shotaddress;
    private ArrayList<ChildDataModel> allItemsInSection;


    public MotherDataModel() {

    }
    public MotherDataModel(String headerTitle,String shotaddress, ArrayList<ChildDataModel> allItemsInSection) {
        this.headerTitle = headerTitle;
        this.shotaddress = shotaddress;
        this.allItemsInSection = allItemsInSection;
    }

    public String getShotaddress() {
        return shotaddress;
    }

    public void setShotaddress(String shotaddress) {
        this.shotaddress = shotaddress;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public ArrayList<ChildDataModel> getAllItemsInSection() {
        return allItemsInSection;
    }

    public void setAllItemsInSection(ArrayList<ChildDataModel> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }
}
