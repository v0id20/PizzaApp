package com.github.v0id20.pizzaapp.order;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.v0id20.pizzaapp.Basket;
import com.github.v0id20.pizzaapp.BasketItem;
import com.github.v0id20.pizzaapp.DatabaseHelper;
import com.github.v0id20.pizzaapp.OrderHistoryActivity;
import com.github.v0id20.pizzaapp.OrderHistoryItem;
import com.github.v0id20.pizzaapp.PizzaAppApplication;
import com.github.v0id20.pizzaapp.R;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {


    private Button placeOrderBtn;
    private TextView emptyTextView;
    RecyclerView recyclerView;
    double amountToPay;
    private Basket currentBasket;
    Integer currentOrderId = 0;
    private ArrayList<BasketItem> currentOrderList;
    OrderAdapter orderAdapter;
    PizzaAppApplication application;

    OnRemoveOrderItemListener onRemoveOrderItemListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Basket");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        application = (PizzaAppApplication) getApplication();
        currentBasket = application.getBasket();
        currentOrderId = application.currentOrderId;
        currentOrderList = currentBasket.basketList;
        amountToPay = currentBasket.totalToPay;


        emptyTextView = findViewById(R.id.empty);
        placeOrderBtn = findViewById(R.id.place_order);

        onRemoveOrderItemListener = new OnRemoveOrderItemListener() {
            @Override
            public void onRemoveOrderItem(int position) {
                BasketItem itemToRemove = currentBasket.basketWithTotal.get(position);
                amountToPay = currentBasket.removeBasketItem(itemToRemove);
                if ( currentBasket.basketWithTotal.size() <1) {
                    setBasketEmpty();
                } //                currentBasket.basketWithTotal.set(currentBasket.basketWithTotal.size()-1, new BasketItem("Total", ));
//                currentBasket.basketWithTotal.remove(position);
//
            }
        };

        //set views
        if (currentOrderList.size() == 0) {
            //set empty state if basket is empty
            setBasketEmpty();
        } else {
            //set views and add data otherwise
            emptyTextView.setVisibility(View.GONE);
            placeOrderBtn.setEnabled(true);

            ArrayList<BasketItem> currentOrderListWithTotal = currentBasket.createBasketWithTotal();
            recyclerView = findViewById(R.id.order_recycler);
            orderAdapter = new OrderAdapter(currentOrderListWithTotal, onRemoveOrderItemListener);
            recyclerView.setAdapter(orderAdapter);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(llm);
        }

        placeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitOrder();

            }
        });
    }

    private void submitOrder() {
        SubmitOrderTask task = new SubmitOrderTask();
        Log.i("order", currentOrderList.size()+" "+currentOrderList.get(0).getName()+" "+currentOrderList.get(0).getName());
        task.execute(currentOrderList);
    }

    public class SubmitOrderTask extends AsyncTask<ArrayList<BasketItem>, Void, Void> {
        DatabaseHelper helper;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(ArrayList<BasketItem>... orderLists) {

            ArrayList<BasketItem> orders = orderLists[0];
            try {
                helper = new DatabaseHelper(OrderActivity.this);
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
            setBasketEmpty();
            ((PizzaAppApplication)getApplication()).getBasket();
            currentBasket.clearBasket();
//           currentBasket.totals = new BasketItem("Total", 0, 0, BasketItem.TYPE_TOTAL);
//            currentBasket.basketWithTotal.clear();
//            currentBasket.basketList.clear();
            Log.i("order list sizec", " "+currentOrderList.size());

            orderAdapter.notifyDataSetChanged();
            ((PizzaAppApplication)getApplication()).currentOrderId++;
            Intent i = new Intent(OrderActivity.this, OrderHistoryActivity.class);
            startActivity(i);
        }
    }

    private void setBasketEmpty() {
        emptyTextView.setVisibility(View.VISIBLE);
        placeOrderBtn.setEnabled(false);
    }
}
