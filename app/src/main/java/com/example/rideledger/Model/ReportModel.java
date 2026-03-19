package com.example.rideledger.Model;

public class ReportModel {

    String date;
    String type;
    int totalAmount;

    public ReportModel() {}

    public ReportModel(String date, String type, int totalAmount) {
        this.date = date;
        this.type = type;
        this.totalAmount = totalAmount;
    }

    public String getDate() { return date; }
    public String getType() { return type; }
    public int getTotalAmount() { return totalAmount; }
}