package com.caramba.ordertool;

import com.caramba.ordertool.Notifications.Notification;
import com.caramba.ordertool.Notifications.NotificationManager;
import com.caramba.ordertool.Notifications.NotificationType;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

public class Application {
    //keeps track of all known products
    private static final ProductList products = new ProductList();
    //Keeps track of all known suppliers
    private static final SupplierList suppliers = new SupplierList();
    //Keeps track of all known sales
    private static final Saleslist saleslist = new Saleslist();
    private static final FireStoreConfig config = new FireStoreConfig();

    private static String[] cmdArguments;

    public static void main(String[] args){
        cmdArguments = args;
        config.fireStoreConfig();
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

            for (Map.Entry<UUID, Supplier> entry : suppliers.getSuppliers().entrySet()) {
                UUID id = entry.getKey();
                Supplier s = entry.getValue();
                NotificationManager.add(new Notification(NotificationType.INFO, "| id: " + id.toString() + " | Name: " + s.getName() + " | Estimated delivery time: " + s.getDeliveryTime() + " |"));
            }
        }
    }

    public static void displayProducts(){
        if(products.size() == 0){
            NotificationManager.add(new Notification(NotificationType.INFO,("The product list is empty")));
        }else{
            NotificationManager.add(new Notification(NotificationType.INFO,("The following products are registered:")));
            for (Map.Entry<UUID, Product> entry : products.getProducts().entrySet()) {
                UUID id = entry.getKey();
                Product p = entry.getValue();
                StringBuilder suppliersString = new StringBuilder("suppliers that sell this product:");
                for (Map.Entry<UUID, Supplier> supplierEntry : suppliers.getSuppliersSellingProduct(p).getSuppliers().entrySet()) {
                    Supplier s = supplierEntry.getValue();
                    suppliersString.append(" ").append(s.getName());
                }
                NotificationManager.add(new Notification(NotificationType.INFO,(
                        "| id: " + id.toString() + " | Product number: " + p.getProductNum()
                        + " | Description: " + p.getDescription() + " | Amount in storage: "
                        + p.getQuantity() +" | Minimum for storage: " + p.getMinQuantity() + " | " + suppliersString)));
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
            int minStorage = Integer.parseInt(command[4]);
            products.add(new Product(productNumber, description, minStorage));
            NotificationManager.add(new Notification(NotificationType.INFO,(description + " was added to the product list")));
        }catch (IndexOutOfBoundsException | NumberFormatException e){
            NotificationManager.add(new Notification(NotificationType.ERROR,("Invalid syntax. Please use add product [product number] [description] [minimum for storage]")));
        }
    }

    public static void addSale(String[] command){
        try{
            HashMap<Product, Integer> saleProducts = new HashMap<>();
            for (int i = 2; i < command.length; i += 2) {
                UUID productNum = UUID.fromString(command[i]);
                int amount = Integer.parseInt(command[(i+1)]);
                if(productNum == null){
                    System.out.println("This product does not exist!");
                }else if(amount < 0){
                    System.out.println("Negative amounts are not allowed!");
                    throw new InvalidParameterException();
                }else{
                    saleProducts.put(products.get(productNum), amount);
                    saleslist.addToSalesList(new Sale(saleProducts));
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
        UUID id;
        try{
            id = UUID.fromString(command[2]);
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
        UUID id;
        try{
            id = UUID.fromString(command[2]);
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
        UUID id;
        try{
            id = UUID.fromString(command[1]);
            Product p = products.get(id);
            id = UUID.fromString(command[2]);
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

    //create a pdf with the selected products
    //todo remove/rework this
    public static void createOrderlistPDF(String[] command){
        if(command.length < 2){
            NotificationManager.add(new Notification(NotificationType.ERROR, "Please select at least one product. Use pdf [product id 1] [product id 2] [ect.]"));
        }else{
            ProductList pl = new ProductList();
            for (int i = 1; i < command.length; i++) {
                try {
                    UUID id = UUID.fromString(command[i]);
                    pl.add(products.get(id));
                } catch (IllegalArgumentException e) {
                    NotificationManager.add(new Notification(NotificationType.ERROR, command[i] + " is not a valid product id and was not added."));
                }
            }
            if(pl.size() == 0){
                NotificationManager.add(new Notification(NotificationType.ERROR, "No pdf was created because the command contained no valid product ids."));
            }else{
                PDFCreator pdfc = new PDFCreator("pdf/", suppliers);
                pdfc.addProductList(pl);
                pdfc.save();
            }
        }
    }

    /**
     * Loads a hardcoded set of test data for preview proposes.
     */
    private static void loadTestData(){
        Product p1 = new Product("12345678", "Wax-Polish", 10);
        Product p2 = new Product("0010-AA", "Antenneplakkers, zak 100 stuks", 10);
        Product p3 = new Product("1230", "Insectenschrik", 10);
        Product p4 = new Product("183247", "Schuim", 10);
        Product p5 = new Product("2393", "Spray", 10);
        Product p6 = new Product("3423875", "Voorreiniger", 10);
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
    }


    //#region getters and setters
    public static ProductList getMainProductList() {
        return products;
    }

    public static SupplierList getMainSupplierList() {
        return suppliers;
    }

    public static Saleslist getMainSalesList(){return saleslist;}

    public static String[] getCmdArguments() {
        return cmdArguments;
    }
    //#endregion
}
