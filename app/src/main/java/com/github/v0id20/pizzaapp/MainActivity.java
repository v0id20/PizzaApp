package com.github.v0id20.pizzaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    String[] tabNames;

    public static final String EXTRA_POSITION = "position";
    public static final String EXTRA_DISH_TYPE = "dish type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = ( Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        tabNames = new String[]{getResources().getString(R.string.pizza), getResources().getString(R.string.pasta), getResources().getString(R.string.stores)};

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), tabNames);

        ViewPager vp = findViewById(R.id.view_pager);
        vp.setAdapter(adapter);

        TabLayout tabs = (TabLayout)findViewById(R.id.tab_layout);
        tabs.setupWithViewPager(vp);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
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
}