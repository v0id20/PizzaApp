package com.github.v0id20.pizzaapp;

import java.util.ArrayList;

public class Basket {

    ArrayList<BasketItem> basket;
    int totalItemCount;
    double totalToPay;
    int orderId;

    public ArrayList<BasketItem> getBasket() {
        return basket;
    }

    public int getTotalItemCount() {
        return totalItemCount;
    }

    public double getTotalToPay() {
        return totalToPay;
    }

    public double countTotalToPay(){
        totalToPay = 0;
        for (BasketItem i :basket){
            totalToPay+=i.getPrice()*i.getQuantity();
        }
        return totalToPay;
    }

    public int countItemQuantity(){
        int q = 0;
        for (BasketItem i :basket){
            q+=i.getQuantity();
        }
        return q;
    }

    public double removeBasketItem(BasketItem item){
        int quantity = item.getQuantity();
        totalItemCount-=quantity;
        totalToPay-=item.getPrice()*quantity;
        return totalToPay;
    }



}
