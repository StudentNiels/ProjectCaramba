package com.caramba.ordertool.scenes;

import com.caramba.ordertool.*;
import com.caramba.ordertool.models.Product;
import com.caramba.ordertool.models.Recommendation;
import com.caramba.ordertool.models.RecommendationList;
import com.caramba.ordertool.models.Supplier;
import com.caramba.ordertool.notifications.Notification;
import com.caramba.ordertool.notifications.NotificationManager;
import com.caramba.ordertool.notifications.NotificationType;
import com.caramba.ordertool.scenes.displayModels.ProductQuantityPair;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.ResourceBundle;

public class RecommendedOrdersController implements Initializable, ViewController {

    @FXML
    private ScrollPane scrollPaneMain;
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
        scrollPaneMain.setStyle("-fx-background: white;");
        //clear the accordions before refreshing
        accordionCheckedRecommendations.getPanes().clear();
        accordionNewRecommendations.getPanes().clear();

        URL res = getClass().getResource("/scenes/recommendation.fxml");
        RecommendationList recommendations = OrderTool.getRecommendations();
        recommendations.sortRecommendationsByDate();
        int i = 0;
        DateTimeFormatter creationDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        DateTimeFormatter finalOrderDateDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter yearMonthFormatter = DateTimeFormatter.ofPattern("MM/yyyy");
        for (Recommendation recommendation : recommendations.getRecommendations()) {
            try {
                TitledPane pane = FXMLLoader.load(res);
                pane.setText("");
                pane.setStyle("-fx-background: #f0f0f0;");
                //created date
                LocalDateTime creationDate = recommendation.getCreationDate();
                String date = creationDate.format(creationDateFormatter);
                pane.setText(pane.getText() + date);
                Text createdDateText = (Text) pane.getContent().lookup("#textCreatedDate");
                createdDateText.setText(date);

                //supplier name
                Supplier supplier = recommendation.getSupplier();
                String supplierName = null;
                if (supplier != null) {
                    supplierName = supplier.getName();
                }
                if (supplierName == null) {
                    supplierName = "(Leverancier onbekend)";
                }
                pane.setText(" Order voor: " + supplierName + " " + pane.getText());
                Text supplierText = (Text) pane.getContent().lookup("#textSupplier");
                supplierText.setText(supplierName);

                //Order for date
                YearMonth yearMonthToOrderFor = recommendation.getYearMonthToOrderFor();
                if (yearMonthToOrderFor != null) {
                    Text textYearMonthToOrderFor = (Text) pane.getContent().lookup("#textOrderForDate");
                    textYearMonthToOrderFor.setText(yearMonthToOrderFor.format(yearMonthFormatter));
                }

                //Final order date
                LocalDate finalOrderDate = recommendation.getFinalOrderDate();
                if (finalOrderDate != null) {
                    Text textFinalOrderDate = (Text) pane.getContent().lookup("#textFinalOrderDate");
                    String s = finalOrderDate.format(finalOrderDateDateFormatter);
                    if (supplier != null) {
                        s = s + " (Geschatte levertijd is " + supplier.getAvgDeliveryTime() + " dag(en))";
                    }
                    textFinalOrderDate.setText(s);
                }

                //products
                //create the table and columns
                TableView<ProductQuantityPair> table = (TableView<ProductQuantityPair>) pane.getContent().lookup("#tableRecommendedProducts");
                TableColumn<ProductQuantityPair, String> colProductNum = new TableColumn<>("Artikel nummer");
                colProductNum.setCellValueFactory(new PropertyValueFactory<>("productNum"));
                TableColumn<ProductQuantityPair, String> colProductDescription = new TableColumn<>("Artikel beschrijving");
                colProductDescription.setCellValueFactory(new PropertyValueFactory<>("productDescription"));
                TableColumn<ProductQuantityPair, String> colAmount = new TableColumn<>("Te bestellen hoeveelheid");
                colAmount.setCellValueFactory(new PropertyValueFactory<>("quantity"));

                table.getColumns().add(colProductNum);
                table.getColumns().add(colProductDescription);
                table.getColumns().add(colAmount);


                ObservableList<ProductQuantityPair> quantityPairs = FXCollections.observableArrayList();
                for (Map.Entry<Product, Integer> entry : recommendation.getProductRecommendation().entrySet()) {
                    Product k = entry.getKey();
                    Integer v = entry.getValue();
                    quantityPairs.add(new ProductQuantityPair(k.getProductNum(), k.getDescription(), v));
                }
                table.setItems(quantityPairs);

                //checkbox
                CheckBox checkConfirm = (CheckBox) pane.getGraphic().lookup("#checkConfirm");
                checkConfirm.setOnAction((ActionEvent event) -> {
                    if (recommendation.isConfirmed()) {
                        recommendation.setConfirmed(false);
                        OrderTool.getConfig().confirmRecommendation(recommendation, false);
                    } else {
                        recommendation.setConfirmed(true);
                        OrderTool.getConfig().confirmRecommendation(recommendation, true);
                    }
                    update();
                });

                //choose which accordion to put the pane in
                if (recommendation.isConfirmed()) {
                    checkConfirm.setSelected(true);
                    accordionCheckedRecommendations.getPanes().add(pane);
                } else {
                    accordionNewRecommendations.getPanes().add(pane);
                }


                savePDF_button = new Button();
                Node recommendationID = pane.getContent().lookup("#recommendation_label");
                recommendationID.setId(Integer.toString(i));

                i++;

            } catch (IOException e) {
                NotificationManager.addExceptionError(e);
            }

        }
    }


    public void saveFile() {
        Stage stage = OrderTool.getMainStage();
        FileChooser chooser = new FileChooser();
        String pdfFileDescription = "PDF file";
        String csvFileDescription = "CSV file";
        FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter(csvFileDescription, "*.csv");
        FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter(pdfFileDescription, "*.pdf");
        chooser.getExtensionFilters().add(csvFilter);
        chooser.getExtensionFilters().add(pdfFilter);
        chooser.setInitialFileName("Order advies " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyy-MM-dd-HH-mm-ss")));
        File selectedFile = chooser.showSaveDialog(stage);
        if (selectedFile != null) {
            String selectedFileType = chooser.getSelectedExtensionFilter().getDescription();
            if (selectedFileType.equals(pdfFileDescription)) {
                PDFCreator creator = new PDFCreator(selectedFile.getAbsolutePath(), OrderTool.getRecommendations().getRecommendations().get(Integer.parseInt(this.recommendation_label.getId())));
                creator.addProducts();
                creator.save();
            } else if (selectedFileType.equals(csvFileDescription)) {
                CSVCreator creator = new CSVCreator();
                creator.saveRecommendation(selectedFile.getAbsolutePath(), OrderTool.getRecommendations().getRecommendations().get(Integer.parseInt(this.recommendation_label.getId())));
            }
        } else {
            NotificationManager.add(new Notification(NotificationType.ERROR, "The selected path is invalid"));
        }
    }
}
