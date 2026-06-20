package com.example.movease.engine;

import com.example.movease.data.model.*;
import java.io.Serializable;
import java.util.List;

public class Plan implements Serializable {
    private House house;
    private LaborProvider labor;
    private PackingProvider packing;
    private TransportProvider transport;
    private double totalCost;
    private double score;
    private List<String> advantages;
    private List<String> disadvantages;

    // constructor, getters, setters
    public Plan(House house, LaborProvider labor, PackingProvider packing, TransportProvider transport,
                double totalCost, double score, List<String> advantages, List<String> disadvantages) {
        this.house = house;
        this.labor = labor;
        this.packing = packing;
        this.transport = transport;
        this.totalCost = totalCost;
        this.score = score;
        this.advantages = advantages;
        this.disadvantages = disadvantages;
    }

    // getters...
    public House getHouse() { return house; }
    public LaborProvider getLabor() { return labor; }
    public PackingProvider getPacking() { return packing; }
    public TransportProvider getTransport() { return transport; }
    public double getTotalCost() { return totalCost; }
    public double getScore() { return score; }
    public List<String> getAdvantages() { return advantages; }
    public List<String> getDisadvantages() { return disadvantages; }
}