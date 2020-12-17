package com.github.v0id20.pizzaapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Locale;

public class DishInfoActivity extends AppCompatActivity {

    TextView quantityTV;
    RadioGroup radioGroup;
    RadioButton smallSize;
    RadioButton mediumSize;
    RadioButton largeSize;
    Button addToOrder;

    int quantityToOrder = 1;
    int basketItemCount;
    String productName;
    double productPrice;
    double productPriceSmall;
    double productPriceMedium;
    double productPriceLarge;

    PizzaAppApplication application;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_info);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent receivedIntent = getIntent();
        int id = receivedIntent.getIntExtra(MainActivity.EXTRA_POSITION, 0);
        String dish_type = receivedIntent.getStringExtra(MainActivity.EXTRA_DISH_TYPE);

        addToOrder = findViewById(R.id.add_to_order_btn);
        quantityTV = findViewById(R.id.quantity);

        final TextView priceTV = findViewById(R.id.price);

        application = (PizzaAppApplication) getApplication();
        final Basket baset = application.getBasket();


        Dish currentDish = new Dish();
        //
        basketItemCount = application.getBasketItemCount();
        if (dish_type.equals(PizzaFragment.EXTRA_DISH_PIZZA)) {

            //initialize radiobuttons and make them visible
            currentDish = (Dish) application.getPizzaList().get(id);
            Pizza currentPizza =(Pizza)currentDish;

            productPriceSmall = currentPizza.getPriceSmall();
            productPriceMedium = currentPizza.getPriceMedium();
            productPriceLarge = currentPizza.getPriceLarge();

            radioGroup = findViewById(R.id.size_radio_group);
            radioGroup.setVisibility(View.VISIBLE);
            radioGroup.clearCheck();
            radioGroup.check(R.id.size_small);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.size_small:
                            priceTV.setText(formatPrice(productPriceSmall));
                            productPrice = productPriceSmall;

                            break;
                        case R.id.size_medium:
                            priceTV.setText(formatPrice(productPriceMedium));
                            productPrice = productPriceMedium;
                            break;
                        case R.id.size_large:
                            priceTV.setText(formatPrice(productPriceLarge));
                            productPrice = productPriceLarge;
                            break;
                        default:
                            break;
                    }
                    String buttonText = String.format(Locale.getDefault(), "Add %d to order - $%.2f", quantityToOrder, productPrice * quantityToOrder);
                    addToOrder.setText(buttonText);
                }
            });
        } else if (dish_type.equals(PastaFragment.EXTRA_DISH_VALUE_PASTA)) {
            currentDish = (Dish) application.getPastaList().get(id);
        }

        TextView nameTV = findViewById(R.id.name);
        productName = currentDish.getName();
        nameTV.setText(productName);

        TextView descriptionTV = findViewById(R.id.description);
        descriptionTV.setText(currentDish.getDescription());

        productPrice = currentDish.getPrice();
        priceTV.setText(formatPrice(productPrice));
        addToOrder.setText(String.format(Locale.getDefault(), "Add 1 to order - $%.2f", productPrice));

        ImageView pizzaIV = findViewById(R.id.pizza_info_image);
        pizzaIV.setImageResource(currentDish.getImageResourceId());


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


        addToOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BasketItem basketItem = new BasketItem();
                if (application != null) {
                    //ArrayList<BasketItem> basket = application.getOrderList();
                    ArrayList<BasketItem> basket = application.getBasket().basket;
                    if (basket.size() > 0) {
                        for (int i = 0; i < basket.size(); i++) {
                            if (basket.get(i).getName().equals(productName)) {
                                if (basket.get(i).getPrice()==productPrice) {
                                    baset.totalItemCount += quantityToOrder;
                                    quantityToOrder = basket.get(i).getQuantity() + quantityToOrder;

                                    application.setBasketItemCount(basketItemCount + quantityToOrder);
                                    basket.set(i, new BasketItem(productName, quantityToOrder, productPrice));
                                    Toast.makeText(DishInfoActivity.this, "Added to your basket", Toast.LENGTH_SHORT).show();
                                    finish();
                                    return;
                                } else {
                                    continue;
                                }
//                                baset.totalItemCount += quantityToOrder;
//                                quantityToOrder = basket.get(i).getQuantity() + quantityToOrder;
//
//                                application.setBasketItemCount(basketItemCount + quantityToOrder);
//                                basket.set(i, new BasketItem(productName, quantityToOrder, productPrice));
//                                Toast.makeText(DishInfoActivity.this, "Added to your basket", Toast.LENGTH_SHORT).show();
//                                finish();
//                                return;
                            }
                        }
                    }
                    basketItem = new BasketItem(productName, quantityToOrder, productPrice);
                    baset.totalItemCount += quantityToOrder;
                    application.setBasketItemCount(basketItemCount + quantityToOrder);
                    basket.add(basketItem);
                }
                Log.i("basket item", productName + " " + quantityToOrder);
                Toast.makeText(DishInfoActivity.this, "Added to your basket", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void changeQuantity(boolean add) {
        int quantity = Integer.valueOf(quantityTV.getText().toString());
        if (add) {
            quantity += 1;
            quantityToOrder += 1;
        } else {
            quantity -= 1;
            quantityToOrder -= 1;
        }
        quantityTV.setText(Integer.toString(quantity));
        String buttonText = String.format(Locale.getDefault(), "Add %d to order - $%.2f", quantityToOrder, productPrice * quantityToOrder);
        addToOrder.setText(buttonText);
    }

    public static String formatPrice(double price){
        return String.format(("$%.2f"), price);

    }

}
