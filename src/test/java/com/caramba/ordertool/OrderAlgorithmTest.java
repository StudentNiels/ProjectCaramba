package com.caramba.ordertool;

import com.caramba.ordertool.models.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.*;

class OrderAlgorithmTest {
    private final String productID = "testID";
    private ProductList products;
    private SupplierList suppliers;
    private YearMonth startDate;

    @BeforeEach
    void setup(){
        Product product = new Product("abc", "test");
        products = new ProductList();
        products.add(productID, product);
        suppliers = new SupplierList();
        String supplierDefName = "testSupplier";
        Supplier s = new Supplier(supplierDefName, 1);
        s.addProduct(product);
        suppliers.add(s);
        startDate = YearMonth.now().minusMonths(24);
    }

    @Test
    void createRecommendationWithProjectedSales() {
        int amountNextMonth = 20;
        int amountOtherMonths = 3;
        OrderAlgorithm algo = new OrderAlgorithm();
        SalesList sales = new SalesList();
        Product product = products.get(productID);
        //get the product with 0 stock
        //test product that needs to meet projected sales
        YearMonth endDate = startDate.plusMonths(24);
        for(YearMonth ym = startDate; ym.isBefore(endDate);  ym = ym.plusMonths(1)){
            //every month sells 3 except the next month from now, wich sells 20. This way the projected sales for the next month will be higher than the average in the last 12 months.
            int amount;
            if(ym.getMonth().equals(YearMonth.now().plusMonths(1).getMonth())){
                amount = amountNextMonth;
            }else{
                amount = amountOtherMonths;
            }
            Sale s = new Sale(LocalDateTime.of(ym.getYear(), ym.getMonth(), 1, 12, 0));
            s.addToProducts(productID, amount);
            sales.addToSalesList(s);
        }

        RecommendationList recs = algo.createRecommendations(sales, products, suppliers);
        Recommendation recommendation = recs.getRecommendations().get(0);
        assertEquals(recommendation.getYearMonthToOrderFor(), YearMonth.now().plusMonths(1));
        int amount = recs.getRecommendations().get(0).getProductRecommendation().get(product);
        assertEquals(amountNextMonth, amount);
    }

    @Test
    void createRecommendationWithMinStock() {
        int amountNextMonth = 3;
        int amountOtherMonths = 20;
        OrderAlgorithm algo = new OrderAlgorithm();
        SalesList sales = new SalesList();
        Product product = products.get(productID);
        //get the product with 0 stock
        //test product that needs to meet projected sales
        YearMonth endDate = startDate.plusMonths(24);
        for(YearMonth ym = startDate; ym.isBefore(endDate);  ym = ym.plusMonths(1)){
            //every month sells 20 except the next month from now, wich sells 3. This way the average sales in the past 12 months should be higher than the projected sales
            int amount;
            if(ym.getMonth().equals(YearMonth.now().plusMonths(1).getMonth())){
                amount = amountNextMonth;
            }else{
                amount = amountOtherMonths;
            }
            Sale s = new Sale(LocalDateTime.of(ym.getYear(), ym.getMonth(), 1, 12, 0));
            s.addToProducts(productID, amount);
            sales.addToSalesList(s);
        }
        RecommendationList recs = algo.createRecommendations(sales, products, suppliers);
        Recommendation recommendation = recs.getRecommendations().get(0);
        assertEquals(recommendation.getYearMonthToOrderFor(), YearMonth.now().plusMonths(1));
        int amount = recs.getRecommendations().get(0).getProductRecommendation().get(product);
        int avgSales = Math.round((((float) amountOtherMonths * 11) + amountNextMonth ) / 12);
        assertEquals(avgSales, amount);
    }

   @Test
    void createRecommendationWithLongDelivery() {
       int amountDeliveryMonth = 20;
       int amountOtherMonths = 3;
       String newSupplierName = "Long Shipping Times Inc.";
       int avgDeliveryTime = 86;
       SupplierList newSupplierList = new SupplierList();
       Supplier newSupplier = new Supplier(newSupplierName, avgDeliveryTime);
       newSupplier.addProduct(products.get(productID));
       newSupplierList.add(newSupplier);
       //calculate the order for date
       LocalDate orderForDate = LocalDate.now().plusDays(avgDeliveryTime);
       YearMonth orderForYearMonth = YearMonth.of(orderForDate.getYear(), orderForDate.getMonth());
       if(orderForDate.getDayOfMonth() != 1){
           orderForYearMonth = orderForYearMonth.plusMonths(1);
       }

       OrderAlgorithm algo = new OrderAlgorithm();
       SalesList sales = new SalesList();
       Product product = products.get(productID);
       //get the product with 0 stock
       //test product that needs to meet projected sales
       YearMonth endDate = startDate.plusMonths(24);
       for(YearMonth ym = startDate; ym.isBefore(endDate);  ym = ym.plusMonths(1)){
           //every month sells 3 except whatever month it is in 86 days, wich sells 20
           int amount;
           if(ym.getMonth().equals(orderForYearMonth.getMonth())){
               amount = amountDeliveryMonth;
           }else{
               amount = amountOtherMonths;
           }
           Sale s = new Sale(LocalDateTime.of(ym.getYear(), ym.getMonth(), 1, 12, 0));
           s.addToProducts(productID, amount);
           sales.addToSalesList(s);
       }
       RecommendationList recs = algo.createRecommendations(sales, products, newSupplierList);
       Recommendation recommendation = recs.getRecommendations().get(0);
       assertEquals(recommendation.getYearMonthToOrderFor(), orderForYearMonth);
       int amount = recs.getRecommendations().get(0).getProductRecommendation().get(product);
       assertEquals(amount, amountDeliveryMonth);
    }

    @Test
    void createRecommendationWithEnoughQuantity() {
        OrderAlgorithm algo = new OrderAlgorithm();
        //test product with enough quantity in stock
        //This product sells 5 units every month, currently has 15 in stock and has a delivery time of one month
        //there should still be enough stock for the rest of the current month and the next 2 months after, so the recommendationsList should be empty
        int amountSoldPerMonth = 5;

        //create the product
        ProductList newProductList = new ProductList();
        Product newProduct = new Product("123", "abc");
        newProduct.setQuantity(15);
        newProductList.add(productID, newProduct);

        //create the supplier
        String newSupplierName = "Long Shipping Times Inc.";
        int avgDeliveryTime = 30;
        SupplierList newSupplierList = new SupplierList();
        Supplier newSupplier = new Supplier(newSupplierName, avgDeliveryTime);
        newSupplier.addProduct(newProductList.get(productID));
        newSupplierList.add(newSupplier);

        //add the sales
        SalesList sales = new SalesList();
        YearMonth endDate = startDate.plusMonths(24);
        for(YearMonth ym = startDate; ym.isBefore(endDate);  ym = ym.plusMonths(1)){
            //sells 5 every month
            Sale s = new Sale(LocalDateTime.of(ym.getYear(), ym.getMonth(), 1, 12, 0));
            s.addToProducts(productID, amountSoldPerMonth);
            sales.addToSalesList(s);
        }
        RecommendationList recs = algo.createRecommendations(sales, newProductList, newSupplierList);
        assertEquals(0, recs.getRecommendations().size());
    }

    @Test
    void createRecommendationWithNoSales() {
        //test product with no sales
        OrderAlgorithm algo = new OrderAlgorithm();
        SalesList sales = new SalesList();
        RecommendationList recs = algo.createRecommendations(sales, products, suppliers);
        assertEquals(0, recs.getRecommendations().size());
    }
    @Test
    void invalidProjectedSalesDate(){
        //test projected sales in the past
        OrderAlgorithm algo = new OrderAlgorithm();
        SalesList sales = new SalesList();
        Assertions.assertThrows(IllegalArgumentException.class, () -> algo.getProjectedSaleAmount(sales, productID, YearMonth.now().minusMonths(1)));
    }

    @Test
    void invalidProjectedStockDate(){
        //test projected stock in the past
        OrderAlgorithm algo = new OrderAlgorithm();
        SalesList sales = new SalesList();
        Assertions.assertThrows(IllegalArgumentException.class, () -> algo.getProjectedStock(sales, products, productID, YearMonth.now().minusMonths(1)));
    }
}
