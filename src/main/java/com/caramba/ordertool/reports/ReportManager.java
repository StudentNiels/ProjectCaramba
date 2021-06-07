package com.caramba.ordertool.reports;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class ReportManager {
    private static List<YearProductReport> yearProductReports;

    public static List<YearProductReport> getYearProductReports() {
        return yearProductReports;
    }
}
