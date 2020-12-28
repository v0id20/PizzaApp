package com.github.v0id20.pizzaapp.orderhistory;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.v0id20.pizzaapp.PizzaAppApplication;
import com.github.v0id20.pizzaapp.R;

import java.util.ArrayList;

public class OrderHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        RecyclerView orderHistoryRecycler = findViewById(R.id.order_history_recycler);
        PizzaAppApplication application = (PizzaAppApplication) getApplication();

        ArrayList<OrderHistoryItem> orderHistory = application.getOrderHistory();
        OrderHistoryAdapter adapter = new OrderHistoryAdapter(orderHistory);
        orderHistoryRecycler.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        orderHistoryRecycler.setLayoutManager(llm);
    }
}
