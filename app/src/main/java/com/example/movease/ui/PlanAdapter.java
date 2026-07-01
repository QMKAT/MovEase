package com.example.movease.ui;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.movease.MapActivity;
import com.example.movease.PlanDetailActivity;
import com.example.movease.R;
import com.example.movease.engine.Plan;
import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {
    private List<Plan> plans;
    private String fromAddress;

    public PlanAdapter(List<Plan> plans, String fromAddress) {
        this.plans = plans;
        this.fromAddress = fromAddress;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Plan plan = plans.get(position);

        holder.tvHouse.setText("House: " + plan.getHouse().getAddress() + " (Price: PKR " + String.format("%.0f", plan.getHouse().getPrice()) + ")");
        holder.tvLabor.setText("Labor: " + plan.getLabor().getName() + " (Rate/hr: " + plan.getLabor().getRatePerHour() + ")");
        holder.tvPacking.setText("Packing: " + plan.getPacking().getName() + " (Cost/box: " + plan.getPacking().getCostPerBox() + ")");
        holder.tvTransport.setText("Transport: " + plan.getTransport().getName() + " (Cost/km: " + plan.getTransport().getCostPerKm() + ")");
        holder.tvTotalCost.setText("Total Estimated Cost: PKR " + String.format("%.0f", plan.getTotalCost()));
        holder.tvScore.setText("Score: " + String.format("%.1f", plan.getScore() * 5) + " / 5");

        // Advantages
        holder.llAdvantages.removeAllViews();
        for (String adv : plan.getAdvantages()) {
            TextView tv = new TextView(holder.itemView.getContext());
            tv.setText("✓ " + adv);
            tv.setTextSize(12);
            tv.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.advantage));
            holder.llAdvantages.addView(tv);
        }

        // Disadvantages
        holder.llDisadvantages.removeAllViews();
        for (String dis : plan.getDisadvantages()) {
            TextView tv = new TextView(holder.itemView.getContext());
            tv.setText("✗ " + dis);
            tv.setTextSize(12);
            tv.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.disadvantage));
            holder.llDisadvantages.addView(tv);
        }

        // Toggle details visibility
        holder.btnToggleDetails.setOnClickListener(v -> {
            if (holder.llAdvantages.getVisibility() == View.GONE) {
                holder.llAdvantages.setVisibility(View.VISIBLE);
                holder.llDisadvantages.setVisibility(View.VISIBLE);
                holder.btnToggleDetails.setText("Hide Details");
            } else {
                holder.llAdvantages.setVisibility(View.GONE);
                holder.llDisadvantages.setVisibility(View.GONE);
                holder.btnToggleDetails.setText("Show Details");
            }
        });

        // Map button
        holder.btnMap.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), MapActivity.class);
            intent.putExtra("lat", plan.getHouse().getLat());
            intent.putExtra("lng", plan.getHouse().getLng());
            intent.putExtra("address", plan.getHouse().getAddress());
            v.getContext().startActivity(intent);
        });

        // Whole card click → Plan Detail Screen
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), PlanDetailActivity.class);
            intent.putExtra("plan", plan);
            intent.putExtra("fromAddress", fromAddress);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHouse, tvLabor, tvPacking, tvTransport, tvTotalCost, tvScore;
        LinearLayout llAdvantages, llDisadvantages;
        Button btnToggleDetails, btnMap;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHouse = itemView.findViewById(R.id.tvHouse);
            tvLabor = itemView.findViewById(R.id.tvLabor);
            tvPacking = itemView.findViewById(R.id.tvPacking);
            tvTransport = itemView.findViewById(R.id.tvTransport);
            tvTotalCost = itemView.findViewById(R.id.tvTotalCost);
            tvScore = itemView.findViewById(R.id.tvScore);
            llAdvantages = itemView.findViewById(R.id.llAdvantages);
            llDisadvantages = itemView.findViewById(R.id.llDisadvantages);
            btnToggleDetails = itemView.findViewById(R.id.btnToggleDetails);
            btnMap = itemView.findViewById(R.id.btnMap);
        }
    }
}