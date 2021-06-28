package com.caramba.ordertool.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

class SalesListTest {

    @Test
    void getSalesUpToYear() {
        SalesList salesList = new SalesList();
        LocalDateTime startDateTime = LocalDateTime.of(2016, 1, 1, 0, 0);
        //add one sale for the next 4 years
        for(LocalDateTime date = startDateTime; date.isBefore(startDateTime.plusYears(4)); date = date.plusYears(1)){
            salesList.addToSalesList(new Sale(date));
        }
        //get the sales starting from the current year up to 2 years later, the list should have 3 sales
        SalesList newSalesList = salesList.getSalesUpToYear(Year.of(startDateTime.plusYears(2).getYear()));
        assertEquals(3, newSalesList.getSales().size());
    }
}