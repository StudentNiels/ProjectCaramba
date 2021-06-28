package com.caramba.ordertool.scenes;

import com.caramba.ordertool.OrderTool;
import com.caramba.ordertool.models.Product;
import com.caramba.ordertool.models.ProductList;
import com.caramba.ordertool.models.Supplier;
import com.caramba.ordertool.models.SupplierList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class SupplierController implements Initializable, ViewController {

    @FXML
    private TableView<Supplier> tableSuppliers;
    @FXML
    private TableColumn<Supplier, String> colSupplierName;
    @FXML
    private TableColumn<Supplier, Integer> colAvgDeliveryTime;

    @FXML
    private TableView<Product> tableProducts;
    @FXML
    private TableColumn<Product, String> colProductId;
    @FXML
    private TableColumn<Product, Integer> colProductDescription;


    /**
     * Sets up the supplier view by creating the cellValueFactories and binding the click event
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colSupplierName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAvgDeliveryTime.setCellValueFactory(new PropertyValueFactory<>("avgDeliveryTime"));

        colProductId.setCellValueFactory(new PropertyValueFactory<>("productNum"));
        colProductDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        tableSuppliers.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                showProducts(tableSuppliers.getSelectionModel().getSelectedItem());
            }
        });
    }

    /**
     * Updates the data in the tables
     */
    @Override
    public void update() {
        SupplierList suppliers = OrderTool.getSuppliers();
        ObservableList<Supplier> observableList = FXCollections.observableArrayList();
        observableList.addAll(suppliers.getSuppliers().values());
        tableSuppliers.setItems(observableList);
    }

    /**
     * Shows the products offered by the specified supplier in the table
     *
     * @param supplier the supplier of wich to show the products for
     */
    public void showProducts(Supplier supplier) {
        if (supplier != null) {
            ProductList productList = supplier.getProducts();
            ObservableList<Product> observableList = FXCollections.observableArrayList();
            observableList.addAll(productList.getProducts().values());
            tableProducts.setItems(observableList);
        } else {
            tableProducts.setItems(null);
        }
    }
}
