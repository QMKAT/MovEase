package com.example.movease;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.movease.data.model.*;
import com.example.movease.data.repository.MoveRepository;
import com.example.movease.engine.Plan;
import com.example.movease.engine.PlanGenerator;
import com.google.firebase.auth.FirebaseAuth;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MoverHomeActivity extends AppCompatActivity {

    private AutoCompleteTextView areaDropdown, bedroomsDropdown;
    private EditText etMaxBudget, etFromAddress;
    private Button btnDate, btnFindPlan, btnLogout;
    private CheckBox cbLabor, cbPacking, cbTransport;
    private Calendar selectedDate = Calendar.getInstance();
    private FirebaseAuth mAuth;
    private MoveRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mover_home);

        mAuth = FirebaseAuth.getInstance();
        repository = new MoveRepository();
        repository.init();

        areaDropdown = findViewById(R.id.spinnerArea);
        bedroomsDropdown = findViewById(R.id.spinnerBedrooms);
        etMaxBudget = findViewById(R.id.etMaxBudget);
        etFromAddress = findViewById(R.id.etFromAddress);
        btnDate = findViewById(R.id.btnDate);
        btnFindPlan = findViewById(R.id.btnFindPlan);
        btnLogout = findViewById(R.id.btnLogout);
        cbLabor = findViewById(R.id.cbLabor);
        cbPacking = findViewById(R.id.cbPacking);
        cbTransport = findViewById(R.id.cbTransport);

        // Area dropdown
        ArrayAdapter<String> areaAdapter = new ArrayAdapter<>(
                this, R.layout.dropdown_item,
                getResources().getStringArray(R.array.lahore_areas));
        areaDropdown.setAdapter(areaAdapter);

        // Bedrooms dropdown
        ArrayAdapter<String> bedroomsAdapter = new ArrayAdapter<>(
                this, R.layout.dropdown_item,
                getResources().getStringArray(R.array.bedrooms));
        bedroomsDropdown.setAdapter(bedroomsAdapter);

        // Date picker
        btnDate.setOnClickListener(v -> {
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
        });

        // Find plan button
        btnFindPlan.setOnClickListener(v -> {
            String area = areaDropdown.getText().toString();
            String bedroomsStr = bedroomsDropdown.getText().toString();
            String budgetStr = etMaxBudget.getText().toString().trim();
            String fromAddress = etFromAddress.getText().toString().trim();

            if (budgetStr.isEmpty()) {
                Toast.makeText(this, "Enter your maximum budget", Toast.LENGTH_SHORT).show();
                return;
            }
            if (area.isEmpty() || bedroomsStr.isEmpty()) {
                Toast.makeText(this, "Please select area and bedrooms", Toast.LENGTH_SHORT).show();
                return;
            }

            int bedrooms = Integer.parseInt(bedroomsStr);
            double maxBudget = Double.parseDouble(budgetStr);
            String moveDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(selectedDate.getTime());

            boolean includeLabor = cbLabor.isChecked();
            boolean includePacking = cbPacking.isChecked();
            boolean includeTransport = cbTransport.isChecked();

            // Fetch data and generate plans
            repository.fetchAllData(area, maxBudget, bedrooms, new MoveRepository.DataCallback() {
                @Override
                public void onDataLoaded(List<House> houses, List<LaborProvider> labors,
                                         List<PackingProvider> packings, List<TransportProvider> transports) {
                    PlanGenerator generator = new PlanGenerator();
                    List<Plan> plans = generator.generatePlans(
                            houses, labors, packings, transports,
                            maxBudget, includeLabor, includePacking, includeTransport);

                    List<Plan> serializablePlans = new ArrayList<>(plans);

                    runOnUiThread(() -> {
                        if (serializablePlans.isEmpty()) {
                            Toast.makeText(MoverHomeActivity.this,
                                    "No plans found for your budget", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent intent = new Intent(MoverHomeActivity.this, PlanListActivity.class);
                        intent.putExtra("plans", (java.io.Serializable) serializablePlans);
                        intent.putExtra("area", area);
                        intent.putExtra("maxBudget", maxBudget);
                        intent.putExtra("bedrooms", bedrooms);
                        intent.putExtra("moveDate", moveDate);
                        intent.putExtra("fromAddress", fromAddress);
                        startActivity(intent);
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() ->
                            Toast.makeText(MoverHomeActivity.this,
                                    "Error: " + error, Toast.LENGTH_SHORT).show());
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