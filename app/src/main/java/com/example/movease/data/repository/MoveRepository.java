package com.example.movease.data.repository;

import android.content.Context;
import com.example.movease.data.api.MoveApiService;
import com.example.movease.data.api.RetrofitClient;
import com.example.movease.data.model.*;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class MoveRepository {
    private MoveApiService apiService;
    private FirebaseFirestore db;

    public MoveRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public void init(Context context) {
        apiService = RetrofitClient.getClient(context).create(MoveApiService.class);
    }

    public interface DataCallback {
        void onDataLoaded(List<House> houses, List<LaborProvider> labors,
                          List<PackingProvider> packings, List<TransportProvider> transports);
        void onError(String error);
    }

    public void fetchAllData(String area, double maxBudget, int bedrooms, DataCallback callback) {
        new Thread(() -> {
            try {
                // 1. Fetch houses from mock (or you can later change to Firestore)
                List<House> houses = apiService.getHouses(area, maxBudget, bedrooms).execute().body();
                if (houses == null) houses = new ArrayList<>();

                // 2. Fetch mock services as fallback
                List<LaborProvider> labors = apiService.getLabor("Lahore").execute().body();
                List<PackingProvider> packings = apiService.getPacking("Lahore").execute().body();
                List<TransportProvider> transports = apiService.getTransport("Lahore").execute().body();
                if (labors == null) labors = new ArrayList<>();
                if (packings == null) packings = new ArrayList<>();
                if (transports == null) transports = new ArrayList<>();

                // 3. Fetch all provider services from Firestore and merge
                db.collection("services").get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                Service service = doc.toObject(Service.class);
                                if (service == null) continue;

                                switch (service.getType()) {
                                    case "labor":
                                        LaborProvider lp = new LaborProvider();
                                        lp.setId(service.getId());
                                        lp.setName(service.getName());
                                        lp.setRatePerHour(service.getRate());
                                        lp.setRating(service.getRating());
                                        lp.setMaxWorkers(5);          // sensible default
                                        lp.setAvailabilityDate("2026-06-21");
                                        labors.add(lp);
                                        break;
                                    case "packing":
                                        PackingProvider pp = new PackingProvider();
                                        pp.setId(service.getId());
                                        pp.setName(service.getName());
                                        pp.setCostPerBox(service.getRate());
                                        pp.setRating(service.getRating());
                                        pp.setMaterialType("General");
                                        pp.setDeliveryAvailable(true);
                                        packings.add(pp);
                                        break;
                                    case "transport":
                                        TransportProvider tp = new TransportProvider();
                                        tp.setId(service.getId());
                                        tp.setName(service.getName());
                                        tp.setCostPerKm(service.getRate());
                                        tp.setRating(service.getRating());
                                        tp.setVehicleType("Truck");
                                        tp.setAvailable(true);
                                        transports.add(tp);
                                        break;
                                }
                            }
                            // Now deliver the combined lists to the callback
                            callback.onDataLoaded(houses, labors, packings, transports);
                        })
                        .addOnFailureListener(e -> callback.onError(e.getMessage()));
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        }).start();
    }
}