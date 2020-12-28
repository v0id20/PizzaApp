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

import com.github.v0id20.pizzaapp.DataManager;
import com.github.v0id20.pizzaapp.model.Basket;
import com.github.v0id20.pizzaapp.model.BasketItem;
import com.github.v0id20.pizzaapp.model.DatabaseHelper;
import com.github.v0id20.pizzaapp.orderhistory.OrderHistoryActivity;
import com.github.v0id20.pizzaapp.PizzaAppApplication;
import com.github.v0id20.pizzaapp.R;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity implements OrderInterface.View {

    private Button placeOrderBtn;
    private TextView emptyTextView;
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private DataManager mDataManager;
    private OrderPresenter presenter;
    private OnRemoveOrderItemListener onRemoveOrderItemListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Basket");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDataManager = ((PizzaAppApplication) getApplication()).getDataManager();
        presenter = new OrderPresenter(this,this, mDataManager);

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
    public void showOrder(ArrayList<BasketItem> orderList, double price) {
        emptyTextView.setVisibility(View.GONE);
        placeOrderBtn.setEnabled(true);
        recyclerView = findViewById(R.id.order_recycler);
        recyclerView.setVisibility(View.VISIBLE);
        orderAdapter = new OrderAdapter(orderList, price, onRemoveOrderItemListener);
        recyclerView.setAdapter(orderAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
    }

    @Override
    public void setBasketEmpty() {
        emptyTextView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        placeOrderBtn.setEnabled(false);
    }

    @Override
    public void startActivity() {
        Intent i = new Intent(OrderActivity.this, OrderHistoryActivity.class);
        startActivity(i);
    }

    @Override
    public void notifyAdapter(double totalPrice) {
        orderAdapter.notifyDataSetChanged();
        orderAdapter.setTotalPrice(totalPrice);
    }


}
