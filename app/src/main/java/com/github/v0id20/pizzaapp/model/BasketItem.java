package com.github.v0id20.pizzaapp.model;

public class BasketItem {


    private String name;
    private int quantity;
    private double price; //per item, not overall

    public BasketItem(String name, int quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


}
