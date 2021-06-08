package com.caramba.ordertool.scenes;

import com.caramba.ordertool.Product;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class AppViewController implements Initializable, ViewController {
    @FXML
    private ProductOverviewViewController productOverviewController;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("test");
    }


    @Override
    public void update() {
        productOverviewController.update();
    }

}
