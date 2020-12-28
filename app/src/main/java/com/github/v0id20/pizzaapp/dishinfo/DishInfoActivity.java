package com.github.v0id20.pizzaapp.dishinfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.v0id20.pizzaapp.model.Basket;
import com.github.v0id20.pizzaapp.model.Dish;
import com.github.v0id20.pizzaapp.main.MainActivity;
import com.github.v0id20.pizzaapp.model.Pasta;
import com.github.v0id20.pizzaapp.model.Pizza;
import com.github.v0id20.pizzaapp.PizzaAppApplication;
import com.github.v0id20.pizzaapp.R;


import java.util.Locale;

public class DishInfoActivity extends AppCompatActivity implements DishInfoPresenterInterface.View {
    public static final String EXTRA_DISH_VALUE_PIZZA = "pizza";

    private RadioGroup radioGroup;
    private TextView nameTV;
    private TextView descriptionTV;
    private TextView quantityTV;
    private TextView priceTV;
    private ImageView dishIV;
    private Button addToOrder;
    private Basket applicationBasket;

    private PizzaAppApplication application;
    private DishInfoPresenter presenter;

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

        ImageView removeIV = findViewById(R.id.remove);
        removeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = quantityTV.getText().toString();
                presenter.changeQuantity(false);
            }
        });
        ImageView addIV = findViewById(R.id.add);
        addIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = quantityTV.getText().toString();
                presenter.changeQuantity(true);
            }
        });

        addToOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.addToOrder();
            }
        });
    }

    public static String formatPrice(double price) {
        return String.format(("$%.2f"), price);
    }

    @Override
    public void showDish(Dish dish) {
        int imageId = getResources().getIdentifier(dish.getImageResourceId(), "drawable", this.getPackageName());
        dishIV.setImageResource(imageId);
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
        radioGroup.setVisibility(View.GONE);
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
