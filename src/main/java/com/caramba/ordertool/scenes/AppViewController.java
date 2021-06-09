package com.caramba.ordertool.scenes;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class AppViewController implements Initializable, ViewController {
    @FXML
    private ProductOverviewViewController productOverviewController;
    @FXML
    private SupplierController suppliersController;
    @FXML
    private RecommendedOrdersController recommendedOrdersController;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    @Override
    public void update() {
        productOverviewController.update();
        suppliersController.update();
    }

}
