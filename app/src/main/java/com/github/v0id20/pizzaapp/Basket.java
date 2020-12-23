package com.github.v0id20.pizzaapp;

import android.widget.Toast;

import java.util.ArrayList;

public class Basket {

    public ArrayList<BasketItem> basketList;
    ArrayList<BasketItem> basketWithTotal;
    BasketItem totals;
    int totalItemCount;
    double totalToPay;
    int orderId;


    public ArrayList<BasketItem> getBasketList() {
        return basketList;
    }

    public int getTotalItemCount() {
        return totalItemCount;
    }

    public double getTotalToPay() {
        return totalToPay;
    }

    public double countTotalToPay() {
        totalToPay = 0;
        for (BasketItem i : basketList) {
            totalToPay += i.getPrice() * i.getQuantity();
        }
        return totalToPay;
    }

    public ArrayList<BasketItem> clearBasket(){
        basketList =new ArrayList<>();
        basketWithTotal = new ArrayList<>();
        totalToPay = 0;
        totalItemCount = 0;
        totals = new BasketItem("Total", 0 , 0, BasketItem.TYPE_TOTAL);
        return basketList;
    }

    public int countItemQuantity() {
        int q = 0;
        for (BasketItem i : basketList) {
            q += i.getQuantity();
        }
        return q;
    }

    public double removeBasketItem(BasketItem item) {
        int quantity = item.getQuantity();
        totalItemCount -= quantity;
        totalToPay -= item.getPrice() * quantity;
        totals.setQuantity(totalItemCount);
        totals.setPrice(totalToPay);


        basketList.remove(item);
        if (basketList.size() == 0) {
            basketWithTotal.clear();
        } else {
            basketWithTotal.set(basketWithTotal.size() - 1, new BasketItem("Total", totalItemCount, totalToPay, BasketItem.TYPE_TOTAL));
            basketWithTotal.remove(item);
        }

        return totalToPay;
    }

    public double addNewBasketItem(BasketItem item) {
        int quantity = item.getQuantity();
        totalItemCount += quantity;
        totalToPay += item.getPrice() * quantity;
        basketList.add(item);
        totals = new BasketItem("Total", totalItemCount, totalToPay, BasketItem.TYPE_TOTAL);
        totals.setPrice(totalToPay);
        totals.setQuantity(totalItemCount);

        //basketWithTotal.add(totals);
        //poka hz
        //basketWithTotal.set(basketWithTotal.size()-1,new BasketItem("Total", totalItemCount,totalToPay));
        return totalToPay;
    }

    public double addExistingBasketItem(int position, int quantity) {
        //int quantity = item.getQuantity();
        totalItemCount += quantity;
        BasketItem currentItem = basketList.get(position);
        totalToPay += basketList.get(position).getPrice() * quantity;
        int newQuantity = quantity + basketList.get(position).getQuantity();
        BasketItem updatedItem = new BasketItem(currentItem.getName(), newQuantity, currentItem.getPrice());
        basketList.set(position, updatedItem);
        totals.setPrice(totalToPay);
        totals.setQuantity(totalItemCount);

        //poka hz
        //basketWithTotal.set(basketWithTotal.size()-1,new BasketItem("Total", totalItemCount,totalToPay));
        return totalToPay;
    }

    public ArrayList<BasketItem> createBasketWithTotal() {
        if (basketWithTotal != null) {
            basketWithTotal.clear();
        } else {
            basketWithTotal = new ArrayList<>();
        }
        basketWithTotal = (ArrayList<BasketItem>) basketList.clone();
        basketWithTotal.add(totals);

//        int ind = 0;
//        for (BasketItem b :basketWithTotal){
//            if (b.getName().equals("Total")){
//
//                break;
//            }
//            ind++;
//        }
//
//        if (ind<basketWithTotal.size()){
//            basketWithTotal.remove(ind);
//            basketWithTotal.add(this.totals);
//        } else {
//            basketWithTotal.add(this.totals);
//        }
        return basketWithTotal;
    }


}
