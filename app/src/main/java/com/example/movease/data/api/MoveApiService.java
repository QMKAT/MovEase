package com.example.movease.data.api;

import com.example.movease.data.model.*;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import java.util.List;

public interface MoveApiService {
    @GET("api/houses")
    Call<List<House>> getHouses(@Query("area") String area,
                                @Query("max_rent") double maxRent,
                                @Query("min_bedrooms") int bedrooms);

    @GET("api/labor")
    Call<List<LaborProvider>> getLabor(@Query("city") String city);

    @GET("api/packing")
    Call<List<PackingProvider>> getPacking(@Query("city") String city);

    @GET("api/transport")
    Call<List<TransportProvider>> getTransport(@Query("city") String city);
}