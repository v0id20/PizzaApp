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

import java.util.ArrayList;

public class PastaFragment extends Fragment {

    RecyclerView pastaRecycler;
    ArrayList<Dish> dishArrayList;
    OnItemClickListener listener;
    public static final String EXTRA_DISH_VALUE_PASTA = "pasta";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listener = new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent i = new Intent(getActivity(), DishInfoActivity.class);
                i.putExtra(MainActivity.EXTRA_POSITION, position);
                i.putExtra(MainActivity.EXTRA_DISH_TYPE, EXTRA_DISH_VALUE_PASTA);
                startActivity(i);

            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.pasta_fragment, container, false);
       pastaRecycler = v.findViewById(R.id.pasta_recycler_view);
       dishArrayList = ((PizzaAppApplication)getActivity().getApplication()).getPastaList();
       PizzaAdapter pastaAdapter = new PizzaAdapter(dishArrayList,listener);
       pastaRecycler.setAdapter(pastaAdapter);
       GridLayoutManager glm = new GridLayoutManager(getContext(), 2);
       pastaRecycler.setLayoutManager(glm);

       return v;
    }
}
