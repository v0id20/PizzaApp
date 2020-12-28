package com.github.v0id20.pizzaapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PizzaAdapter extends RecyclerView.Adapter<PizzaAdapter.MyViewHolder> {

    ArrayList<Dish> dishArrayList;
    OnItemClickListener listener;
    Context context;


    public PizzaAdapter(ArrayList<Dish> list, OnItemClickListener l, Context context){
        dishArrayList = list;
        listener = l;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        View view;

        public MyViewHolder(@NonNull View v) {
            super(v);
            view = v;
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item,parent, false);
        return new MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        View v = holder.view;
        TextView nameTextView = v.findViewById(R.id.name);
        nameTextView.setText(dishArrayList.get(position).getName());

        ImageView pizzaImageView = v.findViewById(R.id.pizzaImageView);
        int imageId = context.getResources().getIdentifier(dishArrayList.get(position).getImageResourceId(), "drawable", context.getPackageName());
        pizzaImageView.setImageResource(imageId);
        pizzaImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position);

            }
        });

    }

    @Override
    public int getItemCount() {
        return dishArrayList.size();
    }
}
