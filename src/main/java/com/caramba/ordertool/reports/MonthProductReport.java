package com.caramba.ordertool.reports;

import java.time.Month;

public class MonthProductReport {
    private Month month;
    private int endOfMonthStock;
    private int salesQuantity;
    private int previousProjectedSaleQuantity;

    public MonthProductReport(int endOfMonthStock, int salesQuantity, Month month) {
        this.endOfMonthStock = endOfMonthStock;
        this.salesQuantity = salesQuantity;
        this.month = month;
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
    public Month getMonth() {
        return month;
    }
}