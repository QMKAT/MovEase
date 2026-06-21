package com.example.movease;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.movease.engine.Plan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class PlanDetailActivity extends AppCompatActivity {

    private TextView tvHouse, tvLabor, tvPacking, tvTransport, tvTotalCost;
    private LinearLayout llAdvantages, llDisadvantages;
    private Button btnMap, btnBook;
    private Plan plan;
    private String fromAddress;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_detail);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        plan = (Plan) getIntent().getSerializableExtra("plan");
        fromAddress = getIntent().getStringExtra("fromAddress");

        if (plan == null) {
            Toast.makeText(this, "Plan data missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvHouse = findViewById(R.id.tvDetailHouse);
        tvLabor = findViewById(R.id.tvDetailLabor);
        tvPacking = findViewById(R.id.tvDetailPacking);
        tvTransport = findViewById(R.id.tvDetailTransport);
        tvTotalCost = findViewById(R.id.tvDetailTotalCost);
        llAdvantages = findViewById(R.id.llDetailAdvantages);
        llDisadvantages = findViewById(R.id.llDetailDisadvantages);
        btnMap = findViewById(R.id.btnDetailMap);
        btnBook = findViewById(R.id.btnBookPlan);

        tvHouse.setText("House: " + plan.getHouse().getAddress() + "\nPrice: PKR " + String.format("%.0f", plan.getHouse().getPrice()));
        tvLabor.setText("Labor: " + plan.getLabor().getName() + " — Rate: PKR " + plan.getLabor().getRatePerHour() + "/hr");
        tvPacking.setText("Packing: " + plan.getPacking().getName() + " — Cost: PKR " + plan.getPacking().getCostPerBox() + "/box");
        tvTransport.setText("Transport: " + plan.getTransport().getName() + " — Cost: PKR " + plan.getTransport().getCostPerKm() + "/km");
        tvTotalCost.setText("Total Estimated Cost: PKR " + String.format("%.0f", plan.getTotalCost()));

        llAdvantages.removeAllViews();
        for (String adv : plan.getAdvantages()) {
            TextView tv = new TextView(this);
            tv.setText("✓ " + adv);
            tv.setTextSize(14);
            tv.setTextColor(0xFF4CAF50);
            llAdvantages.addView(tv);
        }

        llDisadvantages.removeAllViews();
        for (String dis : plan.getDisadvantages()) {
            TextView tv = new TextView(this);
            tv.setText("✗ " + dis);
            tv.setTextSize(14);
            tv.setTextColor(0xFFF44336);
            llDisadvantages.addView(tv);
        }

        btnMap.setOnClickListener(v -> {
            Intent intent = new Intent(PlanDetailActivity.this, MapActivity.class);
            intent.putExtra("lat", plan.getHouse().getLat());
            intent.putExtra("lng", plan.getHouse().getLng());
            intent.putExtra("address", plan.getHouse().getAddress());
            startActivity(intent);
        });

        btnBook.setOnClickListener(v -> bookPlan());
    }

    private void bookPlan() {
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "You must be logged in to book", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();
        Map<String, Object> booking = new HashMap<>();
        booking.put("userId", userId);
        booking.put("fromAddress", fromAddress);
        booking.put("toAddress", plan.getHouse().getAddress());
        booking.put("houseId", plan.getHouse().getId());
        booking.put("laborId", plan.getLabor().getId());
        booking.put("packingId", plan.getPacking().getId());
        booking.put("transportId", plan.getTransport().getId());
        booking.put("totalCost", plan.getTotalCost());
        booking.put("timestamp", System.currentTimeMillis());

        db.collection("bookings")
                .add(booking)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(PlanDetailActivity.this,
                            "Plan booked successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(PlanDetailActivity.this,
                                "Booking failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}