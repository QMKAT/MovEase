package com.example.movease;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Toast;
import java.util.List;

public class PlanListActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rvPlans;
    private PlanAdapter adapter;
    private List<Plan> plans = new ArrayList<>();

    // Search criteria
    private String area;
    private double maxBudget;
    private int bedrooms;
    private String moveDate;

    private MoveRepository repository;
    private Handler autoRefreshHandler;
    private static final int AUTO_REFRESH_INTERVAL = 60000; // 60 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_list);

        swipeRefresh = findViewById(R.id.swipeRefresh);
        rvPlans = findViewById(R.id.rvPlans);
        rvPlans.setLayoutManager(new LinearLayoutManager(this));

        repository = new MoveRepository();
        repository.init(this);

        // Get data from intent
        plans = (List<Plan>) getIntent().getSerializableExtra("plans");
        area = getIntent().getStringExtra("area");
        maxBudget = getIntent().getDoubleExtra("maxBudget", 0);
        bedrooms = getIntent().getIntExtra("bedrooms", 1);
        moveDate = getIntent().getStringExtra("moveDate");

        if (plans != null) {
            adapter = new PlanAdapter(plans);
            rvPlans.setAdapter(adapter);
        }

        // Pull‑to‑refresh
        swipeRefresh.setOnRefreshListener(() -> fetchAndGeneratePlans());

        // Auto‑refresh every 60 seconds
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
        repository.fetchAllData(area, maxBudget, bedrooms, new MoveRepository.DataCallback() {
            @Override
            public void onDataLoaded(List<House> houses, List<LaborProvider> labors,
                                     List<PackingProvider> packings, List<TransportProvider> transports) {
                PlanGenerator generator = new PlanGenerator();
                List<Plan> newPlans = generator.generatePlans(houses, labors, packings, transports, maxBudget);
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