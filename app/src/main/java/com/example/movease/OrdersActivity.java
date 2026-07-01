package com.example.movease;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.util.HashMap;
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
        adapter = new OrdersAdapter(orderList, myServiceIds);
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
                    if (myServiceIds.isEmpty()) {
                        Toast.makeText(this,
                                "You have no services yet. Add a service first.", Toast.LENGTH_SHORT).show();
                    }
                    loadFilteredBookings();
                },
                e -> {
                    Log.e(TAG, "Failed to load services", e);
                    Toast.makeText(this,
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

                        if (myServiceIds.contains(laborId) || myServiceIds.contains(packingId) || myServiceIds.contains(transportId)) {
                            booking.put("docId", doc.getId());   // store the document ID for later updates
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

    // These methods are called by the adapter buttons
    public void acceptBooking(String docId) {
        db.collection("bookings").document(docId)
                .update("status", "accepted")
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Order accepted", Toast.LENGTH_SHORT).show();
                    loadFilteredBookings(); // refresh
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    public void completeBooking(String docId) {
        db.collection("bookings").document(docId)
                .update("status", "completed")
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Order marked as completed", Toast.LENGTH_SHORT).show();
                    loadFilteredBookings();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}