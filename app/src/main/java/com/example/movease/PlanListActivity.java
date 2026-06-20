package com.example.movease;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.movease.engine.Plan;
import com.example.movease.ui.PlanAdapter;
import java.util.List;

public class PlanListActivity extends AppCompatActivity {
    private RecyclerView rvPlans;
    private List<Plan> plans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_list);

        rvPlans = findViewById(R.id.rvPlans);
        rvPlans.setLayoutManager(new LinearLayoutManager(this));

        // Receive plans from intent
        plans = (List<Plan>) getIntent().getSerializableExtra("plans");
        if (plans != null) {
            PlanAdapter adapter = new PlanAdapter(plans);
            rvPlans.setAdapter(adapter);
        }
    }
}