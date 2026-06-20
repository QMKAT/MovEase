package com.example.movease.data.model;

import java.io.Serializable;
import java.util.List;

public class House implements Serializable {
    private String id;
    private String address;
    private String area;
    private double rent;
    private int bedrooms;
    private double rating;
    private double lat;
    private double lng;
    private List<String> disadvantages;

    // Getters and setters (or use public fields for simplicity)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
    public double getRent() { return rent; }
    public void setRent(double rent) { this.rent = rent; }
    public int getBedrooms() { return bedrooms; }
    public void setBedrooms(int bedrooms) { this.bedrooms = bedrooms; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }
    public double getLng() { return lng; }
    public void setLng(double lng) { this.lng = lng; }
    public List<String> getDisadvantages() { return disadvantages; }
    public void setDisadvantages(List<String> disadvantages) { this.disadvantages = disadvantages; }
}