package com.caramba.ordertool.models;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A collection of recommendations
 */
public class RecommendationList {
    private ArrayList<Recommendation> recommendations = new ArrayList<>();

    /**
     * Add the specified recommendation to this list.
     *
     * @param recommendation the recommendation to add
     */
    public void addRecommendation(Recommendation recommendation) {
        recommendations.add(recommendation);
    }

    /**
     * Returns an arraylist with all the recommendations in this list
     *
     * @return an arraylist with recommendations
     */
    public ArrayList<Recommendation> getRecommendations() {
        return recommendations;
    }

    /**
     * Set the arraylist of recommendations directly
     *
     * @param recommendations arraylist of recommendations to set
     */
    public void setRecommendations(ArrayList<Recommendation> recommendations) {
        this.recommendations = recommendations;
    }

    /**
     * Sorts the recommendations of the recommendationList by final order date and creation date.
     */
    public void sortRecommendationsByDate() {
        ArrayList<Recommendation> tempList = this.recommendations;
        ArrayList<Recommendation> sortedList = new ArrayList<>();

        HashMap<String, ArrayList<LocalDateTime>> dateTimes = getRecommendationDates();

        ArrayList<LocalDateTime> finalOrderDates = new ArrayList<>();
        ArrayList<LocalDateTime> creationDates = new ArrayList<>();

        for (Map.Entry<String, ArrayList<LocalDateTime>> entry : dateTimes.entrySet()) {
            if (entry.getKey().equals("FinalOrderDate")) {
                finalOrderDates.addAll(entry.getValue());
            }
            if (entry.getKey().equals("CreationDate")) {
                creationDates.addAll(entry.getValue());
            }
        }

        if (!finalOrderDates.isEmpty()) {
            Collections.sort(finalOrderDates);
            Collections.reverse(finalOrderDates);
        }

        if (!creationDates.isEmpty()) {
            Collections.sort(creationDates);
            Collections.reverse(creationDates);
        }

        if (!finalOrderDates.isEmpty()) {
            for (LocalDateTime dateTime : finalOrderDates) {
                for (Recommendation recommendation : tempList) {
                    if (recommendation.getFinalOrderDate() != null) {
                        if (recommendation.getFinalOrderDate().equals(dateTime.toLocalDate())) {
                            sortedList.add(recommendation);
                        }
                    }
                }
            }
        }

        if (!creationDates.isEmpty()) {
            for (LocalDateTime dateTime : creationDates) {
                for (Recommendation recommendation : tempList) {
                    if (!sortedList.contains(recommendation)) {
                        if (recommendation.getCreationDate().equals(dateTime)) {
                            sortedList.add(recommendation);
                        }
                    }
                }
            }
        }
        setRecommendations(sortedList);
    }

    private HashMap<String, ArrayList<LocalDateTime>> getRecommendationDates() {
        HashMap<String, ArrayList<LocalDateTime>> dateTimes = new HashMap<>();
        ArrayList<LocalDateTime> finalOrderDates = new ArrayList<>();
        ArrayList<LocalDateTime> creationDates = new ArrayList<>();

        for (Recommendation recommendation : this.recommendations) {
            if (recommendation.getFinalOrderDate() != null) {
                finalOrderDates.add(LocalDateTime.of(recommendation.getFinalOrderDate(), LocalTime.MAX));
            } else {
                creationDates.add(recommendation.getCreationDate());
            }
        }
        dateTimes.put("CreationDate", creationDates);
        dateTimes.put("FinalOrderDate", finalOrderDates);

        return dateTimes;
    }

}
