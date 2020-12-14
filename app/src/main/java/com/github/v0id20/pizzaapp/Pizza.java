package com.github.v0id20.pizzaapp;

public class Pizza extends Dish {

//    private String name;
//    private String description;
//    private double price;
    private String size;


    public Pizza(String name, String desc, double price, String size, int resourceId) {
        super(name, desc, price, resourceId);
        this.size = size;
    }


//    public String getName() {
//        return name;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public double getPrice() {
//        return price;
//    }

    public String getSize() {
        return size;
    }

//    public int getImageResourceId() {
//        return imageResourceId;
//    }
}
