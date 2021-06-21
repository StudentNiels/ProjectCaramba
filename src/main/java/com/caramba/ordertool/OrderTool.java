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
}
