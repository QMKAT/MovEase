package com.example.movease;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.movease.data.model.Service;
import com.example.movease.data.repository.ServiceRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OrdersActivity extends AppCompatActivity {

    private RecyclerView rvOrders;
    private OrdersAdapter adapter;
    private List<Map<String, Object>> orderList = new ArrayList<>();
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Set<String> myServiceIds = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        rvOrders = findViewById(R.id.rvOrders);
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrdersAdapter(orderList);
        rvOrders.setAdapter(adapter);

        loadMyServicesAndThenOrders();
    }

    private void loadMyServicesAndThenOrders() {
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String providerId = mAuth.getCurrentUser().getUid();
        ServiceRepository serviceRepo = new ServiceRepository(providerId);

        // 1. Fetch provider's own services
        serviceRepo.getMyServices(
                services -> {
                    myServiceIds.clear();
                    for (Service s : services) {
                        myServiceIds.add(s.getId());
                    }
                    // 2. Now fetch bookings and filter
                    loadFilteredBookings();
                },
                e -> Toast.makeText(this, "Failed to load services: " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }

    private void loadFilteredBookings() {
        db.collection("bookings").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    orderList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Map<String, Object> booking = doc.getData();
                        String laborId = (String) booking.get("laborId");
                        String packingId = (String) booking.get("packingId");
                        String transportId = (String) booking.get("transportId");

                        // Check if any of these IDs belongs to the current provider
                        if (myServiceIds.contains(laborId) || myServiceIds.contains(packingId) || myServiceIds.contains(transportId)) {
                            orderList.add(booking);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    if (orderList.isEmpty()) {
                        Toast.makeText(this, "No orders for your services yet", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load orders: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}