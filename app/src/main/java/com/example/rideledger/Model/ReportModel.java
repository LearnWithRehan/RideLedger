package com.example.rideledger.Model;

public class ReportModel {

    String date;
    int totalAmount;

    public ReportModel() {}

    public ReportModel(String date, int totalAmount) {
        this.date = date;
        this.totalAmount = totalAmount;
    }

    public String getDate() { return date; }
    public int getTotalAmount() { return totalAmount; }
}