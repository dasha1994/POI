package com.example.s4astya.poi.model;

import java.util.ArrayList;

/**
 * Created by S4ASTYA on 29.03.2016.
 */
public class POI {

    private String name;
    private String description;
    private String latitude;
    private String longitude;
    private ArrayList<String> paths = new ArrayList<>();

    public ArrayList<String> getPaths() {
        return paths;
    }

    public void setPaths(ArrayList<String> paths) {
        this.paths = paths;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() { return longitude; }

}
