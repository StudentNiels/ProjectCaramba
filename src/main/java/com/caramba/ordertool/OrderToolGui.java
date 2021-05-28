package com.caramba.ordertool;

import com.caramba.ordertool.Notifications.NotificationManager;
import com.caramba.ordertool.scenes.ProductOverviewViewController;
import com.caramba.ordertool.scenes.ViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class OrderToolGui extends Application {
    private Stage stage;
    //keeps track of all known products
    private static final ProductList products = new ProductList();
    //Keeps track of all known suppliers
    private static final SupplierList suppliers = new SupplierList();
    private static final TimePeriodController timePeriods = new TimePeriodController();
    private static final FireStoreConfig config = new FireStoreConfig();
    private static ViewController activeController = null;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        URL res = getClass().getResource("/scenes/app.fxml");
        if(res == null){
            throw new IOException();
        }
        Parent root = FXMLLoader.load(res);
        Scene scene = new Scene(root);
        //scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        stage.setTitle("JavaFX");
        stage.setScene(scene);
        stage.show();

        //todo replace this with loading from database
        loadTestData();
        switchScene(SceneType.PRODUCT_OVERVIEW);
    }

    public static ProductList getProducts() {
        return products;
    }

    public static SupplierList getSuppliers() {
        return suppliers;
    }


    private void switchScene(SceneType sceneType) throws IOException{
        URL res = getClass().getResource(sceneType.FXMLPath);
        if(res == null){
            throw new IOException("invalid resource path");
        }
        FXMLLoader loader = new FXMLLoader(res);
        Parent sceneParent = loader.load();
        Scene newScene = new Scene(sceneParent);
        stage.setScene(newScene);
        stage.show();
        activeController =  (ViewController) loader.getController();
        activeController.update();
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

    public enum SceneType{
        PRODUCT_OVERVIEW("/scenes/productOverview.fxml");

        public final String FXMLPath;
        SceneType(String FXMLPath) {
            this.FXMLPath = FXMLPath;
        }
    }

}
