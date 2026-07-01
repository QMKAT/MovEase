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
            json = "[" +
                    "{\"id\":\"54380760\",\"address\":\"5 Marla Facing Park Modern House, Lake City Sector M-6, Lahore\",\"area\":\"Lake City\",\"price\":36500000,\"bedrooms\":4,\"rating\":5.0,\"lat\":31.402,\"lng\":74.215,\"disadvantages\":[]}," +
                    "{\"id\":\"54397176\",\"address\":\"5 Marla Beautiful House, Central Park Housing Scheme, Lahore\",\"area\":\"Central Park\",\"price\":23500000,\"bedrooms\":4,\"rating\":5.0,\"lat\":31.479,\"lng\":74.395,\"disadvantages\":[]}," +
                    "{\"id\":\"53991103\",\"address\":\"1 Kanal Elegant House, DHA Phase 6, Lahore\",\"area\":\"DHA Phase 6\",\"price\":97500000,\"bedrooms\":5,\"rating\":5.0,\"lat\":31.464,\"lng\":74.398,\"disadvantages\":[]}," +
                    "{\"id\":\"53512499\",\"address\":\"1 Kanal Ultra Modern House, DHA Phase 7, Lahore\",\"area\":\"DHA Phase 7\",\"price\":78500000,\"bedrooms\":5,\"rating\":5.0,\"lat\":31.484893,\"lng\":74.472342,\"disadvantages\":[]}," +
                    "{\"id\":\"53587175\",\"address\":\"1 Kanal Beautiful Villa, DHA Phase 6, Lahore\",\"area\":\"DHA Phase 6\",\"price\":108000000,\"bedrooms\":5,\"rating\":5.0,\"lat\":31.465,\"lng\":74.399,\"disadvantages\":[]}," +
                    "{\"id\":\"54407526\",\"address\":\"5 Marla House, Al Jalil Garden Block B, Lahore\",\"area\":\"Al Jalil Garden\",\"price\":17000000,\"bedrooms\":4,\"rating\":4.5,\"lat\":31.521,\"lng\":74.293,\"disadvantages\":[]}," +
                    "{\"id\":\"54407568\",\"address\":\"3 Marla House Facing Park, Al Rehman Garden Phase 2, Lahore\",\"area\":\"Al Rehman Garden\",\"price\":12000000,\"bedrooms\":3,\"rating\":4.5,\"lat\":31.440,\"lng\":74.280,\"disadvantages\":[]}," +
                    "{\"id\":\"54381412\",\"address\":\"364 Sq Ft Studio, Kings Town Phase 1, Raiwind Road, Lahore\",\"area\":\"Kings Town\",\"price\":2500000,\"bedrooms\":0,\"rating\":4.5,\"lat\":31.390,\"lng\":74.230,\"disadvantages\":[]}," +
                    "{\"id\":\"52425659\",\"address\":\"2.5 Marla Flat, Jubilee Town, Lahore\",\"area\":\"Jubilee Town\",\"price\":8000000,\"bedrooms\":1,\"rating\":4.5,\"lat\":31.452,\"lng\":74.286,\"disadvantages\":[]}," +
                    "{\"id\":\"48354186\",\"address\":\"3 Marla House, Al-Kabir Town Phase 2, Lahore\",\"area\":\"Al-Kabir Town\",\"price\":14500000,\"bedrooms\":3,\"rating\":4.5,\"lat\":31.518,\"lng\":74.321,\"disadvantages\":[]}," +
                    "{\"id\":\"54363522\",\"address\":\"4 Kanal Farm House, Bedian, Lahore\",\"area\":\"Bedian\",\"price\":70000000,\"bedrooms\":3,\"rating\":5.0,\"lat\":31.489,\"lng\":74.494,\"disadvantages\":[]}," +
                    "{\"id\":\"54363523\",\"address\":\"4 Kanal Farm House, Sofia Farm Houses, Bedian Road, Lahore\",\"area\":\"Sofia Farm Houses\",\"price\":70000000,\"bedrooms\":5,\"rating\":5.0,\"lat\":31.488,\"lng\":74.493,\"disadvantages\":[]}," +
                    "{\"id\":\"54007932\",\"address\":\"1 Kanal Double Storey House, Township Sector A1, Lahore\",\"area\":\"Township\",\"price\":60000000,\"bedrooms\":5,\"rating\":4.5,\"lat\":31.510,\"lng\":74.358,\"disadvantages\":[]}," +
                    "{\"id\":\"51975362\",\"address\":\"1 Kanal House, Thokar Niaz Baig, Lahore\",\"area\":\"Thokar Niaz Baig\",\"price\":120000000,\"bedrooms\":8,\"rating\":4.5,\"lat\":31.464,\"lng\":74.243,\"disadvantages\":[]}," +
                    "{\"id\":\"54409442\",\"address\":\"10 Marla Luxury Designer House, Bahria Town Sector E, Lahore\",\"area\":\"Bahria Town\",\"price\":43000000,\"bedrooms\":5,\"rating\":4.5,\"lat\":31.376,\"lng\":74.190,\"disadvantages\":[]}," +
                    "{\"id\":\"54409482\",\"address\":\"10 Marla Slightly Used House, Bahria Town Talha Block, Lahore\",\"area\":\"Bahria Town\",\"price\":38000000,\"bedrooms\":5,\"rating\":4.5,\"lat\":31.375,\"lng\":74.191,\"disadvantages\":[]}," +
                    "{\"id\":\"54409504\",\"address\":\"5 Marla Brand New Furnished House, Bismillah Housing Scheme, GT Road, Lahore\",\"area\":\"Bismillah HS\",\"price\":25000000,\"bedrooms\":5,\"rating\":3.5,\"lat\":31.545,\"lng\":74.385,\"disadvantages\":[]}," +
                    "{\"id\":\"54409617\",\"address\":\"10 Marla Luxurious House, Bahria Town Jasmine Block, Lahore\",\"area\":\"Bahria Town\",\"price\":27800000,\"bedrooms\":5,\"rating\":4.5,\"lat\":31.375843,\"lng\":74.189715,\"disadvantages\":[]}," +
                    "{\"id\":\"54409616\",\"address\":\"1 Kanal Modern Designer House, DHA Phase 7, Lahore\",\"area\":\"DHA Phase 7\",\"price\":82500000,\"bedrooms\":5,\"rating\":4.5,\"lat\":31.484893,\"lng\":74.472342,\"disadvantages\":[]}," +
                    "{\"id\":\"54211684\",\"address\":\"5 Marla Brand New House, Park View City, Lahore\",\"area\":\"Park View City\",\"price\":14500000,\"bedrooms\":3,\"rating\":3.5,\"lat\":31.408,\"lng\":74.281,\"disadvantages\":[]}" +
                    "]";
        } else if (path.contains("labor")) {
            json = "[]";   // only provider services
        } else if (path.contains("packing")) {
            json = "[]";   // only provider services
        } else if (path.contains("transport")) {
            json = "[]";   // only provider services
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

        return chain.proceed(request);
    }
}