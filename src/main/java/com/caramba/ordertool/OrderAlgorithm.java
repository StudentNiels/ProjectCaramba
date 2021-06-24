package com.caramba.ordertool;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

public class OrderAlgorithm {
    //Amount of months that the projected sales method extrapolates from
    //Should be higher than 0
    private final int PROJECTED_SALES_NUMBER_OF_MONTHS_TO_LOOK_BACK = 6;

    public RecommendationList createRecommendations(){
        RecommendationList result = new RecommendationList();
        SupplierList suppliers = OrderTool.getSuppliers();
        for (Supplier supplier : suppliers.getSuppliers().values()) {
            //some suppliers might have a delivery time longer than 1 month
            //so we need to be sure that the recommendation gets created ahead of time.
            //check what date the products could arrive if they were ordered today
            LocalDate dateToOrderFor = LocalDate.now().plusDays(supplier.getAvgDeliveryTime());
            YearMonth yearMonthToOrderFor = YearMonth.of(dateToOrderFor.getYear(), dateToOrderFor.getMonth());
            if(dateToOrderFor.getDayOfMonth() != 1){
                //if the arrival date is not the first of the month, then it's to late to get the products for this month and we have to make a recommendation for the month after
                yearMonthToOrderFor = yearMonthToOrderFor.plusMonths(1);
            }
            Recommendation rec = createRecommendation(supplier, yearMonthToOrderFor);
            if(rec != null){
                result.addRecommendation(rec);
            }
        }
        return result;
    }

    /**
     * Checks all registered products and checks how many should be ordered from the given supplier
     * @param supplier the supplier to create the recommendation for
     * @param date YearMonth that the products should be in stock
     * @return hashmap with products and quantity to order
     */
    public Recommendation createRecommendation(Supplier supplier, YearMonth date){
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
            if(supplier == supplierToBuyFrom){
                int amount =  RecommendOrderAmount(id, date);
                if(amount > 0){
                    result.addProductToRecommendation(p, amount);
                }
            }
        }
        //do not create empty recommendations
        if(result.getProductRecommendation().size() > 0){
            return result;
        }else{
            return null;
        }

    }

    /**
     * Calculates how many products to order based on current stock and expected sales
     */
    public int RecommendOrderAmount(String productID, YearMonth date){
        int minStock = getAverageSoldLast12Months(productID);
        int projectedSales = getProjectedSaleAmount(productID, date);
        int projectedStock = getProjectedStock(productID, date);
        int result;
        if (minStock > projectedSales){
            result = minStock - projectedStock;
        }else{
            result = projectedSales - projectedStock;
        }
        return Math.max(result, 0);
    }

    /**
     * What is the average sold products for the last 12 months
     */
    public int getAverageSoldLast12Months(String productID) {
        Saleslist saleslist = OrderTool.getSales().getSalesByProduct(productID);

        int averageSoldLast12Months = 0;
        for (Sale sale : saleslist.getSales()) {
            int amount = sale.getAmountByID(productID);

            for (int i = 0; i < 12; i++) {
                if(YearMonth.from(sale.getDate()).equals(YearMonth.now().minusMonths(i))){
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
    public int getProjectedStock(String productID, YearMonth date){
        if(date.isBefore(YearMonth.now())){
            throw new InvalidParameterException("The given date is not in the future");
        }
        Product p = OrderTool.getProducts().get(productID);
        Saleslist sales = OrderTool.getSales();
        YearMonth now = YearMonth.now();
        //the expected stock at the end of the current month is based on the current stock and projected sales minus sales already made this month
        int result = p.getQuantity() - (getProjectedSaleAmount(productID, now) - sales.getSoldInYearMonth(productID, now));
        if(result <= 0){
            return 0;
        }
        //the expected stock for the rest of the month decreases by the projected amount each month until it reaches 0
        for(YearMonth n = now.plusMonths(1); n.isBefore(date); n = n.plusMonths(1)){
            result = result - getProjectedSaleAmount(productID, n);
            if(result <= 0){
                return 0;
            }
        }
        return result;
    }

    /**
     * Calculates how many units of a certain product is expected to be sold in the given month
     * based on the median of sales per month on record.
     * @param productID The uuid of the product to check
     * @param date The yearMonth to get the projected sales for. Must be in the future.
     * @return The amount to order
     */
    public int getProjectedSaleAmount(String productID, YearMonth date){
        if(date.isBefore(YearMonth.now())){
            throw new InvalidParameterException("The given date is not in the future");
        }
        Saleslist allSales = OrderTool.getSales();
        YearMonth now = YearMonth.now();
        HashMap<YearMonth, Integer> dateAmountMap = allSales.getDateAmountMap(productID);
        //we use all the sales of this product so far to get the median amount of sales in a certain month.
        //e.g. In january we typically sell 10 units. In february 5 units, ect.
        //This gives us an overview of what a typical year looks like for this product, and allows us to see seasonal trends.
        //we only use months before the look back number, so that those months do not affect the percentage calculation
        MedianYear medianYear = getMedianYear(allSales.getSalesBeforeYearMonth(date.minusMonths(PROJECTED_SALES_NUMBER_OF_MONTHS_TO_LOOK_BACK)).getDateAmountMap(productID));

        //then we look back at the past X months and see how much they differ from a typical year.
        //the average of the difference is used to project future sales
        //e.g. in the past 6 months we sold 110% of a typical year, so we assume that we will sell 110% every next month.
        int totalTypical = 0;
        int totalActual = 0;
        for(int i = 0; i < PROJECTED_SALES_NUMBER_OF_MONTHS_TO_LOOK_BACK; i++){
            YearMonth selectedDate = now.minusMonths(PROJECTED_SALES_NUMBER_OF_MONTHS_TO_LOOK_BACK - i);
            totalTypical = totalTypical + medianYear.getByMonthNumber(selectedDate.getMonthValue());
            Integer actualSalesThisMonth = dateAmountMap.get(selectedDate);
            //don't use this month for the calculation if there is no data.
                if(actualSalesThisMonth != null){
                    totalActual = totalActual + actualSalesThisMonth;
                }
            }
        double avgDifferenceInPercentage = (double) totalActual / totalTypical;

        //Finally, we calculate the percentages back to a number based on the median year
        return Math.max((int) Math.round(medianYear.getByMonthNumber(date.getMonthValue()) * avgDifferenceInPercentage), 0);
    }

    /**
     * Analyzes the sales of the product in previous years to calculate a 'median year'.
     * The median year includes the median of products sold in per month of the year.
     * @param dateAmountList hashmap with quantity sold in a certain YearMonth
     * @return array with the median of amount sold where i = the month of the year
     */
    public MedianYear getMedianYear(HashMap<YearMonth, Integer> dateAmountList){
        int[] median = new int[12];
        ArrayList<Integer> januaryAmount    = new ArrayList<>();
        ArrayList<Integer> februaryAmount   = new ArrayList<>();
        ArrayList<Integer> marchAmount      = new ArrayList<>();
        ArrayList<Integer> aprilAmount      = new ArrayList<>();
        ArrayList<Integer> mayAmount        = new ArrayList<>();
        ArrayList<Integer> juneAmount       = new ArrayList<>();
        ArrayList<Integer> julyAmount       = new ArrayList<>();
        ArrayList<Integer> augustAmount     = new ArrayList<>();
        ArrayList<Integer> septemberAmount  = new ArrayList<>();
        ArrayList<Integer> octoberAmount    = new ArrayList<>();
        ArrayList<Integer> novemberAmount   = new ArrayList<>();
        ArrayList<Integer> decemberAmount   = new ArrayList<>();
        for (Map.Entry<YearMonth, Integer> entry : dateAmountList.entrySet()) {
            switch (entry.getKey().getMonth()) {
                case JANUARY -> januaryAmount.add(entry.getValue());
                case FEBRUARY -> februaryAmount.add(entry.getValue());
                case MARCH -> marchAmount.add(entry.getValue());
                case APRIL -> aprilAmount.add(entry.getValue());
                case MAY -> mayAmount.add(entry.getValue());
                case JUNE -> juneAmount.add(entry.getValue());
                case JULY -> julyAmount.add(entry.getValue());
                case AUGUST -> augustAmount.add(entry.getValue());
                case SEPTEMBER -> septemberAmount.add(entry.getValue());
                case OCTOBER -> octoberAmount.add(entry.getValue());
                case NOVEMBER -> novemberAmount.add(entry.getValue());
                case DECEMBER -> decemberAmount.add(entry.getValue());
            }
        }
        median[0] = getMedianFromArrayList(januaryAmount);
        median[1] = getMedianFromArrayList(februaryAmount);
        median[2] = getMedianFromArrayList(marchAmount);
        median[3] = getMedianFromArrayList(aprilAmount);
        median[4] = getMedianFromArrayList(mayAmount);
        median[5] = getMedianFromArrayList(juneAmount);
        median[6] = getMedianFromArrayList(julyAmount);
        median[7] = getMedianFromArrayList(augustAmount);
        median[8] = getMedianFromArrayList(septemberAmount);
        median[9] = getMedianFromArrayList(octoberAmount);
        median[10] = getMedianFromArrayList(novemberAmount);
        median[11] = getMedianFromArrayList(decemberAmount);
        return new MedianYear(median);
    }

    public int getMedianFromArrayList(ArrayList<Integer> arrayList){
        Collections.sort(arrayList);
        if(arrayList.size() == 0){
            return 0;
        }else if(arrayList.size() == 1){
            return arrayList.get(0);
        }else if((arrayList.size() % 2) == 0){
            //even
            int centerIndex = (arrayList.size() / 2) - 1;
            return (arrayList.get(centerIndex) + arrayList.get(centerIndex + 1)) / 2;
        }else{
            //odd
            return arrayList.get((int)Math.ceil((float) arrayList.size() / 2) - 1);
        }
    }
}
