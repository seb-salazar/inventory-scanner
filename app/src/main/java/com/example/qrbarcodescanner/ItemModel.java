package com.example.qrbarcodescanner;

public class ItemModel {

    private int id;
    private String name;
    private String brand;
    private String number;
    private String category;
    private String amount;
    private String date;

    //constructors


    public ItemModel(int id, String name, String brand, String number, String category, String amount, String date) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.number = number;
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    public ItemModel() {
    }

    //toString is necessary for print or save


    @Override
    public String toString() {
        return "ItemModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", number='" + number + '\'' +
                ", category='" + category + '\'' +
                ", amount='" + amount + '\'' +
                ", LastDateUpdated='" + date + '\'' +
                '}';
    }

    //getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
