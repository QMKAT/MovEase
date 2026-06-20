package com.example.movease.engine;

import com.example.movease.data.model.*;
import java.util.*;

public class PlanGenerator {
    public List<Plan> generatePlans(List<House> houses, List<LaborProvider> labors,
                                    List<PackingProvider> packings, List<TransportProvider> transports,
                                    double maxBudget) {
        List<Plan> allPlans = new ArrayList<>();

        // Generate Cartesian product
        for (House h : houses) {
            if (h.getPrice() > maxBudget) continue;
            for (LaborProvider l : labors) {
                for (PackingProvider p : packings) {
                    for (TransportProvider t : transports) {
                        double estLaborCost = l.getRatePerHour() * 8; // assume 8 hours
                        double estTransportCost = t.getCostPerKm() * 10; // assume 10 km
                        double totalCost = h.getPrice() + estLaborCost + p.getCostPerBox() * 20 + estTransportCost;
                        if (totalCost > maxBudget) continue;

                        double score = calculateScore(h, l, p, t, totalCost, maxBudget);
                        List<String> adv = generateAdvantages(h, l, p, t);
                        List<String> disadv = generateDisadvantages(h, l, p, t);

                        allPlans.add(new Plan(h, l, p, t, totalCost, score, adv, disadv));
                    }
                }
            }
        }

        // Sort by score descending, take top 5
        Collections.sort(allPlans, (p1, p2) -> Double.compare(p2.getScore(), p1.getScore()));
        if (allPlans.size() > 5) {
            allPlans = allPlans.subList(0, 5);
        }
        return allPlans;
    }

    private double calculateScore(House h, LaborProvider l, PackingProvider p, TransportProvider t,
                                  double totalCost, double maxBudget) {
        // Weighted score: 60% cost, 40% average rating
        double costScore = 1 - (totalCost / maxBudget); // higher for cheaper
        double ratingScore = (h.getRating() + l.getRating() + p.getRating() + t.getRating()) / 4.0 / 5.0;
        return costScore * 0.6 + ratingScore * 0.4;
    }

    private List<String> generateAdvantages(House h, LaborProvider l, PackingProvider p, TransportProvider t) {
        List<String> adv = new ArrayList<>();
        if (h.getRating() >= 4.5) adv.add("🏠 Highly rated house");
        if (l.getRating() >= 4.5) adv.add("👷 Top-rated labor");
        if (p.getRating() >= 4.5) adv.add("📦 Premium packing material");
        if (t.getRating() >= 4.5) adv.add("🚚 Reliable transport");
        if (h.getDisadvantages() == null || h.getDisadvantages().isEmpty())
            adv.add("🏠 No reported house issues");
        return adv;
    }

    private List<String> generateDisadvantages(House h, LaborProvider l, PackingProvider p, TransportProvider t) {
        List<String> dis = new ArrayList<>();
        if (h.getDisadvantages() != null && !h.getDisadvantages().isEmpty())
            dis.addAll(h.getDisadvantages());
        if (l.getRating() < 3.5) dis.add("👷 Labor has low rating (" + l.getRating() + ")");
        if (t.getRating() < 3.5) dis.add("🚚 Transport rated below 3.5");
        if (!p.isDeliveryAvailable()) dis.add("📦 Packing delivery not available");
        return dis;
    }
}