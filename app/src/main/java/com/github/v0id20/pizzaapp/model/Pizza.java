package com.github.v0id20.pizzaapp.model;

public class Pizza extends Dish {

    private double priceSmall;
    private double priceMedium;
    private double priceLarge;



    public Pizza(String name, String desc, double price,  String resourceId) {
        super(name, desc, price, resourceId);
    }

    public Pizza(){

    }

    public Pizza(String name, String desc, double priceSmall, double priceMedium, double priceLarge,  String resourceId) {
        super(name, desc, priceSmall, resourceId);
        this.priceSmall = priceSmall;
        this.priceMedium = priceMedium;
        this.priceLarge = priceLarge;
    }

    public double getPriceSmall() {
        return priceSmall;
    }

    public double getPriceMedium() {
        return priceMedium;
    }

    public double getPriceLarge() {
        return priceLarge;
    }

}
