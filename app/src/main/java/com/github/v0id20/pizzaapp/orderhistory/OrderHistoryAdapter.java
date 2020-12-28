package com.github.v0id20.pizzaapp.orderhistory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.v0id20.pizzaapp.R;

import java.util.ArrayList;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {
    ArrayList<OrderHistoryItem> orderHistory;

    public OrderHistoryAdapter(ArrayList<OrderHistoryItem> orderHistory) {
        this.orderHistory = orderHistory;
    }

    @NonNull
    @Override
    public OrderHistoryAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
            int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_item,
                parent, false);
        return new OrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.OrderViewHolder holder,
            final int position) {

        final OrderHistoryItem item = orderHistory.get(position);
        TextView orderNumberTv = holder.itemView.findViewById(R.id.order_number);
        orderNumberTv.setText("Order #"+ orderHistory.get(position).getOrderId());

//        TextView orderDateTv = holder.itemView.findViewById(R.id.order_date);
//        orderNumberTv.setText(Integer.toString(orderHistory.get(position).getOrderId()));

        TextView orderTotalTv = holder.itemView.findViewById(R.id.order_total);
        orderTotalTv.setText(Double.toString(orderHistory.get(position).getTotalToPay()));

        TextView orderItemsTv = holder.itemView.findViewById(R.id.order_items);
        orderItemsTv.setVisibility(item.isExpanded()? View.VISIBLE : View.GONE);
        orderItemsTv.setText(orderHistory.get(position).toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current state of the item
                boolean expanded = item.isExpanded();
                // Change the state
                item.setExpanded(!expanded);
                // Notify the adapter that item has changed
                notifyItemChanged(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderHistory.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        View itemView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }
}
