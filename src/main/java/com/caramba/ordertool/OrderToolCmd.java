package com.caramba.ordertool;

import com.caramba.ordertool.Notifications.Notification;
import com.caramba.ordertool.Notifications.NotificationManager;
import com.caramba.ordertool.Notifications.NotificationType;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class OrderToolCmd {
    //keeps track of all known products
    private static ProductList products = new ProductList();
    //Keeps track of all known suppliers
    private static SupplierList suppliers = new SupplierList();
    //Keeps track of all known sales
    private static Saleslist saleslist = new Saleslist();
    private static final FireStoreConfig config = new FireStoreConfig();
    private static final OrderAlgorithm orderAlgo = new OrderAlgorithm();

    private static String[] cmdArguments;

    public static void main(String[] args){
        cmdArguments = args;

        //load from db
        config.fireStoreConfig();
        products = config.retrieveAllProducts();
        saleslist = config.retrieveAllSales();
        suppliers = config.retrieveAllSuppliers();


        NotificationManager.add(new Notification(NotificationType.INFO,("Caramba Order Tool started. Ready for commands.")));
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
                case "pdf" -> createOrderlistPDF(command);
                case "loadtest" -> loadTestData();
                case "project" -> displayProjectedSales(command);
                default -> NotificationManager.add(new Notification(NotificationType.ERROR, "Unknown command " + command[0] + ". Use --help to see supported commands"));
            }
        }
    }


    private static void display(String[] command){
        try{
            switch(command[1]) {
                case "suppliers" -> displaySuppliers();
                case "products" -> displayProducts();
                case "sales" -> displaySales();
                default -> throw new InvalidParameterException();
            }
        }catch(IndexOutOfBoundsException | InvalidParameterException e){
            NotificationManager.add(new Notification(NotificationType.ERROR, "Invalid syntax. Please use 'display products' or 'display suppliers"));
        }
    }

    public static void displaySuppliers(){
        if(suppliers.size() == 0){
            NotificationManager.add(new Notification(NotificationType.INFO, "The supplier list is empty"));
        }else{
            NotificationManager.add(new Notification(NotificationType.INFO,("The following suppliers are registered:")));

            for (Map.Entry<String, Supplier> entry : suppliers.getSuppliers().entrySet()) {
                String id = entry.getKey();
                Supplier s = entry.getValue();
                NotificationManager.add(new Notification(NotificationType.INFO, "| id: " + id + " | Name: " + s.getName() + " | Estimated delivery time: " + s.getAvgDeliveryTime() + " |"));
            }
        }
    }

    public static void displayProducts(){
        if(products.size() == 0){
            NotificationManager.add(new Notification(NotificationType.INFO,("The product list is empty")));
        }else{
            NotificationManager.add(new Notification(NotificationType.INFO,("The following products are registered:")));
            for (Map.Entry<String, Product> entry : products.getProducts().entrySet()) {
                String id = entry.getKey();
                Product p = entry.getValue();
                StringBuilder suppliersString = new StringBuilder("suppliers that sell this product:");
                for (Map.Entry<String, Supplier> supplierEntry : suppliers.getSuppliersSellingProduct(p).getSuppliers().entrySet()) {
                    Supplier s = supplierEntry.getValue();
                    suppliersString.append(" ").append(s.getName());
                }
                NotificationManager.add(new Notification(NotificationType.INFO,(
                        "| id: " + id.toString() + " | Product number: " + p.getProductNum()
                        + " | Description: " + p.getDescription() + " | Amount in storage: "
                        + p.getQuantity()  + " | " + suppliersString)));
            }
        }
    }

    public static void displaySales(){
        if(saleslist.size() == 0){
            System.out.println("The sales list is empty");
        }else{
            System.out.println("The following sales are registered:\n");
            for (int i = 0; i < saleslist.size(); i++) {
                Sale selectedSale = saleslist.getSaleByID(i);
                System.out.println(selectedSale.getDate());
                selectedSale.listProducts();
            }
        }
    }

    private static void add(String[] command){
        try{
            switch(command[1]) {
                case "supplier" -> addSupplier(command);
                case "product" -> addProduct(command);
                case "sale" -> addSale(command);
                default -> throw new InvalidParameterException();
            }
        }catch(IndexOutOfBoundsException | InvalidParameterException e){
            NotificationManager.add(new Notification(NotificationType.ERROR,("Invalid syntax. Please use add [product/supplier] [arguments]")));
        }
    }

    public static void addSupplier(String[] command) {
            try{
                String name = command[2];
                int deliveryTime = Integer.parseInt(command[3]);
                if(deliveryTime < 0){
                    NotificationManager.add(new Notification(NotificationType.ERROR,("Delivery time can't be negative")));
                    throw new InvalidParameterException();
                }
                suppliers.add(new Supplier(name, deliveryTime));
                NotificationManager.add(new Notification(NotificationType.INFO,(name + " was added to the supplier list")));
            }catch (IndexOutOfBoundsException | NumberFormatException | InvalidParameterException e){
                NotificationManager.add(new Notification(NotificationType.ERROR,("Invalid syntax. Please use add supplier [name] [delivery time in days]")));
            }
    }

    public static void addProduct(String[] command) {
        try{
            String productNumber = command[2];
            String description = command[3];
            products.add(new Product(productNumber, description));
            NotificationManager.add(new Notification(NotificationType.INFO,(description + " was added to the product list")));
        }catch (IndexOutOfBoundsException | NumberFormatException e){
            NotificationManager.add(new Notification(NotificationType.ERROR,("Invalid syntax. Please use add product [product number] [description] [minimum for storage]")));
        }
    }

    public static void addSale(String[] command){
        try{
            HashMap<String, Integer> saleProducts = new HashMap<>();
            for (int i = 2; i < command.length; i += 2) {
                String id = command[i];
                int amount = Integer.parseInt(command[(i+1)]);
                if(id == null){
                    System.out.println("This product does not exist!");
                }else if(amount < 0){
                    System.out.println("Negative amounts are not allowed!");
                    throw new InvalidParameterException();
                }else{
                    saleProducts.put(id, amount);
                    saleslist.addToSalesList(new Sale(saleProducts, LocalDateTime.now()));
                    System.out.println("Sale has been created.");
                }
            }
        }catch(Exception e){
            System.out.println("Please use add sale [productNum] [amount] [productNum] [amount] etc.");
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
            NotificationManager.add(new Notification(NotificationType.ERROR,("Invalid syntax. Please use remove [product/supplier] [index]")));
        }
    }

    public static void removeSupplier(String[] command){
        String id;
        try{
            id = command[2];
            Supplier v = suppliers.get(id);
            if(v == null){
                NotificationManager.add(new Notification(NotificationType.INFO,("that supplier does not exist")));
            }else{
                NotificationManager.add(new Notification(NotificationType.INFO,("'" + v.getName() + "' was removed")));
                suppliers.remove(id);
            }
        }catch(IndexOutOfBoundsException | IllegalArgumentException e) {
            NotificationManager.add(new Notification(NotificationType.ERROR,("'" + command[2] + "' is not a valid id")));
        }
    }

    public static void removeProduct(String[] command){
        String id;
        try{
            id = command[2];
            Product p = products.get(id);
            if(p == null){
                NotificationManager.add(new Notification(NotificationType.INFO,("that product does not exist")));
            }else{
                NotificationManager.add(new Notification(NotificationType.INFO,("'" + p.getDescription() + "' was removed")));
                products.remove(id);
            }
        }catch(IndexOutOfBoundsException | IllegalArgumentException e) {
            NotificationManager.add(new Notification(NotificationType.ERROR,("'" + command[2] + "' is not a valid index")));
        }
    }

    public static void link(String[] command){
        String id;
        try{
            id = command[1];
            Product p = products.get(id);
            id = command[2];
            Supplier v = suppliers.get(id);
            v.addProduct(p);
            NotificationManager.add(new Notification(NotificationType.INFO,("Created link between " + v.getName() + " and " + p.getDescription())));
        }catch(IndexOutOfBoundsException | IllegalArgumentException e) {
            NotificationManager.add(new Notification(NotificationType.ERROR,("Invalid syntax. use [product index] [supplier index]")));
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
            NotificationManager.add(new Notification(NotificationType.ERROR,("Invalid syntax. Please use clear [products/suppliers/all]")));
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

    //create a pdf of the recommended orderlist for next month
    public static void createOrderlistPDF(String[] command){
        HashMap<Product, Integer> productQuantityMap = orderAlgo.createOrderList(YearMonth.now().plusMonths(1));
        if(productQuantityMap.isEmpty()){
            NotificationManager.add(new Notification(NotificationType.INFO, "There are no products currently recommend to order. The pdf was not created."));
        }else{
            PDFCreator pdfc = new PDFCreator("pdf/", suppliers);
            pdfc.addProducts(productQuantityMap);
            pdfc.save();
        }
    }

    /**
     * Loads a hardcoded set of test data for preview proposes.
     */
    private static void loadTestData(){
        Product p1 = new Product("12345678", "Wax-Polish");
        Product p2 = new Product("0010-AA", "Antenneplakkers, zak 100 stuks");
        Product p3 = new Product("1230", "Insectenschrik");
        Product p4 = new Product("183247", "Schuim");
        Product p5 = new Product("2393", "Spray");
        Product p6 = new Product("3423875", "Voorreiniger");
        Supplier s1 = new Supplier("Bremen", 7);
        Supplier s2 = new Supplier("VoorbeeldLeverancier", 14);

        s1.addProduct(p1);
        s1.addProduct(p2);
        s1.addProduct(p3);
        s2.addProduct(p4);
        s2.addProduct(p5);
        s2.addProduct(p6);
        products.add(p1);
        products.add(p2);
        products.add(p3);
        products.add(p4);
        products.add(p5);
        products.add(p6);
        suppliers.add(s1);
        suppliers.add(s2);

        Sale sl1 = new Sale(LocalDateTime.parse("2020-01-01"));
        Sale sl2 = new Sale(LocalDateTime.parse("2020-01-12"));
        Sale sl3 = new Sale(LocalDateTime.parse("2020-02-01"));
        Sale sl4 = new Sale(LocalDateTime.parse("2020-02-07"));
        Sale sl5 = new Sale(LocalDateTime.parse("2020-03-01"));
        Sale sl6 = new Sale(LocalDateTime.parse("2020-04-01"));
        Sale sl7 = new Sale(LocalDateTime.parse("2020-05-01"));
        Sale sl8 = new Sale(LocalDateTime.parse("2020-06-01"));
        Sale sl9 = new Sale(LocalDateTime.parse("2020-07-01"));
        Sale sl10 = new Sale(LocalDateTime.parse("2020-08-01"));
        Sale sl11 = new Sale(LocalDateTime.parse("2020-09-01"));
        Sale sl12 = new Sale(LocalDateTime.parse("2020-10-01"));
        Sale sl13 = new Sale(LocalDateTime.parse("2020-11-01"));
        Sale sl14 = new Sale(LocalDateTime.parse("2020-12-01"));
        Sale sl15 = new Sale(LocalDateTime.parse("2021-01-01"));
        Sale sl16 = new Sale(LocalDateTime.parse("2021-02-01"));
        Sale sl17 = new Sale(LocalDateTime.parse("2021-03-01"));
        Sale sl18 = new Sale(LocalDateTime.parse("2021-04-01"));
        Sale sl19 = new Sale(LocalDateTime.parse("2021-05-01"));
        Sale sl20 = new Sale(LocalDateTime.parse("2021-06-01"));


        for (Map.Entry<String, Product> entry : products.getProducts().entrySet()) {
            if(entry.getValue() == p1){
                //sold in jan 2020
                sl1.addToProducts(entry.getKey(), 2);
                sl2.addToProducts(entry.getKey(), 1);

                //feb 2020
                sl3.addToProducts(entry.getKey(), 1);
                sl4.addToProducts(entry.getKey(), 1);

                //2020 one per month
                sl5.addToProducts(entry.getKey(), 4);
                sl6.addToProducts(entry.getKey(), 8);
                sl7.addToProducts(entry.getKey(), 7);
                sl8.addToProducts(entry.getKey(), 10);
                sl9.addToProducts(entry.getKey(), 20); //July is the peak season!
                sl10.addToProducts(entry.getKey(), 12);
                sl11.addToProducts(entry.getKey(), 13);
                sl12.addToProducts(entry.getKey(), 7);
                sl13.addToProducts(entry.getKey(), 5);
                sl14.addToProducts(entry.getKey(), 2);

                //2021
                sl15.addToProducts(entry.getKey(), 2);
                sl16.addToProducts(entry.getKey(), 4);
                sl17.addToProducts(entry.getKey(), 3);
                sl18.addToProducts(entry.getKey(), 10);
                sl19.addToProducts(entry.getKey(), 8);
                sl20.addToProducts(entry.getKey(), 11);

                break;
            }
        }

        saleslist.addToSalesList(sl1);
        saleslist.addToSalesList(sl2);
        saleslist.addToSalesList(sl3);
        saleslist.addToSalesList(sl4);
        saleslist.addToSalesList(sl5);
        saleslist.addToSalesList(sl6);
        saleslist.addToSalesList(sl7);
        saleslist.addToSalesList(sl8);
        saleslist.addToSalesList(sl9);
        saleslist.addToSalesList(sl10);
        saleslist.addToSalesList(sl11);
        saleslist.addToSalesList(sl12);
        saleslist.addToSalesList(sl13);
        saleslist.addToSalesList(sl14);
        saleslist.addToSalesList(sl15);
        saleslist.addToSalesList(sl16);
        saleslist.addToSalesList(sl17);
        saleslist.addToSalesList(sl18);
        saleslist.addToSalesList(sl19);
        saleslist.addToSalesList(sl20);
    }

    /**
     * Shows projected sales of given product for the next month
     */
    private static void displayProjectedSales(String[] command) {
        String id;
        try {
            if(command.length < 2){
                throw new IllegalArgumentException();
            }
            id = command[1];
            Product p = products.get(id);
            NotificationManager.add(new Notification(NotificationType.INFO, "The expected number of sales in the next month of this product is: " + orderAlgo.getProjectedSalesNextMonth(id)));
        }catch (IllegalArgumentException e){
            NotificationManager.add(new Notification(NotificationType.ERROR, "Invalid syntax use: project [product id]"));
        }
    }


    //#region getters and setters
    public static ProductList getMainProductList() {
        return products;
    }

    public static SupplierList getMainSupplierList() {
        return suppliers;
    }

    public static Saleslist getMainSalesList(){
        return saleslist;}

    public static String[] getCmdArguments() {
        return cmdArguments;
    }
    //#endregion
}
