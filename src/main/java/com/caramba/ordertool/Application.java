package com.caramba.ordertool;

import java.security.InvalidParameterException;
import java.util.Scanner;

public class Application {
    //keeps track of all known products
    private static final ProductList products = new ProductList();
    //Keeps track of all known suppliers
    private static final SupplierList suppliers = new SupplierList();

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
                case "clear" -> clear(command);
                default -> System.out.println("Unknown command " + command[0] + ". Use --help to see supported commands");
            }
        }
    }

    private static void display(String[] command){
        try{
            switch(command[1]) {
                case "suppliers" -> displaySuppliers();
                case "products" -> displayProducts();
                default -> throw new InvalidParameterException();
            }
        }catch(IndexOutOfBoundsException | InvalidParameterException e){
            System.out.println("Please use 'display products' or 'display suppliers'");
        }
    }

    public static void displaySuppliers(){
        if(suppliers.size() == 0){
            System.out.println("The supplier list is empty");
        }else{
            System.out.println("The following suppliers are registered:\n");
            for (int i = 0; i < suppliers.size(); i++){
                Supplier v = suppliers.get(i);
                System.out.println("| #" + i + " | Name: " + v.getName() + " | Estimated delivery time: " + v.getDeliveryTime() + " |");
            }
        }
    }

    public static void displayProducts(){
        if(products.size() == 0){
            System.out.println("The product list is empty");
        }else{
            System.out.println("The following products are registered:\n");
            for (int i = 0; i < products.size(); i++){
                Product p = products.get(i);
                StringBuilder suppliersString = new StringBuilder("suppliers that sell this product:");
                for(Supplier v : suppliers.getSuppliersSellingProduct(p).getSuppliers()){
                    suppliersString.append(" ").append(v.getName());
                }
                System.out.println("| #" + i + " | Product number: " + p.getProductNum() + " | Description: " + p.getDescription() + " | " + suppliersString);
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
            System.out.println("Please use add [product/supplier] [arguments]");
        }
    }

    public static void addSupplier(String[] command) {
            try{
                String name = command[2];
                int deliveryTime = Integer.parseInt(command[3]);
                if(deliveryTime < 0){
                    System.out.println("Delivery time can't be negative");
                    throw new InvalidParameterException();
                }
                suppliers.add(new Supplier(name, deliveryTime));
                System.out.println(name + " was added to the supplier list");
            }catch (IndexOutOfBoundsException | NumberFormatException | InvalidParameterException e){
                System.out.println("Please use add supplier [name] [delivery time in days]");
            }
    }

    public static void addProduct(String[] command) {
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
                case "suppliers" -> removeSupplier(command);
                case "products" -> removeProduct(command);
                default -> throw new InvalidParameterException();
            }
        }catch(IndexOutOfBoundsException | InvalidParameterException e){
            System.out.println("Please use remove [product/supplier] [index]");
        }
    }

    public static void removeSupplier(String[] command){
        int i;
        try{
             i = Integer.parseInt(command[2]);
            Supplier v = suppliers.get(i);
            if(v == null){
                System.out.println("that supplier does not exist");
            }else{
                System.out.println("'" + v.getName() + "' was removed");
                suppliers.remove(i);
            }
        }catch(NumberFormatException| IndexOutOfBoundsException e) {
            System.out.println("'" + command[2] + "' is not a valid index");
        }
    }

    public static void removeProduct(String[] command){
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
            System.out.println("'" + command[2] + "' is not a valid index");
        }
    }

    public static void link(String[] command){
        int i;
        try{
            i = Integer.parseInt(command[1]);
            Product p = products.get(i);
            i = Integer.parseInt(command[2]);
            Supplier v = suppliers.get(i);
            v.addProduct(p);
            System.out.println("Created link between " + v.getName() + " and " + p.getDescription());
        }catch(NumberFormatException| IndexOutOfBoundsException e) {
            System.out.println("use [product index] [supplier index]");
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
            System.out.println("Please use clear [products/suppliers/all]");
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
