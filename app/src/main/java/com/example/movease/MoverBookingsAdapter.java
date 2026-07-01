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

public class MoverBookingsAdapter extends RecyclerView.Adapter<MoverBookingsAdapter.ViewHolder> {

    private List<Map<String, Object>> bookings;

    public MoverBookingsAdapter(List<Map<String, Object>> bookings) {
        this.bookings = bookings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mover_booking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, Object> booking = bookings.get(position);
        String from = (String) booking.get("fromAddress");
        String to = (String) booking.get("toAddress");
        double cost = (double) booking.get("totalCost");
        String status = (String) booking.get("status");
        if (status == null) status = "pending";

        holder.tvAddresses.setText("From: " + from + "\nTo: " + to);
        holder.tvCost.setText("Total Cost: PKR " + String.format("%.0f", cost));
        holder.tvStatus.setText("Status: " + status);

        String docId = (String) booking.get("docId");
        String laborId = (String) booking.get("laborId");
        String packingId = (String) booking.get("packingId");
        String transportId = (String) booking.get("transportId");

        // Show rate button only if status is completed
        boolean showRate = "completed".equals(status) && docId != null;
        holder.btnRate.setVisibility(showRate ? View.VISIBLE : View.GONE);

        holder.btnRate.setOnClickListener(v -> {
            if (v.getContext() instanceof MoverBookingsActivity) {
                ((MoverBookingsActivity) v.getContext()).showRatingDialog(docId, laborId, packingId, transportId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAddresses, tvCost, tvStatus;
        Button btnRate;

        ViewHolder(View itemView) {
            super(itemView);
            tvAddresses = itemView.findViewById(R.id.tvBookingAddresses);
            tvCost = itemView.findViewById(R.id.tvBookingCost);
            tvStatus = itemView.findViewById(R.id.tvBookingStatus);
            btnRate = itemView.findViewById(R.id.btnRate);
        }
    }
}