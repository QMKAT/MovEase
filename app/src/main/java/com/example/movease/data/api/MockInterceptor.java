package com.example.movease.data.api;

import android.content.Context;
import android.content.res.Resources;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import java.io.IOException;
import java.io.InputStream;

public class MockInterceptor implements Interceptor {
    private Context context;

    public MockInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String path = request.url().encodedPath();
        String jsonFileName = null;

        if (path.contains("houses")) {
            jsonFileName = "houses.json";
        } else if (path.contains("labor")) {
            jsonFileName = "labor.json";
        } else if (path.contains("packing")) {
            jsonFileName = "packing.json";
        } else if (path.contains("transport")) {
            jsonFileName = "transport.json";
        }

        if (jsonFileName != null) {
            String json = loadJSONFromRaw(jsonFileName);
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

    private String loadJSONFromRaw(String fileName) {
        Resources resources = context.getResources();
        int resId = resources.getIdentifier(fileName, "raw", context.getPackageName());
        InputStream inputStream = resources.openRawResource(resId);
        try {
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            return new String(buffer, "UTF-8");
        } catch (IOException e) {
            return "[]";
        }
    }
}