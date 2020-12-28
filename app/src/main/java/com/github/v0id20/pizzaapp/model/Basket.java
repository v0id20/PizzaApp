package com.github.v0id20.pizzaapp.model;

import java.util.ArrayList;

public class Basket {

    private ArrayList<BasketItem> basketList = new ArrayList<>();
    private int totalItemCount;
    private double totalToPay;

    public void clearBasket(){
        basketList.clear();
        totalToPay = 0;
        totalItemCount = 0;
    }

    public double removeBasketItem(BasketItem item) {
        int quantity = item.getQuantity();
        totalItemCount -= quantity;
        totalToPay -= item.getPrice() * quantity;
        basketList.remove(item);

        return totalToPay;
    }

    public double addNewBasketItem(BasketItem item) {
        int quantity = item.getQuantity();
        totalItemCount += quantity;
        totalToPay += item.getPrice() * quantity;
        basketList.add(item);

        return totalToPay;
    }

    public double addExistingBasketItem(int position, int quantity) {
        totalItemCount += quantity;
        BasketItem currentItem = basketList.get(position);
        totalToPay += basketList.get(position).getPrice() * quantity;
        int newQuantity = quantity + basketList.get(position).getQuantity();
        BasketItem updatedItem = new BasketItem(currentItem.getName(), newQuantity, currentItem.getPrice());
        basketList.set(position, updatedItem);

        return totalToPay;
    }

    public ArrayList<BasketItem> getBasketList() {
        return basketList;
    }

    public int getTotalItemCount() {
        return totalItemCount;
    }

    public double getTotalToPay() {
        return totalToPay;
    }


}
