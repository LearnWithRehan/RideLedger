package com.example.rideledger.Model;

public class DashboardItem {

    private String icon;
    private String title;

    public DashboardItem(String icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }
}