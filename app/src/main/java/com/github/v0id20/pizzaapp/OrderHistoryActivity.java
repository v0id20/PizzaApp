package com.github.v0id20.pizzaapp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrderHistoryActivity extends AppCompatActivity {
    PizzaAppApplication application;
    RecyclerView orderHistoryRecycler;
    ArrayList<OrderHistoryItem> orderHistory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        orderHistoryRecycler = findViewById(R.id.order_history_recycler);
        application = (PizzaAppApplication)getApplication();
        orderHistory = application.getOrderHistory();
        OrderHistoryAdapter adapter = new OrderHistoryAdapter(orderHistory);
        orderHistoryRecycler.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        orderHistoryRecycler.setLayoutManager(llm);
    }
}
