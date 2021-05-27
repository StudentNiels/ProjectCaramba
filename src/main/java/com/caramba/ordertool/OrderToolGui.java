package com.caramba.ordertool;

import com.caramba.ordertool.Notifications.NotificationManager;
import com.caramba.ordertool.scenes.ProductOverviewViewController;
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
        showProductOverview();
    }

    public void showProductOverview(){
        try{
            ProductOverviewViewController controller = (ProductOverviewViewController) switchScene("/scenes/productOverview.fxml");
            controller.setProductList(products);
        }catch (IOException e){
            NotificationManager.addExceptionError(e);
        }
    }

    private Object switchScene(String path) throws IOException{
        URL res = getClass().getResource(path);
        if(res == null){
            throw new IOException("invalid resource path");
        }
        FXMLLoader loader = new FXMLLoader(res);
        Parent sceneParent = loader.load();
        Scene subScene = new Scene(sceneParent);
        stage.setScene(subScene);
        stage.show();
        return loader.getController();
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

}
