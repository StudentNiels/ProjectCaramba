package com.caramba.ordertool;

/*
Als: medewerker van de inkoop

Wil ik: op basis van het seizoen, verkoop trends, geschatte levertijd en beschikbare voorraad een aanbeveling krijgen van welke producten er bestelt moeten worden.

Zodat ik: tijd kan besparen bij het kiezen van producten om te bestellen.
*/

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;

public class Saleslist {

    private ArrayList<Sale> sales;

    public Saleslist(){
        this.sales = new ArrayList<>();
    }

    public void addToSalesList(Sale sale){
        this.sales.add(sale);
    }

    public int size(){
        return sales.size();
    }

    public ArrayList<Sale> getSales() {
        return sales;
    }

    public void setSales(ArrayList<Sale> sales) {
        this.sales = sales;
    }

    public Sale getSaleByID(int index){
        return sales.get(index);
    }

    /**
     * @return A new sales list that only includes sales with the given product. Unrelated products are removed from the sale entry
     */
    public Saleslist getSalesByProduct(String productID){
        Saleslist soldProducts = new Saleslist();
        for (Sale sale : this.sales) {
            if(sale.getProducts().containsKey(productID)){
                Sale newSale = new Sale(sale.getDate());
                newSale.addToProducts(productID, sale.getAmountByID(productID));
                soldProducts.addToSalesList(newSale);
            }
        }
        return soldProducts;
    }

    /**
     *
     * @return A new salelist with all the sales in the given year
     */
    public Saleslist getSalesByYear(int year){
        Saleslist result = new Saleslist();
        for (Sale sale : this.sales) {
            if(sale.getDate().getYear() == year){
                result.addToSalesList(sale);
            }
        }
        return result;
    }

    /**
     *
     * @return A new salelist with all the sales before the given year
     */
    public Saleslist getSalesBeforeYear(int year){
        Saleslist result = new Saleslist();
        for (Sale sale : this.sales) {
            if(sale.getDate().getYear() < year){
                result.addToSalesList(sale);
            }
        }
        return result;
    }

    public void listSales(){
        for(Sale sale : this.sales){
            System.out.println(sale.getDate());
            sale.listProducts();
        }
    }

    /**
     * @return How many of the product were sold in a certain year
     */
    public int getTotalSoldInYear(String productID, int year){
        Saleslist salesList = getSalesByProduct(productID);
        int totalSoldThisYear = 0;
        for(Sale sale : salesList.getSales()){
            int amount = sale.getAmountByID(productID);
            if(sale.getDate().getYear() == year){
                totalSoldThisYear = totalSoldThisYear + amount;
            }
        }
        return  totalSoldThisYear;
    }

    /**
     * @return How many of the product were sold in a certain yearMonth
     */
    public int getSoldInYearMonth(String productID, YearMonth date){
        Saleslist salesList = getSalesByProduct(productID);
        int SoldThisMonth = 0;
        for(Sale sale : salesList.getSales()){
            int amount = sale.getAmountByID(productID);
            if(YearMonth.from(sale.getDate()).equals(date)){
                SoldThisMonth = SoldThisMonth + amount;
            }
        }
        return  SoldThisMonth;
    }

    /**
     * Looks trough the sales of the product and adds them up to the total sold per YearMonth
     */
    public HashMap<YearMonth, Integer> getDateAmountMap(String productID){
        Saleslist salesList = getSalesByProduct(productID);
        HashMap<YearMonth, Integer> dateAmountMap = new HashMap<>();
        for(Sale sale : salesList.getSales()){
            int amount = sale.getAmountByID(productID);
            YearMonth yearMonth = YearMonth.from(sale.getDate());
            if(dateAmountMap.containsKey(yearMonth)){
                dateAmountMap.put(yearMonth, amount + dateAmountMap.get(yearMonth));
            }else{
                dateAmountMap.put(yearMonth, amount);
            }
        }
        return  dateAmountMap;
    }
}
