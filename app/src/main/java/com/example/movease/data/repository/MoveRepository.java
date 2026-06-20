package com.example.movease.data.repository;

import android.content.Context;
import com.example.movease.data.api.MoveApiService;
import com.example.movease.data.api.RetrofitClient;
import com.example.movease.data.model.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class MoveRepository {
    private MoveApiService apiService;

    public MoveRepository() {
        // We'll initialize with a null context; we need context for mock interceptor.
        // Better: pass context via constructor or initialize later. We'll use Application context.
    }

    // Call this once with context, e.g., from Activity
    public void init(Context context) {
        apiService = RetrofitClient.getClient(context).create(MoveApiService.class);
    }

    public interface DataCallback {
        void onDataLoaded(List<House> houses, List<LaborProvider> labors,
                          List<PackingProvider> packings, List<TransportProvider> transports);
        void onError(String error);
    }

    public void fetchAllData(String area, double maxRent, int bedrooms, DataCallback callback) {
        // For simplicity, we simulate synchronous execution (since mock interceptor is instant).
        // In production, use enqueue; but we can run on a new thread.
        new Thread(() -> {
            try {
                List<House> houses = apiService.getHouses(area, maxRent, bedrooms).execute().body();
                List<LaborProvider> labors = apiService.getLabor("Lahore").execute().body();
                List<PackingProvider> packings = apiService.getPacking("Lahore").execute().body();
                List<TransportProvider> transports = apiService.getTransport("Lahore").execute().body();

                if (houses == null) houses = new ArrayList<>();
                if (labors == null) labors = new ArrayList<>();
                if (packings == null) packings = new ArrayList<>();
                if (transports == null) transports = new ArrayList<>();

                // Run callback on UI thread
                callback.onDataLoaded(houses, labors, packings, transports);
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        }).start();
    }
}