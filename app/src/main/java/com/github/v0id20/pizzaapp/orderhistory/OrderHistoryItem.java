package com.github.v0id20.pizzaapp.orderhistory;

import androidx.annotation.NonNull;

import com.github.v0id20.pizzaapp.model.BasketItem;

import java.util.ArrayList;

public class OrderHistoryItem {

    private ArrayList<BasketItem> orderList;
    private double totalToPay;
    private int orderId;
    private long orderTimeInMillis;
    private boolean expanded;

    public OrderHistoryItem(int orderId, ArrayList<BasketItem> orderList, long time, double totalToPay){
        this.orderId = orderId;
        this.orderList = orderList;
        this.totalToPay = totalToPay;
        this.orderTimeInMillis = time;
    }

    public double getTotalToPay() {
        return totalToPay;
    }
    public void setTotalToPay(double a) {
        totalToPay = a;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (BasketItem b :orderList){
            String text = b.getQuantity()+"x "+b.getName()+" "+String.format(("$%.2f"), b.getPrice())+"\n";
            builder.append(text);
        }
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

    public long getOrderTimeInMillis() {
        return orderTimeInMillis;
    }

    public ArrayList<BasketItem> getOrderList() {
        return  orderList;
    }

    public int getOrderId(){
        return orderId;
    }
}
