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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {

    Integer currentOrderId = 0;

    Button placeOrderBtn;
    TextView emptyTextView;
    ArrayList<Basket> currentOrderList;
    TextView totalToPayTextView;
    TextView totalTextView;
    double totalToPay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Basket");
        //setTitle("My Basket");

        PizzaAppApplication application = (PizzaAppApplication) getApplication();

        currentOrderList = application.getOrderList();
        emptyTextView = findViewById(R.id.empty);
        totalToPayTextView = findViewById(R.id.total_amount);
        totalTextView = findViewById(R.id.total);
        placeOrderBtn = findViewById(R.id.place_order);


        for (Basket item :currentOrderList){
            totalToPay +=item.getPrice()*item.getQuantity();
        }




        if (currentOrderList.size() == 0) {
            setBasketEmpty();

        } else {
            emptyTextView.setVisibility(View.GONE);
            placeOrderBtn.setEnabled(true);
            totalToPayTextView.setVisibility(View.VISIBLE);
            totalToPayTextView.setText(Double.toString(totalToPay));

            RecyclerView recyclerView = findViewById(R.id.order_recycler);
            OrderAdapter orderAdapter = new OrderAdapter(currentOrderList, totalToPay, totalToPayTextView);
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

    public class SubmitOrderTask extends AsyncTask<ArrayList<Basket>, Void, Void> {

        DatabaseHelper helper;

        @Override
        protected Void doInBackground(ArrayList<Basket>... orderLists) {

            ArrayList<Basket> orders = orderLists[0];
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


    public void setBasketEmpty(){
        emptyTextView.setVisibility(View.VISIBLE);
        placeOrderBtn.setEnabled(false);
        totalToPayTextView.setVisibility(View.INVISIBLE);
        totalTextView.setVisibility(View.INVISIBLE);
    }
}
