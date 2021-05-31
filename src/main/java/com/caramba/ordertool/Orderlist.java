package com.caramba.ordertool;

/*
Als: medewerker van de inkoop

Wil ik: op basis van het seizoen, verkoop trends, geschatte levertijd en beschikbare voorraad een aanbeveling krijgen van welke producten er bestelt moeten worden.

Zodat ik: tijd kan besparen bij het kiezen van producten om te bestellen.
*/

import com.caramba.ordertool.Order;

import java.util.ArrayList;
import java.util.Calendar;

public class Orderlist {

    private ArrayList<Order> orders;

    public Orderlist(){
        this.orders = new ArrayList<>();
    }

    public void addToOrderList(Order order){
        order.setBestelDatum(Calendar.getInstance().getTime());
        order.setFactuurDatum(Calendar.getInstance().getTime());

        this.orders.add(order);
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }

    public void listOrders(){
        for(Order order : this.orders){
            System.out.println(order.getBestelDatum() + "/" + order.getFactuurDatum());
            order.listShoppingCart();
        }
    }
}
