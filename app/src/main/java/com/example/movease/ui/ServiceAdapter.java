package com.example.movease.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.movease.R;
import com.example.movease.data.model.Service;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {
    private List<Service> services;
    private OnServiceActionListener listener;

    public interface OnServiceActionListener {
        void onEditClick(Service service);
        void onDeleteClick(Service service);
    }

    public ServiceAdapter(List<Service> services, OnServiceActionListener listener) {
        this.services = services;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Service service = services.get(position);
        holder.tvName.setText(service.getName());
        holder.tvType.setText("Type: " + service.getType());
        holder.tvRate.setText("Rate: PKR " + service.getRate() +
                (service.getType().equals("labor") ? "/hr" :
                        service.getType().equals("packing") ? "/box" : "/km"));
        holder.tvDescription.setText(service.getDescription());

        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(service));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(service));
    }

    @Override
    public int getItemCount() {
        return services == null ? 0 : services.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvType, tvRate, tvDescription;
        Button btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvServiceName);
            tvType = itemView.findViewById(R.id.tvServiceType);
            tvRate = itemView.findViewById(R.id.tvServiceRate);
            tvDescription = itemView.findViewById(R.id.tvServiceDescription);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}