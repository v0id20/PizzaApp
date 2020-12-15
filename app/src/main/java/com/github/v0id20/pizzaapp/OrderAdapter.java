package com.github.v0id20.pizzaapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    final ArrayList<Basket> orders;
    double totalToPay;
    TextView totalToPayTextView;

    public OrderAdapter(ArrayList<Basket> orders, double totalToPay,TextView totalToPayTextView){
        this.orders = orders;
        this.totalToPay = totalToPay;
        this.totalToPayTextView = totalToPayTextView;
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
                if (orders.size()>1) {
                    double price = orders.get(upToDatePosition).getPrice();
                    int quantity = orders.get(upToDatePosition).getQuantity();
                    totalToPay-=price*quantity;
                    totalToPayTextView.setText(String.format(Double.toString(totalToPay), "d%2"));
                } else {


                    totalToPayTextView.setVisibility(View.INVISIBLE);
                }
                orders.remove(upToDatePosition);
                Log.i("order items", orders.toString());
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
