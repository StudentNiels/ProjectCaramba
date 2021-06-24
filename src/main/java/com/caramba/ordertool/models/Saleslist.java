package com.caramba.ordertool.models;

/*
Als: medewerker van de inkoop

Wil ik: op basis van het seizoen, verkoop trends, geschatte levertijd en beschikbare voorraad een aanbeveling krijgen van welke producten er bestelt moeten worden.

Zodat ik: tijd kan besparen bij het kiezen van producten om te bestellen.
*/

import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;

public class Saleslist {

    private final ArrayList<Sale> sales;

    public Saleslist() {
        this.sales = new ArrayList<>();
    }

    public void addToSalesList(Sale sale) {
        this.sales.add(sale);
    }

    public ArrayList<Sale> getSales() {
        return sales;
    }

    /**
     * @return A new sales list that only includes sales with the given product. Unrelated products are removed from the sale entry
     */
    public Saleslist getSalesByProduct(String productID) {
        Saleslist soldProducts = new Saleslist();
        for (Sale sale : this.sales) {
            if (sale.getProducts().containsKey(productID)) {
                Sale newSale = new Sale(sale.getDate());
                newSale.addToProducts(productID, sale.getAmountByID(productID));
                soldProducts.addToSalesList(newSale);
            }
        }
        return soldProducts;
    }

    /**
     * @return A new salelist with all the sales before the given year
     */
    public Saleslist getSalesBeforeYearMonth(YearMonth yearMonth) {
        Saleslist result = new Saleslist();
        for (Sale sale : this.sales) {
            YearMonth saleYearMonth = YearMonth.of(sale.getDate().getYear(), sale.getDate().getMonthValue());
            if (saleYearMonth.isBefore(yearMonth)) {
                result.addToSalesList(sale);
            }
        }
        return result;
    }

    /**
     * @return Saleslist including all the sales made up to and including the given year
     */
    public Saleslist getSalesUpToYear(Year year) {
        YearMonth oneMonthAfter = YearMonth.of(year.plusYears(1).getValue(), 1);
        return getSalesBeforeYearMonth(oneMonthAfter);
    }

    /**
     * @return How many of the product were sold in a certain yearMonth
     */
    public int getSoldInYearMonth(String productID, YearMonth date) {
        Saleslist salesList = getSalesByProduct(productID);
        int SoldThisMonth = 0;
        for (Sale sale : salesList.getSales()) {
            int amount = sale.getAmountByID(productID);
            if (YearMonth.from(sale.getDate()).equals(date)) {
                SoldThisMonth = SoldThisMonth + amount;
            }
        }
        return SoldThisMonth;
    }

    /**
     * Looks trough the sales of the product and adds them up to the total sold per YearMonth
     */
    public HashMap<YearMonth, Integer> getDateAmountMap(String productID) {
        Saleslist salesList = getSalesByProduct(productID);
        HashMap<YearMonth, Integer> dateAmountMap = new HashMap<>();
        for (Sale sale : salesList.getSales()) {
            int amount = sale.getAmountByID(productID);
            YearMonth yearMonth = YearMonth.from(sale.getDate());
            if (dateAmountMap.containsKey(yearMonth)) {
                dateAmountMap.put(yearMonth, amount + dateAmountMap.get(yearMonth));
            } else {
                dateAmountMap.put(yearMonth, amount);
            }
        }
        return dateAmountMap;
    }
}
