package com.github.v0id20.pizzaapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DishInfoAcivity extends AppCompatActivity {

    TextView quantityTV;
    RadioButton smallSize;
    RadioButton mediumSize;
    RadioButton largeSize;
    Button addToOrder;

    int quantityToOrder = 1;
    String productName;
    double productPrice;

    PizzaAppApplication application;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_info);

        Intent receivedIntent = getIntent();
        int id = receivedIntent.getIntExtra(MainActivity.EXTRA_POSITION, 0);
        String dish_type = receivedIntent.getStringExtra(MainActivity.EXTRA_DISH_TYPE);

        application = (PizzaAppApplication) getApplication();
        if (application != null) {
            Dish currentDish = new Dish();
            if (dish_type.equals(PizzaFragment.EXTRA_DISH_PIZZA)) {
                //initialize radiobuttons and make them visible
                currentDish = (Dish) application.getPizzaList().get(id);
            } else if (dish_type.equals(PastaFragment.EXTRA_DISH_VALUE_PASTA)) {
                currentDish = (Dish) application.getPastaList().get(id);
            }

            if (currentDish!=null) {
                TextView nameTV = findViewById(R.id.name);
                productName = currentDish.getName();
                nameTV.setText(productName);

                TextView descriptionTV = findViewById(R.id.description);
                descriptionTV.setText(currentDish.getDescription());

                TextView priceTV = findViewById(R.id.price);
                productPrice = currentDish.getPrice();
                priceTV.setText(Double.toString(productPrice));

                ImageView pizzaIV = findViewById(R.id.pizza_info_image);
                pizzaIV.setImageResource(currentDish.getImageResourceId());
            }


        }


        quantityTV = findViewById(R.id.quantity);

        ImageView removeIV = findViewById(R.id.remove);
        removeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeQuantity(false);
            }
        });

        ImageView addIV = findViewById(R.id.add);
        addIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeQuantity(true);
            }
        });


        addToOrder = findViewById(R.id.add_to_order_btn);
        addToOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Basket basketItem = new Basket(productName, quantityToOrder, productPrice);
                application.getOrderList().add(basketItem);
                Log.i("basket item", productName + " " + quantityToOrder);
                Toast.makeText(DishInfoAcivity.this, "Added to your basket", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public void changeQuantity(boolean add) {
        int quantity = Integer.valueOf(quantityTV.getText().toString());
        if (add) {
            quantity += 1;
            quantityToOrder += 1;
        } else {
            quantity -= 1;
            quantityToOrder -= 1;
        }
        quantityTV.setText(Integer.toString(quantity));

    }
}
