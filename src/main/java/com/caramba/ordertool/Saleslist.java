package com.caramba.ordertool;

/*
Als: medewerker van de inkoop

Wil ik: op basis van het seizoen, verkoop trends, geschatte levertijd en beschikbare voorraad een aanbeveling krijgen van welke producten er bestelt moeten worden.

Zodat ik: tijd kan besparen bij het kiezen van producten om te bestellen.
*/

import java.util.ArrayList;
import java.util.Calendar;

public class Saleslist {

    private ArrayList<Sale> sales;

    public Saleslist(){
        this.sales = new ArrayList<>();
    }

    public void addToSalesList(Sale sale){
        sale.setDate(Calendar.getInstance().getTime());

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

    public void listsales(){
        for(Sale sale : this.sales){
            System.out.println(sale.getDate());
            sale.listProducts();
        }
    }
}
