package com.github.v0id20.pizzaapp;

import android.content.ContentValues;
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

    Integer currentOrderId= 0;

    Button placeOrderBtn;
    TextView emptyTextView;
    ArrayList<Basket> currentOrderItemList;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Toolbar toolbar = ( Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Basket");
        //setTitle("My Basket");

        PizzaAppApplication application = (PizzaAppApplication) getApplication();

        currentOrderItemList = application.getOrderList();
        emptyTextView = findViewById(R.id.empty);
        placeOrderBtn = findViewById(R.id.place_order);

        if (currentOrderItemList.size() == 0) {
            emptyTextView.setVisibility(View.VISIBLE);
            placeOrderBtn.setEnabled(false);


        } else {
            emptyTextView.setVisibility(View.GONE);
            placeOrderBtn.setEnabled(true);

            RecyclerView recyclerView = findViewById(R.id.order_recycler);
            OrderAdapter orderAdapter = new OrderAdapter(currentOrderItemList);
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

    private void submitOrder(){
        SubmitOrderTask task = new SubmitOrderTask();
        task.execute(currentOrderItemList);

    }

    public class SubmitOrderTask extends AsyncTask<ArrayList<Basket>, Void, Void> {

        DatabaseHelper helper;

        @Override
        protected Void doInBackground(ArrayList<Basket>... orderLists) {

            ArrayList<Basket> orders = orderLists[0];
            try {
                helper = new DatabaseHelper(OrderActivity.this);
                SQLiteDatabase db = helper.getReadableDatabase();
                if (currentOrderId==0){
                    String[] column = new String[]{ "(SELECT max(ORDER_ID) FROM BASKET) AS max" };
                    Cursor cursor = db.query("BASKET", column, null, null, null , null , null);
                    if (cursor.moveToFirst()) {
                        int idColumnIndex = cursor.getColumnIndex("max");
                        currentOrderId = cursor.getInt(idColumnIndex)+1;
                    } else {
                        currentOrderId = 1;
                    }


                }


                for (int i = 0; i<orders.size(); i++){
                    ContentValues orderValues = new ContentValues();
                    orderValues.put("ORDER_ID", currentOrderId);
                    orderValues.put("NAME", orders.get(i).getName());
                    int quantity = orders.get(i).getQuantity();
                    orderValues.put("QUANTITY", quantity);
                    orderValues.put("PRICE", orders.get(i).getPrice()*quantity);
                    db.insert("BASKET", null, orderValues);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            emptyTextView.setVisibility(View.VISIBLE);
            placeOrderBtn.setEnabled(false);
            currentOrderItemList.clear();
            Toast.makeText(OrderActivity.this,"Thank you for your order", Toast.LENGTH_SHORT).show();
        }
    }
}
