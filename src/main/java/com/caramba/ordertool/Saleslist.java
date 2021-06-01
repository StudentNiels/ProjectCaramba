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
        sale.setDate(LocalDate.now());

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

    public Saleslist getSalesByProduct(UUID productID){
        Saleslist soldProducts = new Saleslist();

        for (Sale sale : this.sales) {
            if(sale.getProducts().containsKey(productID)){
                Sale newSale = new Sale();
                newSale.addToProducts(productID, sale.getAmountByID(productID));
                newSale.setDate(sale.getDate());
                soldProducts.addToSalesList(newSale);
            }
        }
        return soldProducts;
    }

    public void listSales(){
        for(Sale sale : this.sales){
            System.out.println(sale.getDate());
            sale.listProducts();
        }
    }
}
