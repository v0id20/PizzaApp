package com.github.v0id20.pizzaapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {
    ArrayList<Store> storeList;
    OnMapListItemClickListener mapItemClickListener;

    public StoreAdapter(ArrayList<Store> storeList, OnMapListItemClickListener listener){
        this.storeList = storeList;
        mapItemClickListener = listener;
    }


    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_item,parent,false);
       // View v = parent.findViewById(R.id.store_item_layout);
        StoreViewHolder viewHolder = new StoreViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, final int position) {
        View v = holder.storeItemView;
        TextView addressTV = v.findViewById(R.id.address);
        addressTV.setText(storeList.get(position).getAddress());
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapItemClickListener.onMapListItemClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }

    public class StoreViewHolder extends RecyclerView.ViewHolder {
        View storeItemView;
        public StoreViewHolder(@NonNull View itemView) {
            super(itemView);
            this.storeItemView = itemView;
        }
    }
}
