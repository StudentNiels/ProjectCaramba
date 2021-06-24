package com.caramba.ordertool.notifications;

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

    public static Notification get(int index) {
        return notifications.get(index);
    }

    //#endregion

    public static Notification getLast(){
        return get(size() - 1);
    }

    public static void add(Notification notification) {
        notifications.add(notification);
        printLatest();
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

    public static void printLatest(){
        printToConsole(getLast());
    }
}
