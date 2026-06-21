package com.example.movease;

import android.os.Bundle;
import android.util.Log;
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
    private static final String TAG = "OrdersActivity";

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

        serviceRepo.getMyServices(
                services -> {
                    myServiceIds.clear();
                    for (Service s : services) {
                        myServiceIds.add(s.getId());
                        Log.d(TAG, "Provider service ID: " + s.getId());
                    }
                    Log.d(TAG, "Total provider services: " + myServiceIds.size());
                    if (myServiceIds.isEmpty()) {
                        Toast.makeText(OrdersActivity.this,
                                "You have no services yet. Add a service first.", Toast.LENGTH_SHORT).show();
                    }
                    loadFilteredBookings();
                },
                e -> {
                    Log.e(TAG, "Failed to load services", e);
                    Toast.makeText(OrdersActivity.this,
                            "Failed to load your services: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
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

                        Log.d(TAG, "Booking laborId=" + laborId + " packingId=" + packingId + " transportId=" + transportId);

                        if (myServiceIds.contains(laborId) || myServiceIds.contains(packingId) || myServiceIds.contains(transportId)) {
                            orderList.add(booking);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    if (orderList.isEmpty()) {
                        Toast.makeText(OrdersActivity.this,
                                "No orders for your services yet", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to load bookings", e);
                    Toast.makeText(OrdersActivity.this,
                            "Failed to load orders: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}