package com.github.v0id20.pizzaapp.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.v0id20.pizzaapp.model.BasketItem;
import com.github.v0id20.pizzaapp.R;
import com.github.v0id20.pizzaapp.dishinfo.DishInfoActivity;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final ArrayList<BasketItem> orders;
    OnRemoveOrderItemListener onRemoveOrderItemListener;

    public OrderAdapter(ArrayList<BasketItem> orders, OnRemoveOrderItemListener listener){
        this.orders = orders;
        this.onRemoveOrderItemListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==BasketItem.TYPE_ITEM){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item,parent, false);
            return new OrderItemViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_total,parent, false);

            return new TotalViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position)==BasketItem.TYPE_ITEM){
            View v = ((OrderItemViewHolder)holder).itemView;
            TextView productNameTV = v.findViewById(R.id.nameOrderTextView);
            TextView quantityTV = v.findViewById(R.id.quantityOrderTextView);
            TextView priceTV = v.findViewById(R.id.priceOrderTextView);

            productNameTV.setText(orders.get(position).getName());
            quantityTV.setText(Integer.toString(orders.get(position).getQuantity()));
            priceTV.setText(DishInfoActivity.formatPrice(orders.get(position).getPrice()*orders.get(position).getQuantity()));

            ImageView deleteImageView = v.findViewById(R.id.deleteOrderImageView);
            deleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int upToDatePosition = holder.getAdapterPosition();
                    onRemoveOrderItemListener.onRemoveOrderItem(upToDatePosition);
                    notifyDataSetChanged();
                }
            });
        } else {
            ((TotalViewHolder)holder).amountToPay.setText(DishInfoActivity.formatPrice(orders.get(position).getPrice()));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (orders.get(position).getType()==1){
            return BasketItem.TYPE_ITEM;
        } else {
            return  BasketItem.TYPE_TOTAL;
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class TotalViewHolder extends RecyclerView.ViewHolder {
        TextView amountToPay;
        public TotalViewHolder(@NonNull View itemView) {
            super(itemView);
            amountToPay = itemView.findViewById(R.id.amount_to_pay);
        }
    }

    public class OrderItemViewHolder extends RecyclerView.ViewHolder {
        View itemView;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }

}
