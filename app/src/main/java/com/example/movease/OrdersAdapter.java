package com.example.movease;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Map;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    private List<Map<String, Object>> orders;

    public OrdersAdapter(List<Map<String, Object>> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, Object> order = orders.get(position);
        String from = (String) order.get("fromAddress");
        String to = (String) order.get("toAddress");
        double cost = (double) order.get("totalCost");
        String labor = (String) order.get("laborId");
        String packing = (String) order.get("packingId");
        String transport = (String) order.get("transportId");

        holder.tvAddresses.setText("From: " + from + "\nTo: " + to);
        holder.tvServices.setText("Labor: " + labor + ", Packing: " + packing + ", Transport: " + transport);
        holder.tvCost.setText("Total Cost: PKR " + String.format("%.0f", cost));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAddresses, tvServices, tvCost;
        ViewHolder(View itemView) {
            super(itemView);
            tvAddresses = itemView.findViewById(R.id.tvOrderAddresses);
            tvServices = itemView.findViewById(R.id.tvOrderServices);
            tvCost = itemView.findViewById(R.id.tvOrderCost);
        }
    }
}