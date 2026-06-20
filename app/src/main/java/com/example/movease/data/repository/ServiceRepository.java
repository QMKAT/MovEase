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
        db.collection("services").add(service)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
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