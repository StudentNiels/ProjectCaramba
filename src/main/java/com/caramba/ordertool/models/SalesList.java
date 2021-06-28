package com.caramba.ordertool.models;

import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A collection of sales
 */
public class SalesList {

    private final ArrayList<Sale> sales = new ArrayList<>();

    /**
     * Add the specified sale to the SalesList
     *
     * @param sale sale to add to the SalesList
     */
    public void addToSalesList(Sale sale) {
        this.sales.add(sale);
    }

    /**
     * Returns the arraylist of sales in this SalesList
     *
     * @return arraylist of sales in this SalesList
     */
    public ArrayList<Sale> getSales() {
        return sales;
    }

    /**
     * Returns a new salesList that only includes sales that contain the specified product. Unrelated products are removed from the sale entry
     *
     * @param productID the id of the product to get sales for
     * @return salesList containing sales of the specified product
     */
    public SalesList getSalesByProduct(String productID) {
        SalesList soldProducts = new SalesList();
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
     * Returns a new SalesList with all the sales before the specified year
     *
     * @param yearMonth the cutoff date to get sales before of
     * @return SalesList with all the sales before the specified year
     */
    public SalesList getSalesBeforeYearMonth(YearMonth yearMonth) {
        SalesList result = new SalesList();
        for (Sale sale : this.sales) {
            YearMonth saleYearMonth = YearMonth.of(sale.getDate().getYear(), sale.getDate().getMonthValue());
            if (saleYearMonth.isBefore(yearMonth)) {
                result.addToSalesList(sale);
            }
        }
        return result;
    }

    /**
     * Returns a new SalesList including all the sales made up to and including the specified year
     *
     * @param year the year up to wich to get sales for
     * @return SalesList including all the sales made up to and including the specified year
     */
    public SalesList getSalesUpToYear(Year year) {
        YearMonth oneMonthAfter = YearMonth.of(year.plusYears(1).getValue(), 1);
        return getSalesBeforeYearMonth(oneMonthAfter);
    }

    /**
     * Returns how many units of the specified product were sold in a certain yearMonth
     *
     * @param productID the id of the product to lookup sales for
     * @param date      the yearMonth to count units sold of
     * @return How many of the product were sold in a certain yearMonth
     */
    public int getSoldInYearMonth(String productID, YearMonth date) {
        SalesList salesList = getSalesByProduct(productID);
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
     *
     * @param productID the id of the product to look up the sales for
     * @return a hashmap with the yearMonth that sales were made as key, and the total amount of units sold that yearMonth as value.
     */
    public HashMap<YearMonth, Integer> getDateAmountMap(String productID) {
        SalesList salesList = getSalesByProduct(productID);
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
