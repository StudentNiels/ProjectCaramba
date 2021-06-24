package com.caramba.ordertool;

import com.caramba.ordertool.models.*;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

public class OrderAlgorithm {

    public RecommendationList createRecommendations() {
        RecommendationList result = new RecommendationList();
        SupplierList suppliers = OrderTool.getSuppliers();
        for (Supplier supplier : suppliers.getSuppliers().values()) {
            //some suppliers might have a delivery time longer than 1 month
            //so we need to be sure that the recommendation gets created ahead of time.
            //check what date the products could arrive if they were ordered today
            LocalDate dateToOrderFor = LocalDate.now().plusDays(supplier.getAvgDeliveryTime());
            YearMonth yearMonthToOrderFor = YearMonth.of(dateToOrderFor.getYear(), dateToOrderFor.getMonth());
            if (dateToOrderFor.getDayOfMonth() != 1) {
                //if the arrival date is not the first of the month, then it's to late to get the products for this month and we have to make a recommendation for the month after
                yearMonthToOrderFor = yearMonthToOrderFor.plusMonths(1);
            }
            Recommendation rec = createRecommendation(supplier, yearMonthToOrderFor);
            if (rec != null) {
                result.addRecommendation(rec);
            }
        }
        return result;
    }

    /**
     * Checks all registered products and checks how many should be ordered from the given supplier
     *
     * @param supplier the supplier to create the recommendation for
     * @param date     YearMonth that the products should be in stock
     * @return hashmap with products and quantity to order
     */
    public Recommendation createRecommendation(Supplier supplier, YearMonth date) {
        SupplierList suppliers = OrderTool.getSuppliers();
        ProductList products = OrderTool.getProducts();
        Recommendation result = new Recommendation(supplier, date);
        for (Map.Entry<String, Product> entry : products.getProducts().entrySet()) {
            String id = entry.getKey();
            Product p = entry.getValue();
            SupplierList suppliersSelling = suppliers.getSuppliersSellingProduct(p);
            //if multiple suppliers offer this product, choose the one with the lowest delivery time
            Supplier supplierToBuyFrom = suppliersSelling.getSupplierWithLowestAvgShippingTime();
            //only add it to this recommendation if this is the correct supplier
            if (supplier == supplierToBuyFrom) {
                int amount = RecommendOrderAmount(id, date);
                if (amount > 0) {
                    result.addProductToRecommendation(p, amount);
                }
            }
        }
        //do not create empty recommendations
        if (result.getProductRecommendation().size() > 0) {
            return result;
        } else {
            return null;
        }

    }

    /**
     * Calculates how many products to order based on current stock and expected sales
     */
    public int RecommendOrderAmount(String productID, YearMonth date) {
        int minStock = getAverageSoldLast12Months(productID);
        int projectedSales = getProjectedSaleAmount(productID, date);
        int projectedStock = getProjectedStock(productID, date);
        int result;
        if (minStock > projectedSales) {
            result = minStock - projectedStock;
        } else {
            result = projectedSales - projectedStock;
        }
        return Math.max(result, 0);
    }

    /**
     * What is the average sold products for the last 12 months
     */
    public int getAverageSoldLast12Months(String productID) {
        SalesList saleslist = OrderTool.getSales().getSalesByProduct(productID);

        int averageSoldLast12Months = 0;
        for (Sale sale : saleslist.getSales()) {
            int amount = sale.getAmountByID(productID);

            for (int i = 0; i < 12; i++) {
                if (YearMonth.from(sale.getDate()).equals(YearMonth.now().minusMonths(i))) {
                    averageSoldLast12Months = averageSoldLast12Months + amount;
                }
            }
        }
        averageSoldLast12Months = averageSoldLast12Months / 12;
        return averageSoldLast12Months;
    }

    /**
     * Returns the expected stock of the given product at the start of the given yearMonth
     * This assumes that the full projected sales amount is sold every month, and no extra products are ordered
     */
    public int getProjectedStock(String productID, YearMonth date) {
        if (date.isBefore(YearMonth.now())) {
            throw new InvalidParameterException("The given date is not in the future");
        }
        Product p = OrderTool.getProducts().get(productID);
        SalesList sales = OrderTool.getSales();
        YearMonth now = YearMonth.now();
        //the expected stock at the end of the current month is based on the current stock and projected sales minus sales already made this month
        int result = p.getQuantity() - (getProjectedSaleAmount(productID, now) - sales.getSoldInYearMonth(productID, now));
        if (result <= 0) {
            return 0;
        }
        //the expected stock for the rest of the month decreases by the projected amount each month until it reaches 0
        for (YearMonth n = now.plusMonths(1); n.isBefore(date); n = n.plusMonths(1)) {
            result = result - getProjectedSaleAmount(productID, n);
            if (result <= 0) {
                return 0;
            }
        }
        return result;
    }

    /**
     * Calculates how many units of a certain product is expected to be sold in the given month
     * based on the median of sales per month on record.
     *
     * @param productID The uuid of the product to check
     * @param date      The yearMonth to get the projected sales for. Must be in the future.
     * @return The amount to order
     */
    public int getProjectedSaleAmount(String productID, YearMonth date) {
        if (date.isBefore(YearMonth.now())) {
            throw new InvalidParameterException("The given date is not in the future");
        }
        SalesList allSales = OrderTool.getSales();
        YearMonth now = YearMonth.now();
        HashMap<YearMonth, Integer> dateAmountMap = allSales.getDateAmountMap(productID);
        //we use all the sales of this product so far to get the median amount of sales in a certain month.
        //e.g. In january we typically sell 10 units. In february 5 units, ect.
        //This gives us an overview of what a typical year looks like for this product, and allows us to see seasonal trends.
        //we only use months before the look back number, so that those months do not affect the percentage calculation
        //Amount of months that the projected sales method extrapolates from
        //Should be higher than 0
        int PROJECTED_SALES_NUMBER_OF_MONTHS_TO_LOOK_BACK = 6;
        MedianYear medianYear = MedianYear.getMedianYear(allSales.getSalesBeforeYearMonth(date.minusMonths(PROJECTED_SALES_NUMBER_OF_MONTHS_TO_LOOK_BACK)).getDateAmountMap(productID));

        //then we look back at the past X months and see how much they differ from a typical year.
        //the average of the difference is used to project future sales
        //e.g. in the past 6 months we sold 110% of a typical year, so we assume that we will sell 110% every next month.
        int totalTypical = 0;
        int totalActual = 0;
        for (int i = 0; i < PROJECTED_SALES_NUMBER_OF_MONTHS_TO_LOOK_BACK; i++) {
            YearMonth selectedDate = now.minusMonths(PROJECTED_SALES_NUMBER_OF_MONTHS_TO_LOOK_BACK - i);
            totalTypical = totalTypical + medianYear.getByMonthNumber(selectedDate.getMonthValue());
            Integer actualSalesThisMonth = dateAmountMap.get(selectedDate);
            //don't use this month for the calculation if there is no data.
            if (actualSalesThisMonth != null) {
                totalActual = totalActual + actualSalesThisMonth;
            }
        }
        double avgDifferenceInPercentage = (double) totalActual / totalTypical;

        //Finally, we calculate the percentages back to a number based on the median year
        return Math.max((int) Math.round(medianYear.getByMonthNumber(date.getMonthValue()) * avgDifferenceInPercentage), 0);
    }
}
