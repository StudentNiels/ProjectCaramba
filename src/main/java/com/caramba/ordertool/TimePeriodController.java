package com.caramba.ordertool;

import java.util.ArrayList;

public class TimePeriodController {

    private final ArrayList<TimePeriod> timePeriods = new ArrayList();

    public ArrayList<TimePeriod> activeTimePeriods(){
        ArrayList<TimePeriod> result = new ArrayList<>();
        for(TimePeriod period: timePeriods) {
            if(period.isActive()){
                result.add(period);
            }
        }
        return result;
    }


}
