package com.caramba.ordertool.scenes;

import com.caramba.ordertool.*;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProductOverviewViewController implements Initializable, ViewController {
    @FXML
    private TableView<DisplayProduct> tableProductOverview;
    @FXML
    private TableColumn<DisplayProduct, String> colProductNum;
    @FXML
    private TableColumn<DisplayProduct, String> colProductDescription;
    @FXML
    private TableColumn<DisplayProduct, Integer> colProductStock;
    @FXML
    private TableColumn<DisplayProduct, String> colProductSuppliers;
    @FXML
    private ComboBox<Year> comboYearSelector;
    @FXML
    private LineChart<String, Integer> lineChartProductTimeLine;

    private DisplayProduct selectedProduct = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colProductNum.setCellValueFactory(new PropertyValueFactory<>("productNum"));
        colProductDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colProductStock.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colProductSuppliers.setCellValueFactory(new PropertyValueFactory<>("supplierNames"));

        tableProductOverview.setRowFactory(tableView -> {
            /*final TableRow<DisplayProduct> row = new TableRow<>();
            final ContextMenu contextMenuRow = new ContextMenu();

            final MenuItem removeRowMenuItem = new MenuItem("Verwijderen");
            removeRowMenuItem.setOnAction(event -> remove(row.getItem()));
            final MenuItem editRowMenuItem = new MenuItem("Wijzigen");
            editRowMenuItem.setOnAction(event -> edit(row.getItem()));
            final MenuItem addRowMenuItem = new MenuItem("Toevoegen");
            addRowMenuItem.setOnAction(event -> add());
            final MenuItem updateRowMenuItem = new MenuItem("Verversen");
            updateRowMenuItem.setOnAction(event -> update());

            contextMenuRow.getItems().addAll(editRowMenuItem, removeRowMenuItem, addRowMenuItem, updateRowMenuItem);
            //context options for clicking on an empty row
            final ContextMenu contextMenu = new ContextMenu();

            final MenuItem addMenuItem = new MenuItem("Toevoegen");
            addMenuItem.setOnAction(event -> add());
            final MenuItem updateMenuItem = new MenuItem("Verversen");
            updateRowMenuItem.setOnAction(event -> update());

            contextMenu.getItems().addAll(addMenuItem, updateMenuItem);
            // Set context menu on row, but use a binding to make it only show for non-empty rows:
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then(contextMenu)
                            .otherwise(contextMenuRow)
            );
            tableProductOverview.setContextMenu(contextMenu);(
            return row;*/
            return new TableRow<>();
        });

        tableProductOverview.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                selectedProduct = tableProductOverview.getSelectionModel().getSelectedItem();
                showChart();
            }
        });

        //create year selector
        for(int y = LocalDate.now().plusYears(1).getYear(); y >= 1900; y--){
            comboYearSelector.getItems().add(Year.of(y));
        }
        comboYearSelector.getSelectionModel().select(Year.now());

        comboYearSelector.setOnAction((ActionEvent event) -> {
            showChart();
        });
    }

    public void update() {
        ProductList productList = OrderTool.getProducts();
        ObservableList<DisplayProduct> observableList = FXCollections.observableArrayList();
        for (Map.Entry<String, Product> entry : productList.getProducts().entrySet()) {
            String k = entry.getKey();
            Product p = entry.getValue();
            SupplierList sl = OrderTool.getSuppliers();
            observableList.add(new DisplayProduct(k, p.getProductNum(), p.getDescription(), p.getQuantity(), sl.getSuppliersSellingProduct(p)));
        }
        tableProductOverview.setItems(observableList);
    }

    private void remove(DisplayProduct displayProduct) {
        ButtonType yesBtnType = new ButtonType("Verwijderen", ButtonBar.ButtonData.OK_DONE);
        ButtonType noBtnType = new ButtonType("Annuleren", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Weet u zeker dat u " + displayProduct.getProductNum() + " " + displayProduct.getDescription() + " wilt verwijderen?", yesBtnType, noBtnType);
        alert.showAndWait();
        if (alert.getResult() == yesBtnType) {
            ProductList productList = OrderTool.getProducts();
            productList.remove(displayProduct.getInternalId());
            update();
        }
    }

    private void edit(DisplayProduct displayProduct) {
        String internalId = displayProduct.getInternalId();
        Product p = showEditDialog("Product wijzigen", "Wijzigen", displayProduct);
        if (p != null) {
            OrderTool.getProducts().remove(internalId);
            OrderTool.getProducts().add(internalId, p);
            update();
        }
    }

    private void add() {
        Product p = showEditDialog("Nieuw product toevoegen", "Toevoegen", null);
        if (p != null) {
            OrderTool.getProducts().add(p);
            update();
        }
    }

    private void showChart() {
        if (selectedProduct != null) {
            String productID = selectedProduct.getInternalId();
            OrderAlgorithm orderAlgo = new OrderAlgorithm();

            //clear chart
            lineChartProductTimeLine.getData().clear();


            if (comboYearSelector.getValue() != null) {
                Year selectedYear = comboYearSelector.getValue();
                XYChart.Series<String, Integer> quantitySoldSeries = new XYChart.Series<>();
                quantitySoldSeries.setName("Verkopen");
                XYChart.Series<String, Integer> medianYearSeries = new XYChart.Series<>();
                medianYearSeries.setName("Mediaan verkopen per maand");
                XYChart.Series<String, Integer> projectedSalesSeries = new XYChart.Series<>();
                projectedSalesSeries.setName("Verwachte verkopen");

                //median year based on sales before the selected year
                Saleslist previousSales = OrderTool.getSales().getSalesBeforeYear(selectedYear.getValue());
                MedianYear my = orderAlgo.getMedianYear(previousSales.getDateAmountMap(productID));
                if(my != null) {
                    for (int i = 1; i <= 12; i++) {
                        XYChart.Data<String, Integer> data = new XYChart.Data<>(Month.of(i).toString(), my.getByMonthNumber(i));
                        medianYearSeries.getData().add(data);
                    }
                }

                //sales
                Saleslist sales = OrderTool.getSales().getSalesByProduct(productID);
                XYChart.Data<String, Integer> lastSaleData = null;
                for (int i = 1; i <= 12; i++) {
                    YearMonth date = YearMonth.of(selectedYear.getValue(), i);
                    //do not add data for the future
                    if(!date.isAfter(YearMonth.now())){
                        String m = Month.of(i).toString();
                        int amount = sales.getSoldInYearMonth(productID, date);
                        XYChart.Data<String, Integer> data = new XYChart.Data<>(m, amount);
                        quantitySoldSeries.getData().add(data);
                        lastSaleData = data;
                    }
                }

                //projected sales
                //todo this should be fixed
                // projected sales are currently based on a full calendar year (jan to dec)
                //This means that we cannot currently project sales of future years, even if they are nearby
                //(e.g. we can't project jan 2021 if we're in dec 2020)
                //until this is fixed projected sales should only show for the current year
                if(selectedYear.equals(Year.now())){
                    if (lastSaleData != null) {
                        XYChart.Data<String, Integer> data = new XYChart.Data<>(lastSaleData.getXValue(), lastSaleData.getYValue());
                        projectedSalesSeries.getData().add(data);
                    }
                    for (int m = LocalDate.now().getMonth().getValue() + 1; m <= 12; m++) {
                        int amount = orderAlgo.getProjectedSaleAmount(productID, YearMonth.of(Year.now().getValue(), m));
                        XYChart.Data<String, Integer> data = new XYChart.Data<>(Month.of(m).toString(), amount);
                        projectedSalesSeries.getData().add(data);
                    }
                }
                lineChartProductTimeLine.getData().add(medianYearSeries);
                lineChartProductTimeLine.getData().add(projectedSalesSeries);
                lineChartProductTimeLine.getData().add(quantitySoldSeries);

                lineChartProductTimeLine.setStyle("CHART_COLOR_1: #33ccff ; CHART_COLOR_2: #64b000 ; CHART_COLOR_3: #00b80c;");
                projectedSalesSeries.getNode().setStyle("-fx-stroke-dash-array: 2 12 12 2;");
            }

        }
    }

    private Product showEditDialog(String title, String buttonText, DisplayProduct placeholder) {
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(title);
        ButtonType confirmButtonType = new ButtonType(buttonText, ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField productNumTextField = new TextField();
        productNumTextField.setPromptText("Artiekel nummer");
        TextField productDescriptionTextField = new TextField();
        productDescriptionTextField.setPromptText("Beschrijving");

        grid.add(new Label("Artiekel nummer"), 0, 0);
        grid.add(productNumTextField, 1, 0);
        grid.add(new Label("Beschrijving"), 0, 1);
        grid.add(productDescriptionTextField, 1, 1);

        if (placeholder != null) {
            productNumTextField.setText(placeholder.getProductNum());
        }

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                return new Product(productNumTextField.getText(), productDescriptionTextField.getText());
            }
            return null;
        });

        Optional<Product> result = dialog.showAndWait();
        return result.orElse(null);
    }

    public class DisplayProduct {
        private final String internalId;
        private final String productNum;
        private final String description;
        private final int quantity;
        private final String supplierNames;

        public DisplayProduct(String internalId, String productNum, String description, int quantity, SupplierList suppliersSelling) {
            this.internalId = internalId;
            this.productNum = productNum;
            this.description = description;
            this.quantity = quantity;

            //Make a string of the names of the suppliers
            StringBuilder supplierNames = new StringBuilder();
            if (suppliersSelling.getSuppliers().isEmpty()) {
                supplierNames = new StringBuilder("Unknown supplier");
            } else {
                int i = 1;
                for (Supplier s : suppliersSelling.getSuppliers().values()) {
                    //add a comma before every name except the first
                    if (i > 1) {
                        supplierNames.append(" ,");
                    }
                    supplierNames.append(s.getName());
                    i++;
                }
            }
            this.supplierNames = supplierNames.toString();
        }

        public String getInternalId() {
            return internalId;
        }

        public String getProductNum() {
            return productNum;
        }

        public String getDescription() {
            return description;
        }

        public int getQuantity() {
            return quantity;
        }

        public String getSupplierNames() {
            return supplierNames;
        }
    }
}
