package com.caramba.ordertool.scenes;

import com.caramba.ordertool.*;
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
    private OrderAlgorithm orderAlgo = new OrderAlgorithm();

    private final static String MONTH_NAME[] = { "jan", "feb", "mrt", "apr", "mei", "jun", "jul", "aug", "sep", "okt", "nov", "dec" };

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //set tickLabelFormatter
        StringConverter<Number> tickLabelFormatter = new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                //make sure all ticks are full ints, so no decimal numbers appear on the axis
                if(object.doubleValue() == object.intValue()){
                    return object.intValue() + "";
                }else{
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


        tableProductOverview.setRowFactory(tableView -> {
            return new TableRow<>();
        });

        tableProductOverview.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                selectedProduct = tableProductOverview.getSelectionModel().getSelectedItem();
                updateProductData();
            }
        });

        search_bar.setOnKeyPressed((KeyEvent keyEvent) -> {
            if(keyEvent.getCode().equals(KeyCode.ENTER)){
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
        for(int y = LocalDate.now().plusYears(1).getYear(); y >= 1900; y--) {
            comboYearSelector.getItems().add(Year.of(y));
        }
        comboYearSelector.getSelectionModel().select(Year.now());

        comboYearSelector.setOnAction((ActionEvent event) -> {
            updateProductData();
        });
    }

    public void update() {
        //Product List
        //Load products and create display products
        ProductList productList = OrderTool.getProducts();
        ObservableList<DisplayProduct> observableList = FXCollections.observableArrayList();
        if(search_value.equals("")){
            for (Map.Entry<String, Product> entry : productList.getProducts().entrySet()) {
                String k = entry.getKey();
                Product p = entry.getValue();
                SupplierList sl = OrderTool.getSuppliers();
                observableList.add(new DisplayProduct(k, p.getProductNum(), p.getDescription(), p.getQuantity(), sl.getSuppliersSellingProduct(p)));
            }
        }else{
            for (Map.Entry<String, Product> entry : productList.getProducts().entrySet()) {
                String k = entry.getKey();
                Product p = entry.getValue();
                SupplierList sl = OrderTool.getSuppliers();
                if(p.getDescription().toLowerCase().contains(search_value) || p.getProductNum().toLowerCase().contains(search_value)){
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
     *Shows the median sales per month of the selected year
     */
    private ProductDetailsTableData getMedianYearData(String displayName, String productID, Year year, boolean selectedByDefault, String color, boolean isDashed){
        MedianYear my = orderAlgo.getMedianYear(OrderTool.getSales().getDateAmountMap(productID));
        if(my != null) {
            ProductDetailsTableData medianYearTableData = createProductDetailsTableData(displayName, color, isDashed);
            //this series is selected by default
            medianYearTableData.getCheckboxToggleVisible().setSelected(true);
            for (int i = 1; i <= 12; i++) {
                int amount = my.getByMonthNumber(i);
                medianYearTableData.setValue(i, amount);
            }
            medianYearTableData.getCheckboxToggleVisible().setSelected(selectedByDefault);
            return medianYearTableData;
        }
        return null;
    }

    /**
     *Shows the sales in the given year
     */
    private ProductDetailsTableData getSalesData(String displayName, String productID, Year year, boolean selectedByDefault, String color, boolean isDashed){
        ProductDetailsTableData salesTableData = createProductDetailsTableData(displayName, color, isDashed);
        Saleslist sales = OrderTool.getSales().getSalesByProduct(productID);
        for (int i = 1; i <= 12; i++) {
            YearMonth date = YearMonth.of(year.getValue(), i);
            //do not add data for the future
            if(!date.isAfter(YearMonth.now())){
                int amount = sales.getSoldInYearMonth(productID, date);
                salesTableData.setValue(i, amount);
            }
        }
        salesTableData.getCheckboxToggleVisible().setSelected(selectedByDefault);
        return salesTableData;
    }

    /**
     *Shows the amount of projected Sales in the selected year
     */
    private ProductDetailsTableData getProjectedSalesData(String displayName, String productID, Year year, boolean selectedByDefault, String color, boolean isDashed){
        ProductDetailsTableData projectedSalesTableData = createProductDetailsTableData(displayName, color, isDashed);
        Year now = Year.now();
        if(!selectedYear.isBefore(now)){
            int monthToStart;
            if(selectedYear.equals(now)){
                monthToStart = LocalDate.now().getMonth().getValue();
            }else{
                monthToStart = 1;
            }
            for (int m = monthToStart; m <= 12; m++) {
                int amount = orderAlgo.getProjectedSaleAmount(productID, YearMonth.of(year.getValue(), m));
                projectedSalesTableData.setValue(m, amount);
            }
        }
        projectedSalesTableData.getCheckboxToggleVisible().setSelected(selectedByDefault);
        return projectedSalesTableData;
    }

    /**
     * Shows the supply of a product at the end of the month
     *
     * Inserts the data from the database into the table underneath the graph.
     */
    private ProductDetailsTableData getProductQuantity(String displayName, String productID, Year year, boolean selectedByDefault, String color, boolean isDashed){
        ProductDetailsTableData productQuantity = createProductDetailsTableData(displayName, color, isDashed);
        Map<YearMonth, Integer> productHistoryQuantityList = OrderTool.getProductHistoryQuantity(productID);
        for (Map.Entry<YearMonth, Integer> entry : productHistoryQuantityList.entrySet()) {
            YearMonth k = entry.getKey();
            Integer p = entry.getValue();
            for (int i = 1; i <= 12; i++) {
                YearMonth date = YearMonth.of(year.getValue(), i);
                if(k.equals(date))
                {
                    if(!date.isAfter(YearMonth.now())) {
                        productQuantity.setValue(i, p);
                    }
                }
            }
        }
        productQuantity.getCheckboxToggleVisible().setSelected(selectedByDefault);
        return productQuantity;
    }

    /**
     * Converts table data to an XYseries that can be displayed in the chart
     */
    private @Nonnull XYChart.Series<String, Integer> convertToChartSeries(ProductDetailsTableData productDetailsTableData){
        XYChart.Series<String, Integer> result = new XYChart.Series<>();
        for(int i = 1; i <= 12; i++){
            Integer value = productDetailsTableData.getMonthValue(i);
            if(value != null){
                result.getData().add(new XYChart.Data<>(MONTH_NAME[i - 1], value));
            }
        }
        result.setName(productDetailsTableData.getName());
        return result;
    }

    private void updateProductData(){
        selectedYear = comboYearSelector.getValue();
        if (selectedProduct != null && selectedYear != null) {
            String productID = selectedProduct.getInternalId();

            //create the observableList for the table
            ObservableList<ProductDetailsTableData> tableData = FXCollections.observableArrayList();

            //add the data
            tableData.add(getMedianYearData("Mediaan verkopen per maand", productID, selectedYear, true, "#33ccff", false));
            tableData.add(getSalesData("Verkopen dit jaar", productID, selectedYear, true,"#64b000", false));
            tableData.add(getProjectedSalesData("Verwachte verkoop", productID, selectedYear, true, "#eb4034", true));
            tableData.add(getSalesData("Verkopen afeglopen jaar (" + selectedYear.minusYears(1) + ")", productID, selectedYear.minusYears(1), false, "#009917", false));
            tableData.add(getProductQuantity("Voorraad", productID, selectedYear, true, "#000f00", false));

            //update the table
            tableProductDetails.getItems().clear();
            tableProductDetails.setItems(tableData);

            //update the chart
            updateChart();
        }
    }

    private void updateChart(){
        //clear chart
        lineChartProductTimeLine.getData().clear();
        //get the data from the table
        ObservableList<ProductDetailsTableData> tableData = tableProductDetails.getItems();
        ArrayList<String> colorOrder = new ArrayList();
        for (ProductDetailsTableData ProductDetailsTableData : tableData) {
            //only display selected data
            if(ProductDetailsTableData.getCheckboxToggleVisible().isSelected()){
                XYChart.Series<String, Integer> series = convertToChartSeries(ProductDetailsTableData);
                lineChartProductTimeLine.getData().add(series);
                String color = ProductDetailsTableData.getColor();
                if(color != null){
                    colorOrder.add(color);
                }
                if(ProductDetailsTableData.hasDashedLine) {
                    series.getNode().setStyle("-fx-stroke-dash-array: 2 12 12 2;");
                }
            }
        }
        //set the line colors
        String styleString = "";
        for(int i = 1; i <= colorOrder.size(); i++){
            styleString = styleString + "CHART_COLOR_" + i + ": " + colorOrder.get(i - 1) + ";";
        }
        if(!styleString.isEmpty()){
            lineChartProductTimeLine.setStyle(styleString);
        }
        //lineChartProductTimeLine.setStyle("CHART_COLOR_1: #33ccff ; CHART_COLOR_2: #64b000 ; CHART_COLOR_3: #00b80c;");
    }

    private ProductDetailsTableData createProductDetailsTableData(String displayName, String color, boolean isDashed){
        CheckBox checkBox = new CheckBox();
        checkBox.setOnAction((ActionEvent event) -> {
            updateChart();
        });
        return new ProductDetailsTableData(displayName, checkBox, color, isDashed);
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
    public class ProductDetailsTableData{
        private final CheckBox checkboxToggleVisible;
        private final String name;
        //the values need to be stored in separate fields like this in order for javafx to place them in a table
        private  Integer janValue;
        private  Integer febValue;
        private  Integer marValue;
        private  Integer aprValue;
        private  Integer mayValue;
        private  Integer junValue;
        private  Integer julValue;
        private  Integer augValue;
        private  Integer septValue;
        private  Integer octValue;
        private  Integer novValue;
        private  Integer decValue;
        //style
        private String color;
        private boolean hasDashedLine;

        public ProductDetailsTableData(String name, CheckBox checkBox, String color, Boolean hasDashedLine) {
            this.name = name;
            this.checkboxToggleVisible = checkBox;
            this.color = color;
            this.hasDashedLine = hasDashedLine;
        }

        public Integer getMonthValue(int monthNumber){
            switch (monthNumber){
                case 1:
                    return janValue;
                case 2:
                    return febValue;
                case 3:
                    return marValue;
                case 4:
                    return aprValue;
                case 5:
                    return mayValue;
                case 6:
                    return junValue;
                case 7:
                    return julValue;
                case 8:
                    return augValue;
                case 9:
                    return septValue;
                case 10:
                    return octValue;
                case 11:
                    return novValue;
                case 12:
                    return decValue;
                default:
                    throw new IllegalArgumentException();
            }
        }

        public Integer getJanValue() {
            return janValue;
        }

        public Integer getFebValue() {
            return febValue;
        }

        public Integer getMarValue() {
            return marValue;
        }

        public Integer getAprValue() {
            return aprValue;
        }

        public Integer getMayValue() {
            return mayValue;
        }

        public Integer getJunValue() {
            return junValue;
        }

        public Integer getJulValue() {
            return julValue;
        }

        public Integer getAugValue() {
            return augValue;
        }

        public Integer getSeptValue() {
            return septValue;
        }

        public Integer getOctValue() {
            return octValue;
        }

        public Integer getNovValue() {
            return novValue;
        }

        public Integer getDecValue() {
            return decValue;
        }

        public String getName() {
            return name;
        }

        public CheckBox getCheckboxToggleVisible() {
            return checkboxToggleVisible;
        }

        public String getColor() {
            return color;
        }

        public boolean isHasDashedLine() {
            return hasDashedLine;
        }

        public void setValue(int monthToSet, Integer value){
            switch (monthToSet){
                case 1:
                    this.janValue = value;
                    break;
                case 2:
                    this.febValue = value;
                    break;
                case 3:
                    this.marValue = value;
                    break;
                case 4:
                    this.aprValue = value;
                    break;
                case 5:
                    this.mayValue = value;
                    break;
                case 6:
                    this.junValue = value;
                    break;
                case 7:
                    this.julValue = value;
                    break;
                case 8:
                    this.augValue = value;
                    break;
                case 9:
                    this.septValue = value;
                    break;
                case 10:
                    this.octValue = value;
                    break;
                case 11:
                    this.novValue = value;
                    break;
                case 12:
                    this.decValue = value;
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }

    }
}
