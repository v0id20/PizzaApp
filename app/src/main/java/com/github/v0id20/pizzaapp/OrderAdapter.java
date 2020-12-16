package com.github.v0id20.pizzaapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    final ArrayList<BasketItem> orders;
    OnRemoveOrderItemListener onRemoveOrderItemListener;

    public OrderAdapter(ArrayList<BasketItem> orders, OnRemoveOrderItemListener listener){
        this.orders = orders;
        this.onRemoveOrderItemListener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item,parent, false);
        return new OrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderViewHolder holder, final int position) {
        View v = holder.itemView;

        TextView productNameTV = v.findViewById(R.id.nameOrderTextView);
        TextView quantityTV = v.findViewById(R.id.quantityOrderTextView);
        TextView priceTV = v.findViewById(R.id.priceOrderTextView);

        productNameTV.setText(orders.get(position).getName());
        quantityTV.setText(Integer.toString(orders.get(position).getQuantity()));
        priceTV.setText(Double.toString(orders.get(position).getPrice()*orders.get(position).getQuantity()));

        ImageView deleteImageView = v.findViewById(R.id.deleteOrderImageView);
        deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int upToDatePosition = holder.getAdapterPosition();
                onRemoveOrderItemListener.onRemoveOrderItem(upToDatePosition);
                notifyItemRemoved(upToDatePosition);

            }
        });

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        View itemView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }
}
