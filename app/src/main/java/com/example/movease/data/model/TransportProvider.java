package com.example.movease.data.model;

import java.io.Serializable;

public class TransportProvider implements Serializable {
    private String id;
    private String name;
    private String vehicleType;
    private double costPerKm;
    private double rating;
    private boolean available;

    public String getId() {
        return id;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getName() {
        return name;
    }

    public double getCostPerKm() {
        return costPerKm;
    }

    public double getRating() {
        return rating;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setCostPerKm(double costPerKm) {
        this.costPerKm = costPerKm;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

}