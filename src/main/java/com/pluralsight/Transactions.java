package com.pluralsight;

public class Transactions {
    //date|time|description|vendor|amount
    private String vendor;
    private double amount;
    private  String description;
    private String time;
    private String date;

    public Transactions(String date, String time, String description, String vendor,double amount) {
        this.vendor = vendor;
        this.time = time;
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String toString(){
        return date + "|" + time + "|" + description + "|" + vendor + "|" + amount;
    }


}
