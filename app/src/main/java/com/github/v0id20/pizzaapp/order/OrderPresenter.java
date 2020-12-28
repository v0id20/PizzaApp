package com.github.v0id20.pizzaapp.order;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.github.v0id20.pizzaapp.model.Basket;
import com.github.v0id20.pizzaapp.model.BasketItem;
import com.github.v0id20.pizzaapp.model.DatabaseHelper;
import com.github.v0id20.pizzaapp.orderhistory.OrderHistoryItem;
import com.github.v0id20.pizzaapp.PizzaAppApplication;

import java.util.ArrayList;

public class OrderPresenter implements  OrderInterface.Presenter{

    private OrderInterface.View view;
    private PizzaAppApplication application;
    private Basket currentBasket;
    private int currentOrderId;
    private ArrayList<BasketItem> currentOrderList;
    private OrderPresenter.SubmitOrderTask task;
    private DatabaseHelper helper;

    public OrderPresenter(OrderInterface.View view, DatabaseHelper helper, PizzaAppApplication application){
        this.view = view;
        this.helper = helper;
        this.application = application;
    }

    @Override
    public void start() {

        currentBasket = application.getBasket();
        currentOrderList = currentBasket.basketList;
        currentOrderId = application.currentOrderId;

        if (currentOrderList.size() == 0) {
            //set empty state if basket is empty
            view.setBasketEmpty();
        } else {
            //set views and add data otherwise
            ArrayList<BasketItem> currentOrderListWithTotal = currentBasket.createBasketWithTotal();
            view.showOrder(currentOrderListWithTotal);
        }
    }

    @Override
    public void removeOrderItem(int position) {
        BasketItem itemToRemove = currentBasket.basketWithTotal.get(position);
        currentBasket.removeBasketItem(itemToRemove);

        Log.i("order", currentBasket.totalToPay+" item quantity :"+currentBasket.totalItemCount);
        if ( currentBasket.basketWithTotal.size() <1) {
            view.setBasketEmpty();
        }
    }


    @Override
    public void submitOrder() {
        if (task!=null){
            task.cancel(true);
        }
        task = new OrderPresenter.SubmitOrderTask();
        Log.i("order", currentOrderList.size()+" "+currentOrderList.get(0).getName()+" "+currentOrderList.get(0).getName());
        task.execute(currentOrderList);
    }

    @Override
    public void stop() {
        if (task!=null) {
            task.cancel(true);
        }
    }


    public class SubmitOrderTask extends AsyncTask<ArrayList<BasketItem>, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(ArrayList<BasketItem>... orderLists) {

            ArrayList<BasketItem> orders = orderLists[0];
            try {
                SQLiteDatabase db = helper.getReadableDatabase();
                ArrayList<BasketItem> currentBasketToHistory = new ArrayList<>();

                for (int i = 0; i < orders.size(); i++) {

                    ContentValues orderValues = new ContentValues();
                    orderValues.put("ORDER_ID", currentOrderId);
                    orderValues.put("NAME", orders.get(i).getName());
                    orderValues.put("QUANTITY", orders.get(i).getQuantity());
                    orderValues.put("PRICE", orders.get(i).getPrice());
                    db.insert("ORDER_HISTORY", null, orderValues);
                    currentBasketToHistory.add(new BasketItem(orders.get(i).getName(),orders.get(i).getQuantity(),orders.get(i).getPrice()));
                }
                OrderHistoryItem ohi = new OrderHistoryItem(currentOrderId, currentBasketToHistory , 0);
                ohi.countTotal();
                application.getOrderHistory().add(0,ohi);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            view.setBasketEmpty();
            currentBasket.clearBasket();
            Log.i("order list sizec", " "+currentOrderList.size());
            view.notifyAdapter();
            application.currentOrderId++;
            view.startActivity();
        }
    }


}
