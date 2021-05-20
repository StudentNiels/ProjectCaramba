package com.caramba.ordertool;

import com.caramba.ordertool.Notifications.Notification;
import com.caramba.ordertool.Notifications.NotificationManager;
import com.caramba.ordertool.Notifications.NotificationType;

import java.security.InvalidParameterException;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

public class Application {
    //keeps track of all known products
    private static final ProductList products = new ProductList();
    //Keeps track of all known suppliers
    private static final SupplierList suppliers = new SupplierList();

    private static final NotificationManager notifications = new NotificationManager();

    private static String[] cmdArguments;

    public static void main(String[] args){
        cmdArguments = args;
        notifications.add(new Notification(NotificationType.INFO,("Caramba Order Tool started. Ready for commands.")));
        while(true){
            Scanner input = new Scanner(System.in);
            String[] command = input.nextLine().split("\\s+");
            switch (command[0]) {
                case "exit" -> System.exit(0);
                case "display" -> display(command);
                case "add" -> add(command);
                case "remove" -> remove(command);
                case "link" -> link(command);
                case "clear" -> clear(command);
                default -> notifications.add(new Notification(NotificationType.ERROR, "Unknown command " + command[0] + ". Use --help to see supported commands"));
            }
        }
    }

    private static void printNotifications(){

    }

    private static void display(String[] command){
        try{
            switch(command[1]) {
                case "suppliers" -> displaySuppliers();
                case "products" -> displayProducts();
                default -> throw new InvalidParameterException();
            }
        }catch(IndexOutOfBoundsException | InvalidParameterException e){
            notifications.add(new Notification(NotificationType.ERROR, "Invalid syntax. Please use 'display products' or 'display suppliers"));
        }
    }

    public static void displaySuppliers(){
        if(suppliers.size() == 0){
            notifications.add(new Notification(NotificationType.INFO, "The supplier list is empty"));
        }else{
            notifications.add(new Notification(NotificationType.INFO,("The following suppliers are registered:")));

            for (Map.Entry<UUID, Supplier> entry : suppliers.getSuppliers().entrySet()) {
                UUID id = entry.getKey();
                Supplier s = entry.getValue();
                notifications.add(new Notification(NotificationType.INFO, "| id: " + id.toString() + " | Name: " + s.getName() + " | Estimated delivery time: " + s.getDeliveryTime() + " |"));
            }
        }
    }

    public static void displayProducts(){
        if(products.size() == 0){
            notifications.add(new Notification(NotificationType.INFO,("The product list is empty")));
        }else{
            notifications.add(new Notification(NotificationType.INFO,("The following products are registered:")));
            for (Map.Entry<UUID, Product> entry : products.getProducts().entrySet()) {
                UUID id = entry.getKey();
                Product p = entry.getValue();
                StringBuilder suppliersString = new StringBuilder("suppliers that sell this product:");
                for (Map.Entry<UUID, Supplier> supplierEntry : suppliers.getSuppliersSellingProduct(p).getSuppliers().entrySet()) {
                    Supplier s = supplierEntry.getValue();
                    suppliersString.append(" ").append(s.getName());
                }
                notifications.add(new Notification(NotificationType.INFO,("| id: " + id.toString() + " | Product number: " + p.getProductNum() + " | Description: " + p.getDescription() + " | " + suppliersString)));
            }
        }
    }

    private static void add(String[] command){
        try{
            switch(command[1]) {
                case "supplier" -> addSupplier(command);
                case "product" -> addProduct(command);
                default -> throw new InvalidParameterException();
            }
        }catch(IndexOutOfBoundsException | InvalidParameterException e){
            notifications.add(new Notification(NotificationType.ERROR,("Invalid syntax. Please use add [product/supplier] [arguments]")));
        }
    }

    public static void addSupplier(String[] command) {
            try{
                String name = command[2];
                int deliveryTime = Integer.parseInt(command[3]);
                if(deliveryTime < 0){
                    notifications.add(new Notification(NotificationType.ERROR,("Delivery time can't be negative")));
                    throw new InvalidParameterException();
                }
                suppliers.add(new Supplier(name, deliveryTime));
                notifications.add(new Notification(NotificationType.INFO,(name + " was added to the supplier list")));
            }catch (IndexOutOfBoundsException | NumberFormatException | InvalidParameterException e){
                notifications.add(new Notification(NotificationType.ERROR,("Invalid syntax. Please use add supplier [name] [delivery time in days]")));
            }
    }

    public static void addProduct(String[] command) {
        try{
            String productNumber = command[2];
            String description = command[3];
            products.add(new Product(productNumber, description));
            notifications.add(new Notification(NotificationType.INFO,(description + " was added to the product list")));
        }catch (IndexOutOfBoundsException | NumberFormatException e){
            notifications.add(new Notification(NotificationType.ERROR,("Invalid syntax. Please use add product [product number] [description]")));
        }
    }

    private static void remove(String[] command){
        try{
            switch(command[1]) {
                case "suppliers" -> removeSupplier(command);
                case "products" -> removeProduct(command);
                default -> throw new InvalidParameterException();
            }
        }catch(IndexOutOfBoundsException | InvalidParameterException e){
            notifications.add(new Notification(NotificationType.ERROR,("Invalid syntax. Please use remove [product/supplier] [index]")));
        }
    }

    public static void removeSupplier(String[] command){
        UUID id;
        try{
            id = UUID.fromString(command[2]);
            Supplier v = suppliers.get(id);
            if(v == null){
                notifications.add(new Notification(NotificationType.INFO,("that supplier does not exist")));
            }else{
                notifications.add(new Notification(NotificationType.INFO,("'" + v.getName() + "' was removed")));
                suppliers.remove(id);
            }
        }catch(IndexOutOfBoundsException | IllegalArgumentException e) {
            notifications.add(new Notification(NotificationType.ERROR,("'" + command[2] + "' is not a valid id")));
        }
    }

    public static void removeProduct(String[] command){
        UUID id;
        try{
            id = UUID.fromString(command[2]);
            Product p = products.get(id);
            if(p == null){
                notifications.add(new Notification(NotificationType.INFO,("that product does not exist")));
            }else{
                notifications.add(new Notification(NotificationType.INFO,("'" + p.getDescription() + "' was removed")));
                products.remove(id);
            }
        }catch(IndexOutOfBoundsException | IllegalArgumentException e) {
            notifications.add(new Notification(NotificationType.ERROR,("'" + command[2] + "' is not a valid index")));
        }
    }

    public static void link(String[] command){
        UUID id;
        try{
            id = UUID.fromString(command[1]);
            Product p = products.get(id);
            id = UUID.fromString(command[2]);
            Supplier v = suppliers.get(id);
            v.addProduct(p);
            notifications.add(new Notification(NotificationType.INFO,("Created link between " + v.getName() + " and " + p.getDescription())));
        }catch(IndexOutOfBoundsException | IllegalArgumentException e) {
            notifications.add(new Notification(NotificationType.ERROR,("Invalid syntax. use [product index] [supplier index]")));
        }
    }

    private static void clear(String[] command){
        try{
            switch(command[1]) {
                case "suppliers" -> clearSuppliers();
                case "products" -> clearProducts();
                case "all" -> clearAll();
                default -> throw new InvalidParameterException();
            }
        }catch(IndexOutOfBoundsException | InvalidParameterException e){
            notifications.add(new Notification(NotificationType.ERROR,("Invalid syntax. Please use clear [products/suppliers/all]")));
        }
    }

    public static void clearAll() {
        clearProducts();
        clearSuppliers();
    }

    public static void clearProducts() {
        products.clear();
    }

    public static void clearSuppliers() {
        suppliers.clear();
    }


    //#region getters and setters
    public static ProductList getMainProductList() {
        return products;
    }

    public static SupplierList getMainSupplierList() {
        return suppliers;
    }

    public static String[] getCmdArguments() {
        return cmdArguments;
    }
    //#endregion
}
