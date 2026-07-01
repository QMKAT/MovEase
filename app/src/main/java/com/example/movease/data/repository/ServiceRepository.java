package com.example.movease.data.repository;

import androidx.annotation.NonNull;
import com.example.movease.data.model.Service;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceRepository {
    private FirebaseFirestore db;
    private String providerId;

    public ServiceRepository(String providerId) {
        db = FirebaseFirestore.getInstance();
        this.providerId = providerId;
    }

    // Add a new service
    public void addService(Service service, OnSuccessListener<DocumentReference> onSuccess,
                           OnFailureListener onFailure) {
        service.setProviderId(providerId);
        // Calculate initial rating based on cost
        service.setRating(calculateInitialRating(service.getType(), service.getRate()));
        db.collection("services").add(service)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    // Inside ServiceRepository class
    private double calculateInitialRating(String type, double rate) {
        double maxRate;
        switch (type) {
            case "labor": maxRate = 1000; break;      // max hourly rate expected
            case "packing": maxRate = 500; break;      // max cost per box
            case "transport": maxRate = 200; break;     // max cost per km
            default: maxRate = 1000;
        }
        if (rate <= 0) return 5.0;
        double normalized = rate / maxRate;
        if (normalized > 1.0) normalized = 1.0;
        double rating = 5.0 - normalized * 4.0;  // cheaper = higher, range 1–5
        return Math.round(rating * 10.0) / 10.0; // one decimal
    }

    // Update existing service
    public void updateService(Service service, OnSuccessListener<Void> onSuccess,
                              OnFailureListener onFailure) {
        db.collection("services").document(service.getId())
                .set(service)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    // Delete service
    public void deleteService(String serviceId, OnSuccessListener<Void> onSuccess,
                              OnFailureListener onFailure) {
        db.collection("services").document(serviceId)
                .delete()
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    // Fetch all services of this provider
    public void getMyServices(OnSuccessListener<List<Service>> onSuccess,
                              OnFailureListener onFailure) {
        db.collection("services")
                .whereEqualTo("providerId", providerId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Service> services = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Service service = doc.toObject(Service.class);
                        if (service != null) {
                            service.setId(doc.getId()); // set Firestore document ID
                            services.add(service);
                        }
                    }
                    onSuccess.onSuccess(services);
                })
                .addOnFailureListener(onFailure);
    }
}