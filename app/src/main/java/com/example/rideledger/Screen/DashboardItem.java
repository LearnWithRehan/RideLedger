package com.example.rideledger.Screen;

public class DashboardItem {

    String icon;
    String title;

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