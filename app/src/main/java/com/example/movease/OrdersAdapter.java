package com.example.movease;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    private List<Map<String, Object>> orders;
    private Set<String> myServiceIds;

    public OrdersAdapter(List<Map<String, Object>> orders, Set<String> myServiceIds) {
        this.orders = orders;
        this.myServiceIds = myServiceIds;
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
        String status = (String) order.get("status");
        if (status == null) status = "pending";

        holder.tvAddresses.setText("From: " + from + "\nTo: " + to);
        holder.tvCost.setText("Total Cost: PKR " + String.format("%.0f", cost));
        holder.tvStatus.setText("Status: " + status);

        String docId = (String) order.get("docId");
        boolean isAccepted = "accepted".equals(status);
        boolean isCompleted = "completed".equals(status);
        boolean isRated = "rated".equals(status);

        // Show Accept button only if pending and contains one of our services
        holder.btnAccept.setVisibility(
                "pending".equals(status) && docId != null ? View.VISIBLE : View.GONE);
        // Show Complete button only if accepted
        holder.btnComplete.setVisibility(isAccepted && docId != null ? View.VISIBLE : View.GONE);

        holder.btnAccept.setOnClickListener(v -> {
            if (v.getContext() instanceof OrdersActivity) {
                ((OrdersActivity) v.getContext()).acceptBooking(docId);
            }
        });

        holder.btnComplete.setOnClickListener(v -> {
            if (v.getContext() instanceof OrdersActivity) {
                ((OrdersActivity) v.getContext()).completeBooking(docId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAddresses, tvCost, tvStatus;
        Button btnAccept, btnComplete;

        ViewHolder(View itemView) {
            super(itemView);
            tvAddresses = itemView.findViewById(R.id.tvOrderAddresses);
            tvCost = itemView.findViewById(R.id.tvOrderCost);
            tvStatus = itemView.findViewById(R.id.tvOrderStatus);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnComplete = itemView.findViewById(R.id.btnComplete);
        }
    }
}