package com.caramba.ordertool.scenes;

import com.caramba.ordertool.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class RecommendedOrdersController implements Initializable, ViewController {

    @FXML
    private Accordion accordionNewRecommendations;

    @FXML
    private Accordion accordionCheckedRecommendations;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    @Override
    public void update() {

        //clear the accordions before refreshing
        accordionCheckedRecommendations.getPanes().clear();
        accordionNewRecommendations.getPanes().clear();

        URL res = getClass().getResource("/scenes/recommendation.fxml");
        RecommendationList recommendations = OrderTool.getRecommendations();
        DateTimeFormatter creationDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        DateTimeFormatter finalOrderDateDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (Recommendation recommendation : recommendations.getRecommendations()) {
            try {
                TitledPane pane = FXMLLoader.load(res);
                pane.setText("");
                //created date
                LocalDateTime creationDate = recommendation.getCreationDate();
                String date = creationDate.format(creationDateFormatter);
                pane.setText(pane.getText() + date);
                Text createdDateText = (Text) pane.getContent().lookup("#textCreatedDate");
                createdDateText.setText(date);

                //supplier name
                Supplier supplier = recommendation.getSupplier();
                String supplierName = null;
                if(supplier != null){
                    supplierName = supplier.getName();
                }
                if(supplierName == null){
                    supplierName = "(Leverancier onbekend)";
                }
                pane.setText(" Order voor: " + supplierName + " " + pane.getText());
                Text supplierText = (Text) pane.getContent().lookup("#textSupplier");
                supplierText.setText(supplierName);

                //Final order date
                LocalDate finalOrderDate = recommendation.getFinalOrderDate();
                if(finalOrderDate != null){
                    Text textFinalOrderDate = (Text) pane.getContent().lookup("#textFinalOrderDate");
                    textFinalOrderDate.setText(finalOrderDate.format(finalOrderDateDateFormatter));
                }

                //products
                //create the table and columns
                TableView<productQuantityPair> table = (TableView<productQuantityPair>) pane.getContent().lookup("#tableRecommendedProducts");
                TableColumn<productQuantityPair, String> colProductNum = new TableColumn<>("Artikel nummer");
                colProductNum.setCellValueFactory(new PropertyValueFactory<>("productNum"));
                TableColumn<productQuantityPair, String> colProductDescription = new TableColumn<>("Artikel beschrijving");
                colProductDescription.setCellValueFactory(new PropertyValueFactory<>("productDescription"));
                TableColumn<productQuantityPair, String> colAmount = new TableColumn<>("Te bestellen hoeveelheid");
                colAmount.setCellValueFactory(new PropertyValueFactory<>("quantity"));

                table.getColumns().add(colProductNum);
                table.getColumns().add(colProductDescription);
                table.getColumns().add(colAmount);



                ObservableList<productQuantityPair> quantityPairs = FXCollections.observableArrayList();
                for (Map.Entry<Product, Integer> entry : recommendation.getProductRecommendation().entrySet()) {
                    Product k = entry.getKey();
                    Integer v = entry.getValue();
                    quantityPairs.add(new productQuantityPair(k.getProductNum(), k.getDescription(), v));
                }
                table.setItems(quantityPairs);

                //checkbox
                CheckBox checkConfirm = (CheckBox) pane.getGraphic().lookup("#checkConfirm");
                checkConfirm.setOnAction((ActionEvent event) -> {
                    if(recommendation.isConfirmed()){
                        recommendation.setConfirmed(false);
                    }else{
                        recommendation.setConfirmed(true);
                    }
                    update();
                });

                //choose which accordion to put the pane in
                if(recommendation.isConfirmed()){
                    checkConfirm.setSelected(true);
                    accordionCheckedRecommendations.getPanes().add(pane);
                }else{
                    accordionNewRecommendations.getPanes().add(pane);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public class productQuantityPair {
        String productNum;
        String productDescription;
        int quantity;

        public productQuantityPair(String productNum, String productDescription, int quantity) {
            this.productNum = productNum;
            this.productDescription = productDescription;
            this.quantity = quantity;
        }

        public String getProductNum() {
            return productNum;
        }

        public String getProductDescription() {
            return productDescription;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}

