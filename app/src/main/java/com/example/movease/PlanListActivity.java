package com.example.movease;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.movease.data.model.*;
import com.example.movease.data.repository.MoveRepository;
import com.example.movease.engine.Plan;
import com.example.movease.engine.PlanGenerator;
import com.example.movease.ui.PlanAdapter;
import java.util.ArrayList;
import java.util.List;

public class PlanListActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rvPlans;
    private PlanAdapter adapter;
    private List<Plan> plans = new ArrayList<>();

    private String area;
    private double maxBudget;
    private int bedrooms;
    private String moveDate;
    private String fromAddress;
    private String toAddress;

    private MoveRepository repository;
    private Handler autoRefreshHandler;
    private static final int AUTO_REFRESH_INTERVAL = 60000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_list);

        swipeRefresh = findViewById(R.id.swipeRefresh);
        rvPlans = findViewById(R.id.rvPlans);
        rvPlans.setLayoutManager(new LinearLayoutManager(this));

        repository = new MoveRepository();
        repository.init();

        plans = (List<Plan>) getIntent().getSerializableExtra("plans");
        area = getIntent().getStringExtra("area");
        maxBudget = getIntent().getDoubleExtra("maxBudget", 0);
        bedrooms = getIntent().getIntExtra("bedrooms", 1);
        moveDate = getIntent().getStringExtra("moveDate");
        fromAddress = getIntent().getStringExtra("fromAddress");
        toAddress = getIntent().getStringExtra("toAddress");

        if (plans != null) {
            adapter = new PlanAdapter(plans, fromAddress, toAddress);
            rvPlans.setAdapter(adapter);
        }

        swipeRefresh.setOnRefreshListener(() -> fetchAndGeneratePlans());

        autoRefreshHandler = new Handler();
        autoRefreshHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchAndGeneratePlans();
                autoRefreshHandler.postDelayed(this, AUTO_REFRESH_INTERVAL);
            }
        }, AUTO_REFRESH_INTERVAL);
    }

    private void fetchAndGeneratePlans() {
        swipeRefresh.setRefreshing(true);
        // For simplicity, we re‑generate with all services included (refresh only if has house? We'll handle generically)
        // If area is empty (own house mode), we cannot refresh – fallback
        if (area == null || area.isEmpty()) {
            Toast.makeText(this, "Refresh not available in own‑house mode", Toast.LENGTH_SHORT).show();
            swipeRefresh.setRefreshing(false);
            return;
        }
        repository.fetchAllData(area, maxBudget, bedrooms, new MoveRepository.DataCallback() {
            @Override
            public void onDataLoaded(List<House> houses, List<LaborProvider> labors,
                                     List<PackingProvider> packings, List<TransportProvider> transports) {
                PlanGenerator generator = new PlanGenerator();
                List<Plan> newPlans = generator.generatePlans(houses, labors, packings, transports, maxBudget, true, true, true);
                plans.clear();
                plans.addAll(newPlans);
                adapter.notifyDataSetChanged();
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onError(String error) {
                swipeRefresh.setRefreshing(false);
                Toast.makeText(PlanListActivity.this, "Refresh failed: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (autoRefreshHandler != null) {
            autoRefreshHandler.removeCallbacksAndMessages(null);
        }
    }
}