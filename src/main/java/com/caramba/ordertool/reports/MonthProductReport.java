package com.caramba.ordertool.reports;

public class MonthProductReport {
    private int endOfMonthStock;
    private int salesQuantity;
    private int previousProjectedSaleQuantity;

    public MonthProductReport(int endOfMonthStock, int salesQuantity) {
        this.endOfMonthStock = endOfMonthStock;
        this.salesQuantity = salesQuantity;
    }

    public int getEndOfMonthStock() {
        return endOfMonthStock;
    }

    public int getSalesQuantity() {
        return salesQuantity;
    }

    public int getPreviousProjectedSaleQuantity() {
        return previousProjectedSaleQuantity;
    }
}
