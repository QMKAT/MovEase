package com.example.movease;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MoverHomeActivity extends AppCompatActivity {

    private AutoCompleteTextView areaDropdown, bedroomsDropdown;
    private EditText etMaxBudget, etFromAddress, etToAddress;
    private LinearLayout llHouseSearch;
    private View tilToAddress;
    private CheckBox cbHasHouse, cbLabor, cbPacking, cbTransport;
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
        repository.init();

        areaDropdown = findViewById(R.id.spinnerArea);
        bedroomsDropdown = findViewById(R.id.spinnerBedrooms);
        etMaxBudget = findViewById(R.id.etMaxBudget);
        etFromAddress = findViewById(R.id.etFromAddress);
        etToAddress = findViewById(R.id.etToAddress);
        llHouseSearch = findViewById(R.id.llHouseSearch);
        tilToAddress = findViewById(R.id.tilToAddress);
        cbHasHouse = findViewById(R.id.cbHasHouse);
        cbLabor = findViewById(R.id.cbLabor);
        cbPacking = findViewById(R.id.cbPacking);
        cbTransport = findViewById(R.id.cbTransport);
        btnDate = findViewById(R.id.btnDate);
        btnFindPlan = findViewById(R.id.btnFindPlan);
        btnLogout = findViewById(R.id.btnLogout);

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

        // Toggle fields when checkbox changes
        cbHasHouse.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                llHouseSearch.setVisibility(View.GONE);
                tilToAddress.setVisibility(View.VISIBLE);
            } else {
                llHouseSearch.setVisibility(View.VISIBLE);
                tilToAddress.setVisibility(View.GONE);
            }
        });

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
            boolean hasHouse = cbHasHouse.isChecked();
            String fromAddress = etFromAddress.getText().toString().trim();

            if (fromAddress.isEmpty()) {
                Toast.makeText(this, "Enter your current address", Toast.LENGTH_SHORT).show();
                return;
            }

            if (hasHouse) {
                String toAddress = etToAddress.getText().toString().trim();
                if (toAddress.isEmpty()) {
                    Toast.makeText(this, "Enter your destination address", Toast.LENGTH_SHORT).show();
                    return;
                }
                generatePlansForOwnHouse(fromAddress, toAddress);
            } else {
                String area = areaDropdown.getText().toString();
                String bedroomsStr = bedroomsDropdown.getText().toString();
                String budgetStr = etMaxBudget.getText().toString().trim();

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

                repository.fetchAllData(area, maxBudget, bedrooms, new MoveRepository.DataCallback() {
                    @Override
                    public void onDataLoaded(List<House> houses, List<LaborProvider> labors,
                                             List<PackingProvider> packings, List<TransportProvider> transports) {
                        PlanGenerator generator = new PlanGenerator();
                        List<Plan> plans = generator.generatePlans(
                                houses, labors, packings, transports,
                                maxBudget, includeLabor, includePacking, includeTransport);
                        showPlans(plans, area, maxBudget, bedrooms, moveDate, fromAddress, null);
                    }

                    @Override
                    public void onError(String error) {
                        runOnUiThread(() -> Toast.makeText(MoverHomeActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show());
                    }
                });
            }
        });

        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            new UserSessionManager(this).clearSession();
            startActivity(new Intent(MoverHomeActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void generatePlansForOwnHouse(String fromAddress, String toAddress) {
        boolean includeLabor = cbLabor.isChecked();
        boolean includePacking = cbPacking.isChecked();
        boolean includeTransport = cbTransport.isChecked();
        String moveDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(selectedDate.getTime());

        repository.fetchServicesOnly(new MoveRepository.DataCallback() {
            @Override
            public void onDataLoaded(List<House> housesIgnored, List<LaborProvider> labors,
                                     List<PackingProvider> packings, List<TransportProvider> transports) {
                // Create a dummy house with the destination address
                House dummyHouse = new House();
                dummyHouse.setId("none");
                dummyHouse.setAddress(toAddress);
                dummyHouse.setPrice(0);
                dummyHouse.setBedrooms(0);
                dummyHouse.setRating(3.0);
                dummyHouse.setLat(0);
                dummyHouse.setLng(0);
                dummyHouse.setDisadvantages(new ArrayList<>());

                List<House> singleHouse = Collections.singletonList(dummyHouse);

                PlanGenerator generator = new PlanGenerator();
                // maxBudget irrelevant – we pass a huge number to bypass budget filter
                List<Plan> plans = generator.generatePlans(
                        singleHouse, labors, packings, transports,
                        Double.MAX_VALUE, includeLabor, includePacking, includeTransport);
                showPlans(plans, null, 0, 0, moveDate, fromAddress, toAddress);
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> Toast.makeText(MoverHomeActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void showPlans(List<Plan> plans, String area, double maxBudget, int bedrooms,
                           String moveDate, String fromAddress, String toAddress) {
        List<Plan> serializablePlans = new ArrayList<>(plans);

        runOnUiThread(() -> {
            if (serializablePlans.isEmpty()) {
                Toast.makeText(MoverHomeActivity.this, "No plans found", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(MoverHomeActivity.this, PlanListActivity.class);
            intent.putExtra("plans", (java.io.Serializable) serializablePlans);
            intent.putExtra("area", area != null ? area : "");
            intent.putExtra("maxBudget", maxBudget);
            intent.putExtra("bedrooms", bedrooms);
            intent.putExtra("moveDate", moveDate);
            intent.putExtra("fromAddress", fromAddress);
            intent.putExtra("toAddress", toAddress != null ? toAddress : "");
            startActivity(intent);
        });
    }
}