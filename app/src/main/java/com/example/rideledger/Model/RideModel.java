package com.example.rideledger.Model;

public class RideModel {

    public String date;
    public String platform;
    public int cash;
    public int online;
    public int fuel;
    public int cng;
    public int total;
    public int profit;

    public String rideDate;
    public String createdDate;
    public String userId;

    public RideModel(){}

    public RideModel(String date,String platform,int cash,int online,int fuel,int cng,int total,int profit){

        this.date=date;
        this.platform=platform;
        this.cash=cash;
        this.online=online;
        this.fuel=fuel;
        this.cng=cng;
        this.total=total;
        this.profit=profit;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public int getFuel() {
        return fuel;
    }

    public void setFuel(int fuel) {
        this.fuel = fuel;
    }

    public int getCng() {
        return cng;
    }

    public void setCng(int cng) {
        this.cng = cng;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getProfit() {
        return profit;
    }

    public void setProfit(int profit) {
        this.profit = profit;
    }

    public String getRideDate() {
        return rideDate;
    }

    public void setRideDate(String rideDate) {
        this.rideDate = rideDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}