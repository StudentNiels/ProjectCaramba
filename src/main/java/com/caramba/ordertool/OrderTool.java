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
import java.time.YearMonth;
import java.util.Map;

public class OrderTool extends javafx.application.Application {
    //keeps track of all known products
    private static ProductList products = new ProductList();
    //Keeps track of all known suppliers
    private static SupplierList suppliers = new SupplierList();
    //Keeps track of all known sales
    private static Saleslist sales = new Saleslist();

    private static RecommendationList recommendations = new RecommendationList();
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

        //send data to controllers
        viewController.update();
    }

    private static void loadFieldsFromDB(){
        System.out.println("Retrieving data from firebase...");
        config.fireStoreConfig();
        products = config.retrieveAllProducts();
        sales = config.retrieveAllSales();
        suppliers = config.retrieveAllSuppliers();
        OrderAlgorithm algo = new OrderAlgorithm();
        config.addRecommendations(algo.createRecommendations());
        recommendations = config.getRecommendations();
        System.out.println("Finished loading from firebase");
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
}
