package com.caramba.ordertool.scenes;

import com.caramba.ordertool.OrderAlgorithm;
import com.caramba.ordertool.OrderTool;
import com.caramba.ordertool.models.*;
import com.caramba.ordertool.scenes.displayModels.DisplayProduct;
import com.caramba.ordertool.scenes.displayModels.ProductDetailsTableData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

import javax.annotation.Nonnull;
import java.net.URL;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

public class ProductOverviewViewController implements Initializable, ViewController {
    private final static String[] MONTH_NAME = {"jan", "feb", "mrt", "apr", "mei", "jun", "jul", "aug", "sep", "okt", "nov", "dec"};
    private final OrderAlgorithm orderAlgo = new OrderAlgorithm();
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
    @FXML
    private NumberAxis NumberAxisAmount;
    @FXML
    private TableView<ProductDetailsTableData> tableProductDetails;
    @FXML
    private TableColumn<ProductDetailsTableData, CheckBox> colProductDetailsVisible;
    @FXML
    private TableColumn<ProductDetailsTableData, String> colProductDetailsName;
    @FXML
    private TableColumn<ProductDetailsTableData, Integer> colProductDetailsJan;
    @FXML
    private TableColumn<ProductDetailsTableData, Integer> colProductDetailsFeb;
    @FXML
    private TableColumn<ProductDetailsTableData, Integer> colProductDetailsMar;
    @FXML
    private TableColumn<ProductDetailsTableData, Integer> colProductDetailsApr;
    @FXML
    private TableColumn<ProductDetailsTableData, Integer> colProductDetailsMay;
    @FXML
    private TableColumn<ProductDetailsTableData, Integer> colProductDetailsJun;
    @FXML
    private TableColumn<ProductDetailsTableData, Integer> colProductDetailsJul;
    @FXML
    private TableColumn<ProductDetailsTableData, Integer> colProductDetailsAug;
    @FXML
    private TableColumn<ProductDetailsTableData, Integer> colProductDetailsSept;
    @FXML
    private TableColumn<ProductDetailsTableData, Integer> colProductDetailsOct;
    @FXML
    private TableColumn<ProductDetailsTableData, Integer> colProductDetailsNov;
    @FXML
    private TableColumn<ProductDetailsTableData, Integer> colProductDetailsDec;
    @FXML
    private TextField search_bar;
    @FXML
    private Button search_button;
    @FXML
    private Button reset_button;
    private String search_value = "";
    private DisplayProduct selectedProduct = null;
    private Year selectedYear = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //set tickLabelFormatter
        StringConverter<Number> tickLabelFormatter = new StringConverter<>() {
            @Override
            public String toString(Number object) {
                //make sure all ticks are full ints, so no decimal numbers appear on the axis
                if (object.doubleValue() == object.intValue()) {
                    return object.intValue() + "";
                } else {
                    return "";
                }
            }

            @Override
            public Integer fromString(String string) {
                return null;
            }
        };
        NumberAxisAmount.setTickLabelFormatter(tickLabelFormatter);

        colProductNum.setCellValueFactory(new PropertyValueFactory<>("productNum"));
        colProductDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colProductStock.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colProductSuppliers.setCellValueFactory(new PropertyValueFactory<>("supplierNames"));

        colProductDetailsVisible.setCellValueFactory(new PropertyValueFactory<>("checkboxToggleVisible"));
        colProductDetailsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colProductDetailsJan.setCellValueFactory(new PropertyValueFactory<>("janValue"));
        colProductDetailsFeb.setCellValueFactory(new PropertyValueFactory<>("febValue"));
        colProductDetailsMar.setCellValueFactory(new PropertyValueFactory<>("marValue"));
        colProductDetailsApr.setCellValueFactory(new PropertyValueFactory<>("aprValue"));
        colProductDetailsMay.setCellValueFactory(new PropertyValueFactory<>("mayValue"));
        colProductDetailsJun.setCellValueFactory(new PropertyValueFactory<>("junValue"));
        colProductDetailsJul.setCellValueFactory(new PropertyValueFactory<>("julValue"));
        colProductDetailsAug.setCellValueFactory(new PropertyValueFactory<>("augValue"));
        colProductDetailsSept.setCellValueFactory(new PropertyValueFactory<>("septValue"));
        colProductDetailsOct.setCellValueFactory(new PropertyValueFactory<>("octValue"));
        colProductDetailsNov.setCellValueFactory(new PropertyValueFactory<>("novValue"));
        colProductDetailsDec.setCellValueFactory(new PropertyValueFactory<>("decValue"));


        tableProductOverview.setRowFactory(tableView -> new TableRow<>());

        tableProductOverview.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                selectedProduct = tableProductOverview.getSelectionModel().getSelectedItem();
                updateProductData();
            }
        });

        search_bar.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                search_value = search_bar.getCharacters().toString().toLowerCase();
                update();
            }
        });

        search_button.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                search_value = search_bar.getCharacters().toString().toLowerCase();
                update();
            }
        });

        reset_button.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                search_value = "";
                search_bar.clear();
                update();
            }
        });

        //create year selector
        for (int y = LocalDate.now().plusYears(1).getYear(); y >= 1900; y--) {
            comboYearSelector.getItems().add(Year.of(y));
        }
        comboYearSelector.getSelectionModel().select(Year.now());

        comboYearSelector.setOnAction((ActionEvent event) -> updateProductData());
    }

    public void update() {
        //Product List
        //Load products and create display products
        ProductList productList = OrderTool.getProducts();
        ObservableList<DisplayProduct> observableList = FXCollections.observableArrayList();
        if (search_value.equals("")) {
            for (Map.Entry<String, Product> entry : productList.getProducts().entrySet()) {
                String k = entry.getKey();
                Product p = entry.getValue();
                SupplierList sl = OrderTool.getSuppliers();
                observableList.add(new DisplayProduct(k, p.getProductNum(), p.getDescription(), p.getQuantity(), sl.getSuppliersSellingProduct(p)));
            }
        } else {
            for (Map.Entry<String, Product> entry : productList.getProducts().entrySet()) {
                String k = entry.getKey();
                Product p = entry.getValue();
                SupplierList sl = OrderTool.getSuppliers();
                if (p.getDescription().toLowerCase().contains(search_value) || p.getProductNum().toLowerCase().contains(search_value)) {
                    observableList.add(new DisplayProduct(k, p.getProductNum(), p.getDescription(), p.getQuantity(), sl.getSuppliersSellingProduct(p)));
                }
            }
        }
        //add them to the overview list
        tableProductOverview.setItems(observableList);

        //Product Details
        updateProductData();
    }

    /**
     * Shows the median sales per month of the selected year
     */
    private ProductDetailsTableData getMedianYearData(String productID, Year year) {
        MedianYear my = MedianYear.getMedianYear(OrderTool.getSales().getSalesUpToYear(year).getDateAmountMap(productID));
        ProductDetailsTableData medianYearTableData = createProductDetailsTableData("Mediaan verkopen per maand", "#33ccff", false);
        //this series is selected by default
        medianYearTableData.getCheckboxToggleVisible().setSelected(true);
        for (int i = 1; i <= 12; i++) {
            int amount = my.getByMonthNumber(i);
            medianYearTableData.setValue(i, amount);
        }
        medianYearTableData.getCheckboxToggleVisible().setSelected(true);
        return medianYearTableData;
    }

    /**
     * Shows the sales in the given year
     */
    private ProductDetailsTableData getSalesData(String displayName, String productID, Year year, boolean selectedByDefault, String color) {
        ProductDetailsTableData salesTableData = createProductDetailsTableData(displayName, color, false);
        SalesList sales = OrderTool.getSales().getSalesByProduct(productID);
        for (int i = 1; i <= 12; i++) {
            YearMonth date = YearMonth.of(year.getValue(), i);
            //do not add data for the future
            if (!date.isAfter(YearMonth.now())) {
                int amount = sales.getSoldInYearMonth(productID, date);
                salesTableData.setValue(i, amount);
            }
        }
        salesTableData.getCheckboxToggleVisible().setSelected(selectedByDefault);
        return salesTableData;
    }

    /**
     * Shows the amount of projected Sales in the selected year
     */
    private ProductDetailsTableData getProjectedSalesData(String productID, Year year) {
        ProductDetailsTableData projectedSalesTableData = createProductDetailsTableData("Verwachte verkoop", "#eb4034", true);
        Year now = Year.now();
        if (!selectedYear.isBefore(now)) {
            int monthToStart;
            if (selectedYear.equals(now)) {
                monthToStart = LocalDate.now().getMonth().getValue();
            } else {
                monthToStart = 1;
            }
            for (int m = monthToStart; m <= 12; m++) {
                int amount = orderAlgo.getProjectedSaleAmount(productID, YearMonth.of(year.getValue(), m));
                projectedSalesTableData.setValue(m, amount);
            }
        }
        projectedSalesTableData.getCheckboxToggleVisible().setSelected(true);
        return projectedSalesTableData;
    }

    /**
     * Shows the supply of a product at the end of the month
     * <p>
     * Inserts the data from the database into the table underneath the graph.
     */
    private ProductDetailsTableData getProductQuantity(String productID, Year year) {
        ProductDetailsTableData productQuantity = createProductDetailsTableData("Voorraad", "#000f00", false);
        Map<YearMonth, Integer> productHistoryQuantityList = OrderTool.getProductHistoryQuantity(productID);
        for (Map.Entry<YearMonth, Integer> entry : productHistoryQuantityList.entrySet()) {
            YearMonth k = entry.getKey();
            Integer p = entry.getValue();
            for (int i = 1; i <= 12; i++) {
                YearMonth date = YearMonth.of(year.getValue(), i);
                if (k.equals(date)) {
                    if (!date.isAfter(YearMonth.now())) {
                        productQuantity.setValue(i, p);
                    }
                }
            }
        }
        productQuantity.getCheckboxToggleVisible().setSelected(false);
        return productQuantity;
    }

    /**
     * Converts table data to an XYSeries that can be displayed in the chart
     */
    private @Nonnull
    XYChart.Series<String, Integer> convertToChartSeries(ProductDetailsTableData productDetailsTableData) {
        XYChart.Series<String, Integer> result = new XYChart.Series<>();
        for (int i = 1; i <= 12; i++) {
            Integer value = productDetailsTableData.getMonthValue(i);
            if (value != null) {
                result.getData().add(new XYChart.Data<>(MONTH_NAME[i - 1], value));
            }
        }
        result.setName(productDetailsTableData.getName());
        return result;
    }

    private void updateProductData() {
        selectedYear = comboYearSelector.getValue();
        if (selectedProduct != null && selectedYear != null) {
            String productID = selectedProduct.getInternalId();

            //create the observableList for the table
            ObservableList<ProductDetailsTableData> tableData = FXCollections.observableArrayList();

            //add the data
            tableData.add(getMedianYearData(productID, selectedYear));
            tableData.add(getSalesData("Verkopen dit jaar", productID, selectedYear, true, "#64b000"));
            tableData.add(getProjectedSalesData(productID, selectedYear));
            tableData.add(getSalesData("Verkopen afeglopen jaar (" + selectedYear.minusYears(1) + ")", productID, selectedYear.minusYears(1), false, "#009917"));
            tableData.add(getProductQuantity(productID, selectedYear));

            //update the table
            tableProductDetails.getItems().clear();
            tableProductDetails.setItems(tableData);

            //update the chart
            updateChart();
        }
    }

    private void updateChart() {
        //clear chart
        lineChartProductTimeLine.getData().clear();
        //get the data from the table
        ObservableList<ProductDetailsTableData> tableData = tableProductDetails.getItems();
        ArrayList<String> colorOrder = new ArrayList<>();
        for (ProductDetailsTableData ProductDetailsTableData : tableData) {
            //only display selected data
            if (ProductDetailsTableData.getCheckboxToggleVisible().isSelected()) {
                XYChart.Series<String, Integer> series = convertToChartSeries(ProductDetailsTableData);
                lineChartProductTimeLine.getData().add(series);
                String color = ProductDetailsTableData.getColor();
                if (color != null) {
                    colorOrder.add(color);
                }
                if (ProductDetailsTableData.hasDashedLine()) {
                    series.getNode().setStyle("-fx-stroke-dash-array: 2 12 12 2;");
                }
            }
        }
        //set the line colors
        StringBuilder styleString = new StringBuilder();
        for (int i = 1; i <= colorOrder.size(); i++) {
            styleString.append("CHART_COLOR_").append(i).append(": ").append(colorOrder.get(i - 1)).append(";");
        }
        if (styleString.length() > 0) {
            lineChartProductTimeLine.setStyle(styleString.toString());
        }
        //lineChartProductTimeLine.setStyle("CHART_COLOR_1: #33ccff ; CHART_COLOR_2: #64b000 ; CHART_COLOR_3: #00b80c;");
    }

    private ProductDetailsTableData createProductDetailsTableData(String displayName, String color, boolean isDashed) {
        CheckBox checkBox = new CheckBox();
        checkBox.setOnAction((ActionEvent event) -> updateChart());
        return new ProductDetailsTableData(displayName, checkBox, color, isDashed);
    }

}
