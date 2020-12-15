package com.github.v0id20.pizzaapp;

public class Basket {

    private String name;
    private  int productId;
    private int quantity;
    private double price; //per item, not overall

    public Basket(String name, int quantity, double price){
        this.name = name;
        this.quantity = quantity;
        this.price = price;

    }

    public Basket(){

    }


    public String getName() {
        return name;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}
