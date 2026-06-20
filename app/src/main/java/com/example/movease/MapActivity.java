package com.example.movease;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Retrieve extras from the intent
        double lat = getIntent().getDoubleExtra("lat", 31.5204);   // default: Lahore centre
        double lng = getIntent().getDoubleExtra("lng", 74.3587);
        String address = getIntent().getStringExtra("address");

        // If no address is provided, use a placeholder
        if (address == null || address.isEmpty()) {
            address = "House Location";
        }

        WebView webView = findViewById(R.id.webViewMap);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        // Build OpenStreetMap embed URL with a marker
        // We'll use an HTML page to display the map and a marker (more flexible)
        String html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "<link rel=\"stylesheet\" href=\"https://unpkg.com/leaflet@1.9.4/dist/leaflet.css\" />\n" +
                "<script src=\"https://unpkg.com/leaflet@1.9.4/dist/leaflet.js\"></script>\n" +
                "<style>body{margin:0;height:100vh;width:100vw;}</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div id=\"map\" style=\"width:100%;height:100%;\"></div>\n" +
                "<script>\n" +
                "var map = L.map('map').setView([" + lat + "," + lng + "], 16);\n" +
                "L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {\n" +
                "   maxZoom: 19,\n" +
                "   attribution: '&copy; OpenStreetMap contributors'\n" +
                "}).addTo(map);\n" +
                "L.marker([" + lat + "," + lng + "]).addTo(map)\n" +
                "   .bindPopup('" + address + "')\n" +
                "   .openPopup();\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>";

        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
    }
}