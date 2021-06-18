package com.caramba.ordertool;
import com.caramba.ordertool.scenes.ViewController;
import com.google.cloud.Timestamp;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Map;

public class OrderTool extends javafx.application.Application {
    //keeps track of all known products
    private static ProductList products = new ProductList();
    //Keeps track of all known suppliers
    private static SupplierList suppliers = new SupplierList();
    //Keeps track of all known sales
    private static Saleslist sales = new Saleslist();

    private static final RecommendationList recommendations = new RecommendationList();
    private static final FireStoreConfig config = new FireStoreConfig();

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws IOException {
        //create the main window
        URL res = getClass().getResource("/scenes/app.fxml");
        if(res == null){
            throw new IOException();
        }
        FXMLLoader loader = new FXMLLoader(res);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Caramba OrderTool");
        ViewController viewController = loader.getController();
        stage.setScene(scene);
        stage.show();

        //load from db
        OrderTool.loadFieldsFromDB();

        //test for recommendations
        Recommendation test = new Recommendation();
        test.setSupplier(suppliers.get("4EnOt6bg2NC6Uj8t0qVR"));
        test.addProductToRecommendation(products.get("QmuYT34bznQUAc3rN0Xa"), 4);
        test.addProductToRecommendation(products.get("RgGAlJ7xZI0GbBt1FscH"), 10);
        test.addProductToRecommendation(products.get("dTZiBgFEIVJY8XGhd08N"), 1);

        Recommendation test2 = new Recommendation();
        test2.setSupplier(suppliers.get("7OO9OpsX65LjPQpPzoBE"));
        test2.addProductToRecommendation(products.get("nidDfiYlO2JbEb1JuCfu"), 40);
        test2.addProductToRecommendation(products.get("mZBqigkeFLYYBy9gkQMh"), 30);
        recommendations.addRecommendation(test);
        recommendations.addRecommendation(test2);
        //send data to controllers
        viewController.update();
    }

    private static void loadFieldsFromDB(){
        config.fireStoreConfig();
        products = config.retrieveAllProducts();
        sales = config.retrieveAllSales();
        suppliers = config.retrieveAllSuppliers();
    }

    public static ProductList getProducts() {
        return products;
    }

    public static SupplierList getSuppliers() {
        return suppliers;
    }

    public static Saleslist getSales() {
        return sales;
    }

    public static RecommendationList getRecommendations() {
        return recommendations;
    }

    public static Map<YearMonth, Integer> getProductHistoryQuantity(String productID) {
        Map<YearMonth,Integer> productHistoryQuantityList = config.getProductHistoryQuantity(productID);
        return productHistoryQuantityList;
    }

    public static FireStoreConfig getConfig() {
        return config;
    }

    public static void addSale(String productID, String date, int amount){
        //debug stuff for adding sales quickly
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Timestamp time = Timestamp.of(format.parse(date));
            config.addSale(time, productID, amount);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a hardcoded set of test data for preview proposes.
     */
    private void loadTestData(){
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
    }
}
