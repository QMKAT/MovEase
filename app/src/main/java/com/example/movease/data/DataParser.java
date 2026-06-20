package com.example.movease.data;

import java.util.List;

public class DataParser {
    public static double parsePrice(String priceStr) {
        if (priceStr == null) return 0;
        // remove "PKR " prefix and " Crore"/" Lakh" etc.
        String numeric = priceStr.replace("PKR ", "").replace(" Crore", "").replace(" Lakh", "").trim();
        try {
            double value = Double.parseDouble(numeric);
            // Convert to actual rupees (1 Crore = 10,000,000, 1 Lakh = 100,000)
            if (priceStr.contains("Crore")) {
                return value * 10_000_000;
            } else if (priceStr.contains("Lakh")) {
                return value * 100_000;
            } else {
                return value; // already in rupees?
            }
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // Derive a rating from badge labels (optional)
    public static double deriveRating(List<String> badges) {
        if (badges == null) return 3.5; // default neutral rating
        int score = 3;
        if (badges.contains("Titanium")) score += 1;
        if (badges.contains("Trusted badge")) score += 0.5;
        if (badges.contains("Verified badge")) score += 0.5;
        if (badges.contains("super hot")) score += 0.2; // popular, slightly better
        return Math.min(5.0, score);
    }
}