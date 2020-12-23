package com.github.v0id20.pizzaapp.dishinfo;

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

import com.github.v0id20.pizzaapp.Basket;
import com.github.v0id20.pizzaapp.BasketItem;
import com.github.v0id20.pizzaapp.Dish;
import com.github.v0id20.pizzaapp.MainActivity;
import com.github.v0id20.pizzaapp.Pasta;
import com.github.v0id20.pizzaapp.PastaFragment;
import com.github.v0id20.pizzaapp.Pizza;
import com.github.v0id20.pizzaapp.PizzaAppApplication;
import com.github.v0id20.pizzaapp.PizzaFragment;
import com.github.v0id20.pizzaapp.R;

import java.util.ArrayList;
import java.util.Locale;

public class DishInfoActivity extends AppCompatActivity implements DishInfoPresenterInterface.View {
    public static final String EXTRA_DISH_VALUE_PIZZA = "pizza";

    TextView quantityTV;
    RadioGroup radioGroup;
    TextView nameTV;
    TextView descriptionTV;
    TextView priceTV;
    ImageView dishIV;
    Button addToOrder;

    Basket applicationBasket;
    PizzaAppApplication application;
    DishInfoPresenter presenter;

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

        application = (PizzaAppApplication) getApplication();
        applicationBasket = application.getBasket();

        presenter = new DishInfoPresenter(this, id, dish_type, applicationBasket, application);

        addToOrder = findViewById(R.id.add_to_order_btn);
        nameTV = findViewById(R.id.name);
        descriptionTV = findViewById(R.id.description);
        quantityTV = findViewById(R.id.quantity);
        priceTV = findViewById(R.id.price);
        dishIV = findViewById(R.id.pizza_info_image);
        radioGroup = findViewById(R.id.size_radio_group);

        presenter.start();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                presenter.onSizeSelected(checkedId);
            }
        });


//        Dish currentDish = new Dish();
//        if (dish_type.equals(EXTRA_DISH_VALUE_PIZZA)) {
//
//            //initialize radiobuttons and make them visible
//            currentDish = (Dish) application.getPizzaList().get(id);
//            Pizza currentPizza = (Pizza) currentDish;
//
//            productPriceSmall = currentPizza.getPriceSmall();
//            productPriceMedium = currentPizza.getPriceMedium();
//            productPriceLarge = currentPizza.getPriceLarge();
//
//            radioGroup = findViewById(R.id.size_radio_group);
//            radioGroup.setVisibility(View.VISIBLE);
//            radioGroup.clearCheck();
//            radioGroup.check(R.id.size_small);
//            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(RadioGroup group, int checkedId) {
//                    presenter.onSizeSelected(checkedId);
////                    switch (checkedId) {
////                        case R.id.size_small:
////                            priceTV.setText(formatPrice(productPriceSmall));
////                            productPrice = productPriceSmall;
////                            break;
////                        case R.id.size_medium:
////                            priceTV.setText(formatPrice(productPriceMedium));
////                            productPrice = productPriceMedium;
////                            break;
////                        case R.id.size_large:
////                            priceTV.setText(formatPrice(productPriceLarge));
////                            productPrice = productPriceLarge;
////                            break;
////                        default:
////                            break;
////                    }
////                    String buttonText = String.format(Locale.getDefault(), "Add %d to order - $%.2f", quantityToOrder, productPrice * quantityToOrder);
////                    addToOrder.setText(buttonText);
//                }
//            });
//        } else if (dish_type.equals(PastaFragment.EXTRA_DISH_VALUE_PASTA)) {
//            currentDish = (Dish) application.getPastaList().get(id);
//        }

        ImageView removeIV = findViewById(R.id.remove);
        removeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = quantityTV.getText().toString();
                presenter.changeQuantity(false, text);
            }
        });
        ImageView addIV = findViewById(R.id.add);
        addIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = quantityTV.getText().toString();
                presenter.changeQuantity(true, text);
            }
        });

        addToOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.addToOrder();
            }
        });
    }

//    private void changeQuantity(boolean add) {
//        int quantity = Integer.valueOf(quantityTV.getText().toString());
//        if (add) {
//            quantity += 1;
//            quantityToOrder += 1;
//        } else {
//            quantity -= 1;
//            quantityToOrder -= 1;
//        }
//        quantityTV.setText(Integer.toString(quantity));
//        String buttonText = String.format(Locale.getDefault(), "Add %d to order - $%.2f", quantityToOrder, productPrice * quantityToOrder);
//        addToOrder.setText(buttonText);
//    }

    public static String formatPrice(double price) {
        return String.format(("$%.2f"), price);
    }

    @Override
    public void showDish(Dish dish) {
        dishIV.setImageResource(dish.getImageResourceId());
        nameTV.setText(dish.getName());
        descriptionTV.setText(dish.getDescription());
        priceTV.setText(formatPrice(dish.getPrice()));
        addToOrder.setText(String.format(Locale.getDefault(), "Add 1 to order - $%.2f", dish.getPrice()));
    }

    @Override
    public void showPizza(Pizza pizza) {
        showDish(pizza);
        radioGroup.setVisibility(View.VISIBLE);
        radioGroup.clearCheck();
        radioGroup.check(R.id.size_small);
    }

    @Override
    public void showPasta(Pasta pasta) {
        showDish(pasta);
        // radioGroup.setVisibility(View.GONE);
    }

    @Override
    public void setQuantity(int quantity) {
        quantityTV.setText(Integer.toString(quantity));
    }

    @Override
    public void setPrice(double price) {
        priceTV.setText(formatPrice(price));
    }

    @Override
    public void setButtonText(String text) {
        addToOrder.setText(text);
    }

    @Override
    public void close() {
        Toast.makeText(DishInfoActivity.this, "Added to your basket", Toast.LENGTH_SHORT).show();
        finish();
    }
}