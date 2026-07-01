package com.example.movease;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoverBookingsActivity extends AppCompatActivity {

    private RecyclerView rvBookings;
    private MoverBookingsAdapter adapter;
    private List<Map<String, Object>> bookingList = new ArrayList<>();
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mover_bookings);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        rvBookings = findViewById(R.id.rvBookings);
        rvBookings.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MoverBookingsAdapter(bookingList);
        rvBookings.setAdapter(adapter);

        loadBookings();
    }

    private void loadBookings() {
        if (mAuth.getCurrentUser() == null) return;
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("bookings")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    bookingList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Map<String, Object> booking = doc.getData();
                        booking.put("docId", doc.getId());   // store document ID
                        bookingList.add(booking);
                    }
                    adapter.notifyDataSetChanged();
                    if (bookingList.isEmpty()) {
                        Toast.makeText(this, "No bookings yet", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load bookings: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // Called from adapter when rate button is tapped
    public void showRatingDialog(String bookingDocId, String laborId, String packingId, String transportId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_rate_services, null);
        builder.setView(dialogView);
        builder.setTitle("Rate Services");

        RatingBar rbLabor = dialogView.findViewById(R.id.rbLabor);
        RatingBar rbPacking = dialogView.findViewById(R.id.rbPacking);
        RatingBar rbTransport = dialogView.findViewById(R.id.rbTransport);

        // Show rating bars only if the service ID is not "none" or null
        if (laborId == null || laborId.equals("none")) {
            dialogView.findViewById(R.id.llLabor).setVisibility(View.GONE);
        }
        if (packingId == null || packingId.equals("none")) {
            dialogView.findViewById(R.id.llPacking).setVisibility(View.GONE);
        }
        if (transportId == null || transportId.equals("none")) {
            dialogView.findViewById(R.id.llTransport).setVisibility(View.GONE);
        }

        builder.setPositiveButton("Submit", (dialog, which) -> {
            float laborRating = rbLabor.getRating();
            float packingRating = rbPacking.getRating();
            float transportRating = rbTransport.getRating();

            updateServiceRating(laborId, laborRating, bookingDocId);
            updateServiceRating(packingId, packingRating, bookingDocId);
            updateServiceRating(transportId, transportRating, bookingDocId);

            // Mark booking as rated
            db.collection("bookings").document(bookingDocId)
                    .update("status", "rated")
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(MoverBookingsActivity.this, "Thank you for rating!", Toast.LENGTH_SHORT).show();
                        loadBookings(); // refresh list
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(MoverBookingsActivity.this, "Error updating status: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    private void updateServiceRating(String serviceId, float newRating, String bookingDocId) {
        if (serviceId == null || serviceId.equals("none") || newRating == 0) return;

        db.collection("services").document(serviceId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        double currentAvg = documentSnapshot.getDouble("rating");
                        Long numRatingsObj = documentSnapshot.getLong("numRatings");
                        long numRatings = (numRatingsObj != null) ? numRatingsObj : 0L;

                        double newAvg;
                        if (numRatings == 0) {
                            newAvg = newRating;
                        } else {
                            newAvg = (currentAvg * numRatings + newRating) / (numRatings + 1);
                        }
                        numRatings = numRatings + 1;

                        db.collection("services").document(serviceId)
                                .update("rating", Math.round(newAvg * 10.0) / 10.0, "numRatings", numRatings);
                    }
                });
    }
}