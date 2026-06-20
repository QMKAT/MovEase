package com.example.movease.data.api;

import android.util.Log;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import java.io.IOException;

public class MockInterceptor implements Interceptor {

    private static final String TAG = "MockInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String path = request.url().encodedPath();
        Log.d(TAG, "Intercepted: " + path);

        String json = null;

        if (path.contains("houses")) {
            json = "[{\"id\":\"h1\",\"address\":\"12-A, DHA Phase 5, Lahore\",\"area\":\"DHA\",\"price\":45000000,\"bedrooms\":3,\"rating\":4.2,\"lat\":31.46271,\"lng\":74.40168,\"disadvantages\":[]}," +
                    "{\"id\":\"h2\",\"address\":\"34-B, Gulberg III, Lahore\",\"area\":\"Gulberg\",\"price\":60000000,\"bedrooms\":4,\"rating\":4.5,\"lat\":31.5075,\"lng\":74.3389,\"disadvantages\":[]}]";
        } else if (path.contains("labor")) {
            json = "[{\"id\":\"l1\",\"name\":\"Ahmad Movers\",\"ratePerHour\":300,\"rating\":4.0,\"maxWorkers\":5,\"availabilityDate\":\"2026-06-21\"}]";
        } else if (path.contains("packing")) {
            json = "[{\"id\":\"p1\",\"name\":\"SafePack\",\"materialType\":\"Cardboard\",\"costPerBox\":150,\"rating\":4.3,\"deliveryAvailable\":true}]";
        } else if (path.contains("transport")) {
            json = "[{\"id\":\"t1\",\"name\":\"Porter Express\",\"vehicleType\":\"Truck\",\"costPerKm\":50,\"rating\":4.6,\"available\":true}]";
        }

        if (json != null) {
            return new Response.Builder()
                    .code(200)
                    .message("OK")
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .body(ResponseBody.create(MediaType.parse("application/json"), json))
                    .build();
        }

        // No mock data for this request – proceed to the real server (or fail)
        return chain.proceed(request);
    }
}