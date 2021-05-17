package com.caramba.ordertool;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Scanner;

public class Application {
    //keeps track of all known products
    private static ProductList products = new ProductList();
    //Keeps track of all known vendors
    private static VendorList vendors = new VendorList();

    private static String[] cmdArguments;

    public static void main(String[] args){
        cmdArguments = args;
        System.out.println("Caramba Order Tool started");
        while(true){
            Scanner input = new Scanner(System.in);
            String[] command = input.nextLine().split("\\s+");
            switch (command[0]) {
                case "exit" -> System.exit(0);
                case "display" -> display(command);
                case "add" -> add(command);
                case "remove" -> remove(command);
                case "link" -> link(command);
                default -> System.out.println("Unknown command " + command[0] + ". Use --help to see supported commands");
            }
        }
    }

    private static void display(String[] command){
        try{
            switch(command[1]) {
                case "vendors" -> displayVendors();
                case "products" -> displayProducts();
                default -> throw new InvalidParameterException();
            }
        }catch(IndexOutOfBoundsException | InvalidParameterException e){
            System.out.println("Please use 'display products' or 'display vendors'");
        }
    }

    private static void displayVendors(){
        if(vendors.size() == 0){
            System.out.println("The vendor list is empty");
        }else{
            System.out.println("The following vendors are registered:\n");
            for (int i = 0; i < vendors.size(); i++){
                Vendor v = vendors.get(i);
                System.out.println("| #" + i + " | Name: " + v.getName() + " | Estimated delivery time: " + v.getDeliveryTime() + " |");
            }
        }
    }

    private static void displayProducts(){
        if(products.size() == 0){
            System.out.println("The product list is empty");
        }else{
            System.out.println("The following products are registered:\n");
            for (int i = 0; i < products.size(); i++){
                Product p = products.get(i);
                StringBuilder vendorsString = new StringBuilder("Vendors that sell this product:");
                for(Vendor v : vendors.getVendorsSellingProduct(p).getVendors()){
                    vendorsString.append(" ").append(v.getName());
                }
                System.out.println("| #" + i + " | Product number: " + p.getProductNum() + " | Description: " + p.getDescription() + " | " + vendorsString);
            }
        }
    }

    private static void add(String[] command){
        try{
            switch(command[1]) {
                case "vendor" -> addVendor(command);
                case "product" -> addProduct(command);
                default -> throw new InvalidParameterException();
            }
        }catch(IndexOutOfBoundsException | InvalidParameterException e){
            System.out.println("Please use add [product/vendor] [arguments]");
        }
    }

    private static void addVendor(String[] command) {
            try{
                String name = command[2];
                int deliveryTime = Integer.parseInt(command[3]);
                vendors.add(new Vendor(name, deliveryTime));
                System.out.println(name + " was added to the vendor list");
            }catch (IndexOutOfBoundsException | NumberFormatException e){
                System.out.println("Please use add vendor [name] [delivery time in days]");
            }
    }

    private static void addProduct(String[] command) {
        try{
            String productNumber = command[2];
            String description = command[3];
            products.add(new Product(productNumber, description));
            System.out.println(description + " was added to the product list");
        }catch (IndexOutOfBoundsException | NumberFormatException e){
            System.out.println("Please use add product [product number] [description]");
        }
    }

    private static void remove(String[] command){
        try{
            switch(command[1]) {
                case "vendors" -> removeVendor(command);
                case "products" -> removeProduct(command);
                default -> throw new InvalidParameterException();
            }
        }catch(IndexOutOfBoundsException | InvalidParameterException e){
            System.out.println("Please use remove [product/vendor] [index]");
        }
    }

    private static void removeVendor(String[] command){
        int i;
        try{
             i = Integer.parseInt(command[2]);
            Vendor v = vendors.get(i);
            if(v == null){
                System.out.println("that vendor does not exist");
            }else{
                System.out.println("'" + v.getName() + "' was removed");
                vendors.remove(i);
            }
        }catch(NumberFormatException| IndexOutOfBoundsException e) {
            System.out.println("'" + command[2] + "' is not a valid index");
        }
    }

    private static void removeProduct(String[] command){
        int i;
        try{
            i = Integer.parseInt(command[2]);
            Product p = products.get(i);
            if(p == null){
                System.out.println("that product does not exist");
            }else{
                System.out.println("'" + p.getDescription() + "' was removed");
                products.remove(i);
            }
        }catch(NumberFormatException| IndexOutOfBoundsException e) {
            System.out.println("'" + command[2] + "' is not a cvalid index");
        }
    }

    private static void link(String[] command){
        int i;
        try{
            i = Integer.parseInt(command[1]);
            Product p = products.get(i);
            i = Integer.parseInt(command[2]);
            Vendor v = vendors.get(i);
            v.addProduct(p);
            System.out.println("Created link between " + v.getName() + " and " + p.getDescription());
        }catch(NumberFormatException| IndexOutOfBoundsException e) {
            System.out.println("use [product index] [vendor index]");
        }
    }


    //#region getters and setters
    public static ProductList getMainProductList() {
        return products;
    }

    public static VendorList getMainVendorList() {
        return vendors;
    }

    public static String[] getCmdArguments() {
        return cmdArguments;
    }
    //#endregion
}
