package com.caramba.ordertool.reports;

import com.caramba.ordertool.MedianYear;
import com.caramba.ordertool.OrderAlgorithm;
import com.caramba.ordertool.OrderTool;
import com.caramba.ordertool.Product;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class ReportManager {
    private static List<YearProductReport> yearProductReports = new ArrayList<>();
    private static OrderAlgorithm orderAlog = new OrderAlgorithm();

    public static List<YearProductReport> getYearProductReports() {
        return yearProductReports;
    }

    public static void generateProductReport(String productId) {
        MedianYear medianYear = orderAlog.getMedianYear(orderAlog.getDateAmountMap(productId));
        YearProductReport result = new YearProductReport(Year.now(), medianYear, OrderTool.getProducts().get(productId));
        for (int i = 1; i <= 6; i++) {
            int amount = orderAlog.getSoldInYearMonth(productId, YearMonth.of(2021, i));
            result.add(new MonthProductReport(0, amount, Month.of(i)));
        }
        yearProductReports.add(result);
    }

    public static List<YearProductReport> getReportsByProduct(Product product) {
        List<YearProductReport> result = new ArrayList<>();
        for (YearProductReport report : yearProductReports) {
                if(report.getProduct() == product){
                    result.add(report);
                }
        }
        return result;
    }
}
