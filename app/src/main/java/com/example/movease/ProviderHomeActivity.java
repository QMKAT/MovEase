package com.example.movease;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.movease.data.model.Service;
import com.example.movease.data.repository.ServiceRepository;
import com.example.movease.ui.ServiceAdapter;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.List;

public class ProviderHomeActivity extends AppCompatActivity {

    private RecyclerView rvServices;
    private ServiceAdapter adapter;
    private List<Service> serviceList = new ArrayList<>();
    private ServiceRepository serviceRepository;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_home);

        mAuth = FirebaseAuth.getInstance();
        String providerId = mAuth.getCurrentUser().getUid();
        serviceRepository = new ServiceRepository(providerId);

        rvServices = findViewById(R.id.rvServices);
        rvServices.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ServiceAdapter(serviceList, new ServiceAdapter.OnServiceActionListener() {
            @Override
            public void onEditClick(Service service) {
                showAddEditDialog(service);
            }
            @Override
            public void onDeleteClick(Service service) {
                deleteService(service);
            }
        });
        rvServices.setAdapter(adapter);

        findViewById(R.id.btnAddService).setOnClickListener(v -> showAddEditDialog(null));
        findViewById(R.id.btnLogout).setOnClickListener(v -> logout());

        loadServices();
    }
    private void loadServices() {
        serviceRepository.getMyServices(
                services -> {
                    serviceList.clear();
                    serviceList.addAll(services);
                    adapter.notifyDataSetChanged();
                    if (services.isEmpty()) {
                        Toast.makeText(this, "No services yet. Add one!", Toast.LENGTH_SHORT).show();
                    }
                },
                e -> Toast.makeText(this, "Failed to load services: " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }

    private void showAddEditDialog(Service existingService) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_service, null);
        builder.setView(dialogView);

        Spinner spinnerType = dialogView.findViewById(R.id.spinnerType);
        EditText etName = dialogView.findViewById(R.id.etServiceName);
        EditText etRate = dialogView.findViewById(R.id.etRate);
        EditText etDesc = dialogView.findViewById(R.id.etDescription);

        // Populate type spinner
        String[] types = {"labor", "packing", "transport"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);

        // If editing, pre‑fill fields
        if (existingService != null) {
            etName.setText(existingService.getName());
            etRate.setText(String.valueOf(existingService.getRate()));
            etDesc.setText(existingService.getDescription());
            int pos = typeAdapter.getPosition(existingService.getType());
            spinnerType.setSelection(pos);
            builder.setTitle("Edit Service");
        } else {
            builder.setTitle("Add Service");
        }

        builder.setPositiveButton("Save", (dialog, which) -> {
            String type = spinnerType.getSelectedItem().toString();
            String name = etName.getText().toString().trim();
            String rateStr = etRate.getText().toString().trim();
            String desc = etDesc.getText().toString().trim();

            if (name.isEmpty() || rateStr.isEmpty()) {
                Toast.makeText(ProviderHomeActivity.this, "Name and rate required", Toast.LENGTH_SHORT).show();
                return;
            }
            double rate = Double.parseDouble(rateStr);

            if (existingService == null) {
                // Add new
                Service newService = new Service(null, null, type, name, rate, desc, 4.0); // default rating
                serviceRepository.addService(newService,
                        docRef -> {
                            Toast.makeText(this, "Service added", Toast.LENGTH_SHORT).show();
                            loadServices();
                        },
                        e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            } else {
                // Update existing
                existingService.setType(type);
                existingService.setName(name);
                existingService.setRate(rate);
                existingService.setDescription(desc);
                serviceRepository.updateService(existingService,
                        aVoid -> {
                            Toast.makeText(this, "Service updated", Toast.LENGTH_SHORT).show();
                            loadServices();
                        },
                        e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    private void deleteService(Service service) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Service")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    serviceRepository.deleteService(service.getId(),
                            aVoid -> {
                                Toast.makeText(this, "Service deleted", Toast.LENGTH_SHORT).show();
                                loadServices();
                            },
                            e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void logout() {
        mAuth.signOut();
        new UserSessionManager(this).clearSession();
        startActivity(new android.content.Intent(ProviderHomeActivity.this, LoginActivity.class));
        finish();
    }
}