package com.github.v0id20.pizzaapp;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class BasketMenuItem {

    TextView basketItemCountTextView;
    Basket basket;
    int basketItemCount;
    Activity activity;



//    public boolean onBasketClick(MenuItem item){
//        if (item.getItemId() == R.id.basket) {
//            Intent i = new Intent(MainActivity.this, OrderActivity.class);
//            startActivity(i);
//            return true;
//        } else {
//            return false;
//        }
//    }

    public void setupBasketMenuItem(Menu menu){

        MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.basket);
        menuItem.setActionView(R.layout.actionbar_cart);
        View actionView = menuItem.getActionView();
        basketItemCountTextView = actionView.findViewById(R.id.cart_badge);
        setupBasketBadge();

    }

    public void setupBasketBadge(){
        if (basketItemCountTextView != null) {
            //hide item count if basket is empty, show it otherwise
            if (basketItemCount == 0) {
                if (basketItemCountTextView.getVisibility() != View.GONE) {
                    basketItemCountTextView.setVisibility(View.GONE);
                }
            } else {
                basketItemCountTextView.setText(String.valueOf(Math.min(basketItemCount, 99)));
                if (basketItemCountTextView.getVisibility() != View.VISIBLE) {
                    basketItemCountTextView.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
