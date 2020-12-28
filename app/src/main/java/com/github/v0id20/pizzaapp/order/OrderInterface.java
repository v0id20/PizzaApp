package com.github.v0id20.pizzaapp.order;

import com.github.v0id20.pizzaapp.model.BasketItem;

import java.util.ArrayList;

public interface OrderInterface {
    interface View {
        void showOrder(ArrayList<BasketItem> orderList);
        void setBasketEmpty();
        void startActivity();
        void notifyAdapter();
    }
    interface Presenter{
        void start();
        void removeOrderItem(int position);
        void submitOrder();
        void stop();
    }
}
