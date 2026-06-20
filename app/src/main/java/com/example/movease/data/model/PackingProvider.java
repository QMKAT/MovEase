package com.example.movease.data.model;

import java.io.Serializable;

public class PackingProvider implements Serializable {
    private String id;
    private String name;
    private String materialType;
    private double costPerBox;
    private double rating;
    private boolean deliveryAvailable;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public double getCostPerBox() {
        return costPerBox;
    }

    public void setCostPerBox(double costPerBox) {
        this.costPerBox = costPerBox;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public boolean isDeliveryAvailable() {
        return deliveryAvailable;
    }

    public void setDeliveryAvailable(boolean deliveryAvailable) {
        this.deliveryAvailable = deliveryAvailable;
    }
}