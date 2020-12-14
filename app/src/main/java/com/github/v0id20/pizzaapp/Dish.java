package com.github.v0id20.pizzaapp;

public class Dish {


    private String name;
    private String description;
    private double price;
    private int imageResourceId;

    public Dish(){
        super();
    }


    public Dish(String name, String desc, double price, int resourceId) {
        this.name = name;
        description = desc;
        this.price = price;
        this.imageResourceId = resourceId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }


    public int getImageResourceId() {
        return imageResourceId;
    }
}
