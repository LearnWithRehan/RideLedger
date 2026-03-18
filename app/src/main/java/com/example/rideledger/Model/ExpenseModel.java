package com.example.rideledger.Model;

public class ExpenseModel {

    private String id;
    private String expenseDate;
    private String createdDate;
    private String type;
    private int amount;

    public ExpenseModel() {
        // required for Firestore
    }

    public ExpenseModel(String id, String expenseDate, String createdDate, String type, int amount) {
        this.id = id;
        this.expenseDate = expenseDate;
        this.createdDate = createdDate;
        this.type = type;
        this.amount = amount;
    }

    // 🔽 Getters
    public String getId() { return id; }
    public String getExpenseDate() { return expenseDate; }
    public String getCreatedDate() { return createdDate; }
    public String getType() { return type; }
    public int getAmount() { return amount; }

    // 🔽 Setters
    public void setId(String id) { this.id = id; }
    public void setExpenseDate(String expenseDate) { this.expenseDate = expenseDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }
    public void setType(String type) { this.type = type; }
    public void setAmount(int amount) { this.amount = amount; }
}