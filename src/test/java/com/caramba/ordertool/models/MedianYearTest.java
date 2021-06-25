package com.caramba.ordertool.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class MedianYearTest {

    ProductList products;
    SalesList sales1;
    SalesList sales2;
    SalesList oneYearSales;
    LocalDateTime startDate;
    final int yearsToLookForward = 5;
    String productID = "testID";
    @BeforeEach
    void setup(){
        //add products
        products = new ProductList();
        products.add(productID, new Product("abc", "testProduct"));
        //In order to test medians with both odd and even number of years, add sales of X number of years to one list and X + 1 to the other
        //we also test with only 1 year worth of sales
        sales1 = new SalesList();
        sales2 = new SalesList();
        oneYearSales = new SalesList();
        startDate = LocalDateTime.of(2016, 6, 15, 12, 30);
        //i = 0: x amount of years
        //i = 1: x + 1 amount of years
        //i = 2: 1 year
        for(int i = 0; i < 3; i++){
            //sales increase by 10 every year
            //as the sales only increase, the median should be the values of the middle year or the average of the two middle years if the year count is even
            int yearsToLook = yearsToLookForward;
            if(i == 1){
                yearsToLook = yearsToLook + 1;
            }else if(i == 2){
                yearsToLook = 1;
            }
            for(int year = 0; year < yearsToLook; year++){
                //sales increase by 3 until the peak month of july, then sales decrease by 3 until december
                for(int month = 0; month < 12 ; month++){
                    int amount = 20 + (10 * year) + (3 * month);
                    if(month > 7){
                        amount = amount - (6 * (month - 7));
                    }
                    HashMap<String, Integer> products = new HashMap<>();
                    products.put(productID, amount);
                    Sale sale = new Sale(products, startDate.plusYears(year).plusMonths(month));
                    if(i == 0){
                        sales1.addToSalesList(sale);
                    }else if (i == 1){
                        sales2.addToSalesList(sale);
                    }else {
                        oneYearSales.addToSalesList(sale);
                    }
                }
            }
        }

    }

    @Test
    void getByMonthNumber() {
        MedianYear medianYear1 = MedianYear.getMedianYear(sales1.getDateAmountMap(productID));
        MedianYear medianYear2 = MedianYear.getMedianYear(sales2.getDateAmountMap(productID));
        MedianYear medianYearOneYear = MedianYear.getMedianYear(oneYearSales.getDateAmountMap(productID));
        MedianYear medianYear;
        int amount;
        int yearsToLook = yearsToLookForward;
        //i = 0: x amount of years
        //i = 1: x + 1 amount of years
        //i = 2: 1 year
        for(int i = 0; i < 3; i++){
            if(i == 0){
                medianYear = medianYear1;
            }else if(i == 1){
                medianYear = medianYear2;
                yearsToLook = yearsToLook + 1;
            }else{
                yearsToLook = 1;
                medianYear = medianYearOneYear;
            }
            if(yearsToLook % 2 == 0){
                //get the two middle years
                int year1 = (yearsToLook / 2) - 1;
                int year2 = year1 + 1;
                for(int month = 0; month < 12 ; month++) {
                    int amount1 = 20 + (10 * year1) + (3 * month);
                    int amount2 = 20 + (10 * year2) + (3 * month);
                    if (month > 7) {
                        amount1 = amount1 - (6 * (month - 7));
                        amount2 = amount2 - (6 * (month - 7));
                    }
                    //get the average
                    amount = (amount1 + amount2) / 2;
                    assertEquals(amount, medianYear.getByMonthNumber(startDate.plusMonths(month).getMonthValue()));
                }
            }else{
                //get the middle year
                int year = (int) Math.ceil((double) yearsToLook / 2) - 1;
                for(int month = 0; month < 12 ; month++){
                    amount = 20 + (10 * year) + (3 * month);
                    if(month > 7){
                        amount = amount - (6 * (month - 7));
                    }
                    assertEquals(amount, medianYear.getByMonthNumber(startDate.plusMonths(month).getMonthValue()));
                }
            }

        }

        //assert that an invalid month number throws an exception
        Assertions.assertThrows(IllegalArgumentException.class, () -> medianYear1.getByMonthNumber(0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> medianYear1.getByMonthNumber(13));

        //test with no sales
        SalesList noSales = new SalesList();
        MedianYear noSalesMedian = MedianYear.getMedianYear(noSales.getDateAmountMap(productID));
        for(int m = 1; m <= 12; m++ ){
            assertEquals(noSalesMedian.getByMonthNumber(m), 0);
        }
    }
}