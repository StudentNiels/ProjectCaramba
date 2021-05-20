package com.caramba.ordertool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class NotificationManager {
    private final ArrayList<Notification> notifications = new ArrayList<>();

    public NotificationManager(){}

    //#region delegate methods
    public int size() {
        return notifications.size();
    }

    public boolean isEmpty() {
        return notifications.isEmpty();
    }

    public boolean contains(Object o) {
        //noinspection SuspiciousMethodCalls
        return notifications.contains(o);
    }

    public Notification get(int index) {
        return notifications.get(index);
    }

    public Notification remove(int index) {
        return notifications.remove(index);
    }

    public void clear() {
        notifications.clear();
    }

    public boolean containsAll(Collection<Notification> c) {
        return notifications.containsAll(c);
    }
    //#endregion

    public Notification getLast(){
        return get(size() - 1);
    }

    public ArrayList<Notification> getByType(NotificationType type){
        ArrayList<Notification> result = new ArrayList<>();
        for (Notification n : notifications) {
            if(n.getType() == type){
                result.add(n);
            }
        }
        return result;
    }

    public void add(Notification notification) {
        notifications.add(notification);
        printLatest();
    }

    public void addExceptionError(Exception e){
        e.printStackTrace();
        add(new Notification(NotificationType.ERROR, Arrays.toString(e.getStackTrace())));
    }

    public void printToConsole(int index){
        StringBuilder s = new StringBuilder();
        try{
            Notification n = notifications.get(index);
            s.append("[").append(n.getTimestamp().toString()).append("] ");
            s.append(n.getType().toString()).append(": ");
            s.append(n.getContents());
            System.out.println(s);
        }catch (IndexOutOfBoundsException e){
            addExceptionError(e);
        }
    }

    public void printAllToConsole(){
        for(int i = 0; i < size(); i++){
            printToConsole(i);
        }
    }

    public void printLatest(){
        printToConsole(size() - 1);
    }
}
