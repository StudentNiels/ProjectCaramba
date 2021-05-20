package com.caramba.ordertool.Notifications;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class NotificationManager {
    private static final ArrayList<Notification> notifications = new ArrayList<>();

    //#region delegate methods
    public static int size() {
        return notifications.size();
    }

    public static boolean isEmpty() {
        return notifications.isEmpty();
    }

    public static boolean contains(Object o) {
        //noinspection SuspiciousMethodCalls
        return notifications.contains(o);
    }

    public static Notification get(int index) {
        return notifications.get(index);
    }

    public static Notification remove(int index) {
        return notifications.remove(index);
    }

    public static void clear() {
        notifications.clear();
    }

    public static boolean containsAll(Collection<Notification> c) {
        return notifications.containsAll(c);
    }
    //#endregion

    public static Notification getLast(){
        return get(size() - 1);
    }

    public static ArrayList<Notification> getByType(NotificationType type){
        ArrayList<Notification> result = new ArrayList<>();
        for (Notification n : notifications) {
            if(n.getType() == type){
                result.add(n);
            }
        }
        return result;
    }

    public static void add(Notification notification) {
        notifications.add(notification);
        printLatest();
    }

    public static void Display(int index){
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

    public static void addExceptionError(Exception e){
        e.printStackTrace();
        add(new Notification(NotificationType.ERROR, Arrays.toString(e.getStackTrace())));
    }

    public static void printToConsole(Notification n){
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String s = "[" + n.getTimestamp().format(f) + "] " +
                n.getType().toString() + ": " +
                n.getContents();
        System.out.println(s);
    }

    public static void printAllToConsole(){
        for(Notification n : notifications){
            printToConsole(n);
        }
    }

    public static void printLatest(){
        printToConsole(getLast());
    }
}
