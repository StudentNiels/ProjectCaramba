package com.caramba.ordertool.scenes;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Viewcontroller for the main app window. Keeps track of the controllers of it's children.
 */
public class AppViewController implements Initializable, ViewController {
    //these controllers are automatically loaded from the fxml but IntelliJ doesn't recognize this, so we suppress the warnings.
    @SuppressWarnings("unused")
    @FXML
    private ProductOverviewViewController productOverviewController;
    @SuppressWarnings("unused")
    @FXML
    private SupplierController suppliersController;
    @SuppressWarnings("unused")
    @FXML
    private RecommendedOrdersController recommendedOrdersController;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Updates the data in the controllers
     */
    @Override
    public void update() {
        productOverviewController.update();
        suppliersController.update();
        recommendedOrdersController.update();
    }

}
