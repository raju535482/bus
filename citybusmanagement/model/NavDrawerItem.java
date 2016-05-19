package com.example.sony.citybusmanagement.model;

/**
 * Created by SONY on 22-02-2016.
 */
public class NavDrawerItem {
    private String title;
    public NavDrawerItem(){}

    public NavDrawerItem(String title){
        this.title = title;
    }

    public String getTitle(){
        return this.title;
    }
    public void setTitle(String title){
        this.title = title;
    }

}
