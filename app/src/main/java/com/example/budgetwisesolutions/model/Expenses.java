package com.example.budgetwisesolutions.model;

public class Expenses  {
    //public String id;
    public String date;
    public String note;
    public String amount;


    public Expenses(  String date, String note, String amount ) {
        //this.id = id;
        this.date = date;
        this.note = note;
        this.amount = amount;
    }



    public void setAmount(String amount) {
        this.amount = amount;
    }


    public void setNote(String note) {
        this.note = note;
    }

    public void setDate(String date) {
        date = date;
    }



    public String getAmount() {
        return amount;
    }


    public String getNote() {
        return note;
    }

    public String getDate() {
        return date;
    }
}
