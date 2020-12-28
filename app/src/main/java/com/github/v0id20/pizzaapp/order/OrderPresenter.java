package com.github.v0id20.pizzaapp.order;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.github.v0id20.pizzaapp.DataManager;
import com.github.v0id20.pizzaapp.model.Basket;
import com.github.v0id20.pizzaapp.model.BasketItem;


import java.util.ArrayList;

public class OrderPresenter implements  OrderInterface.Presenter{

    private OrderInterface.View view;
    private Basket currentBasket;
    private double totalToPay;
    private ArrayList<BasketItem> currentOrderList;
    private OrderPresenter.SubmitOrderTask task;
    private DataManager mDataManager;
    private Context mContext;

    public OrderPresenter(OrderInterface.View view, Context context, DataManager dm){
        this.view = view;
        mContext = context;
        mDataManager = dm;
    }

    @Override
    public void start() {
        currentBasket = mDataManager.getBasket();
        currentOrderList = currentBasket.getBasketList();
        totalToPay = currentBasket.getTotalToPay();

        if (currentOrderList.size() == 0) {
            //set empty state if basket is empty
            view.setBasketEmpty();
        } else {
            //set views and add data otherwise
            view.showOrder(currentOrderList, totalToPay);
            //view.showOrder(currentOrderList);
        }
    }

    @Override
    public void removeOrderItem(int position) {
        BasketItem itemToRemove = currentOrderList.get(position);
        currentBasket.removeBasketItem(itemToRemove);
        view.notifyAdapter(currentBasket.getTotalToPay());

        Log.i("order", currentBasket.getTotalToPay()+" item quantity :"+currentBasket.getTotalItemCount());
        if ( currentOrderList.size() <1) {
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


    public class SubmitOrderTask extends AsyncTask<ArrayList<BasketItem>, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(ArrayList<BasketItem>... orderLists) {

            ArrayList<BasketItem> orders = orderLists[0];
            return mDataManager.submitOrder(orders);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success){
                view.setBasketEmpty();
                currentBasket.clearBasket();
                Log.i("order list sizec", " "+currentOrderList.size());
                view.notifyAdapter(0);
                mDataManager.incrementCurrentOrderId();
                view.startActivity();
            } else {
                Toast.makeText(mContext, "Database unavailable. Please try again later.", Toast.LENGTH_SHORT).show();
            }

        }
    }


}
