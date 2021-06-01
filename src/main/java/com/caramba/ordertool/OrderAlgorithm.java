package com.caramba.ordertool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OrderAlgorithm {
    /**
     * This method is the algorithm that will return a suggestion list with amounts
     * NOTE: this is only a suggestion, sales user will still have to either confirm the amount or enter their own
     *
     * magazijn capaciteit vragen
     * Kijken of inkoop mogelijkheid verminderd kan worden voor meer winst
     * mooie auto, maar geen motor, dus dat werkt niet
     *
     * Period
     * Beschikbare voorraad - done (staat in product)
     * Geschatte levertijd
     * verkoop trend
     *
     * @param productList A list containing the products that are to be filtered
     * @return suggestionList with products and amounts
     * */
    public HashMap<Product, Integer> createSuggestionList(HashMap<UUID, Product> productList){
        HashMap<Product, Integer> suggestionList = new HashMap<>();

        return suggestionList;
    }
}
