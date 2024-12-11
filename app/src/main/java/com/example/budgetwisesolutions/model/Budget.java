package com.example.budgetwisesolutions.model;
public class Budget {
    private int id;
    private String source;
    private String startDate;
    private String endDate;
    private String category;
    private String note;
    private double amount;

    public Budget(int id, String source, String startDate, String endDate, String category, String note, double amount) {
        this.id = id;
        this.source = source;
        this.startDate = startDate;
        this.endDate = endDate;
        this.category = category;
        this.note = note;
        this.amount = amount;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}
