package com.caramba.ordertool;

import com.caramba.ordertool.Notifications.Notification;
import com.caramba.ordertool.Notifications.NotificationManager;
import com.caramba.ordertool.Notifications.NotificationType;

import java.time.MonthDay;
import java.util.ArrayList;

public class TimePeriodController {

    private final ArrayList<TimePeriod> timePeriods = new ArrayList<>();

    public TimePeriodController(){
        load();
        notifyStartedToday();
        notifyEndedToday();
        notifyActiveToday();
    }

    private void load(){
        timePeriods.add(new TimePeriod("Winter", "--12-01", "--02-29"));
        timePeriods.add(new TimePeriod("Spring", "--03-01", "--05-31"));
        timePeriods.add(new TimePeriod("summer", "--06-01", "--08-31"));
        timePeriods.add(new TimePeriod("summer", "--09-01", "--11-30"));
    }

    public ArrayList<TimePeriod> getActiveTimePeriods(){
        ArrayList<TimePeriod> result = new ArrayList<>();
        for(TimePeriod period: timePeriods) {
            if(period.isActive()){
                result.add(period);
            }
        }
        return result;
    }

    public ArrayList<TimePeriod> getStaredToday(){
        ArrayList<TimePeriod> result = new ArrayList<>();
        MonthDay current = MonthDay.now();
        for(TimePeriod period: timePeriods) {
            if(current.compareTo(period.startDate) == 0){
                result.add(period);
            }
        }
        return result;
    }

    public ArrayList<TimePeriod> getEndedToday(){
        ArrayList<TimePeriod> result = new ArrayList<>();
        MonthDay current = MonthDay.now();
        for(TimePeriod period: timePeriods) {
            if(current.compareTo(period.endDate) == 0){
                result.add(period);
            }
        }
        return result;
    }

    public void addTimePeriod(String name, String startDate, String endDate){
        timePeriods.add(new TimePeriod(name, startDate, endDate));
    }

    public void notifyStartedToday(){
        ArrayList<TimePeriod> n = getStaredToday();
        if(n.size() > 0){
            StringBuilder s = new StringBuilder();
            for(TimePeriod period : n){
                s.append(" ").append(period.name).append(",");
            }
            NotificationManager.add(new Notification(NotificationType.INFO, "The following time periods started today:" + s));
        }
    }

    public void notifyEndedToday(){
        ArrayList<TimePeriod> n = getEndedToday();
        if(n.size() > 0) {
            StringBuilder s = new StringBuilder();
            for (TimePeriod period : n) {
                s.append(" ").append(period.name).append(",");
            }
            NotificationManager.add(new Notification(NotificationType.INFO, "The following time periods ended today:" + s));
        }
    }

    public void notifyActiveToday(){
        ArrayList<TimePeriod> n = getActiveTimePeriods();
        if(n.size() > 0) {
            StringBuilder s = new StringBuilder();
            for (TimePeriod period : n) {
                s.append(" ").append(period.name).append(",");
            }
            NotificationManager.add(new Notification(NotificationType.INFO, "The following time periods are currently active:" + s));
        }
    }
}
