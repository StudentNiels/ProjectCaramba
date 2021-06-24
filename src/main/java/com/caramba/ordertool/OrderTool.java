package com.caramba.ordertool;

import com.caramba.ordertool.models.ProductList;
import com.caramba.ordertool.models.RecommendationList;
import com.caramba.ordertool.models.SalesList;
import com.caramba.ordertool.models.SupplierList;
import com.caramba.ordertool.notifications.Notification;
import com.caramba.ordertool.notifications.NotificationManager;
import com.caramba.ordertool.notifications.NotificationType;
import com.caramba.ordertool.scenes.ViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.YearMonth;
import java.util.Map;

public class OrderTool extends javafx.application.Application {
    private static final FireStoreConfig config = new FireStoreConfig();
    //keeps track of all known products
    private static ProductList products = new ProductList();
    //Keeps track of all known suppliers
    private static SupplierList suppliers = new SupplierList();
    //Keeps track of all known sales
    private static SalesList sales = new SalesList();
    private static Stage mainStage;
    private static RecommendationList recommendations = new RecommendationList();

    public static void main(String[] args) {
        launch(args);
    }

    private static void loadFieldsFromDB() {
        NotificationManager.show(new Notification(NotificationType.INFO, "Retrieving data from firebase..."));
        config.fireStoreConfig();
        products = config.retrieveAllProducts();
        sales = config.retrieveAllSales();
        suppliers = config.retrieveAllSuppliers();
        OrderAlgorithm algo = new OrderAlgorithm();
        config.addRecommendations(algo.createRecommendations());
        recommendations = config.getRecommendations();
        NotificationManager.show(new Notification(NotificationType.INFO, "Finished loading from firebase"));
    }

    /**
     * \
     * Returns all products registered to the orderTool
     *
     * @return ProductList with all products
     */
    public static ProductList getProducts() {
        return products;
    }

    /**
     * Returns all suppliers registered to the orderTool
     *
     * @return SupplierList with all suppliers
     */
    public static SupplierList getSuppliers() {
        return suppliers;
    }

    /**
     * Returns all sales registered to the orderTool
     *
     * @return SalesList with all sales
     */
    public static SalesList getSales() {
        return sales;
    }

    /**
     * Returns all recommendations registered to the orderTool
     *
     * @return RecommendationList with all recommendations
     */
    public static RecommendationList getRecommendations() {
        return recommendations;
    }

    /**
     * Returns a hashmap with the history of amount of units sold per month. Uses the YearMonth of the history as key and the amount of units sold as value
     *
     * @param productID id of product to get the history of
     * @return hashmap with the history of amount of units sold per month
     */
    public static Map<YearMonth, Integer> getProductHistoryQuantity(String productID) {
        return config.getProductHistoryQuantity(productID);
    }

    /**
     * Returns the FireStoreConfig
     *
     * @return the FireStoreConfig
     */
    public static FireStoreConfig getConfig() {
        return config;
    }

    /**
     * Returns the main javafx stage of the application
     *
     * @return the main stage
     */
    public static Stage getMainStage() {
        return mainStage;
    }

    /**
     * Starts the javafx application and loads the app.fxml
     *
     * @param stage the main javafx stage of the application
     * @throws IOException if the app.fxml could not be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        //create the main window
        URL res = getClass().getResource("/scenes/app.fxml");
        if (res == null) {
            throw new IOException();
        }
        FXMLLoader loader = new FXMLLoader(res);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Caramba OrderTool");
        ViewController viewController = loader.getController();
        stage.setScene(scene);
        mainStage = stage;
        stage.show();

        //load from db
        OrderTool.loadFieldsFromDB();

        //send data to controllers
        viewController.update();
    }

}
