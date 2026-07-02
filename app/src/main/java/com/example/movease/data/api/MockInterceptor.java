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
                    "{\"id\":\"53924213\",\"address\":\"5 Marla Furnished House, DHA 9 Town, Lahore\",\"area\":\"DHA 9 Town\",\"price\":25600000,\"bedrooms\":3,\"rating\":5.0,\"lat\":31.470,\"lng\":74.420,\"disadvantages\":[],\"imageUrl\":\"https://media.zameen.com/thumbnails/304060258-400x300.jpeg\"}," +
                    "{\"id\":\"53594990\",\"address\":\"10 Marla Luxury House, Johar Town, Lahore\",\"area\":\"Johar Town\",\"price\":50000000,\"bedrooms\":5,\"rating\":5.0,\"lat\":31.4697,\"lng\":74.2728,\"disadvantages\":[],\"imageUrl\":\"https://media.zameen.com/thumbnails/300689355-400x300.jpeg\"}," +
                    "{\"id\":\"54288665\",\"address\":\"5 Marla Ultra Modern House, DHA 11 Rahbar Sector 2, Lahore\",\"area\":\"DHA 11 Rahbar\",\"price\":28500000,\"bedrooms\":3,\"rating\":5.0,\"lat\":31.452,\"lng\":74.393,\"disadvantages\":[],\"imageUrl\":\"https://media.zameen.com/thumbnails/304058959-400x300.jpeg\"}," +
                    "{\"id\":\"54412456\",\"address\":\"2 Kanal Elegant Bungalow, DHA Phase 6, Lahore\",\"area\":\"DHA Phase 6\",\"price\":560000000,\"bedrooms\":6,\"rating\":5.0,\"lat\":31.464,\"lng\":74.398,\"disadvantages\":[],\"imageUrl\":\"https://media.zameen.com/thumbnails/304054985-400x300.jpeg\"}," +
                    "{\"id\":\"54407186\",\"address\":\"5 Marla Designer House, DHA 9 Town, Lahore\",\"area\":\"DHA 9 Town\",\"price\":28500000,\"bedrooms\":3,\"rating\":5.0,\"lat\":31.471,\"lng\":74.421,\"disadvantages\":[],\"imageUrl\":\"https://media.zameen.com/thumbnails/304005149-400x300.jpeg\"}," +
                    "{\"id\":\"54345098\",\"address\":\"5 Marla Brand New House, Park View City Platinum Block, Lahore\",\"area\":\"Park View City\",\"price\":18500000,\"bedrooms\":4,\"rating\":4.5,\"lat\":31.408,\"lng\":74.281,\"disadvantages\":[],\"imageUrl\":\"https://media.zameen.com/thumbnails/302353609-400x300.jpeg\"}," +
                    "{\"id\":\"54172985\",\"address\":\"5 Marla House, Low Cost Block C, Lahore\",\"area\":\"Low Cost\",\"price\":21000000,\"bedrooms\":3,\"rating\":4.5,\"lat\":31.545,\"lng\":74.332,\"disadvantages\":[],\"imageUrl\":\"https://media.zameen.com/thumbnails/300958162-400x300.jpeg\"}," +
                    "{\"id\":\"54326976\",\"address\":\"1 Kanal Corner House, DHA Phase 5 Block J, Lahore\",\"area\":\"DHA Phase 5\",\"price\":83500000,\"bedrooms\":4,\"rating\":4.2,\"lat\":31.482,\"lng\":74.391,\"disadvantages\":[],\"imageUrl\":\"https://media.zameen.com/thumbnails/300550653-400x300.jpeg\"}," +
                    "{\"id\":\"49963515\",\"address\":\"6 Marla House, Paragon City, Lahore\",\"area\":\"Paragon City\",\"price\":22500000,\"bedrooms\":3,\"rating\":4.5,\"lat\":31.467,\"lng\":74.365,\"disadvantages\":[],\"imageUrl\":\"https://media.zameen.com/thumbnails/300395568-400x300.jpeg\"}," +
                    "{\"id\":\"50697267\",\"address\":\"10 Marla House, Paragon City, Lahore\",\"area\":\"Paragon City\",\"price\":41000000,\"bedrooms\":5,\"rating\":4.5,\"lat\":31.468,\"lng\":74.366,\"disadvantages\":[],\"imageUrl\":\"https://media.zameen.com/thumbnails/302639828-400x300.jpeg\"}," +
                    "{\"id\":\"54399127\",\"address\":\"1 Kanal Modern House with Pool, DHA Phase 6, Lahore\",\"area\":\"DHA Phase 6\",\"price\":92500000,\"bedrooms\":5,\"rating\":4.5,\"lat\":31.463,\"lng\":74.397,\"disadvantages\":[],\"imageUrl\":\"https://media.zameen.com/thumbnails/303927366-400x300.jpeg\"}," +
                    "{\"id\":\"52102827\",\"address\":\"1 Kanal Spanish Bungalow, DHA Phase 6 Block F, Lahore\",\"area\":\"DHA Phase 6\",\"price\":91100000,\"bedrooms\":5,\"rating\":4.5,\"lat\":31.460,\"lng\":74.396,\"disadvantages\":[],\"imageUrl\":\"https://media.zameen.com/thumbnails/300607352-400x300.jpeg\"}," +
                    "{\"id\":\"54389284\",\"address\":\"5 Marla Modern House, Lake City Sector M-7B, Lahore\",\"area\":\"Lake City\",\"price\":28000000,\"bedrooms\":4,\"rating\":4.0,\"lat\":31.401,\"lng\":74.214,\"disadvantages\":[],\"imageUrl\":\"https://media.zameen.com/thumbnails/303831337-400x300.jpeg\"}," +
                    "{\"id\":\"54399903\",\"address\":\"10 Marla Ultra Modern House, Lake City Sector M-2A, Lahore\",\"area\":\"Lake City\",\"price\":55000000,\"bedrooms\":6,\"rating\":4.0,\"lat\":31.402,\"lng\":74.213,\"disadvantages\":[],\"imageUrl\":\"https://media.zameen.com/thumbnails/303936182-400x300.jpeg\"}," +
                    "{\"id\":\"54316824\",\"address\":\"8 Marla Facing Park House, Bahria Nasheman Zinia, Lahore\",\"area\":\"Bahria Nasheman\",\"price\":22000000,\"bedrooms\":6,\"rating\":4.0,\"lat\":31.375,\"lng\":74.188,\"disadvantages\":[],\"imageUrl\":\"https://media.zameen.com/thumbnails/301119200-400x300.jpeg\"}," +
                    "{\"id\":\"54370284\",\"address\":\"6 Marla Corner House, Bahria Nasheman Iris, Lahore\",\"area\":\"Bahria Nasheman\",\"price\":16200000,\"bedrooms\":4,\"rating\":4.0,\"lat\":31.376,\"lng\":74.189,\"disadvantages\":[],\"imageUrl\":\"https://media.zameen.com/thumbnails/303180074-400x300.jpeg\"}," +
                    "{\"id\":\"54368253\",\"address\":\"5 Marla Furnished Luxury House, Lake City Sector M7 Block C, Lahore\",\"area\":\"Lake City\",\"price\":32000000,\"bedrooms\":4,\"rating\":4.0,\"lat\":31.401,\"lng\":74.215,\"disadvantages\":[],\"imageUrl\":\"https://media.zameen.com/thumbnails/303678248-400x300.jpeg\"}," +
                    "{\"id\":\"53314803\",\"address\":\"1 Kanal Luxurious Villa, DHA Phase 7 Block W, Lahore\",\"area\":\"DHA Phase 7\",\"price\":82500000,\"bedrooms\":5,\"rating\":5.0,\"lat\":31.485,\"lng\":74.473,\"disadvantages\":[],\"imageUrl\":\"https://media.zameen.com/thumbnails/299938813-400x300.jpeg\"}," +
                    "{\"id\":\"53904812\",\"address\":\"1 Kanal Brand New Bungalow, DHA Phase 7, Lahore\",\"area\":\"DHA Phase 7\",\"price\":94500000,\"bedrooms\":5,\"rating\":5.0,\"lat\":31.486,\"lng\":74.474,\"disadvantages\":[],\"imageUrl\":\"https://media.zameen.com/thumbnails/301074147-400x300.jpeg\"}," +
                    "{\"id\":\"54113920\",\"address\":\"7 Marla Brand New House, Lake City Sector M-7, Lahore\",\"area\":\"Lake City\",\"price\":39000000,\"bedrooms\":6,\"rating\":3.5,\"lat\":31.402,\"lng\":74.214,\"disadvantages\":[],\"imageUrl\":\"https://media.zameen.com/thumbnails/301024041-400x300.jpeg\"}," +
                    "{\"id\":\"54113880\",\"address\":\"12 Marla Brand New House, Lake City Sector M-3A, Lahore\",\"area\":\"Lake City\",\"price\":60000000,\"bedrooms\":6,\"rating\":3.5,\"lat\":31.403,\"lng\":74.216,\"disadvantages\":[],\"imageUrl\":\"https://media.zameen.com/thumbnails/301030472-400x300.jpeg\"}," +
                    "{\"id\":\"53634055\",\"address\":\"1 Kanal Modern Bungalow, DHA Phase 4, Lahore\",\"area\":\"DHA Phase 4\",\"price\":70000000,\"bedrooms\":5,\"rating\":3.5,\"lat\":31.472,\"lng\":74.388,\"disadvantages\":[],\"imageUrl\":\"https://media.zameen.com/thumbnails/301753703-400x300.jpeg\"}," +
                    "{\"id\":\"53633999\",\"address\":\"1 Kanal Ultra-Modern Designer Home, DHA Phase 3 Block XX, Lahore\",\"area\":\"DHA Phase 3\",\"price\":75000000,\"bedrooms\":5,\"rating\":3.5,\"lat\":31.478,\"lng\":74.385,\"disadvantages\":[],\"imageUrl\":\"https://media.zameen.com/thumbnails/301753622-400x300.jpeg\"}," +
                    "{\"id\":\"53633866\",\"address\":\"1 Kanal Architectural Masterpiece, DHA Phase 1, Lahore\",\"area\":\"DHA Phase 1\",\"price\":70000000,\"bedrooms\":5,\"rating\":3.5,\"lat\":31.483,\"lng\":74.382,\"disadvantages\":[],\"imageUrl\":\"https://media.zameen.com/thumbnails/301753568-400x300.jpeg\"}," +
                    "{\"id\":\"54407558\",\"address\":\"10 Marla House, DHA Phase 5, Lahore\",\"area\":\"DHA Phase 5\",\"price\":57000000,\"bedrooms\":4,\"rating\":4.5,\"lat\":31.481,\"lng\":74.390,\"disadvantages\":[],\"imageUrl\":\"https://media.zameen.com/thumbnails/304009616-400x300.jpeg\"}" +
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