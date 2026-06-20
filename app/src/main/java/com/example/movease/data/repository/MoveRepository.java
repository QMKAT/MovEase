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

    public void init() {
        apiService = RetrofitClient.getClient().create(MoveApiService.class);
    }

    public interface DataCallback {
        void onDataLoaded(List<House> houses, List<LaborProvider> labors,
                          List<PackingProvider> packings, List<TransportProvider> transports);
        void onError(String error);
    }

    public void fetchAllData(String area, double maxBudget, int bedrooms, DataCallback callback) {
        new Thread(() -> {
            try {
                // 1. Houses from mock (effectively final)
                final List<House> houses = new ArrayList<>();
                List<House> mockHouses = apiService.getHouses(area, maxBudget, bedrooms).execute().body();
                if (mockHouses != null) houses.addAll(mockHouses);

                // 2. Prepare effectively final lists for services
                final List<LaborProvider> labors = new ArrayList<>();
                final List<PackingProvider> packings = new ArrayList<>();
                final List<TransportProvider> transports = new ArrayList<>();

                // Add mock data to these lists
                List<LaborProvider> mockLabors = apiService.getLabor("Lahore").execute().body();
                if (mockLabors != null) labors.addAll(mockLabors);

                List<PackingProvider> mockPackings = apiService.getPacking("Lahore").execute().body();
                if (mockPackings != null) packings.addAll(mockPackings);

                List<TransportProvider> mockTransports = apiService.getTransport("Lahore").execute().body();
                if (mockTransports != null) transports.addAll(mockTransports);

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
                                        lp.setMaxWorkers(5);
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
                            // Deliver the combined lists
                            callback.onDataLoaded(houses, labors, packings, transports);
                        })
                        .addOnFailureListener(e -> callback.onError(e.getMessage()));
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        }).start();
    }
}