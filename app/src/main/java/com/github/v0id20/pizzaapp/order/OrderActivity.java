package com.github.v0id20.pizzaapp.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.v0id20.pizzaapp.model.Basket;
import com.github.v0id20.pizzaapp.model.BasketItem;
import com.github.v0id20.pizzaapp.model.DatabaseHelper;
import com.github.v0id20.pizzaapp.orderhistory.OrderHistoryActivity;
import com.github.v0id20.pizzaapp.PizzaAppApplication;
import com.github.v0id20.pizzaapp.R;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity implements OrderInterface.View{


    private Button placeOrderBtn;
    private TextView emptyTextView;
    RecyclerView recyclerView;
    double amountToPay;
    private Basket currentBasket;
    Integer currentOrderId = 0;
    private ArrayList<BasketItem> currentOrderList;
    OrderAdapter orderAdapter;
    PizzaAppApplication application;
OrderPresenter presenter;
    OnRemoveOrderItemListener onRemoveOrderItemListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Basket");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        application = (PizzaAppApplication) getApplication();
        DatabaseHelper helper = new DatabaseHelper(OrderActivity.this);
        presenter = new OrderPresenter(this, helper, application);

        emptyTextView = findViewById(R.id.empty);
        placeOrderBtn = findViewById(R.id.place_order);

        onRemoveOrderItemListener = new OnRemoveOrderItemListener() {
            @Override
            public void onRemoveOrderItem(int position) {
                presenter.removeOrderItem(position);
            }
        };

        presenter.start();

        placeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.submitOrder();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.stop();
    }

    @Override
    public void showOrder(ArrayList<BasketItem> orderList) {
        emptyTextView.setVisibility(View.GONE);
        placeOrderBtn.setEnabled(true);
        recyclerView = findViewById(R.id.order_recycler);
        orderAdapter = new OrderAdapter(orderList, onRemoveOrderItemListener);
        recyclerView.setAdapter(orderAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
    }

    @Override
    public void setBasketEmpty() {
        emptyTextView.setVisibility(View.VISIBLE);
        placeOrderBtn.setEnabled(false);
    }

    @Override
    public void startActivity() {
        Intent i = new Intent(OrderActivity.this, OrderHistoryActivity.class);
        startActivity(i);
    }

    @Override
    public void notifyAdapter() {
        orderAdapter.notifyDataSetChanged();
    }


}
