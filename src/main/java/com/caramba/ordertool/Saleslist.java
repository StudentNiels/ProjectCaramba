package com.caramba.ordertool;

/*
Als: medewerker van de inkoop

Wil ik: op basis van het seizoen, verkoop trends, geschatte levertijd en beschikbare voorraad een aanbeveling krijgen van welke producten er bestelt moeten worden.

Zodat ik: tijd kan besparen bij het kiezen van producten om te bestellen.
*/

import java.util.ArrayList;
import java.util.Calendar;

public class Saleslist {

    private ArrayList<Sale> orders;

    public Saleslist(){
        this.orders = new ArrayList<>();
    }

    public void addToOrderList(Sale order){
        order.setBestelDatum(Calendar.getInstance().getTime());

        this.orders.add(order);
    }

    public int size(){
        return orders.size();
    }

    public ArrayList<Sale> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Sale> orders) {
        this.orders = orders;
    }

    public Sale getOrderByID(int index){
        return orders.get(index);
    }

    public void listOrders(){
        for(Sale order : this.orders){
            System.out.println(order.getBestelDatum());
            order.listProducts();
        }
    }
}
