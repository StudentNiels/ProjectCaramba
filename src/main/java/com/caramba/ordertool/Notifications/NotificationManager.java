package com.caramba.ordertool.Notifications;

import java.time.format.DateTimeFormatter;
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

    public void Display(int index){
        try{
            Notification n = notifications.get(index);
            NotificationType t = n.getType();
            if(t.printToConsole){
                printToConsole(n);
            }
        }catch (IndexOutOfBoundsException e){
            addExceptionError(e);
         }
    }

    public void addExceptionError(Exception e){
        e.printStackTrace();
        add(new Notification(NotificationType.ERROR, Arrays.toString(e.getStackTrace())));
    }

    public void printToConsole(Notification n){
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String s = "[" + n.getTimestamp().format(f) + "] " +
                n.getType().toString() + ": " +
                n.getContents();
        System.out.println(s);
    }

    public void printAllToConsole(){
        for(Notification n : notifications){
            printToConsole(n);
        }
    }

    public void printLatest(){
        printToConsole(getLast());
    }
}
