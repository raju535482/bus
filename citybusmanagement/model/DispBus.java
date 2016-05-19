package com.example.sony.citybusmanagement.model;
public class DispBus {
    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    String lat,lng;

    public String getBus() {
        return mid;
    }

    public void setBus(String mid) {
        this.mid = mid;
    }

    String mid;
}
