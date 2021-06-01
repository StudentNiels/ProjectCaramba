package com.caramba.ordertool;

/*
Als: medewerker van de inkoop

Wil ik: op basis van het seizoen, verkoop trends, geschatte levertijd en beschikbare voorraad een aanbeveling krijgen van welke producten er bestelt moeten worden.

Zodat ik: tijd kan besparen bij het kiezen van producten om te bestellen.
*/

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

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
     * @param productID
     * @return A new sales list that only includes sales with the given product. Unrelated products are removed from the sale entry
     */
    public Saleslist getSalesByProduct(UUID productID){
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
     * @param year
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

    public void listSales(){
        for(Sale sale : this.sales){
            System.out.println(sale.getDate());
            sale.listProducts();
        }
    }
}
