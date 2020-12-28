package com.github.v0id20.pizzaapp;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class OrderHistoryItem {

    public ArrayList<BasketItem> orderList;
    public double totalToPay;
    public int orderId;
    private boolean expanded;

    public OrderHistoryItem(int orderId, ArrayList<BasketItem> orderList, double totalToPay){
        this.orderId = orderId;
        this.orderList = orderList;
        this.totalToPay = totalToPay;
    }

    public ArrayList<BasketItem> getOrderList() {
        return  orderList;
    }

    public int getOrderId(){
        return orderId;
    }

    public double getTotalToPay() {
        return totalToPay;
    }
    public void setTotalToPay(double a) {
        totalToPay = a;
    }

    @NonNull

    public String toString() {
        StringBuilder builder = new StringBuilder();
        this.getOrderList();
        for (BasketItem b :orderList){
            String text = b.getQuantity()+"x "+b.getName()+" "+b.getPrice()+"\n";
            builder.append(text);
        }
        //builder.append("Total: "+totalToPay);
        return  builder.toString();
    }

    public void countTotal(){
        totalToPay = 0;
        for (BasketItem b :orderList){
            totalToPay+=b.getPrice()*b.getQuantity();
        }

    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
