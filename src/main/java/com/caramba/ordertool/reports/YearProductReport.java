package com.caramba.ordertool.reports;

import com.caramba.ordertool.MedianYear;
import com.caramba.ordertool.Product;

import java.time.Year;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class YearProductReport {
    private final Year year;
    private final MedianYear medianYear;
    private final Product product;
    private final List<MonthProductReport> monthReports = new ArrayList<>();

    public YearProductReport(Year year, MedianYear medianYear, Product product) {
        this.year = year;
        this.medianYear = medianYear;
        this.product = product;
    }

    public Year getYear() {
        return year;
    }

    public MedianYear getMedianYear() {
        return medianYear;
    }

    public Product getProduct() {
        return product;
    }

    public List<MonthProductReport> getMonthReports() {
        return monthReports;
    }

    public int size() {
        return monthReports.size();
    }

    public boolean isEmpty() {
        return monthReports.isEmpty();
    }

    public boolean contains(Object o) {
        return monthReports.contains(o);
    }

    public boolean add(MonthProductReport monthProductReport) {
        return monthReports.add(monthProductReport);
    }

    public boolean addAll(Collection<? extends MonthProductReport> c) {
        return monthReports.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends MonthProductReport> c) {
        return monthReports.addAll(index, c);
    }

    public MonthProductReport get(int index) {
        return monthReports.get(index);
    }

    public void add(int index, MonthProductReport element) {
        monthReports.add(index, element);
    }

    public int indexOf(Object o) {
        return monthReports.indexOf(o);
    }
}
