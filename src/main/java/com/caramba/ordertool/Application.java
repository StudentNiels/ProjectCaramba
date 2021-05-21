package com.caramba.ordertool;

import java.security.InvalidParameterException;
import java.util.*;

public class Application {
    //keeps track of all known products
    private static final ProductList products = new ProductList();
    //Keeps track of all known suppliers
    private static final SupplierList suppliers = new SupplierList();
    //Keeps track of all known orders
    private static final Orderlist orders = new Orderlist();

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
                case "orders" -> displayOrders();
                case "seasonProducts" -> displayProductsBySeason(command);
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
            for (Map.Entry<UUID, Supplier> entry : suppliers.getSuppliers().entrySet()) {
                UUID id = entry.getKey();
                Supplier s = entry.getValue();
                System.out.println("| id: " + id.toString() + " | Name: " + s.getName() + " | Estimated delivery time: " + s.getDeliveryTime() + " |");
            }
        }
    }

    public static void displayProducts(){
        if(products.size() == 0){
            System.out.println("The product list is empty");
        }else{
            System.out.println("The following products are registered:\n");
            for (Map.Entry<UUID, Product> entry : products.getProducts().entrySet()) {
                UUID id = entry.getKey();
                Product p = entry.getValue();
                StringBuilder suppliersString = new StringBuilder("suppliers that sell this product:");
                for (Map.Entry<UUID, Supplier> supplierEntry : suppliers.getSuppliersSellingProduct(p).getSuppliers().entrySet()) {
                    Supplier s = supplierEntry.getValue();
                    suppliersString.append(" ").append(s.getName());
                }
                System.out.println("| id: " + id.toString() + " | Product number: " + p.getProductNum() + " | Description: " + p.getDescription() + " | " + suppliersString);
            }
        }
    }

    public static void displayOrders(){
        if(orders.size() == 0){
            System.out.println("The order list is empty");
        }else{
            System.out.println("The following orders are registered:\n");
            for (int i = 0; i < orders.size(); i++) {
                Order selectedOrder = orders.getOrderByID(i);
                System.out.println(selectedOrder.getBestelDatum() +"/"+ selectedOrder.getFactuurDatum());
                selectedOrder.listShoppingCart();
            }
        }
    }

    public static void displayProductsBySeason(String[] command){
        products.showProductBySeason(command[2]);
    }

    private static void add(String[] command){
        try{
            switch(command[1]) {
                case "supplier" -> addSupplier(command);
                case "product" -> addProduct(command);
                case "order" -> addOrder(command);
                default -> throw new InvalidParameterException();
            }
        }catch(IndexOutOfBoundsException | InvalidParameterException e){
            System.out.println("Please use add [product/supplier/order] [arguments]");
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

    public static void addOrder(String[] command){
        try{
            HashMap<Product, Integer> orderProducts = new HashMap<>();
            for (int i = 2; i < command.length; i += 2) {
                UUID productNum = UUID.fromString(command[i]);
                int amount = Integer.parseInt(command[(i+1)]);
                if(productNum == null){
                    System.out.println("This product does not exist!");
                }else if(amount < 0){
                    System.out.println("Negative amounts are not allowed!");
                    throw new InvalidParameterException();
                }else{
                    orderProducts.put(products.get(productNum), amount);
                    orders.addToOrderList(new Order(orderProducts));
                    System.out.println("Order has been created.");
                }
            }
        }catch(Exception e){
            System.out.println("Please use add order [productNum] [amount] [productNum] [amount] etc.");
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
        UUID id;
        try{
            id = UUID.fromString(command[2]);
            Supplier v = suppliers.get(id);
            if(v == null){
                System.out.println("that supplier does not exist");
            }else{
                System.out.println("'" + v.getName() + "' was removed");
                suppliers.remove(id);
            }
        }catch(IndexOutOfBoundsException | IllegalArgumentException e) {
            System.out.println("'" + command[2] + "' is not a valid id");
        }
    }

    public static void removeProduct(String[] command){
        UUID id;
        try{
            id = UUID.fromString(command[2]);
            Product p = products.get(id);
            if(p == null){
                System.out.println("that product does not exist");
            }else{
                System.out.println("'" + p.getDescription() + "' was removed");
                products.remove(id);
            }
        }catch(IndexOutOfBoundsException | IllegalArgumentException e) {
            System.out.println("'" + command[2] + "' is not a valid index");
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
            System.out.println("Created link between " + v.getName() + " and " + p.getDescription());
        }catch(IndexOutOfBoundsException | IllegalArgumentException e) {
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

    public static Orderlist getMainOrderList(){return orders;}

    public static String[] getCmdArguments() {
        return cmdArguments;
    }
    //#endregion
}
