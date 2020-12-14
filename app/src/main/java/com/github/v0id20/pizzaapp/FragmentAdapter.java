package com.github.v0id20.pizzaapp;

import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter {

    String[] tabNames;

    public FragmentAdapter(@NonNull FragmentManager fm, String[] tabNames) {
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
                return null;

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
