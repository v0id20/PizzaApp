package com.github.v0id20.pizzaapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {


    private Button placeOrderBtn;
    private TextView emptyTextView;
    private TextView amountToPayTextView;
    private TextView totalTextView;

    double amountToPay;
    Basket currentBasket;
    Integer currentOrderId = 0;
    ArrayList<BasketItem> currentOrderList;

    OnRemoveOrderItemListener onRemoveOrderItemListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Basket");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PizzaAppApplication application = (PizzaAppApplication) getApplication();
        currentBasket = application.getBasket();
        currentOrderList = currentBasket.basket;
        amountToPay = currentBasket.countTotalToPay();

        emptyTextView = findViewById(R.id.empty);
        amountToPayTextView = findViewById(R.id.total_amount);
        totalTextView = findViewById(R.id.total);
        placeOrderBtn = findViewById(R.id.place_order);

        onRemoveOrderItemListener = new OnRemoveOrderItemListener() {
            @Override
            public void onRemoveOrderItem(int position) {
                BasketItem itemToRemove = currentOrderList.get(position);
                amountToPay = currentBasket.removeBasketItem(itemToRemove);
                if (currentOrderList.size() > 1) {
                    amountToPayTextView.setText(String.format("%.2f", amountToPay));
                } else {
                    setBasketEmpty();
                }
                currentOrderList.remove(position);
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
            amountToPayTextView.setVisibility(View.VISIBLE);
            amountToPayTextView.setText(String.format("%.2f", amountToPay));

            RecyclerView recyclerView = findViewById(R.id.order_recycler);
            OrderAdapter orderAdapter = new OrderAdapter(currentOrderList, onRemoveOrderItemListener);
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
        task.execute(currentOrderList);
    }

    public class SubmitOrderTask extends AsyncTask<ArrayList<BasketItem>, Void, Void> {
        DatabaseHelper helper;

        @Override
        protected Void doInBackground(ArrayList<BasketItem>... orderLists) {

            ArrayList<BasketItem> orders = orderLists[0];
            try {
                helper = new DatabaseHelper(OrderActivity.this);
                SQLiteDatabase db = helper.getReadableDatabase();
                if (currentOrderId == 0) {
                    String[] column = new String[]{"(SELECT max(ORDER_ID) FROM BASKET) AS max"};
                    Cursor cursor = db.query("BASKET", column, null, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        int idColumnIndex = cursor.getColumnIndex("max");
                        currentOrderId = cursor.getInt(idColumnIndex) + 1;
                    } else {
                        currentOrderId = 1;
                    }
                }

                for (int i = 0; i < orders.size(); i++) {
                    ContentValues orderValues = new ContentValues();
                    orderValues.put("ORDER_ID", currentOrderId);
                    orderValues.put("NAME", orders.get(i).getName());
                    int quantity = orders.get(i).getQuantity();

                    orderValues.put("QUANTITY", quantity);
                    orderValues.put("PRICE", orders.get(i).getPrice() * quantity);
                    db.insert("BASKET", null, orderValues);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setBasketEmpty();
            currentOrderList.clear();
            currentOrderId++;
            Intent i = new Intent(OrderActivity.this, OrderHistoryActivity.class);
            startActivity(i);
            //Toast.makeText(OrderActivity.this, "Thank you for your order", Toast.LENGTH_SHORT).show();
        }
    }

    private void setBasketEmpty() {
        emptyTextView.setVisibility(View.VISIBLE);
        placeOrderBtn.setEnabled(false);
        amountToPayTextView.setVisibility(View.INVISIBLE);
        totalTextView.setVisibility(View.INVISIBLE);
    }
}
