package com.github.v0id20.pizzaapp.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.v0id20.pizzaapp.DataManager;
import com.github.v0id20.pizzaapp.PizzaAppApplication;
import com.github.v0id20.pizzaapp.R;
import com.github.v0id20.pizzaapp.order.OrderActivity;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_POSITION = "position";
    public static final String EXTRA_DISH_TYPE = "dish type";

    private String[] tabNames;
    private int basketItemCount;
    private TextView textBasketItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabNames = new String[]{getResources().getString(R.string.pizza), getResources().getString(R.string.pasta), getResources().getString(R.string.stores)};
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager(), tabNames);

        ViewPager vp = findViewById(R.id.view_pager);
        vp.setAdapter(adapter);

        TabLayout tabs = findViewById(R.id.tab_layout);
        tabs.setupWithViewPager(vp);

        DataManager m = ((PizzaAppApplication)getApplication()).getDataManager();
        basketItemCount = m.getBasket().getTotalItemCount();

    }

    @Override
    protected void onResume() {
        super.onResume();
        DataManager m = ((PizzaAppApplication)getApplication()).getDataManager();
        basketItemCount = m.getBasket().getTotalItemCount();
        setupBasketBadge(basketItemCount);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.basket);
        menuItem.setActionView(R.layout.actionbar_cart);
        View actionView = menuItem.getActionView();
        textBasketItemCount = actionView.findViewById(R.id.cart_badge);
        setupBasketBadge(basketItemCount);

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.basket) {
            Intent i = new Intent(MainActivity.this, OrderActivity.class);
            startActivity(i);
            return true;
        } else {
            return false;
        }

    }

    public void setupBasketBadge(int basketItemCount) {
        if (textBasketItemCount != null) {
            //hide item count if basket is empty, show it otherwise
            if (basketItemCount == 0) {
                if (textBasketItemCount.getVisibility() != View.GONE) {
                    textBasketItemCount.setVisibility(View.GONE);
                }
            } else {
                textBasketItemCount.setText(String.valueOf(Math.min(basketItemCount, 99)));
                if (textBasketItemCount.getVisibility() != View.VISIBLE) {
                    textBasketItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}