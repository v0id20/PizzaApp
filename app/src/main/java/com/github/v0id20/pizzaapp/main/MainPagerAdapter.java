package com.github.v0id20.pizzaapp.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.github.v0id20.pizzaapp.main.PastaFragment;
import com.github.v0id20.pizzaapp.main.PizzaFragment;
import com.github.v0id20.pizzaapp.main.StoreFragment;

public class MainPagerAdapter extends androidx.fragment.app.FragmentPagerAdapter {

    String[] tabNames;

    public MainPagerAdapter(@NonNull FragmentManager fm, String[] tabNames) {
        super(fm);
        this.tabNames = tabNames;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PizzaFragment();
            case 1:
                return new PastaFragment();
            case 2:
                return new StoreFragment();
            default:
                throw new IllegalArgumentException("Wrong position");
        }
    }

    @Override
    public int getCount() {
        return tabNames.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabNames[position];
    }
}
