package com.caramba.ordertool.scenes;

import com.caramba.ordertool.Product;
import com.caramba.ordertool.ProductList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ProductOverviewViewController implements Initializable {
    @FXML private TableView<Product> tableProductOverview;
    @FXML private TableColumn<Product, String> colProductNum;
    @FXML private TableColumn<Product, String> colProductDescription;
    @FXML private TableColumn<Product, Integer> colProductStock;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colProductNum.setCellValueFactory(new PropertyValueFactory<Product, String>("productNum"));
        colProductDescription.setCellValueFactory(new PropertyValueFactory<Product, String>("description"));
        colProductStock.setCellValueFactory(new PropertyValueFactory<Product, Integer>("quantity"));
    }

    public void setProductList(ProductList productList){
        ObservableList<Product> observableList = FXCollections.observableArrayList();
        observableList.addAll(productList.getProducts().values());
        if(!observableList.isEmpty()) {
            tableProductOverview.setItems(observableList);
        }
    }
}
