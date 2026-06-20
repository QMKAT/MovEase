package com.example.movease.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.movease.R;
import com.example.movease.engine.Plan;
import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {
    private List<Plan> plans;

    public PlanAdapter(List<Plan> plans) {
        this.plans = plans;
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
        holder.tvHouse.setText("House: " + plan.getHouse().getAddress() + " (Rent: PKR " + plan.getHouse().getRent() + ")");
        holder.tvLabor.setText("Labor: " + plan.getLabor().getName() + " (Rate/hr: " + plan.getLabor().getRatePerHour() + ")");
        holder.tvPacking.setText("Packing: " + plan.getPacking().getName() + " (Cost/box: " + plan.getPacking().getCostPerBox() + ")");
        holder.tvTransport.setText("Transport: " + plan.getTransport().getName() + " (Cost/km: " + plan.getTransport().getCostPerKm() + ")");
        holder.tvTotalCost.setText("Total Estimated Cost: PKR " + String.format("%.0f", plan.getTotalCost()));
        holder.tvScore.setText("Score: " + String.format("%.2f", plan.getScore()));

        // Add advantage/disadvantage text
        holder.llAdvantages.removeAllViews();
        for (String adv : plan.getAdvantages()) {
            TextView tv = new TextView(holder.itemView.getContext());
            tv.setText(adv);
            tv.setTextSize(12);
            tv.setTextColor(0xFF4CAF50);
            holder.llAdvantages.addView(tv);
        }

        holder.llDisadvantages.removeAllViews();
        for (String dis : plan.getDisadvantages()) {
            TextView tv = new TextView(holder.itemView.getContext());
            tv.setText(dis);
            tv.setTextSize(12);
            tv.setTextColor(0xFFF44336);
            holder.llDisadvantages.addView(tv);
        }

        // Toggle visibility
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
    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHouse, tvLabor, tvPacking, tvTransport, tvTotalCost, tvScore;
        LinearLayout llAdvantages, llDisadvantages;
        Button btnToggleDetails;

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
        }
    }
}