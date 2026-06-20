package com.example.movease.data.model;

import java.io.Serializable;

public class Service implements Serializable {
    private String id;          // Firestore document ID
    private String providerId;  // UID of the provider
    private String type;        // "labor", "packing", "transport"
    private String name;
    private double rate;        // for labor: rate per hour; packing: cost per box; transport: cost per km
    private String description; // e.g., "Professional movers", "Cardboard boxes"
    private double rating;      // self-assigned or calculated later

    public Service() {} // needed for Firestore

    public Service(String id, String providerId, String type, String name, double rate,
                   String description, double rating) {
        this.id = id;
        this.providerId = providerId;
        this.type = type;
        this.name = name;
        this.rate = rate;
        this.description = description;
        this.rating = rating;
    }

    // Getters and setters (generate via Alt+Insert or copy below)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getProviderId() { return providerId; }
    public void setProviderId(String providerId) { this.providerId = providerId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getRate() { return rate; }
    public void setRate(double rate) { this.rate = rate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
}