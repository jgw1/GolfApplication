package com.example.myapplication.Map;

import java.util.ArrayList;

public class MapRecyclerViewModel {
    private String headerTitle;

    public MapRecyclerViewModel(String headerTitle) {
        this.headerTitle = headerTitle;
    }
    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }
}
