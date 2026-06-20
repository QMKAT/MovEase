package com.example.movease;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.movease.data.model.*;
import com.example.movease.data.repository.MoveRepository;
import com.example.movease.engine.Plan;
import com.example.movease.engine.PlanGenerator;
import com.google.firebase.auth.FirebaseAuth;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MoverHomeActivity extends AppCompatActivity {

    private Spinner spinnerArea, spinnerBedrooms;
    private EditText etMaxRent;
    private Button btnDate, btnFindPlan, btnLogout;
    private Calendar selectedDate = Calendar.getInstance();
    private FirebaseAuth mAuth;
    private MoveRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mover_home);

        mAuth = FirebaseAuth.getInstance();
        repository = new MoveRepository();
        repository.init(this);

        spinnerArea = findViewById(R.id.spinnerArea);
        spinnerBedrooms = findViewById(R.id.spinnerBedrooms);
        etMaxRent = findViewById(R.id.etMaxRent);
        btnDate = findViewById(R.id.btnDate);
        btnFindPlan = findViewById(R.id.btnFindPlan);
        btnLogout = findViewById(R.id.btnLogout);

        // Populate area spinner (Lahore areas)
        ArrayAdapter<CharSequence> areaAdapter = ArrayAdapter.createFromResource(
                this, R.array.lahore_areas, android.R.layout.simple_spinner_item);
        areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerArea.setAdapter(areaAdapter);

        // Populate bedrooms spinner
        ArrayAdapter<CharSequence> bedroomAdapter = ArrayAdapter.createFromResource(
                this, R.array.bedrooms, android.R.layout.simple_spinner_item);
        bedroomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBedrooms.setAdapter(bedroomAdapter);

        // Date picker
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePicker = new DatePickerDialog(
                        MoverHomeActivity.this,
                        (view, year, month, dayOfMonth) -> {
                            selectedDate.set(year, month, dayOfMonth);
                            btnDate.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                    .format(selectedDate.getTime()));
                        },
                        selectedDate.get(Calendar.YEAR),
                        selectedDate.get(Calendar.MONTH),
                        selectedDate.get(Calendar.DAY_OF_MONTH));
                datePicker.show();
            }
        });

        // Find plan button
        btnFindPlan.setOnClickListener(v -> {
            String area = spinnerArea.getSelectedItem().toString();
            String rentStr = etMaxRent.getText().toString().trim();
            int bedrooms = Integer.parseInt(spinnerBedrooms.getSelectedItem().toString());

            if (rentStr.isEmpty()) {
                Toast.makeText(this, "Enter maximum rent", Toast.LENGTH_SHORT).show();
                return;
            }
            double maxRent = Double.parseDouble(rentStr);
            String moveDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(selectedDate.getTime());

            // Fetch data asynchronously and generate plans
            repository.fetchAllData(area, maxRent, bedrooms, new MoveRepository.DataCallback() {
                @Override
                public void onDataLoaded(List<House> houses, List<LaborProvider> labors,
                                         List<PackingProvider> packings, List<TransportProvider> transports) {
                    PlanGenerator generator = new PlanGenerator();
                    List<Plan> plans = generator.generatePlans(houses, labors, packings, transports, maxRent);

                    if (plans.isEmpty()) {
                        Toast.makeText(MoverHomeActivity.this, "No plans found for your criteria", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Pass to PlanListActivity
                    Intent intent = new Intent(MoverHomeActivity.this, PlanListActivity.class);
                    intent.putExtra("plans", (java.io.Serializable) plans);
                    startActivity(intent);
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(MoverHomeActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Logout
        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            new UserSessionManager(this).clearSession();
            startActivity(new Intent(MoverHomeActivity.this, LoginActivity.class));
            finish();
        });
    }
}