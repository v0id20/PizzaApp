package com.github.v0id20.pizzaapp;

import java.util.ArrayList;

public class Basket {

    ArrayList<BasketItem> basket;
    int totalBasketItemCount;
    double totalToPay;

    public ArrayList<BasketItem> getBasket() {
        return basket;
    }

    public int getTotalBasketItemCount() {
        return totalBasketItemCount;
    }

    public double getTotalToPay() {
        return totalToPay;
    }



}
