package com.github.v0id20.pizzaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.v0id20.pizzaapp.dishinfo.DishInfoActivity;

public class PizzaFragment extends Fragment {

    OnItemClickListener listener;
    PizzaAppApplication application;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        listener = new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent i = new Intent(getActivity(), DishInfoActivity.class);
                i.putExtra(MainActivity.EXTRA_POSITION, position);
                i.putExtra(MainActivity.EXTRA_DISH_TYPE,DishInfoActivity.EXTRA_DISH_VALUE_PIZZA);
                startActivity(i);
            }
        };
//
//
    }
        @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        RecyclerView rec = (RecyclerView) inflater.inflate(R.layout.pizza_fragment, container,false);
        PizzaAdapter pizzaAdapter = new PizzaAdapter(((PizzaAppApplication)getActivity().getApplication()).getPizzaList(), listener);
        rec.setAdapter(pizzaAdapter);
        GridLayoutManager glm = new GridLayoutManager(getContext(), 2);
        rec.setLayoutManager(glm);
        return rec;

    }
}
