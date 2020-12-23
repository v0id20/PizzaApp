package com.github.v0id20.pizzaapp;

public class BasketItem {

    private String name;
    private int quantity;
    private int type;
    public static int TYPE_TOTAL = 2;
    public static int TYPE_ITEM = 1;



    private double price; //per item, not overall

    public BasketItem(String name, int quantity, double price){
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        type = 1;
    }
    public BasketItem(String name, int quantity, double price, int type){
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
    }

    public BasketItem(){
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

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getType() {
        return type;
    }
}
