package com.caramba.ordertool;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.MonthDay;

public class TimePeriod {
    String name;
    MonthDay startDate;
    MonthDay endDate;

    public TimePeriod(String name, MonthDay startDate, MonthDay endDate){
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    public TimePeriod(String name, String startDate, String endDate) throws DateTimeException {
        this(name, MonthDay.parse(startDate), MonthDay.parse(endDate));
    }

    public boolean isActive(){
        MonthDay current = MonthDay.now();
        if(current.compareTo(startDate) >= 0 && current.compareTo(endDate) <= 0){
            return true;
        } else {
            return false;
        }
    }
}
