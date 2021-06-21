package com.caramba.ordertool.scenes;

import com.caramba.ordertool.*;
import com.google.firestore.v1.StructuredQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class RecommendedOrdersController implements Initializable, ViewController {

    @FXML
    private Accordion accordionNewRecommendations;

    @FXML
    private Accordion accordionCheckedRecommendations;

    @FXML
    private Button savePDF_button;

    @FXML
    private Label recommendation_label;



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
        recommendations.sortRecommendationsByDate();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");


        int i = 0;
        for (Recommendation recommendation : recommendations.getRecommendations()) {
            try {
                TitledPane pane = FXMLLoader.load(res);
                pane.setText("");
                //created date
                LocalDateTime creationDate = recommendation.getCreationDate();
                String date = creationDate.format(dateTimeFormatter);
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
                LocalDateTime finalOrderDate = recommendation.getFinalOrderDate();
                if(finalOrderDate != null){
                    Text textFinalOrderDate = (Text) pane.getContent().lookup("#textFinalOrderDate");
                    textFinalOrderDate.setText(finalOrderDate.format(dateTimeFormatter));
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


                savePDF_button = new Button();
                Node recommendationID = pane.getContent().lookup("#recommendation_label");
                recommendationID.setId(Integer.toString(i));

                i++;

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public void savePDF(){
        Stage stage = OrderTool.getMainStage();
        DirectoryChooser chooser = new DirectoryChooser();
        File selectedDirectory = chooser.showDialog(stage);

        PDFCreator creator = new PDFCreator(selectedDirectory.getAbsolutePath(), OrderTool.getRecommendations().getRecommendations().get(Integer.parseInt(this.recommendation_label.getId())));
        creator.addProducts();
        creator.save();

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

